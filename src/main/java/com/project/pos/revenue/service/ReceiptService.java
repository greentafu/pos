package com.project.pos.revenue.service;

import com.project.pos.revenue.dto.ReceiptDTO;
import com.project.pos.revenue.entity.Receipt;

public interface ReceiptService {
    Receipt getReceiptById(Long id);
    ReceiptDTO getReceiptDTOById(Long id);

    ReceiptDTO createReceipt(ReceiptDTO dto);
    Long deleteReceipt(Long id);

    Receipt dtoToEntity(ReceiptDTO dto);
    ReceiptDTO entityToDto(Receipt entity);
}
