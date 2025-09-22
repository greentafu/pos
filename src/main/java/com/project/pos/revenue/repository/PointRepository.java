package com.project.pos.revenue.repository;

import com.project.pos.revenue.entity.Payment;
import com.project.pos.revenue.entity.PaymentMethod;
import com.project.pos.revenue.entity.Point;
import com.project.pos.revenue.repositoryDSL.PointRepositoryCustom;
import com.project.pos.store.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long>, PointRepositoryCustom {
    @Query("select p.member from Point p where p.paymentMethod.payment=:payment")
    List<Member> getMemberByPayment(@Param("payment") Payment payment);

    @Query("select p from Point p where p.paymentMethod=:paymentMethod order by p.id desc")
    List<Point> getPointByPaymentMethod(@Param("paymentMethod") PaymentMethod paymentMethod);
}
