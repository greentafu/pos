package com.project.pos.store.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScreenArrangementDTO {
    private Long storeId;
    private Long screenId;
    @NotNull(message = "{notNull.page}")
    private Integer page;
    @NotNull(message = "{notNull.index}")
    private Integer indexValue;

    @NotNull(message = "{notNull.screen}")
    private ScreenDTO screenDTO;
    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
}
