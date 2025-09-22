package com.project.pos.option.dto;

import com.project.pos.stock.dto.StockDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OptionReceiptDTO {
    private Long optionId;
    private Long stockId;
    @NotNull(message = "{notNull.quantity}")
    private Long quantity;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.stock}")
    private StockDTO stockDTO;
    @NotNull(message = "{notNull.option}")
    private OptionDTO optionDTO;
}
