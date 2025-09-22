package com.project.pos.revenue.repositoryDSL;

import com.project.pos.revenue.dto.responseDTO.NonIssueReceiptDTO;
import com.project.pos.revenue.entity.Payment;
import com.project.pos.revenue.entity.QPaymentMethod;
import com.project.pos.revenue.entity.QReceiptMethod;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class PaymentMethodRepositoryImpl implements PaymentMethodRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Autowired
    PaymentMethodRepositoryImpl(EntityManager entityManager){
        this.queryFactory=new JPAQueryFactory(entityManager);
    }

    @Override
    public List<NonIssueReceiptDTO> getNonIssuePaymentList(Payment payment){
        QPaymentMethod qPaymentMethod=QPaymentMethod.paymentMethod;
        QReceiptMethod qReceiptMethod=QReceiptMethod.receiptMethod;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qPaymentMethod.typeValue.eq(0));
        builder.and(qPaymentMethod.payment.id.eq(payment.getId()));
        builder.and(qPaymentMethod.status.isTrue());

        return queryFactory
                .select(Projections.constructor(
                        NonIssueReceiptDTO.class,
                        qPaymentMethod.id, qPaymentMethod.paymentAmount, qPaymentMethod.regDate, qReceiptMethod.issuePrice.sum().coalesce(0L)
                ))
                .from(qPaymentMethod)
                .leftJoin(qReceiptMethod).on(qReceiptMethod.paymentMethod.eq(qPaymentMethod).and(qReceiptMethod.receipt.status.isTrue()))
                .where(builder)
                .groupBy(qPaymentMethod.id)
                .orderBy(qPaymentMethod.id.desc())
                .fetch();
    }
    @Override
    public List<NonIssueReceiptDTO> getNonIssuePaymentListByIdList(List<Long> idList){
        QPaymentMethod qPaymentMethod=QPaymentMethod.paymentMethod;
        QReceiptMethod qReceiptMethod=QReceiptMethod.receiptMethod;

        BooleanBuilder builder=new BooleanBuilder();
        builder.and(qPaymentMethod.id.in(idList));

        return queryFactory
                .select(Projections.constructor(
                        NonIssueReceiptDTO.class,
                        qPaymentMethod.id, qPaymentMethod.paymentAmount, qPaymentMethod.regDate, qReceiptMethod.issuePrice.sum().coalesce(0L)
                ))
                .from(qPaymentMethod)
                .leftJoin(qReceiptMethod).on(qReceiptMethod.paymentMethod.eq(qPaymentMethod).and(qReceiptMethod.receipt.status.isTrue()))
                .where(builder)
                .groupBy(qPaymentMethod.id)
                .orderBy(qPaymentMethod.id.desc())
                .fetch();
    }
}
