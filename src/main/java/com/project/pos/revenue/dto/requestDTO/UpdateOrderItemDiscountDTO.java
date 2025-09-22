package com.project.pos.revenue.dto.requestDTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateOrderItemDiscountDTO {
    private Long orderItemId;
    private List<Long> discountList;

}
