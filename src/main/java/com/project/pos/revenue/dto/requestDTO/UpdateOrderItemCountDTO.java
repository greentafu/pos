package com.project.pos.revenue.dto.requestDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateOrderItemCountDTO {
    private Integer waiting;
    private Long id;
    private Long count;
}
