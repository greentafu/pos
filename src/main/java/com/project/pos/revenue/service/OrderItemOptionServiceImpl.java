package com.project.pos.revenue.service;

import com.project.pos.option.dto.OptionDTO;
import com.project.pos.option.entity.Option;
import com.project.pos.option.service.OptionService;
import com.project.pos.revenue.dto.OrderItemDTO;
import com.project.pos.revenue.dto.OrderItemOptionDTO;
import com.project.pos.revenue.entity.OrderItem;
import com.project.pos.revenue.entity.OrderItemOption;
import com.project.pos.revenue.entity.OrderItemOptionID;
import com.project.pos.revenue.repository.OrderItemOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemOptionServiceImpl implements OrderItemOptionService {
    private final OrderItemOptionRepository orderItemOptionRepository;
    private final OrderItemService orderItemService;
    private final OptionService optionService;

    @Override
    public OrderItemOption getOrderItemOptionById(Long orderItemKey, Long optionKey){
        return orderItemOptionRepository.findById(new OrderItemOptionID(orderItemKey, optionKey)).orElse(null);
    }
    @Override
    public OrderItemOptionDTO getOrderItemOptionDTOById(Long orderItemKey, Long optionKey){
        OrderItemOption entity=getOrderItemOptionById(orderItemKey, optionKey);
        if(entity==null) return null;
        return entityToDto(entity);
    }

    @Override
    public OrderItemOptionDTO createOrderItemOption(OrderItemOptionDTO dto){
        OrderItemOption entity=dtoToEntity(dto);
        OrderItemOption saved=orderItemOptionRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    public void deleteOrderItemOption(Long orderItemKey, Long optionKey){
        orderItemOptionRepository.deleteById(new OrderItemOptionID(orderItemKey, optionKey));
    }
    @Override
    @Transactional
    public void deleteOrderItemOptionByOrderItem(OrderItemDTO orderItemDTO){
        OrderItem orderItem = orderItemService.dtoToEntity(orderItemDTO);
        orderItemOptionRepository.deleteOrderItemOptionByOrderItem(orderItem);
    }

    @Override
    public OrderItemOption dtoToEntity(OrderItemOptionDTO dto){
        return OrderItemOption.builder()
                .id(new OrderItemOptionID(dto.getOrderItemKey(), dto.getOptionKey()))
                .optionName(dto.getOptionName())
                .optionPrice(dto.getOptionPrice())
                .optionCost(dto.getOptionCost())
                .orderItem(dto.getOrderItemDTO()!=null? orderItemService.dtoToEntity(dto.getOrderItemDTO()):null)
                .option(dto.getOptionDTO()!=null? optionService.dtoToEntity(dto.getOptionDTO()):null)
                .build();
    }
    @Override
    public OrderItemOptionDTO entityToDto(OrderItemOption entity){
        return OrderItemOptionDTO.builder()
                .orderItemKey(entity.getId().getOrderItemKey())
                .optionKey(entity.getId().getOptionKey())
                .optionName(entity.getOptionName())
                .optionPrice(entity.getOptionPrice())
                .optionCost(entity.getOptionCost())
                .orderItemDTO(entity.getOrderItem()!=null? orderItemService.entityToDto(entity.getOrderItem()):null)
                .optionDTO(entity.getOption()!=null? optionService.entityToDto(entity.getOption()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public List<Long> getOrderItemOptionList(OrderItemDTO orderItemDTO){
        OrderItem orderItem = orderItemService.dtoToEntity(orderItemDTO);
        return orderItemOptionRepository.getOrderItemOptionList(orderItem);
    }
    @Override
    public List<OptionDTO> getOptionListByOrderItem(OrderItemDTO orderItemDTO){
        OrderItem orderItem = orderItemService.dtoToEntity(orderItemDTO);
        List<Option> entityList = orderItemOptionRepository.getOptionListByOrderItem(orderItem);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(optionService::entityToDto).toList();
    }
}
