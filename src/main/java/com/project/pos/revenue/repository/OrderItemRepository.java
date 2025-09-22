package com.project.pos.revenue.repository;

import com.project.pos.revenue.entity.OrderItem;
import com.project.pos.revenue.entity.Orders;
import com.project.pos.revenue.repositoryDSL.OrderItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>, OrderItemRepositoryCustom {
    @Query("select o from OrderItem o where o.orders=:orders")
    List<OrderItem> getOrderItemListByOrders(@Param("orders") Orders orders);
    @Query("select o from OrderItem o where o.orders=:orders and o.id=:id")
    OrderItem getOrderItemByCopyId(@Param("orders") Orders orders, @Param("id") Long id);
    @Query("select o from OrderItem o where o.orders=:orders and o.copyLocation=:copyLocation")
    OrderItem getOrderItemByCopyLocation(@Param("orders") Orders orders, @Param("copyLocation") Long copyLocation);
}
