package com.project.pos.revenue.dto.responseDTO;

import com.project.pos.discount.dto.DiscountDTO;
import com.project.pos.revenue.dto.OrderItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderItemDiscountDetailDTO {
    private OrderItemDTO orderItemDTO;
    private List<DiscountDTO> discountList;
}
