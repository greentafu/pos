package com.project.pos.revenue.service;

import com.project.pos.revenue.dto.OrdersDTO;
import com.project.pos.revenue.dto.PaymentDTO;
import com.project.pos.revenue.dto.PaymentMethodDTO;
import com.project.pos.revenue.dto.responseDTO.NonIssueReceiptDTO;
import com.project.pos.revenue.entity.Orders;
import com.project.pos.revenue.entity.Payment;
import com.project.pos.revenue.entity.PaymentMethod;
import com.project.pos.revenue.repository.PaymentMethodRepository;
import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.entity.Pos;
import com.project.pos.store.service.PosService;
import com.project.pos.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {
    private final PaymentMethodRepository paymentMethodRepository;
    private final StoreService storeService;
    private final PosService posService;
    private final PaymentService paymentService;
    private final OrderService orderService;

    @Override
    public PaymentMethod getPaymentMethodById(Long id){
        return paymentMethodRepository.findById(id).orElse(null);
    }
    @Override
    public PaymentMethodDTO getPaymentMethodDTOById(Long id){
        PaymentMethod entity=getPaymentMethodById(id);
        if(entity==null) return null;
        return entityToDto(entity);
    }

    @Override
    public PaymentMethodDTO createPaymentMethod(PaymentMethodDTO dto){
        PaymentMethod entity=dtoToEntity(dto);
        PaymentMethod saved=paymentMethodRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public Long deletePaymentMethod(Long id){
        PaymentMethod entity = getPaymentMethodById(id);
        if(entity==null) return null;
        entity.setStatus(false);
        return id;
    }

    @Override
    public PaymentMethod dtoToEntity(PaymentMethodDTO dto){
        return PaymentMethod.builder()
                .id(dto.getId())
                .typeValue(dto.getTypeValue())
                .paymentAmount(dto.getPaymentAmount())
                .received(dto.getReceived())
                .changeValue(dto.getChangeValue())
                .status(dto.getStatus())
                .pos(dto.getPosDTO()!=null? posService.dtoToEntity(dto.getPosDTO()):null)
                .payment(dto.getPaymentDTO()!=null? paymentService.dtoToEntity(dto.getPaymentDTO()):null)
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public PaymentMethodDTO entityToDto(PaymentMethod entity){
        return PaymentMethodDTO.builder()
                .id(entity.getId())
                .typeValue(entity.getTypeValue())
                .paymentAmount(entity.getPaymentAmount())
                .received(entity.getReceived())
                .changeValue(entity.getChangeValue())
                .status(entity.getStatus())
                .posDTO(entity.getPos()!=null? posService.entityToDto(entity.getPos()):null)
                .paymentDTO(entity.getPayment()!=null? paymentService.entityToDto(entity.getPayment()):null)
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public Long getPaymentPriceByPos(PosDTO posDTO, LocalDateTime regDate, Integer typeValue){
        Pos pos=posService.dtoToEntity(posDTO);
        return paymentMethodRepository.getPaymentPriceByPos(pos, regDate, typeValue);
    }
    @Override
    public Long getCancelPaymentPriceByPos(PosDTO posDTO, LocalDateTime regDate, Integer typeValue){
        Pos pos=posService.dtoToEntity(posDTO);
        return paymentMethodRepository.getCancelPaymentPriceByPos(pos, regDate, typeValue);
    }
    @Override
    public Long getPaymentPriceByOrder(OrdersDTO ordersDTO){
        Orders orders=orderService.dtoToEntity(ordersDTO);
        return paymentMethodRepository.getPaymentPriceByOrder(orders);
    }
    @Override
    public Long getCountPaymentMethodByPayment(PaymentDTO paymentDTO){
        Payment payment = paymentService.dtoToEntity(paymentDTO);
        return paymentMethodRepository.getCountPaymentMethod(payment);
    }
    @Override
    public Long getCountProgressPaymentMethodByPayment(PaymentDTO paymentDTO){
        Payment payment = paymentService.dtoToEntity(paymentDTO);
        return paymentMethodRepository.getCountProgressPaymentMethod(payment);
    }
    @Override
    public List<PaymentMethodDTO> getPaymentMethodListByPayment(PaymentDTO paymentDTO){
        Payment payment = paymentService.dtoToEntity(paymentDTO);
        List<PaymentMethod> entityList = paymentMethodRepository.getPaymentMethodByPayment(payment);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<NonIssueReceiptDTO> getNonIssuePaymentMethodList(PaymentDTO paymentDTO){
        Payment payment = paymentService.dtoToEntity(paymentDTO);
        List<NonIssueReceiptDTO> tempList = paymentMethodRepository.getNonIssuePaymentList(payment);
        if(tempList.isEmpty()) return null;
        List<NonIssueReceiptDTO> result = new ArrayList<>();
        for(NonIssueReceiptDTO dto:tempList){ if(dto.getPaymentAmount()>dto.getTotalPrice()) result.add(dto); }
        return result.isEmpty()?null:result;
    }
    @Override
    public List<NonIssueReceiptDTO> getNonIssuePaymentMethodByIdList(List<Long> idList){
        if(idList.isEmpty() || idList==null) return null;
        List<NonIssueReceiptDTO> result = paymentMethodRepository.getNonIssuePaymentListByIdList(idList);
        return result.isEmpty()?null:result;
    }
}
