package com.project.pos.discount.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiscountPageDTO {
    private Long id;
    private Long number;
    private String name;
    private String category;
    private Long cost;
    private Boolean typeValue;
}
