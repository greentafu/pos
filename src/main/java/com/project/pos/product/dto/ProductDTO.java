package com.project.pos.product.dto;

import com.project.pos.store.dto.StoreDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProductDTO {
    private Long id;
    @NotNull(message = "{notNull.number}")
    private Long number;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    private String displayName;
    @NotNull(message = "{notNull.price}")
    private Long productPrice;
    @NotNull(message = "{notNull.cost}")
    private Long productCost;
    @NotNull(message = "{notNull.sold_out_type}")
    private Boolean soldOutType;
    @NotNull(message = "{notNull.sold_out}")
    private Boolean soldOut;
    @NotNull(message = "{notNull.deleted}")
    private Boolean deleted;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
    private ProductCategoryDTO productCategoryDTO;
}
