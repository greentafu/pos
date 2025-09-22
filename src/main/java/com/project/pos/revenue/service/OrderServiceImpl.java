package com.project.pos.revenue.service;

import com.project.pos.dslDTO.PageRequestDTO;
import com.project.pos.revenue.dto.OrdersDTO;
import com.project.pos.revenue.dto.queryDTO.*;
import com.project.pos.revenue.dto.responseDTO.ReceiptPageDTO;
import com.project.pos.revenue.entity.Orders;
import com.project.pos.revenue.repository.OrdersRepository;
import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import com.project.pos.store.entity.Pos;
import com.project.pos.store.entity.Store;
import com.project.pos.store.service.PosService;
import com.project.pos.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrdersRepository ordersRepository;
    private final StoreService storeService;
    private final PosService posService;

    @Override
    public Orders getOrdersById(Long id){
        return ordersRepository.findById(id).orElse(null);
    }
    @Override
    public OrdersDTO getOrdersDTOById(Long id){
        Orders entity=getOrdersById(id);
        if(entity==null) return null;
        return entityToDto(entity);
    }

    @Override
    public OrdersDTO createOrders(OrdersDTO dto){
        Orders entity=dtoToEntity(dto);
        Orders saved=ordersRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public Long deleteOrders(Long id){
        ordersRepository.deleteById(id);
        return id;
    }
    @Override
    @Transactional
    public OrdersDTO updateOrders(Long id){
        Orders entity = getOrdersById(id);
        if(entity==null) return null;

        Long itemPrice = ordersRepository.getItemAmount(id);
        if(itemPrice==null) itemPrice = 0L;
        Long itemDiscount = ordersRepository.getItemDiscount(id);
        if(itemDiscount==null) itemDiscount = 0L;
        Long basicDiscount = ordersRepository.getAllBasic(id);
        if(basicDiscount==null) basicDiscount = 0L;
        Long percentDiscount = ordersRepository.getAllPercent(id);
        if(percentDiscount==null) percentDiscount = 0L;

        Long totalPayment = itemPrice - itemDiscount;
        Long totalDiscount = 0L;

        Long tempDiscount = basicDiscount;
        totalPayment -= tempDiscount;
        totalDiscount += tempDiscount;

        tempDiscount = Math.round(totalPayment * (percentDiscount/100.0));
        totalPayment -= tempDiscount;
        totalDiscount += tempDiscount;

        entity.setOrderAmount(itemPrice);
        entity.setOrderDiscountAmount(itemDiscount);
        entity.setTotalDiscountAmount(totalDiscount);
        entity.setTotalPaymentAmount(totalPayment);

        return entityToDto(entity);
    }
    @Override
    @Transactional
    public OrdersDTO updateOrdersWaiting(Long id, Integer waiting){
        Orders entity = getOrdersById(id);
        if(entity==null) return null;
        entity.setWaiting(waiting);
        if(waiting==null) entity.setReceiptNumber(generateApprovalNumber());
        return entityToDto(entity);
    }
    private String generateApprovalNumber() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return date + "-" + uuid;
    }

    @Override
    public Orders dtoToEntity(OrdersDTO dto){
        return Orders.builder()
                .id(dto.getId())
                .orderAmount(dto.getOrderAmount())
                .orderDiscountAmount(dto.getOrderDiscountAmount())
                .totalDiscountAmount(dto.getTotalDiscountAmount())
                .totalPaymentAmount(dto.getTotalPaymentAmount())
                .waiting(dto.getWaiting())
                .receiptNumber(dto.getReceiptNumber())
                .pos(dto.getPosDTO()!=null? posService.dtoToEntity(dto.getPosDTO()):null)
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public OrdersDTO entityToDto(Orders entity){
        return OrdersDTO.builder()
                .id(entity.getId())
                .orderAmount(entity.getOrderAmount())
                .orderDiscountAmount(entity.getOrderDiscountAmount())
                .totalDiscountAmount(entity.getTotalDiscountAmount())
                .totalPaymentAmount(entity.getTotalPaymentAmount())
                .waiting(entity.getWaiting())
                .receiptNumber(entity.getReceiptNumber())
                .posDTO(entity.getPos()!=null? posService.entityToDto(entity.getPos()):null)
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public OrdersDTO getOrdersByStorePosWaiting(StoreDTO storeDTO, PosDTO posDTO, Integer waiting){
        Store store = storeService.dtoToEntity(storeDTO);
        Pos pos = posService.dtoToEntity(posDTO);
        Orders entity = ordersRepository.getOrdersByStorePosWaiting(store, pos, waiting);
        if(entity==null) return null;
        return entityToDto(entity);
    }
    @Override
    public Page<ReceiptPageDTO> getReceiptPage(PageRequestDTO requestDTO){
        return ordersRepository.getReceiptPage(requestDTO);
    }
    @Override
    public Page<RevenueOrderPageDTO> getRevenueOrderPage(PageRequestDTO requestDTO){
        return ordersRepository.getRevenueOrderPage(requestDTO);
    }
    @Override
    public RevenueOrderSummaryDTO getRevenueOrderSummary(PageRequestDTO requestDTO){
        return ordersRepository.getRevenueOrderSummary(requestDTO);
    }
    @Override
    public Page<RevenueDatePageDTO> getRevenueDatePage(PageRequestDTO requestDTO){
        return ordersRepository.getRevenueDatePage(requestDTO);
    }
    @Override
    public RevenueDateSummaryDTO getRevenueDateSummary(PageRequestDTO requestDTO){
        return ordersRepository.getRevenueDateSummary(requestDTO);
    }
    @Override
    public Page<RevenueMenuPageDTO> getRevenueMenuPage(PageRequestDTO requestDTO){
        return ordersRepository.getRevenueMenuPage(requestDTO);
    }
    @Override
    public RevenueMenuSummaryDTO getRevenueMenuSummary(PageRequestDTO requestDTO){
        return ordersRepository.getRevenueMenuSummary(requestDTO);
    }
    @Override
    public List<RevenueMenuPageDTO> getRevenueMenuChartList(PageRequestDTO requestDTO){
        return ordersRepository.getRevenueMenuChartList(requestDTO);
    }
}
