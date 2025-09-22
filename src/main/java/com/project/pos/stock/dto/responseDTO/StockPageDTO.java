package com.project.pos.stock.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockPageDTO {
    private Long id;
    private Long number;
    private String name;
    private String category;
    private Long stockCost;
    private String unit;
}
