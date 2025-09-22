package com.project.pos.product.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveProductCategoryDTO {
    private Long id;
    @NotBlank(message = "{notBlank.name}")
    private String name;
}
