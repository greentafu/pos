package com.project.pos.revenue.dto;

import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentMethodDTO {
    private Long id;
    @NotNull(message = "{notNull.type}")
    private Integer typeValue;
    @NotNull(message = "{notNull.price}")
    private Long paymentAmount;
    private Long received;
    private Long changeValue;
    @NotNull(message = "{notNull.status}")
    private Boolean status;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
    @NotNull(message = "{notNull.pos}")
    private PosDTO posDTO;
    @NotNull(message = "{notNull.payment}")
    private PaymentDTO paymentDTO;
}
