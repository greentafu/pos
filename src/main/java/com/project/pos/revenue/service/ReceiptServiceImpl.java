package com.project.pos.revenue.service;

import com.project.pos.revenue.dto.ReceiptDTO;
import com.project.pos.revenue.entity.Receipt;
import com.project.pos.revenue.repository.ReceiptRepository;
import com.project.pos.store.service.PosService;
import com.project.pos.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final StoreService storeService;
    private final PosService posService;

    @Override
    public Receipt getReceiptById(Long id){
        return receiptRepository.findById(id).orElse(null);
    }
    @Override
    public ReceiptDTO getReceiptDTOById(Long id){
        Receipt entity=getReceiptById(id);
        if(entity==null) return null;
        return entityToDto(entity);
    }

    @Override
    public ReceiptDTO createReceipt(ReceiptDTO dto){
        Receipt entity=dtoToEntity(dto);
        Receipt saved=receiptRepository.save(entity);
        return entityToDto(saved);
    }
    @Override
    @Transactional
    public Long deleteReceipt(Long id){
        Receipt entity = getReceiptById(id);
        if(entity==null) return null;
        entity.setStatus(false);
        return id;
    }

    @Override
    public Receipt dtoToEntity(ReceiptDTO dto){
        return Receipt.builder()
                .id(dto.getId())
                .totalAmount(dto.getTotalAmount())
                .cardCompany(dto.getCardCompany())
                .cardNumber(dto.getCardNumber())
                .cardMonth(dto.getCardMonth())
                .cashReceiptType(dto.getCashReceiptType())
                .cashReceiptNumber(dto.getCashReceiptNumber())
                .status(dto.getStatus())
                .pos(dto.getPosDTO()!=null? posService.dtoToEntity(dto.getPosDTO()):null)
                .store(dto.getStoreDTO()!=null? storeService.dtoToEntity(dto.getStoreDTO()):null)
                .build();
    }
    @Override
    public ReceiptDTO entityToDto(Receipt entity){
        return ReceiptDTO.builder()
                .id(entity.getId())
                .totalAmount(entity.getTotalAmount())
                .cardCompany(entity.getCardCompany())
                .cardNumber(entity.getCardNumber())
                .cardMonth(entity.getCardMonth())
                .cashReceiptType(entity.getCashReceiptType())
                .cashReceiptNumber(entity.getCashReceiptNumber())
                .authorizationDate(entity.getAuthorizationDate())
                .authorizationNumber(entity.getAuthorizationNumber())
                .receiptNumber(entity.getReceiptNumber())
                .status(entity.getStatus())
                .posDTO(entity.getPos()!=null? posService.entityToDto(entity.getPos()):null)
                .storeDTO(entity.getStore()!=null? storeService.entityToDto(entity.getStore()):null)
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
    }
}
