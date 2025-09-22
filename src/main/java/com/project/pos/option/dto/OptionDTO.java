package com.project.pos.option.dto;

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
public class OptionDTO {
    private Long id;
    @NotNull(message = "{notNull.number}")
    private Long number;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    private String displayName;
    @NotNull(message = "{notNull.price}")
    private Long optionPrice;
    @NotNull(message = "{notNull.cost}")
    private Long optionCost;
    private Long arrangement;
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
    private OptionCategoryDTO optionCategoryDTO;

    public OptionDTO(Long id, Long number, String name, String displayName, Long optionPrice, Long optionCost,
                     Long arrangement, Boolean soldOutType, Boolean soldOut) {
        this.id=id;
        this.number=number;
        this.name=name;
        this.displayName=displayName;
        this.optionPrice=optionPrice;
        this.optionCost=optionCost;
        this.arrangement=arrangement;
        this.soldOutType=soldOutType;
        this.soldOut=soldOut;
    }

    public OptionDTO(Long id, String displayName, Long optionPrice) {
        this.id = id;
        this.displayName = displayName;
        this.optionPrice = optionPrice;
    }
}
