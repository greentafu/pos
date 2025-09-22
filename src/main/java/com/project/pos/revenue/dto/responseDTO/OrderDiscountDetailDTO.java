package com.project.pos.revenue.dto.responseDTO;

import com.project.pos.discount.dto.DiscountDTO;
import com.project.pos.revenue.dto.OrdersDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderDiscountDetailDTO {
    private OrdersDTO ordersDTO;
    private List<DiscountDTO> discountList;
}
