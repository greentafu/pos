package com.project.pos.revenue.repository;

import com.project.pos.option.entity.Option;
import com.project.pos.revenue.entity.OrderItem;
import com.project.pos.revenue.entity.OrderItemOption;
import com.project.pos.revenue.entity.OrderItemOptionID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OrderItemOptionRepository extends JpaRepository<OrderItemOption, OrderItemOptionID> {
    @Query("select o.id.optionKey from OrderItemOption o where o.orderItem=:orderItem")
    List<Long> getOrderItemOptionList(@Param("orderItem") OrderItem orderItem);
    @Query("select o.option from OrderItemOption o where o.orderItem=:orderItem")
    List<Option> getOptionListByOrderItem(@Param("orderItem") OrderItem orderItem);
    @Modifying
    @Transactional
    @Query("delete from OrderItemOption o where o.orderItem=:orderItem")
    void deleteOrderItemOptionByOrderItem(@Param("orderItem") OrderItem orderItem);
}
