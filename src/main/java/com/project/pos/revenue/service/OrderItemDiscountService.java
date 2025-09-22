package com.project.pos.revenue.service;

import com.project.pos.discount.dto.DiscountDTO;
import com.project.pos.revenue.dto.OrderItemDTO;
import com.project.pos.revenue.dto.OrderItemDiscountDTO;
import com.project.pos.revenue.entity.OrderItemDiscount;

import java.util.List;

public interface OrderItemDiscountService {
    OrderItemDiscount getOrderItemDiscountById(Long orderItemKey, Long discountKey);
    OrderItemDiscountDTO getOrderItemDiscountDTOById(Long orderItemKey, Long discountKey);

    OrderItemDiscountDTO createOrderItemDiscount(OrderItemDiscountDTO dto);
    void deleteOrderItemDiscount(Long orderItemKey, Long discountKey);
    void deleteOrderItemDiscountByOrderItem(OrderItemDTO orderItemDTO);

    OrderItemDiscount dtoToEntity(OrderItemDiscountDTO dto);
    OrderItemDiscountDTO entityToDto(OrderItemDiscount entity);

    List<Long> getOrderItemDiscountList(OrderItemDTO orderItemDTO);
    List<DiscountDTO> getDiscountListByOnlyOrderItem(OrderItemDTO orderItemDTO);
    List<DiscountDTO> getDiscountListByOrderItem(OrderItemDTO orderItemDTO, Boolean discountType, Boolean typeValue);
}
