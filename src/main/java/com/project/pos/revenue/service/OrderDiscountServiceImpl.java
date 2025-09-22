package com.project.pos.revenue.service;

import com.project.pos.discount.dto.DiscountDTO;
import com.project.pos.discount.entity.Discount;
import com.project.pos.discount.service.DiscountService;
import com.project.pos.revenue.dto.OrdersDTO;
import com.project.pos.revenue.dto.OrderDiscountDTO;
import com.project.pos.revenue.entity.Orders;
import com.project.pos.revenue.entity.OrderDiscount;
import com.project.pos.revenue.entity.OrderDiscountID;
import com.project.pos.revenue.repository.OrderDiscountRepository;
import com.project.pos.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDiscountServiceImpl implements OrderDiscountService {
    private final OrderDiscountRepository orderDiscountRepository;
    private final StoreService storeService;
    private final OrderService orderService;
    private final DiscountService discountService;

    @Override
    public OrderDiscount getOrderDiscountById(Long orderKey, Long discountKey){
        return orderDiscountRepository.findById(new OrderDiscountID(orderKey, discountKey)).orElse(null);
    }
    @Override
    public OrderDiscountDTO getOrderDiscountDTOById(Long orderKey, Long discountKey){
        OrderDiscount entity=getOrderDiscountById(orderKey, discountKey);
        if(entity==null) return null;
        return entityToDto(entity);
    }

    @Override
    public OrderDiscountDTO createOrderDiscount(OrderDiscountDTO dto){
        OrderDiscount entity=dtoToEntity(dto);
        OrderDiscount saved=orderDiscountRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    public void deleteOrderDiscount(Long orderKey, Long discountKey){
        orderDiscountRepository.deleteById(new OrderDiscountID(orderKey, discountKey));
    }

    @Override
    public OrderDiscount dtoToEntity(OrderDiscountDTO dto){
        return OrderDiscount.builder()
                .id(new OrderDiscountID(dto.getOrderKey(), dto.getDiscountKey()))
                .discountPrice(dto.getDiscountPrice())
                .orders(dto.getOrdersDTO()!=null? orderService.dtoToEntity(dto.getOrdersDTO()):null)
                .discount(dto.getDiscountDTO()!=null? discountService.dtoToEntity(dto.getDiscountDTO()):null)
                .build();
    }
    @Override
    public OrderDiscountDTO entityToDto(OrderDiscount entity){
        return OrderDiscountDTO.builder()
                .orderKey(entity.getId().getOrderKey())
                .discountKey(entity.getId().getDiscountKey())
                .discountPrice(entity.getDiscountPrice())
                .ordersDTO(entity.getOrders()!=null? orderService.entityToDto(entity.getOrders()):null)
                .discountDTO(entity.getDiscount()!=null? discountService.entityToDto(entity.getDiscount()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public List<Long> getOrderDiscountList(OrdersDTO orderDTO){
        Orders order = orderService.dtoToEntity(orderDTO);
        return orderDiscountRepository.getOrderDiscountList(order);
    }
    @Override
    public List<DiscountDTO> getDiscountListByOnlyOrder(OrdersDTO orderDTO){
        Orders order = orderService.dtoToEntity(orderDTO);
        List<Discount> entityList = orderDiscountRepository.getDiscountListByOnlyOrder(order);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(discountService::entityToDto).toList();
    }
    @Override
    public List<DiscountDTO> getDiscountListByOrder(OrdersDTO orderDTO, Boolean typeValue){
        Orders order = orderService.dtoToEntity(orderDTO);
        List<Discount> entityList = orderDiscountRepository.getDiscountListByOrder(order, typeValue);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(discountService::entityToDto).toList();
    }
}
