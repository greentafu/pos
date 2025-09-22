package com.project.pos.revenue.repository;

import com.project.pos.discount.entity.Discount;
import com.project.pos.revenue.entity.OrderDiscount;
import com.project.pos.revenue.entity.OrderDiscountID;
import com.project.pos.revenue.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDiscountRepository extends JpaRepository<OrderDiscount, OrderDiscountID> {
    @Query("select o.id.discountKey from OrderDiscount o where o.orders=:orders")
    List<Long> getOrderDiscountList(@Param("orders") Orders orders);
    @Query("select o.discount from OrderDiscount o " +
            "where o.orders=:orders and o.discount.typeValue=:typeValue")
    List<Discount> getDiscountListByOrder(@Param("orders") Orders orders, @Param("typeValue") Boolean typeValue);
    @Query("select o.discount from OrderDiscount o where o.orders=:orders")
    List<Discount> getDiscountListByOnlyOrder(@Param("orders") Orders orders);
}
