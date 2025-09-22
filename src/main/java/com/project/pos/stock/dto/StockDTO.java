package com.project.pos.stock.dto;

import com.project.pos.store.dto.StoreDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StockDTO {
    private Long id;
    @NotNull(message = "{notNull.number}")
    private Long number;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    @NotNull(message = "{notNull.cost}")
    private Long stockCost;
    @NotBlank(message = "{notBlank.unit}")
    private String unit;
    @NotNull(message = "{notNull.quantity}")
    private Long quantity;
    @NotNull(message = "{notNull.sold_out}")
    private Boolean soldOut;
    @NotNull(message = "{notNull.deleted}")
    private Boolean deleted;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
    private StockCategoryDTO stockCategoryDTO;
}
