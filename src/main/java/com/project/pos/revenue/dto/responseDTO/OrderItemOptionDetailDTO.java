package com.project.pos.revenue.dto.responseDTO;

import com.project.pos.option.dto.OptionDTO;
import com.project.pos.revenue.dto.OrderItemDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderItemOptionDetailDTO {
    private OrderItemDTO orderItemDTO;
    private List<OptionDTO> optionList;
}
