package com.project.pos.stock.dto;

import com.project.pos.store.dto.StoreDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StockCategoryDTO {
    private Long id;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    @NotNull(message = "{notNull.deleted}")
    private Boolean deleted;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
}
