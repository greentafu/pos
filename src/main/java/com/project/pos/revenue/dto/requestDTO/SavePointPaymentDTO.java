package com.project.pos.revenue.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SavePointPaymentDTO {
    @NotNull(message = "{notNull.waiting}")
    private Integer waiting;
    @NotNull(message = "{notNull.price_received}")
    private Long received;
    @NotBlank(message = "{notBlank.tel_number}")
    private String phoneNumber;
}
