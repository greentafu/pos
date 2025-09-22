package com.project.pos.revenue.dto.responseDTO;

import com.project.pos.revenue.dto.PaymentMethodDTO;
import com.project.pos.revenue.dto.ReceiptDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentReceiptDTO {
    private PaymentMethodDTO methodDTO;
    private ReceiptDTO receiptDTO;
}
