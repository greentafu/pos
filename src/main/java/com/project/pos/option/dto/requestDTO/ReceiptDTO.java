package com.project.pos.option.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReceiptDTO {
    @NotNull(message = "{notNull.id}")
    private Long id;
    @NotBlank(message = "{notBlank.quantity}")
    private String quantity;
}
