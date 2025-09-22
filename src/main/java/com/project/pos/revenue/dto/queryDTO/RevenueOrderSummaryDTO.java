package com.project.pos.revenue.dto.queryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevenueOrderSummaryDTO {
    private Long orderCount;
    private Long price;
    private Long discount1;
    private Long discount2;
    private Long totalPayment;
    private Long cash;
    private Long card;
    private Long point;
    private Long statusCount;
}
