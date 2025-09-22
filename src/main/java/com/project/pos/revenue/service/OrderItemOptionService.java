package com.project.pos.revenue.service;

import com.project.pos.option.dto.OptionDTO;
import com.project.pos.revenue.dto.OrderItemDTO;
import com.project.pos.revenue.dto.OrderItemOptionDTO;
import com.project.pos.revenue.entity.OrderItemOption;

import java.util.List;

public interface OrderItemOptionService {
    OrderItemOption getOrderItemOptionById(Long orderItemKey, Long optionKey);
    OrderItemOptionDTO getOrderItemOptionDTOById(Long orderItemKey, Long optionKey);

    OrderItemOptionDTO createOrderItemOption(OrderItemOptionDTO dto);
    void deleteOrderItemOption(Long orderItemKey, Long optionKey);
    void deleteOrderItemOptionByOrderItem(OrderItemDTO orderItemDTO);

    OrderItemOption dtoToEntity(OrderItemOptionDTO dto);
    OrderItemOptionDTO entityToDto(OrderItemOption entity);

    List<Long> getOrderItemOptionList(OrderItemDTO orderItemDTO);
    List<OptionDTO> getOptionListByOrderItem(OrderItemDTO orderItemDTO);
}
