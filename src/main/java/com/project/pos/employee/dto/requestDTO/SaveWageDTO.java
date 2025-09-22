package com.project.pos.employee.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveWageDTO {
    private Long id;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    @NotBlank(message = "{notBlank.per_wage}")
    private String perWage;
    private String notes;
}
