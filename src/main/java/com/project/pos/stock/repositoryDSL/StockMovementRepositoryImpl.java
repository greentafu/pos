package com.project.pos.stock.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.dslDTO.SoldOutListRequestDTO;
import com.project.pos.stock.dto.responseDTO.StockHistoryPageDTO;
import com.project.pos.stock.entity.QStock;
import com.project.pos.stock.entity.QStockMovement;
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

public class StockMovementRepositoryImpl implements StockMovementRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    StockMovementRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<StockHistoryPageDTO> getStockHistoryPage(PageRequestDTO requestDTO){
        QStock qStock=QStock.stock;
        QStockMovement qStockMovement=QStockMovement.stockMovement;
        QStore qStore=QStore.store;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qStockMovement.regDate.between(requestDTO.getStartDate(), requestDTO.getEndDate()));
        builder.and(qStore.id.eq(requestDTO.getStoreDTO().getId()));
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qStock.name.containsIgnoreCase(requestDTO.getSearchText()));
        }
        if (requestDTO.getSearchCategory() !=null && requestDTO.getSearchCategory()!=0) {
            builder.and(qStock.stockCategory.id.eq(requestDTO.getSearchCategory()));
        }
        if (requestDTO.getType() !=null && requestDTO.getType()!=999) {
            builder.and(qStockMovement.typeValue.eq(requestDTO.getType()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<StockHistoryPageDTO> content = queryFactory
                .select(Projections.constructor(
                        StockHistoryPageDTO.class,
                        qStockMovement.id, qStockMovement.regDate, qStock.number, qStock.name,
                        qStockMovement.typeValue, qStockMovement.movementAmount, qStockMovement.presentAmount,
                        qStockMovement.stockCost, qStockMovement.notes
                ))
                .from(qStockMovement)
                .innerJoin(qStockMovement.stock, qStock)
                .leftJoin(qStockMovement.store, qStore)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qStockMovement.id.desc())
                .fetch();

        Long total = queryFactory
                .select(qStockMovement.countDistinct())
                .from(qStockMovement)
                .innerJoin(qStockMovement.stock, qStock)
                .leftJoin(qStockMovement.store, qStore)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
}
