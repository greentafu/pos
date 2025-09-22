package com.project.pos.revenue.dto.queryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevenueOrderSmallSummaryDTO {
    private Long orderCount;
    private Long cash;
    private Long card;
    private Long point;
    private Long statusCount;
}
