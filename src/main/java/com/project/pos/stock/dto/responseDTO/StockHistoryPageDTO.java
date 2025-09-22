package com.project.pos.stock.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StockHistoryPageDTO {
    private Long id;
    private LocalDateTime date;
    private Long number;
    private String name;
    private Integer type;
    private Long changedCount;
    private Long presentCount;
    private Long cost;
    private String notes;
}
