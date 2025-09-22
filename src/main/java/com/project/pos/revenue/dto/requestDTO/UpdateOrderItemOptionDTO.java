package com.project.pos.revenue.dto.requestDTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateOrderItemOptionDTO {
    private Long orderItemId;
    private List<Long> optionList;
}
