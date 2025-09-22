package com.project.pos.revenue.dto;

import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrdersDTO {
    private Long id;
    @NotNull(message = "{notNull.quantity}")
    private Long orderAmount;
    @NotNull(message = "{notNull.discount_order}")
    private Long orderDiscountAmount;
    @NotNull(message = "{notNull.discount_total}")
    private Long totalDiscountAmount;
    @NotNull(message = "{notNull.price}")
    private Long totalPaymentAmount;
    private Integer waiting;
    private String receiptNumber;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
    @NotNull(message = "{notNull.pos}")
    private PosDTO posDTO;
}
