package com.project.pos.revenue.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePointDTO {
    private Long id;
    @NotBlank(message = "{notBlank.point_change}")
    private String changingPoint;
}
