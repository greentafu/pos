package com.project.pos.revenue.service;

import com.project.pos.revenue.dto.OrdersDTO;
import com.project.pos.revenue.dto.PaymentDTO;
import com.project.pos.revenue.dto.PaymentMethodDTO;
import com.project.pos.revenue.dto.responseDTO.NonIssueReceiptDTO;
import com.project.pos.revenue.entity.PaymentMethod;
import com.project.pos.store.dto.PosDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentMethodService {
    PaymentMethod getPaymentMethodById(Long id);
    PaymentMethodDTO getPaymentMethodDTOById(Long id);

    PaymentMethodDTO createPaymentMethod(PaymentMethodDTO dto);
    Long deletePaymentMethod(Long id);

    PaymentMethod dtoToEntity(PaymentMethodDTO dto);
    PaymentMethodDTO entityToDto(PaymentMethod entity);

    Long getPaymentPriceByPos(PosDTO posDTO, LocalDateTime regDate, Integer typeValue);
    Long getCancelPaymentPriceByPos(PosDTO posDTO, LocalDateTime regDate, Integer typeValue);
    Long getPaymentPriceByOrder(OrdersDTO ordersDTO);
    Long getCountPaymentMethodByPayment(PaymentDTO paymentDTO);
    Long getCountProgressPaymentMethodByPayment(PaymentDTO paymentDTO);
    List<PaymentMethodDTO> getPaymentMethodListByPayment(PaymentDTO paymentDTO);
    List<NonIssueReceiptDTO> getNonIssuePaymentMethodList(PaymentDTO paymentDTO);
    List<NonIssueReceiptDTO> getNonIssuePaymentMethodByIdList(List<Long> idList);
}
