package com.project.pos.revenue.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NonIssueReceiptDTO {
    private Long id;
    private Long paymentAmount;
    private LocalDateTime regDate;
    private Long totalPrice;
}
