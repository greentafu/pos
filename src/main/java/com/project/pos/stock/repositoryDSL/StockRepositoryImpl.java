package com.project.pos.stock.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.dslDTO.SoldOutListRequestDTO;
import com.project.pos.stock.entity.Stock;
import com.project.pos.stock.entity.QStock;
import com.project.pos.stock.dto.responseDTO.StockPageDTO;
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

public class StockRepositoryImpl implements StockRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    StockRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<StockPageDTO> getStockPage(PageRequestDTO requestDTO){
        QStock qStock=QStock.stock;
        QStore qStore=QStore.store;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qStock.deleted.isFalse());
        builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qStock.name.containsIgnoreCase(requestDTO.getSearchText()));
        }
        if (requestDTO.getSearchCategory() !=null && requestDTO.getSearchCategory()!=0) {
            builder.and(qStock.stockCategory.id.eq(requestDTO.getSearchCategory()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<StockPageDTO> content = queryFactory
                .select(Projections.constructor(
                        StockPageDTO.class,
                        qStock.id, qStock.number, qStock.name, qStock.stockCategory.name, qStock.stockCost, qStock.unit
                ))
                .from(qStock)
                .leftJoin(qStock.store, qStore)
                .where(builder)
                .groupBy(qStock.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qStock.id.asc())
                .fetch();

        Long total = queryFactory
                .select(qStock.countDistinct())
                .from(qStock)
                .leftJoin(qStock.store, qStore)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    @Override
    public List<Stock> getSoldOutStockList(SoldOutListRequestDTO requestDTO){
        QStock qStock=QStock.stock;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qStock.deleted.isFalse());
        builder.and(qStock.store.id.eq(requestDTO.getStoreDTO().getId()));
        builder.and(qStock.soldOut.eq(requestDTO.getSoldOutType()));
        if (requestDTO.getSearchCategory() !=null && requestDTO.getSearchCategory()!=0) {
            builder.and(qStock.stockCategory.id.eq(requestDTO.getSearchCategory()));
        }

        return queryFactory
                .select(qStock)
                .from(qStock)
                .where(builder)
                .orderBy(qStock.id.asc())
                .fetch();
    }
}
