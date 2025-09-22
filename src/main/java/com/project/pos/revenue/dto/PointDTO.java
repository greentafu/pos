package com.project.pos.revenue.dto;

import com.project.pos.store.dto.MemberDTO;
import com.project.pos.store.dto.StoreDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PointDTO {
    private Long id;
    @NotNull(message = "{notNull.point_change}")
    private Long changingPoint;
    @NotNull(message = "{notNull.point_remain}")
    private Long remainingPoint;
    @NotNull(message = "{notNull.type}")
    private Boolean typeValue;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
    @NotNull(message = "{notNull.member}")
    private MemberDTO memberDTO;
    private PaymentMethodDTO paymentMethodDTO;
    private PaymentDTO paymentDTO;
}
