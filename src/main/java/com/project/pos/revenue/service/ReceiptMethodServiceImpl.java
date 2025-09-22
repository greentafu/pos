package com.project.pos.revenue.service;

import com.project.pos.revenue.dto.PaymentDTO;
import com.project.pos.revenue.dto.PaymentMethodDTO;
import com.project.pos.revenue.dto.ReceiptDTO;
import com.project.pos.revenue.dto.ReceiptMethodDTO;
import com.project.pos.revenue.entity.*;
import com.project.pos.revenue.repository.ReceiptMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceiptMethodServiceImpl implements ReceiptMethodService {
    private final ReceiptMethodRepository receiptMethodRepository;
    private final ReceiptService receiptService;
    private final PaymentMethodService paymentMethodService;
    private final PaymentService paymentService;

    @Override
    public ReceiptMethod getReceiptMethodById(Long receiptKey, Long paymentMethodKey){
        return receiptMethodRepository.findById(new ReceiptMethodID(receiptKey, paymentMethodKey)).orElse(null);
    }
    @Override
    public ReceiptMethodDTO getReceiptMethodDTOById(Long receiptKey, Long paymentMethodKey){
        ReceiptMethod entity=getReceiptMethodById(receiptKey, paymentMethodKey);
        if(entity==null) return null;
        return entityToDto(entity);
    }

    @Override
    public ReceiptMethodDTO createReceiptMethod(ReceiptMethodDTO dto){
        ReceiptMethod entity=dtoToEntity(dto);
        ReceiptMethod saved=receiptMethodRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public void deleteReceiptMethod(Long receiptKey, Long paymentMethodKey){
        ReceiptMethod entity=getReceiptMethodById(receiptKey, paymentMethodKey);
        entity.setStatus(false);
    }

    @Override
    public ReceiptMethod dtoToEntity(ReceiptMethodDTO dto){
        return ReceiptMethod.builder()
                .id(new ReceiptMethodID(dto.getReceiptKey(), dto.getPaymentMethodKey()))
                .issuePrice(dto.getIssuePrice())
                .status(dto.getStatus())
                .receipt(dto.getReceiptDTO()!=null? receiptService.dtoToEntity(dto.getReceiptDTO()):null)
                .paymentMethod(dto.getPaymentMethodDTO()!=null? paymentMethodService.dtoToEntity(dto.getPaymentMethodDTO()):null)
                .build();
    }
    @Override
    public ReceiptMethodDTO entityToDto(ReceiptMethod entity){
        return ReceiptMethodDTO.builder()
                .receiptKey(entity.getId().getReceiptKey())
                .paymentMethodKey(entity.getId().getPaymentMethodKey())
                .issuePrice(entity.getIssuePrice())
                .status(entity.getStatus())
                .receiptDTO(entity.getReceipt()!=null? receiptService.entityToDto(entity.getReceipt()):null)
                .paymentMethodDTO(entity.getPaymentMethod()!=null? paymentMethodService.entityToDto(entity.getPaymentMethod()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }

    @Override
    public List<ReceiptMethodDTO> getAllReceiptMethodByPaymentMethod(PaymentMethodDTO paymentMethodDTO){
        PaymentMethod paymentMethod = paymentMethodService.dtoToEntity(paymentMethodDTO);
        List<ReceiptMethod> entityList = receiptMethodRepository.getAllReceiptMethodByPaymentMethod(paymentMethod);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<ReceiptMethodDTO> getTrueReceiptMethodByPaymentMethod(PaymentMethodDTO paymentMethodDTO){
        PaymentMethod paymentMethod = paymentMethodService.dtoToEntity(paymentMethodDTO);
        List<ReceiptMethod> entityList = receiptMethodRepository.getTrueReceiptMethodByPaymentMethod(paymentMethod);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
    @Override
    public List<ReceiptDTO> getCashReceiptByPayment(PaymentDTO paymentDTO){
        Payment payment = paymentService.dtoToEntity(paymentDTO);
        List<Receipt> entityList = receiptMethodRepository.getCashReceiptListByPayment(payment);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(receiptService::entityToDto).toList();
    }
    @Override
    public List<ReceiptDTO> getCancelCashReceiptByPayment(PaymentDTO paymentDTO){
        Payment payment = paymentService.dtoToEntity(paymentDTO);
        List<Receipt> entityList = receiptMethodRepository.getCancelCashReceiptListByPayment(payment);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(receiptService::entityToDto).toList();
    }
    @Override
    public List<ReceiptDTO> getCardReceiptByPayment(PaymentDTO paymentDTO){
        Payment payment = paymentService.dtoToEntity(paymentDTO);
        List<Receipt> entityList = receiptMethodRepository.getCardReceiptListByPayment(payment);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(receiptService::entityToDto).toList();
    }
    @Override
    public List<ReceiptDTO> getCancelCardReceiptByPayment(PaymentDTO paymentDTO){
        Payment payment = paymentService.dtoToEntity(paymentDTO);
        List<Receipt> entityList = receiptMethodRepository.getCancelCardReceiptListByPayment(payment);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(receiptService::entityToDto).toList();
    }
    @Override
    public List<ReceiptMethodDTO> getReceiptMethodByReceipt(ReceiptDTO receiptDTO){
        Receipt receipt = receiptService.dtoToEntity(receiptDTO);
        List<ReceiptMethod> entityList = receiptMethodRepository.getReceiptMethodByReceipt(receipt);
        if(entityList.isEmpty()) return null;
        return entityList.stream().map(this::entityToDto).toList();
    }
}
