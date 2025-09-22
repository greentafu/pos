package com.project.pos.revenue.repositoryDSL;

import com.project.pos.revenue.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    OrderItemRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Long getOptionAmount(Long id){
        QOrderItem qOrderItem = QOrderItem.orderItem;
        QOrderItemOption qOrderItemOption = QOrderItemOption.orderItemOption;

        return queryFactory.select(qOrderItemOption.option.optionPrice.sum())
                .from(qOrderItem)
                .leftJoin(qOrderItemOption).on(qOrderItemOption.orderItem.eq(qOrderItem))
                .where(qOrderItem.id.eq(id))
                .groupBy(qOrderItem.id)
                .fetchOne();
    }

    @Override
    public Long getPerBasic(Long id){
        QOrderItem qOrderItem = QOrderItem.orderItem;
        QOrderItemDiscount qOrderItemDiscount = QOrderItemDiscount.orderItemDiscount;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOrderItem.id.eq(id));
        builder.and(qOrderItemDiscount.discount.discountType.isTrue()).and(qOrderItemDiscount.discount.typeValue.isTrue());

        return queryFactory.select(qOrderItemDiscount.discount.discountPrice.sum())
                .from(qOrderItem)
                .leftJoin(qOrderItemDiscount).on(qOrderItemDiscount.orderItem.eq(qOrderItem))
                .where(builder)
                .groupBy(qOrderItem.id)
                .fetchOne();
    }

    @Override
    public Long getPerPercent(Long id){
        QOrderItem qOrderItem = QOrderItem.orderItem;
        QOrderItemDiscount qOrderItemDiscount = QOrderItemDiscount.orderItemDiscount;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOrderItem.id.eq(id));
        builder.and(qOrderItemDiscount.discount.discountType.isTrue()).and(qOrderItemDiscount.discount.typeValue.isFalse());

        return queryFactory.select(qOrderItemDiscount.discount.discountPrice.sum())
                .from(qOrderItem)
                .leftJoin(qOrderItemDiscount).on(qOrderItemDiscount.orderItem.eq(qOrderItem))
                .where(builder)
                .groupBy(qOrderItem.id)
                .fetchOne();
    }

    @Override
    public Long getAllBasic(Long id){
        QOrderItem qOrderItem = QOrderItem.orderItem;
        QOrderItemDiscount qOrderItemDiscount = QOrderItemDiscount.orderItemDiscount;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOrderItem.id.eq(id));
        builder.and(qOrderItemDiscount.discount.discountType.isFalse()).and(qOrderItemDiscount.discount.typeValue.isTrue());

        return queryFactory.select(qOrderItemDiscount.discount.discountPrice.sum())
                .from(qOrderItem)
                .leftJoin(qOrderItemDiscount).on(qOrderItemDiscount.orderItem.eq(qOrderItem))
                .where(builder)
                .groupBy(qOrderItem.id)
                .fetchOne();
    }

    @Override
    public Long getAllPercent(Long id){
        QOrderItem qOrderItem = QOrderItem.orderItem;
        QOrderItemDiscount qOrderItemDiscount = QOrderItemDiscount.orderItemDiscount;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOrderItem.id.eq(id));
        builder.and(qOrderItemDiscount.discount.discountType.isFalse()).and(qOrderItemDiscount.discount.typeValue.isFalse());

        return queryFactory.select(qOrderItemDiscount.discount.discountPrice.sum())
                .from(qOrderItem)
                .leftJoin(qOrderItemDiscount).on(qOrderItemDiscount.orderItem.eq(qOrderItem))
                .where(builder)
                .groupBy(qOrderItem.id)
                .fetchOne();
    }
}
