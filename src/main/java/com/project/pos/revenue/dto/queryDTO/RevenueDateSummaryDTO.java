package com.project.pos.revenue.dto.queryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RevenueDateSummaryDTO {
    private Long price;
    private Long discount1;
    private Long discount2;
    private Long totalPayment;
    private Long count;
    private Long cost1;
    private Long cost2;
}
