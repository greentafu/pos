package com.project.pos.stock.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.stock.dto.responseDTO.StockCategoryPageDTO;
import com.project.pos.stock.entity.QStock;
import com.project.pos.stock.entity.QStockCategory;
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

public class StockCategoryRepositoryImpl implements StockCategoryRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    StockCategoryRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<StockCategoryPageDTO> getStockCategoryPage(PageRequestDTO requestDTO){
        QStockCategory qStockCategory=QStockCategory.stockCategory;
        QStore qStore=QStore.store;
        QStock qStock=QStock.stock;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qStockCategory.deleted.isFalse());
        builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qStockCategory.name.containsIgnoreCase(requestDTO.getSearchText()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<StockCategoryPageDTO> content = queryFactory
                .select(Projections.constructor(
                        StockCategoryPageDTO.class,
                        qStockCategory.id, qStockCategory.name,
                        qStock.deleted
                                .when(false).then(1L)
                                .otherwise(0L)
                                .sum()
                                .coalesce(0L)
                ))
                .from(qStockCategory)
                .leftJoin(qStockCategory.store, qStore)
                .leftJoin(qStock).on(qStock.stockCategory.eq(qStockCategory))
                .where(builder)
                .groupBy(qStockCategory.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qStockCategory.id.asc())
                .fetch();

        Long total = queryFactory
                .select(qStockCategory.countDistinct())
                .from(qStockCategory)
                .leftJoin(qStockCategory.store, qStore)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
