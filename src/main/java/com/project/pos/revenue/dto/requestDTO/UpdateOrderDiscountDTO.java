package com.project.pos.revenue.dto.requestDTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateOrderDiscountDTO {
    private Integer waiting;
    private List<Long> discountList;

}
