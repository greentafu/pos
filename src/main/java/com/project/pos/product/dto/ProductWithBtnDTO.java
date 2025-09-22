package com.project.pos.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductWithBtnDTO {
    private ProductDTO productDTO;
    private ProductBtnDTO productBtnDTO;
}
