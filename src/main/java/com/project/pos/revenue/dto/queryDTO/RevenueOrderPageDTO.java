package com.project.pos.revenue.dto.queryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RevenueOrderPageDTO {
    private Long orderId;
    private LocalDateTime date;
    private Long price;
    private Long discount1;
    private Long discount2;
    private Long totalPayment;
    private Long cash;
    private Long card;
    private Long point;
    private String pos;
    private Boolean status;
    private Long count;
    private String product;
}
