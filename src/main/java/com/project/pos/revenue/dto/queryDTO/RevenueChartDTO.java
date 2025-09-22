package com.project.pos.revenue.dto.queryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RevenueChartDTO {
    private String name;
    private Integer net;
    private Long profit;
    private Double percent;
    private Double totalPercent;
}
