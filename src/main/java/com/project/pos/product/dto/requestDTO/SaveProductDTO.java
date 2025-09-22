package com.project.pos.product.dto.requestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SaveProductDTO {
    private Long id;
    @NotNull(message = "{notNull.number}")
    private Long number;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    private String displayName;
    @NotBlank(message = "{notBlank.price}")
    private String productPrice;
    @NotBlank(message = "{notBlank.cost}")
    private String productCost;
    private Boolean soldOutType;
    private Long productCategory;
    @Valid
    private List<ReceiptDTO> receiptList;
}
