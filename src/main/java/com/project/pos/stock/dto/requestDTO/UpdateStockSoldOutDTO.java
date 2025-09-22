package com.project.pos.stock.dto.requestDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateStockSoldOutDTO {
    private Long id;
    @NotNull(message = "{notNull.sold_out_type}")
    private Boolean soldOutType;
}
