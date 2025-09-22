package com.project.pos.product.dto.queryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProductQuantityDTO {
    private Long id;
    private Long cost;
    private Long quantity;
}
