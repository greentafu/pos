package com.project.pos.revenue.repository;

import com.project.pos.revenue.entity.Orders;
import com.project.pos.revenue.entity.Payment;
import com.project.pos.revenue.entity.PaymentMethod;
import com.project.pos.revenue.repositoryDSL.PaymentMethodRepositoryCustom;
import com.project.pos.store.entity.Pos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long>, PaymentMethodRepositoryCustom {
    @Query("select coalesce(sum(p.paymentAmount), 0) from PaymentMethod p " +
            "where p.payment.orders=:orders and p.status=true")
    Long getPaymentPriceByOrder(@Param("orders") Orders orders);

    @Query("select coalesce(sum(p.paymentAmount), 0) from PaymentMethod p " +
            "where p.pos=:pos and p.payment.regDate>=:regDate and p.typeValue=:typeValue and p.status=true")
    Long getPaymentPriceByPos(@Param("pos") Pos pos, @Param("regDate") LocalDateTime regDate, @Param("typeValue") Integer typeValue);
    @Query("select coalesce(sum(p.paymentAmount), 0) from PaymentMethod p " +
            "where p.pos=:pos and p.payment.regDate>=:regDate and p.typeValue=:typeValue and p.status=false")
    Long getCancelPaymentPriceByPos(@Param("pos") Pos pos, @Param("regDate") LocalDateTime regDate, @Param("typeValue") Integer typeValue);

    @Query("select count(p) from PaymentMethod p " +
            "where p.payment=:payment and p.status=true")
    Long getCountProgressPaymentMethod(@Param("payment") Payment payment);

    @Query("select count(p) from PaymentMethod p " +
            "where p.payment=:payment")
    Long getCountPaymentMethod(@Param("payment") Payment payment);

    @Query("select p from PaymentMethod p where p.payment=:payment order by p.id asc")
    List<PaymentMethod> getPaymentMethodByPayment(@Param("payment") Payment payment);
}
