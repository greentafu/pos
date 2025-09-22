package com.project.pos.revenue.dto;

import com.project.pos.option.dto.OptionDTO;
import com.project.pos.store.dto.StoreDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderItemOptionDTO {
    private Long orderItemKey;
    private Long optionKey;
    @NotBlank(message = "{notBlank.name}")
    private String optionName;
    @NotNull(message = "{notNull.price}")
    private Long optionPrice;
    @NotNull(message = "{notNull.cost}")
    private Long optionCost;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.order_item}")
    private OrderItemDTO orderItemDTO;
    @NotNull(message = "{notNull.option}")
    private OptionDTO optionDTO;
}
