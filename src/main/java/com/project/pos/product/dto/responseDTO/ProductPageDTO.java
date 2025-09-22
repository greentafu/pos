package com.project.pos.product.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductPageDTO {
    private Long id;
    private Long number;
    private String name;
    private String category;
    private Long productPrice;
}
