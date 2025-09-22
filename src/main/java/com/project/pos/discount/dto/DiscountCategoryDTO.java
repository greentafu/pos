package com.project.pos.discount.dto;

import com.project.pos.store.dto.StoreDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DiscountCategoryDTO {
    private Long id;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    private Long arrangement;
    @NotNull(message = "{notNull.multi}")
    private Boolean multi;
    @NotNull(message = "{notNull.deleted}")
    private Boolean deleted;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
}
