package com.project.pos.revenue.dto;

import com.project.pos.discount.dto.DiscountDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderItemDiscountDTO {
    private Long orderItemKey;
    private Long discountKey;
    @NotNull(message = "{notNull.price}")
    private Long discountPrice;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.order_item}")
    private OrderItemDTO orderItemDTO;
    @NotNull(message = "{notNull.discount}")
    private DiscountDTO discountDTO;
}
