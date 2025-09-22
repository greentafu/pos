package com.project.pos.stock.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveStockMovementDTO {
    private Long id;
    @NotBlank(message = "{notBlank.quantity}")
    private String movementAmount;
    @NotNull(message = "{notNull.movement_type}")
    private Integer typeValue;
    private Boolean plusType;
    private String notes;
}
