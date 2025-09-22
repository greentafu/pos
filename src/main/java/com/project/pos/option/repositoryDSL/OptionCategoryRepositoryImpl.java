package com.project.pos.option.repositoryDSL;

import com.project.pos.option.dto.responseDTO.OptionCategoryPageDTO;
import com.project.pos.option.entity.QOption;
import com.project.pos.option.entity.QOptionCategory;
import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.store.entity.QStore;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class OptionCategoryRepositoryImpl implements OptionCategoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    OptionCategoryRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<OptionCategoryPageDTO> getOptionCategoryPage(PageRequestDTO requestDTO){
        QOptionCategory qOptionCategory=QOptionCategory.optionCategory;
        QStore qStore=QStore.store;
        QOption qOption=QOption.option;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOptionCategory.deleted.isFalse());
        builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qOptionCategory.name.containsIgnoreCase(requestDTO.getSearchText()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<OptionCategoryPageDTO> content = queryFactory
                .select(Projections.constructor(
                        OptionCategoryPageDTO.class,
                        qOptionCategory.id, qOptionCategory.name,
                        qOption.deleted
                                .when(false).then(1L)
                                .otherwise(0L)
                                .sum()
                                .coalesce(0L)
                ))
                .from(qOptionCategory)
                .leftJoin(qOptionCategory.store, qStore)
                .leftJoin(qOption).on(qOption.optionCategory.eq(qOptionCategory))
                .where(builder)
                .groupBy(qOptionCategory.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qOptionCategory.id.asc())
                .fetch();

        Long total = queryFactory
                .select(qOptionCategory.countDistinct())
                .from(qOptionCategory)
                .leftJoin(qOptionCategory.store, qStore)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
