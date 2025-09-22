package com.project.pos.revenue.repository;

import com.project.pos.discount.entity.Discount;
import com.project.pos.revenue.entity.OrderItem;
import com.project.pos.revenue.entity.OrderItemDiscount;
import com.project.pos.revenue.entity.OrderItemDiscountID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderItemDiscountRepository extends JpaRepository<OrderItemDiscount, OrderItemDiscountID> {
    @Query("select o.id.discountKey from OrderItemDiscount o where o.orderItem=:orderItem")
    List<Long> getOrderItemDiscountList(@Param("orderItem") OrderItem orderItem);
    @Query("select o.discount from OrderItemDiscount o " +
            "where o.orderItem=:orderItem and o.discount.discountType=:discountType and o.discount.typeValue=:typeValue")
    List<Discount> getDiscountListByOrderItem(@Param("orderItem") OrderItem orderItem,
                                              @Param("discountType") Boolean discountType,
                                              @Param("typeValue") Boolean typeValue);
    @Query("select o.discount from OrderItemDiscount o where o.orderItem=:orderItem")
    List<Discount> getDiscountListByOnlyOrderItem(@Param("orderItem") OrderItem orderItem);
    @Modifying
    @Transactional
    @Query("delete from OrderItemDiscount o where o.orderItem=:orderItem")
    void deleteOrderItemDiscountByOrderItem(@Param("orderItem") OrderItem orderItem);
}
