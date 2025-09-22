package com.project.pos.revenue.repository;

import com.project.pos.revenue.entity.Orders;
import com.project.pos.revenue.repositoryDSL.OrdersRepositoryCustom;
import com.project.pos.store.entity.Pos;
import com.project.pos.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrdersRepository extends JpaRepository<Orders, Long>, OrdersRepositoryCustom {
    @Query("select o from Orders o " +
            "where o.store=:store and o.pos=:pos and o.waiting=:waiting")
    Orders getOrdersByStorePosWaiting(@Param("store") Store store, @Param("pos") Pos pos, @Param("waiting") Integer waiting);
}
