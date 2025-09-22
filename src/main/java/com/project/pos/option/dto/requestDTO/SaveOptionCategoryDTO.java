package com.project.pos.option.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveOptionCategoryDTO {
    private Long id;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    @NotNull(message = "{notNull.multi}")
    private Boolean multi;
}
