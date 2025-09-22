package com.project.pos.revenue.service;

import com.project.pos.discount.dto.DiscountDTO;
import com.project.pos.revenue.dto.OrderDiscountDTO;
import com.project.pos.revenue.dto.OrdersDTO;
import com.project.pos.revenue.entity.OrderDiscount;

import java.util.List;

public interface OrderDiscountService {
    OrderDiscount getOrderDiscountById(Long orderKey, Long discountKey);
    OrderDiscountDTO getOrderDiscountDTOById(Long orderKey, Long discountKey);

    OrderDiscountDTO createOrderDiscount(OrderDiscountDTO dto);
    void deleteOrderDiscount(Long orderKey, Long discountKey);

    OrderDiscount dtoToEntity(OrderDiscountDTO dto);
    OrderDiscountDTO entityToDto(OrderDiscount entity);

    List<Long> getOrderDiscountList(OrdersDTO orderDTO);
    List<DiscountDTO> getDiscountListByOnlyOrder(OrdersDTO orderDTO);
    List<DiscountDTO> getDiscountListByOrder(OrdersDTO orderDTO, Boolean typeValue);
}
