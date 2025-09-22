package com.project.pos.revenue.service;

import com.project.pos.product.service.ProductService;
import com.project.pos.revenue.dto.OrderItemDTO;
import com.project.pos.revenue.dto.OrdersDTO;
import com.project.pos.revenue.entity.OrderItem;
import com.project.pos.revenue.entity.Orders;
import com.project.pos.revenue.repository.OrderItemRepository;
import com.project.pos.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final StoreService storeService;
    private final OrderService orderService;
    private final ProductService productService;

    @Override
    public OrderItem getOrderItemById(Long id){
        return orderItemRepository.findById(id).orElse(null);
    }
    @Override
    public OrderItemDTO getOrderItemDTOById(Long id){
        OrderItem entity=getOrderItemById(id);
        if(entity==null) return null;
        return entityToDto(entity);
    }

    @Override
    public OrderItemDTO createOrderItem(OrderItemDTO dto){
        OrderItem entity=dtoToEntity(dto);
        OrderItem saved=orderItemRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    public Long deleteOrderItem(Long id){
        orderItemRepository.deleteById(id);
        return id;
    }
    @Override
    @Transactional
    public OrderItemDTO updateOrderItemCount(Long id, Long count){
        OrderItem entity = getOrderItemById(id);
        entity.setProductCount(count);
        return entityToDto(entity);
    }
    @Override
    @Transactional
    public OrderItemDTO updateOrderItem(Long id){
        OrderItem entity = getOrderItemById(id);

        Long productPrice = entity.getProduct().getProductPrice();
        Long productCount = entity.getProductCount();
        Long optionAmount = orderItemRepository.getOptionAmount(id);
        if(optionAmount==null) optionAmount = 0L;
        Long perBasic = orderItemRepository.getPerBasic(id);
        if(perBasic==null) perBasic = 0L;
        Long perPercent = orderItemRepository.getPerPercent(id);
        if(perPercent==null) perPercent = 0L;
        Long allBasic = orderItemRepository.getAllBasic(id);
        if(allBasic==null) allBasic = 0L;
        Long allPercent = orderItemRepository.getAllPercent(id);
        if(allPercent==null) allPercent = 0L;

        Long basicPrice = productPrice+optionAmount;
        entity.setTotalOption(optionAmount);
        entity.setProductPerUnit(basicPrice);
        entity.setTotalPerUnit(basicPrice * productCount);

        Long totalPayment = basicPrice * productCount;
        Long totalDiscount = 0L;

        Long tempDiscount = perBasic * productCount;
        totalPayment -= tempDiscount;
        totalDiscount += tempDiscount;
        basicPrice -= perBasic;

        tempDiscount = Math.round(basicPrice * (perPercent/100.0) * productCount);
        totalPayment -= tempDiscount;
        totalDiscount += tempDiscount;

        tempDiscount = allBasic;
        totalPayment -= tempDiscount;
        totalDiscount += tempDiscount;

        tempDiscount = Math.round(totalPayment * (allPercent/100.0));
        totalPayment -= tempDiscount;
        totalDiscount += tempDiscount;

        entity.setTotalDiscount(totalDiscount);
        entity.setTotalPayment(totalPayment);

        return entityToDto(entity);
    }

    @Override
    public OrderItem dtoToEntity(OrderItemDTO dto){
        return OrderItem.builder()
                .id(dto.getId())
                .name(dto.getName())
                .productPrice(dto.getProductPrice())
                .productCost(dto.getProductCost())
                .productCount(dto.getProductCount())
                .productPerUnit(dto.getProductPerUnit())
                .totalOption(dto.getTotalOption())
                .totalDiscount(dto.getTotalDiscount())
                .totalPerUnit(dto.getTotalPerUnit())
                .totalPayment(dto.getTotalPayment())
                .copyLocation(dto.getCopyLocation())
                .product(dto.getProductDTO()!=null? productService.dtoToEntity(dto.getProductDTO()):null)
                .orders(dto.getOrdersDTO()!=null? orderService.dtoToEntity(dto.getOrdersDTO()):null)
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public OrderItemDTO entityToDto(OrderItem entity){
        return OrderItemDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .productPrice(entity.getProductPrice())
                .productCost(entity.getProductCost())
                .productCount(entity.getProductCount())
                .productPerUnit(entity.getProductPerUnit())
                .totalOption(entity.getTotalOption())
                .totalDiscount(entity.getTotalDiscount())
                .totalPerUnit(entity.getTotalPerUnit())
                .totalPayment(entity.getTotalPayment())
                .copyLocation(entity.getCopyLocation())
                .productDTO(entity.getProduct()!=null? productService.entityToDto(entity.getProduct()):null)
                .ordersDTO(entity.getOrders()!=null? orderService.entityToDto(entity.getOrders()):null)
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public OrderItemDTO getOrderItemByCopyId(OrdersDTO ordersDTO, Long id){
        Orders orders = orderService.dtoToEntity(ordersDTO);
        OrderItem entity = orderItemRepository.getOrderItemByCopyId(orders, id);
        if(entity==null) return null;
        return entityToDto(entity);
    }
    @Override
    public OrderItemDTO getOrderItemByCopyLocation(OrdersDTO ordersDTO, Long copyLocation){
        Orders orders = orderService.dtoToEntity(ordersDTO);
        OrderItem entity = orderItemRepository.getOrderItemByCopyLocation(orders, copyLocation);
        if(entity==null) return null;
        return entityToDto(entity);
    }
    @Override
    public List<OrderItemDTO> getOrderItemByOrders(OrdersDTO ordersDTO){
        Orders orders = orderService.dtoToEntity(ordersDTO);
        List<OrderItem> entityList = orderItemRepository.getOrderItemListByOrders(orders);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
}
