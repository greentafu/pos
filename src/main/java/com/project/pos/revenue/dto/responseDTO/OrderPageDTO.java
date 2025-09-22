package com.project.pos.revenue.dto.responseDTO;

import com.project.pos.revenue.dto.OrdersDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderPageDTO {
    private OrdersDTO ordersDTO;
    private Boolean status;
    private Long paymentPrice;
    private Long totalCount;
    private List<OrderRowDTO> rowList;
}
