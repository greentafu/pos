package com.project.pos.product.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductCategoryPageDTO {
    private Long id;
    private String name;
    private Long count;
}
