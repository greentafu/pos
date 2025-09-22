package com.project.pos.employee.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SaveJobTitleDTO {
    private Long id;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    @NotNull(message = "{notNull.wage}")
    private Long wage;
    private List<Boolean> screenAuthList;
}
