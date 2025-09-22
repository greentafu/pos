package com.project.pos.revenue.service;

import com.project.pos.revenue.dto.OrdersDTO;
import com.project.pos.revenue.dto.PaymentDTO;
import com.project.pos.revenue.entity.Payment;
import com.project.pos.store.dto.PosDTO;

import java.time.LocalDateTime;

public interface PaymentService {
    Payment getPaymentById(Long id);
    PaymentDTO getPaymentDTOById(Long id);

    PaymentDTO createPayment(PaymentDTO dto);
    PaymentDTO updatePaymentStatus(Long id, Boolean status);

    Payment dtoToEntity(PaymentDTO dto);
    PaymentDTO entityToDto(Payment entity);

    Long getPaymentCountByPos(PosDTO posDTO, LocalDateTime regDate);
    PaymentDTO getPaymentByOrders(OrdersDTO ordersDTO);

}
