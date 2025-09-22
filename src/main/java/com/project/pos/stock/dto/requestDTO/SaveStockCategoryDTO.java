package com.project.pos.stock.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveStockCategoryDTO {
    private Long id;
    @NotBlank(message = "{notBlank.name}")
    private String name;
}
