package com.project.pos.revenue.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReceiptPageDTO {
    private Long orderId;
    private LocalDateTime finishTime;
    private String receiptNumber;
    private Long price;
    private Boolean status;
}
