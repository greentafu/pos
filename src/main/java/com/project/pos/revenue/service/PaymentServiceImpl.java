package com.project.pos.revenue.service;

import com.project.pos.discount.service.DiscountService;
import com.project.pos.revenue.dto.OrdersDTO;
import com.project.pos.revenue.dto.PaymentDTO;
import com.project.pos.revenue.entity.Orders;
import com.project.pos.revenue.entity.Payment;
import com.project.pos.revenue.repository.PaymentRepository;
import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.entity.Pos;
import com.project.pos.store.service.PosService;
import com.project.pos.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final StoreService storeService;
    private final OrderService orderService;
    private final PosService posService;

    @Override
    public Payment getPaymentById(Long id){
        return paymentRepository.findById(id).orElse(null);
    }
    @Override
    public PaymentDTO getPaymentDTOById(Long id){
        Payment entity=getPaymentById(id);
        if(entity==null) return null;
        return entityToDto(entity);
    }

    @Override
    public PaymentDTO createPayment(PaymentDTO dto){
        Payment entity=dtoToEntity(dto);
        Payment saved=paymentRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public PaymentDTO updatePaymentStatus(Long id, Boolean status){
        Payment entity = getPaymentById(id);
        if(entity==null) return null;
        entity.setStatus(status);
        return entityToDto(entity);
    }

    @Override
    public Payment dtoToEntity(PaymentDTO dto){
        return Payment.builder()
                .id(dto.getId())
                .totalAmount(dto.getTotalAmount())
                .status(dto.getStatus())
                .orders(dto.getOrdersDTO()!=null? orderService.dtoToEntity(dto.getOrdersDTO()):null)
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public PaymentDTO entityToDto(Payment entity){
        return PaymentDTO.builder()
                .id(entity.getId())
                .totalAmount(entity.getTotalAmount())
                .status(entity.getStatus())
                .ordersDTO(entity.getOrders()!=null? orderService.entityToDto(entity.getOrders()):null)
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public Long getPaymentCountByPos(PosDTO posDTO, LocalDateTime regDate){
        Pos pos=posService.dtoToEntity(posDTO);
        return paymentRepository.getPaymentCountByPos(pos, regDate);
    }
    @Override
    public PaymentDTO getPaymentByOrders(OrdersDTO ordersDTO){
        Orders orders = orderService.dtoToEntity(ordersDTO);
        Payment entity = paymentRepository.getPaymentByOrders(orders);
        if(entity==null) return null;
        return entityToDto(entity);
    }
}
