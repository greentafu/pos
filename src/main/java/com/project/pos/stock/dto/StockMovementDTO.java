package com.project.pos.stock.dto;

import com.project.pos.store.dto.StoreDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StockMovementDTO {
    private Long id;
    @NotNull(message = "{notNull.quantity}")
    private Long movementAmount;
    @NotNull(message = "{notNull.quantity}")
    private Long presentAmount;
    @NotNull(message = "{notNull.cost}")
    private Long stockCost;
    @NotNull(message = "{notNull.movement_type}")
    private Integer typeValue;
    @NotNull(message = "{notNull.plusType}")
    private Boolean plusType;
    private String notes;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
    @NotNull(message = "{notNull.stock}")
    private StockDTO stockDTO;
}
