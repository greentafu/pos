package com.project.pos.stock.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockCategoryPageDTO {
    private Long id;
    private String name;
    private Long count;
}
