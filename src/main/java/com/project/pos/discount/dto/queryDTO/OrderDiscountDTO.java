package com.project.pos.discount.dto.queryDTO;

import com.project.pos.discount.dto.DiscountDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderDiscountDTO {
    private Long id;
    private String name;
    private Long arrangement;
    private Boolean multi;
    private List<DiscountDTO> discountList;
}
