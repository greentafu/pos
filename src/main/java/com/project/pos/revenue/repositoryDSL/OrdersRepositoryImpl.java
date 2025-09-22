package com.project.pos.revenue.repositoryDSL;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.product.entity.QProduct;
import com.project.pos.revenue.dto.queryDTO.*;
import com.project.pos.revenue.dto.responseDTO.ReceiptPageDTO;
import com.project.pos.revenue.entity.*;
import com.project.pos.store.entity.QPos;
import com.project.pos.store.entity.QStore;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrdersRepositoryImpl implements OrdersRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    OrdersRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public Long getItemAmount(Long id){
        QOrders qOrders = QOrders.orders;
        QOrderItem qOrderItem = QOrderItem.orderItem;

        return queryFactory.select(qOrderItem.totalPerUnit.sum())
                .from(qOrders)
                .leftJoin(qOrderItem).on(qOrderItem.orders.eq(qOrders))
                .where(qOrders.id.eq(id))
                .groupBy(qOrders.id)
                .fetchOne();
    }
    @Override
    public Long getItemDiscount(Long id){
        QOrders qOrders = QOrders.orders;
        QOrderItem qOrderItem = QOrderItem.orderItem;

        return queryFactory.select(qOrderItem.totalDiscount.sum())
                .from(qOrders)
                .leftJoin(qOrderItem).on(qOrderItem.orders.eq(qOrders))
                .where(qOrders.id.eq(id))
                .groupBy(qOrders.id)
                .fetchOne();
    }
    @Override
    public Long getAllBasic(Long id){
        QOrders qOrders = QOrders.orders;
        QOrderDiscount qOrderDiscount = QOrderDiscount.orderDiscount;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOrders.id.eq(id));
        builder.and(qOrderDiscount.discount.typeValue.isTrue());

        return queryFactory.select(qOrderDiscount.discount.discountPrice.sum())
                .from(qOrders)
                .leftJoin(qOrderDiscount).on(qOrderDiscount.orders.eq(qOrders))
                .where(builder)
                .groupBy(qOrders.id)
                .fetchOne();
    }

    @Override
    public Long getAllPercent(Long id){
        QOrders qOrders = QOrders.orders;
        QOrderDiscount qOrderDiscount = QOrderDiscount.orderDiscount;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOrders.id.eq(id));
        builder.and(qOrderDiscount.discount.typeValue.isFalse());

        return queryFactory.select(qOrderDiscount.discount.discountPrice.sum())
                .from(qOrders)
                .leftJoin(qOrderDiscount).on(qOrderDiscount.orders.eq(qOrders))
                .where(builder)
                .groupBy(qOrders.id)
                .fetchOne();
    }

    @Override
    public Page<ReceiptPageDTO> getReceiptPage(PageRequestDTO requestDTO){
        QOrders qOrders = QOrders.orders;
        QStore qStore = QStore.store;
        QPos qPos = QPos.pos;
        QPayment qPayment = QPayment.payment;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOrders.store.id.eq(requestDTO.storeDTO.getId()));
        builder.and(qOrders.pos.id.eq(requestDTO.getPosDTO().getId()));
        builder.and(qOrders.receiptNumber.isNotNull());
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            builder.and(qOrders.receiptNumber.containsIgnoreCase(requestDTO.getSearchText()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<ReceiptPageDTO> content = queryFactory
                .select(Projections.constructor(
                        ReceiptPageDTO.class,
                        qOrders.id, qOrders.modDate, qOrders.receiptNumber, qOrders.totalPaymentAmount, qPayment.status
                ))
                .from(qOrders)
                .leftJoin(qOrders.store, qStore)
                .leftJoin(qOrders.pos, qPos)
                .leftJoin(qPayment).on(qPayment.orders.eq(qOrders))
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qOrders.modDate.desc())
                .fetch();

        Long total = queryFactory
                .select(qOrders.countDistinct())
                .from(qOrders)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
    @Override
    public Page<RevenueOrderPageDTO> getRevenueOrderPage(PageRequestDTO requestDTO){
        QOrders qOrders = QOrders.orders;
        QStore qStore = QStore.store;
        QPos qPos = QPos.pos;
        QPayment qPayment = QPayment.payment;
        QPaymentMethod qPaymentMethod = QPaymentMethod.paymentMethod;
        QOrderItem qOrderItem = QOrderItem.orderItem;
        QOrderItem itemSub = new QOrderItem("itemSub");

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOrders.store.id.eq(requestDTO.storeDTO.getId()));
        builder.and(qOrders.receiptNumber.isNotNull());
        builder.and(qOrders.modDate.before(requestDTO.getEndDate()));

        if (requestDTO.getPosNumber() != null && requestDTO.getPosNumber() != 0L) {
            builder.and(qOrders.pos.id.eq(requestDTO.getPosNumber()));
        }
        if (requestDTO.getReceiptNumber() !=null && !requestDTO.getReceiptNumber().isBlank()){
            builder.and(qOrders.receiptNumber.containsIgnoreCase(requestDTO.getReceiptNumber()));
        }

        BooleanBuilder subBuilder=new BooleanBuilder();
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            subBuilder.and(qOrderItem.name.containsIgnoreCase(requestDTO.getSearchText()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<RevenueOrderPageDTO> content = queryFactory
                .select(Projections.constructor(
                        RevenueOrderPageDTO.class,
                        qOrders.id, qPayment.regDate, qOrders.orderAmount,
                        qOrders.orderDiscountAmount, qOrders.totalDiscountAmount, qOrders.totalPaymentAmount,
                        qPaymentMethod.typeValue.when(0).then(qPaymentMethod.paymentAmount).otherwise(0L).sum().coalesce(0L),
                        qPaymentMethod.typeValue.when(1).then(qPaymentMethod.paymentAmount).otherwise(0L).sum().coalesce(0L),
                        qPaymentMethod.typeValue.when(2).then(qPaymentMethod.paymentAmount).otherwise(0L).sum().coalesce(0L),
                        qOrders.pos.name, qPayment.status, qOrderItem.count(),
                        Expressions.stringTemplate("MIN({0})", qOrderItem.name)
                ))
                .from(qOrders)
                .innerJoin(qPayment).on(qPayment.orders.eq(qOrders).and(qPayment.regDate.after(requestDTO.getStartDate())))
                .innerJoin(qOrderItem).on(qOrderItem.orders.eq(qOrders).and(subBuilder))
                .leftJoin(qOrders.store, qStore)
                .leftJoin(qOrders.pos, qPos)
                .leftJoin(qPaymentMethod).on(qPaymentMethod.payment.eq(qPayment).and(qPaymentMethod.status.isTrue()))
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(qOrders.id, qOrders.orderAmount, qOrders.orderDiscountAmount, qOrders.totalDiscountAmount, qOrders.totalPaymentAmount)
                .orderBy(qOrders.id.desc())
                .fetch();

        Long total = queryFactory
                .select(qOrders.countDistinct())
                .from(qOrders)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
    @Override
    public RevenueOrderSummaryDTO getRevenueOrderSummary(PageRequestDTO requestDTO){
        QOrders qOrders = QOrders.orders;
        QStore qStore = QStore.store;
        QPos qPos = QPos.pos;
        QPayment qPayment = QPayment.payment;
        QPaymentMethod qPaymentMethod = QPaymentMethod.paymentMethod;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOrders.store.id.eq(requestDTO.storeDTO.getId()));
        builder.and(qOrders.receiptNumber.isNotNull());
        builder.and(qOrders.modDate.before(requestDTO.getEndDate()));

        if (requestDTO.getPosNumber() != null && requestDTO.getPosNumber() != 0L) {
            builder.and(qOrders.pos.id.eq(requestDTO.getPosNumber()));
        }
        if (requestDTO.getReceiptNumber() !=null && !requestDTO.getReceiptNumber().isBlank()){
            builder.and(qOrders.receiptNumber.containsIgnoreCase(requestDTO.getReceiptNumber()));
        }

        List<Orders> orderList = queryFactory
                .select(qOrders).distinct()
                .from(qOrders)
                .innerJoin(qPayment).on(qPayment.orders.eq(qOrders)
                        .and(qPayment.regDate.after(requestDTO.getStartDate()))
                        .and(qPayment.status.isTrue()))
                .leftJoin(qOrders.store, qStore)
                .leftJoin(qOrders.pos, qPos)
                .leftJoin(qPaymentMethod).on(qPaymentMethod.payment.eq(qPayment).and(qPaymentMethod.status.isTrue()))
                .where(builder)
                .fetch();
        Long price = 0L;
        Long discount1 = 0L;
        Long discount2 = 0L;
        Long payment = 0L;
        if(!orderList.isEmpty()){
            for(Orders orders:orderList){
                price+=orders.getOrderAmount();
                discount1+=orders.getOrderDiscountAmount();
                discount2+=orders.getTotalDiscountAmount();
                payment+=orders.getTotalPaymentAmount();
            }
        }

        RevenueOrderSmallSummaryDTO smallDTO = queryFactory
                .select(Projections.constructor(
                        RevenueOrderSmallSummaryDTO.class,
                        qOrders.countDistinct().coalesce(0L),
                        qPaymentMethod.typeValue.when(0).then(qPaymentMethod.paymentAmount).otherwise(0L).sum().coalesce(0L),
                        qPaymentMethod.typeValue.when(1).then(qPaymentMethod.paymentAmount).otherwise(0L).sum().coalesce(0L),
                        qPaymentMethod.typeValue.when(2).then(qPaymentMethod.paymentAmount).otherwise(0L).sum().coalesce(0L),
                        qPayment.status.when(false).then(1L).otherwise(0L).sum().coalesce(0L)
                ))
                .from(qOrders)
                .innerJoin(qPayment).on(qPayment.orders.eq(qOrders).and(qPayment.regDate.after(requestDTO.getStartDate())))
                .leftJoin(qOrders.store, qStore)
                .leftJoin(qOrders.pos, qPos)
                .leftJoin(qPaymentMethod).on(qPaymentMethod.payment.eq(qPayment).and(qPaymentMethod.status.isTrue()))
                .where(builder)
                .fetchOne();

        return new RevenueOrderSummaryDTO(smallDTO.getOrderCount(), price, discount1, discount2, payment,
                smallDTO.getCash(), smallDTO.getCard(), smallDTO.getPoint(), smallDTO.getStatusCount());
    }
    @Override
    public Page<RevenueDatePageDTO> getRevenueDatePage(PageRequestDTO requestDTO){
        QOrders qOrders = QOrders.orders;
        QStore qStore = QStore.store;
        QPos qPos = QPos.pos;
        QPayment qPayment = QPayment.payment;
        QOrderItem qOrderItem = QOrderItem.orderItem;
        QOrderItemOption qOrderItemOption = QOrderItemOption.orderItemOption;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOrders.store.id.eq(requestDTO.storeDTO.getId()));
        builder.and(qOrders.receiptNumber.isNotNull());
        builder.and(qOrders.modDate.before(requestDTO.getEndDate()));

        if (requestDTO.getPosNumber() != null && requestDTO.getPosNumber() != 0L) {
            builder.and(qOrders.pos.id.eq(requestDTO.getPosNumber()));
        }

        StringTemplate groupByDate0 = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                qPayment.regDate, Expressions.constant("%Y-%m-%d")
        );
        StringTemplate groupByDate1 = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                qPayment.regDate, Expressions.constant("%W")
        );
        StringTemplate groupByDate2 = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                qPayment.regDate, Expressions.constant("%Y-%m")
        );
        StringTemplate groupByDate3 = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                qPayment.regDate, Expressions.constant("%Y")
        );

        List<StringTemplate> groupList = new ArrayList<>(Arrays.asList(groupByDate0, groupByDate1, groupByDate2, groupByDate3));

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<RevenueDatePageDTO> content = queryFactory
                .select(Projections.constructor(
                        RevenueDatePageDTO.class,
                        groupList.get(requestDTO.getType()), qOrders.orderAmount.sum(),
                        qOrders.orderDiscountAmount.sum(), qOrders.totalDiscountAmount.sum(),
                        qOrders.totalPaymentAmount.sum(), qOrderItem.productCount.sum(),
                        qOrderItem.productCost.coalesce(0L).multiply(qOrderItem.productCount).sum(),
                        qOrderItemOption.optionCost.coalesce(0L).multiply(qOrderItem.productCount).sum()
                ))
                .from(qOrders)
                .innerJoin(qPayment).on(qPayment.orders.eq(qOrders)
                        .and(qPayment.status.isTrue())
                        .and(qPayment.regDate.after(requestDTO.getStartDate())))
                .innerJoin(qOrderItem).on(qOrderItem.orders.eq(qOrders))
                .leftJoin(qOrderItemOption).on(qOrderItemOption.orderItem.eq(qOrderItem))
                .leftJoin(qOrders.store, qStore)
                .leftJoin(qOrders.pos, qPos)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(groupList.get(requestDTO.getType()))
                .orderBy(groupList.get(requestDTO.getType()).desc())
                .fetch();

        Long total = queryFactory
                .select(groupList.get(requestDTO.getType()).count())
                .from(qOrders)
                .innerJoin(qPayment).on(qPayment.orders.eq(qOrders)
                        .and(qPayment.status.isTrue())
                        .and(qPayment.regDate.after(requestDTO.getStartDate())))
                .where(qOrders.modDate.before(requestDTO.getEndDate()))
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
    @Override
    public RevenueDateSummaryDTO getRevenueDateSummary(PageRequestDTO requestDTO){
        QOrders qOrders = QOrders.orders;
        QStore qStore = QStore.store;
        QPos qPos = QPos.pos;
        QPayment qPayment = QPayment.payment;
        QOrderItem qOrderItem = QOrderItem.orderItem;
        QOrderItemOption qOrderItemOption = QOrderItemOption.orderItemOption;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOrders.store.id.eq(requestDTO.storeDTO.getId()));
        builder.and(qOrders.receiptNumber.isNotNull());
        builder.and(qOrders.modDate.before(requestDTO.getEndDate()));

        if (requestDTO.getPosNumber() != null && requestDTO.getPosNumber() != 0L) {
            builder.and(qOrders.pos.id.eq(requestDTO.getPosNumber()));
        }

        return queryFactory
                .select(Projections.constructor(
                        RevenueDateSummaryDTO.class,
                        qOrders.orderAmount.sum(),
                        qOrders.orderDiscountAmount.sum(), qOrders.totalDiscountAmount.sum(),
                        qOrders.totalPaymentAmount.sum(), qOrderItem.productCount.sum(),
                        qOrderItem.productCost.coalesce(0L).multiply(qOrderItem.productCount).sum(),
                        qOrderItemOption.optionCost.coalesce(0L).multiply(qOrderItem.productCount).sum()
                ))
                .from(qOrders)
                .innerJoin(qPayment).on(qPayment.orders.eq(qOrders)
                        .and(qPayment.status.isTrue())
                        .and(qPayment.regDate.after(requestDTO.getStartDate())))
                .innerJoin(qOrderItem).on(qOrderItem.orders.eq(qOrders))
                .leftJoin(qOrderItemOption).on(qOrderItemOption.orderItem.eq(qOrderItem))
                .leftJoin(qOrders.store, qStore)
                .leftJoin(qOrders.pos, qPos)
                .where(builder)
                .fetchOne();
    }
    @Override
    public Page<RevenueMenuPageDTO> getRevenueMenuPage(PageRequestDTO requestDTO){
        QOrders qOrders = QOrders.orders;
        QStore qStore = QStore.store;
        QPos qPos = QPos.pos;
        QPayment qPayment = QPayment.payment;
        QOrderItem qOrderItem = QOrderItem.orderItem;
        QOrderItemOption qOrderItemOption = QOrderItemOption.orderItemOption;
        QProduct qProduct = QProduct.product;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOrders.store.id.eq(requestDTO.storeDTO.getId()));
        builder.and(qOrders.receiptNumber.isNotNull());
        builder.and(qOrders.modDate.before(requestDTO.getEndDate()));

        BooleanBuilder subBuilder=new BooleanBuilder();
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            subBuilder.and(qOrderItem.name.containsIgnoreCase(requestDTO.getSearchText()));
        }

        Pageable pageable= PageRequest.of(requestDTO.getPage()-1, requestDTO.getSize());
        List<RevenueMenuPageDTO> content = queryFactory
                .select(Projections.constructor(
                        RevenueMenuPageDTO.class,
                        qProduct.number, qProduct.name, qOrders.orderAmount.sum(),
                        qOrders.orderDiscountAmount.sum(), qOrders.totalDiscountAmount.sum(),
                        qOrders.totalPaymentAmount.sum(), qOrderItem.productCount.sum(),
                        qOrderItem.productCost.coalesce(0L).multiply(qOrderItem.productCount).sum(),
                        qOrderItemOption.optionCost.coalesce(0L).multiply(qOrderItem.productCount).sum()
                ))
                .from(qOrders)
                .innerJoin(qPayment).on(qPayment.orders.eq(qOrders)
                        .and(qPayment.status.isTrue())
                        .and(qPayment.regDate.after(requestDTO.getStartDate())))
                .innerJoin(qOrderItem).on(qOrderItem.orders.eq(qOrders).and(subBuilder))
                .leftJoin(qOrderItem.product, qProduct)
                .leftJoin(qOrderItemOption).on(qOrderItemOption.orderItem.eq(qOrderItem))
                .leftJoin(qOrders.store, qStore)
                .leftJoin(qOrders.pos, qPos)
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .groupBy(qProduct.id)
                .orderBy(qProduct.number.asc())
                .fetch();

        Long total = queryFactory
                .select(qProduct.count())
                .from(qOrders)
                .leftJoin(qOrderItem).on(qOrderItem.orders.eq(qOrders).and(subBuilder))
                .leftJoin(qOrderItem.product, qProduct)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }
    @Override
    public RevenueMenuSummaryDTO getRevenueMenuSummary(PageRequestDTO requestDTO){
        QOrders qOrders = QOrders.orders;
        QStore qStore = QStore.store;
        QPos qPos = QPos.pos;
        QPayment qPayment = QPayment.payment;
        QOrderItem qOrderItem = QOrderItem.orderItem;
        QOrderItemOption qOrderItemOption = QOrderItemOption.orderItemOption;
        QProduct qProduct = QProduct.product;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOrders.store.id.eq(requestDTO.storeDTO.getId()));
        builder.and(qOrders.receiptNumber.isNotNull());
        builder.and(qOrders.modDate.before(requestDTO.getEndDate()));

        BooleanBuilder subBuilder=new BooleanBuilder();
        if (requestDTO.getSearchText() != null && !requestDTO.getSearchText().isBlank()) {
            subBuilder.and(qProduct.name.containsIgnoreCase(requestDTO.getSearchText()));
        }

        return queryFactory
                .select(Projections.constructor(
                        RevenueMenuSummaryDTO.class,
                        qProduct.count().coalesce(0L), qOrders.orderAmount.sum(),
                        qOrders.orderDiscountAmount.sum(), qOrders.totalDiscountAmount.sum(),
                        qOrders.totalPaymentAmount.sum(), qOrderItem.productCount.sum(),
                        qOrderItem.productCost.coalesce(0L).multiply(qOrderItem.productCount).sum(),
                        qOrderItemOption.optionCost.coalesce(0L).multiply(qOrderItem.productCount).sum()
                ))
                .from(qOrders)
                .innerJoin(qPayment).on(qPayment.orders.eq(qOrders)
                        .and(qPayment.status.isTrue())
                        .and(qPayment.regDate.after(requestDTO.getStartDate())))
                .innerJoin(qOrderItem).on(qOrderItem.orders.eq(qOrders).and(subBuilder))
                .leftJoin(qOrderItem.product, qProduct)
                .leftJoin(qOrderItemOption).on(qOrderItemOption.orderItem.eq(qOrderItem))
                .leftJoin(qOrders.store, qStore)
                .leftJoin(qOrders.pos, qPos)
                .where(builder)
                .fetchOne();
    }
    @Override
    public List<RevenueMenuPageDTO> getRevenueMenuChartList(PageRequestDTO requestDTO){
        QOrders qOrders = QOrders.orders;
        QStore qStore = QStore.store;
        QPos qPos = QPos.pos;
        QPayment qPayment = QPayment.payment;
        QOrderItem qOrderItem = QOrderItem.orderItem;
        QOrderItemOption qOrderItemOption = QOrderItemOption.orderItemOption;
        QProduct qProduct = QProduct.product;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qOrders.store.id.eq(requestDTO.storeDTO.getId()));
        builder.and(qOrders.receiptNumber.isNotNull());
        builder.and(qOrders.modDate.before(requestDTO.getEndDate()));

        return queryFactory
                .select(Projections.constructor(
                        RevenueMenuPageDTO.class,
                        qProduct.number, qProduct.name, qOrders.orderAmount.sum(),
                        qOrders.orderDiscountAmount.sum(), qOrders.totalDiscountAmount.sum(),
                        qOrders.totalPaymentAmount.sum(), qOrderItem.productCount.sum(),
                        qOrderItem.productCost.coalesce(0L).multiply(qOrderItem.productCount).sum(),
                        qOrderItemOption.optionCost.coalesce(0L).multiply(qOrderItem.productCount).sum()
                ))
                .from(qOrders)
                .innerJoin(qPayment).on(qPayment.orders.eq(qOrders)
                        .and(qPayment.status.isTrue())
                        .and(qPayment.regDate.after(requestDTO.getStartDate())))
                .innerJoin(qOrderItem).on(qOrderItem.orders.eq(qOrders))
                .leftJoin(qOrderItem.product, qProduct)
                .leftJoin(qOrderItemOption).on(qOrderItemOption.orderItem.eq(qOrderItem))
                .leftJoin(qOrders.store, qStore)
                .leftJoin(qOrders.pos, qPos)
                .where(builder)
                .groupBy(qProduct.id)
                .orderBy(qOrderItem.productCount.sum().desc())
                .fetch();
    }
}
