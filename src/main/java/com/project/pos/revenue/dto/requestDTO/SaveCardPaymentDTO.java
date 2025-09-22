package com.project.pos.revenue.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveCardPaymentDTO {
    @NotNull(message = "{notNull.waiting}")
    private Integer waiting;
    @NotNull(message = "{notNull.price_received}")
    private Long received;
    @NotBlank(message = "{notBlank.card_company}")
    private String cardCompany;
    @NotBlank(message = "{notBlank.card_number}")
    private String cardNumber;
    @NotNull(message = "{notNull.card_month}")
    private Integer cardMonth;
}
