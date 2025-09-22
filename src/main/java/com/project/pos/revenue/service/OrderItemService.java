package com.project.pos.revenue.service;

import com.project.pos.revenue.dto.OrderItemDTO;
import com.project.pos.revenue.dto.OrdersDTO;
import com.project.pos.revenue.entity.OrderItem;

import java.util.List;

public interface OrderItemService {
    OrderItem getOrderItemById(Long id);
    OrderItemDTO getOrderItemDTOById(Long id);

    OrderItemDTO createOrderItem(OrderItemDTO dto);
    Long deleteOrderItem(Long id);
    OrderItemDTO updateOrderItemCount(Long id, Long count);
    OrderItemDTO updateOrderItem(Long id);

    OrderItem dtoToEntity(OrderItemDTO dto);
    OrderItemDTO entityToDto(OrderItem entity);

    OrderItemDTO getOrderItemByCopyId(OrdersDTO ordersDTO, Long id);
    OrderItemDTO getOrderItemByCopyLocation(OrdersDTO ordersDTO, Long copyLocation);
    List<OrderItemDTO> getOrderItemByOrders(OrdersDTO ordersDTO);
}
