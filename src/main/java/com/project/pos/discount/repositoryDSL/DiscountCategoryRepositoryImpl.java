package com.project.pos.discount.repositoryDSL;

import com.project.pos.discount.dto.responseDTO.DiscountCategoryPageDTO;
import com.project.pos.discount.entity.QDiscount;
import com.project.pos.discount.entity.QDiscountCategory;
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

public class DiscountCategoryRepositoryImpl implements DiscountCategoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    DiscountCategoryRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<DiscountCategoryPageDTO> getDiscountCategoryPage(PageRequestDTO requestDTO){
        QDiscountCategory qDiscountCategory=QDiscountCategory.discountCategory;
        QStore qStore=QStore.store;
        QDiscount qDiscount=QDiscount.discount;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qDiscountCategory.deleted.isFalse());
        builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qDiscountCategory.name.containsIgnoreCase(requestDTO.getSearchText()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<DiscountCategoryPageDTO> content = queryFactory
                .select(Projections.constructor(
                        DiscountCategoryPageDTO.class,
                        qDiscountCategory.id, qDiscountCategory.name,
                        qDiscount.deleted
                                .when(false).then(1L)
                                .otherwise(0L)
                                .sum()
                                .coalesce(0L)
                ))
                .from(qDiscountCategory)
                .leftJoin(qDiscountCategory.store, qStore)
                .leftJoin(qDiscount).on(qDiscount.discountCategory.eq(qDiscountCategory))
                .where(builder)
                .groupBy(qDiscountCategory.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qDiscountCategory.id.asc())
                .fetch();

        Long total = queryFactory
                .select(qDiscountCategory.countDistinct())
                .from(qDiscountCategory)
                .leftJoin(qDiscountCategory.store, qStore)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
