package com.project.pos.discount.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DiscountCategoryPageDTO {
    private Long id;
    private String name;
    private Long count;
}
