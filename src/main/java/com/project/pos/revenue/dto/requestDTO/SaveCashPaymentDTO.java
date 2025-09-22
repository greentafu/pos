package com.project.pos.revenue.dto.requestDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveCashPaymentDTO {
    @NotNull(message = "{notNull.waiting}")
    private Integer waiting;
    @NotNull(message = "{notNull.price_received}")
    private Long received;
    @NotNull(message = "{notNull.price}")
    private Long price;
    @NotNull(message = "{notNull.change}")
    private Long change;
}
