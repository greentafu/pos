package com.project.pos.revenue.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReceiptMethodDTO {
    private Long receiptKey;
    private Long paymentMethodKey;
    @NotNull(message = "{notNull.price}")
    private Long issuePrice;
    @NotNull(message = "{notNull.status}")
    private Boolean status;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.receipt}")
    private ReceiptDTO receiptDTO;
    @NotNull(message = "{notNull.payment_method}")
    private PaymentMethodDTO paymentMethodDTO;
}
