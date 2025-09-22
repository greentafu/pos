package com.project.pos.revenue.repository;

import com.project.pos.revenue.entity.Orders;
import com.project.pos.revenue.entity.Payment;
import com.project.pos.store.entity.Pos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select p from Payment p where p.orders=:orders")
    Payment getPaymentByOrders(@Param("orders") Orders orders);

    @Query("select count(p) from Payment p " +
            "where p.orders.pos=:pos and p.regDate>=:regDate and p.status=true")
    Long getPaymentCountByPos(@Param("pos") Pos pos, @Param("regDate") LocalDateTime regDate);
}
