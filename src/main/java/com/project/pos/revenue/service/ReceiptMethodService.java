package com.project.pos.revenue.service;

import com.project.pos.revenue.dto.PaymentDTO;
import com.project.pos.revenue.dto.PaymentMethodDTO;
import com.project.pos.revenue.dto.ReceiptDTO;
import com.project.pos.revenue.dto.ReceiptMethodDTO;
import com.project.pos.revenue.entity.ReceiptMethod;

import java.util.List;

public interface ReceiptMethodService {
    ReceiptMethod getReceiptMethodById(Long receiptKey, Long paymentMethodKey);
    ReceiptMethodDTO getReceiptMethodDTOById(Long receiptKey, Long paymentMethodKey);

    ReceiptMethodDTO createReceiptMethod(ReceiptMethodDTO dto);
    void deleteReceiptMethod(Long receiptKey, Long paymentMethodKey);

    ReceiptMethod dtoToEntity(ReceiptMethodDTO dto);
    ReceiptMethodDTO entityToDto(ReceiptMethod entity);

    List<ReceiptMethodDTO> getAllReceiptMethodByPaymentMethod(PaymentMethodDTO paymentMethodDTO);
    List<ReceiptMethodDTO> getTrueReceiptMethodByPaymentMethod(PaymentMethodDTO paymentMethodDTO);
    List<ReceiptDTO> getCashReceiptByPayment(PaymentDTO paymentDTO);
    List<ReceiptDTO> getCancelCashReceiptByPayment(PaymentDTO paymentDTO);
    List<ReceiptDTO> getCardReceiptByPayment(PaymentDTO paymentDTO);
    List<ReceiptDTO> getCancelCardReceiptByPayment(PaymentDTO paymentDTO);
    List<ReceiptMethodDTO> getReceiptMethodByReceipt(ReceiptDTO receiptDTO);
}
