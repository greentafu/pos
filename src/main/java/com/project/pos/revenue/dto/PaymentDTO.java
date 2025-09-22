package com.project.pos.revenue.dto;

import com.project.pos.store.dto.StoreDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentDTO {
    private Long id;
    @NotNull(message = "{notNull.price_total}")
    private Long totalAmount;
    private Boolean status;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
    @NotNull(message = "{notNull.order}")
    private OrdersDTO ordersDTO;
}
