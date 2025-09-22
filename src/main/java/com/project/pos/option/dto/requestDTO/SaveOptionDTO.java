package com.project.pos.option.dto.requestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SaveOptionDTO {
    private Long id;
    @NotNull(message = "{notNull.number}")
    private Long number;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    private String displayName;
    @NotBlank(message = "{notBlank.price}")
    private String optionPrice;
    @NotBlank(message = "{notBlank.cost}")
    private String optionCost;
    private Boolean soldOutType;
    private Long optionCategory;
    @Valid
    private List<ReceiptDTO> receiptList;
}
