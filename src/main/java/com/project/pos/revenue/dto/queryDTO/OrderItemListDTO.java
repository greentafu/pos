package com.project.pos.revenue.dto.queryDTO;

import com.project.pos.discount.dto.DiscountDTO;
import com.project.pos.option.dto.OptionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderItemListDTO {
    private Long id;
    private String name;
    private Long perPrice;
    private Long count;
    private List<OptionDTO> itemOptionList;
    private List<DiscountDTO> itemDiscountList;
    private List<DiscountDTO> orderDiscountList;
}
