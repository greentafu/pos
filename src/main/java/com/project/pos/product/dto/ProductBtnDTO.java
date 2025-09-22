package com.project.pos.product.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductBtnDTO {
    private Long id;
    @NotNull(message = "{notNull.page}")
    private Integer page;
    @NotNull(message = "{notNull.index}")
    private Integer indexValue;
    @NotNull(message = "{notNull.color}")
    private Integer color;
    @NotNull(message = "{notNull.size}")
    private Integer size;

    @NotNull(message = "{notNull.product}")
    private ProductDTO productDTO;
}
