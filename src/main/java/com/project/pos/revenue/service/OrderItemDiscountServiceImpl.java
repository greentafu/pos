package com.project.pos.revenue.service;

import com.project.pos.discount.dto.DiscountDTO;
import com.project.pos.discount.entity.Discount;
import com.project.pos.discount.service.DiscountService;
import com.project.pos.revenue.dto.OrderItemDTO;
import com.project.pos.revenue.dto.OrderItemDiscountDTO;
import com.project.pos.revenue.entity.OrderItem;
import com.project.pos.revenue.entity.OrderItemDiscount;
import com.project.pos.revenue.entity.OrderItemDiscountID;
import com.project.pos.revenue.repository.OrderItemDiscountRepository;
import com.project.pos.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemDiscountServiceImpl implements OrderItemDiscountService {
    private final OrderItemDiscountRepository orderItemDiscountRepository;
    private final StoreService storeService;
    private final OrderItemService orderItemService;
    private final DiscountService discountService;

    @Override
    public OrderItemDiscount getOrderItemDiscountById(Long orderItemKey, Long discountKey){
        return orderItemDiscountRepository.findById(new OrderItemDiscountID(orderItemKey, discountKey)).orElse(null);
    }
    @Override
    public OrderItemDiscountDTO getOrderItemDiscountDTOById(Long orderItemKey, Long discountKey){
        OrderItemDiscount entity=getOrderItemDiscountById(orderItemKey, discountKey);
        if(entity==null) return null;
        return entityToDto(entity);
    }

    @Override
    public OrderItemDiscountDTO createOrderItemDiscount(OrderItemDiscountDTO dto){
        OrderItemDiscount entity=dtoToEntity(dto);
        OrderItemDiscount saved=orderItemDiscountRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    public void deleteOrderItemDiscount(Long orderItemKey, Long discountKey){
        orderItemDiscountRepository.deleteById(new OrderItemDiscountID(orderItemKey, discountKey));
    }
    @Override
    @Transactional
    public void deleteOrderItemDiscountByOrderItem(OrderItemDTO orderItemDTO){
        OrderItem orderItem = orderItemService.dtoToEntity(orderItemDTO);
        orderItemDiscountRepository.deleteOrderItemDiscountByOrderItem(orderItem);
    }

    @Override
    public OrderItemDiscount dtoToEntity(OrderItemDiscountDTO dto){
        return OrderItemDiscount.builder()
                .id(new OrderItemDiscountID(dto.getOrderItemKey(), dto.getDiscountKey()))
                .discountPrice(dto.getDiscountPrice())
                .orderItem(dto.getOrderItemDTO()!=null? orderItemService.dtoToEntity(dto.getOrderItemDTO()):null)
                .discount(dto.getDiscountDTO()!=null? discountService.dtoToEntity(dto.getDiscountDTO()):null)
                .build();
    }
    @Override
    public OrderItemDiscountDTO entityToDto(OrderItemDiscount entity){
        return OrderItemDiscountDTO.builder()
                .orderItemKey(entity.getId().getOrderItemKey())
                .discountKey(entity.getId().getDiscountKey())
                .discountPrice(entity.getDiscountPrice())
                .orderItemDTO(entity.getOrderItem()!=null? orderItemService.entityToDto(entity.getOrderItem()):null)
                .discountDTO(entity.getDiscount()!=null? discountService.entityToDto(entity.getDiscount()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public List<Long> getOrderItemDiscountList(OrderItemDTO orderItemDTO){
        OrderItem orderItem = orderItemService.dtoToEntity(orderItemDTO);
        return orderItemDiscountRepository.getOrderItemDiscountList(orderItem);
    }
    @Override
    public List<DiscountDTO> getDiscountListByOnlyOrderItem(OrderItemDTO orderItemDTO){
        OrderItem orderItem = orderItemService.dtoToEntity(orderItemDTO);
        List<Discount> entityList = orderItemDiscountRepository.getDiscountListByOnlyOrderItem(orderItem);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(discountService::entityToDto).toList();
    }
    @Override
    public List<DiscountDTO> getDiscountListByOrderItem(OrderItemDTO orderItemDTO, Boolean discountType, Boolean typeValue){
        OrderItem orderItem = orderItemService.dtoToEntity(orderItemDTO);
        List<Discount> entityList = orderItemDiscountRepository.getDiscountListByOrderItem(orderItem, discountType, typeValue);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(discountService::entityToDto).toList();
    }
}
