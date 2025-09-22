package com.project.pos.revenue.repositoryDSL;

import com.project.pos.revenue.dto.responseDTO.NonIssueReceiptDTO;
import com.project.pos.revenue.entity.Payment;

import java.util.List;

public interface PaymentMethodRepositoryCustom {
    List<NonIssueReceiptDTO> getNonIssuePaymentList(Payment payment);
    List<NonIssueReceiptDTO> getNonIssuePaymentListByIdList(List<Long> idList);
}
