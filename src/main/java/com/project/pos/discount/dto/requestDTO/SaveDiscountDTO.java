package com.project.pos.discount.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveDiscountDTO {
    private Long id;
    @NotNull(message = "{notNull.number}")
    private Long number;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    private String displayName;
    @NotNull(message = "{notNull.type}")
    private Boolean discountType;
    @NotNull(message = "{notNull.type}")
    private Boolean typeValue;
    @NotBlank(message = "{notBlank.price}")
    private String price;
    private Long category;
}
