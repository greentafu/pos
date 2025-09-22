package com.project.pos.discount.dto;

import com.project.pos.store.dto.StoreDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDTO {
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
    @NotBlank(message = "{notNull.price}")
    private Long discountPrice;
    private Long arrangement;
    @NotNull(message = "{notNull.deleted}")
    private Boolean deleted;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
    private DiscountCategoryDTO discountCategoryDTO;

    public DiscountDTO(Long id, Long number, String name, String displayName, Boolean typeValue, Long discountPrice, Long arrangement) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.displayName = displayName;
        this.typeValue = typeValue;
        this.discountPrice = discountPrice;
        this.arrangement = arrangement;
    }

    public DiscountDTO(Long id, String displayName, Boolean discountType, Boolean typeValue, Long discountPrice) {
        this.id = id;
        this.displayName = displayName;
        this.discountType = discountType;
        this.typeValue = typeValue;
        this.discountPrice = discountPrice;
    }
}
