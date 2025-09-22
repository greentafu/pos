package com.project.pos.revenue.dto.requestDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateOrderItemLocationDTO {
    private Integer waiting;
    private Long id;
    private Integer type;
}
