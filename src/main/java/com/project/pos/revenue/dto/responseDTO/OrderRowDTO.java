package com.project.pos.revenue.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderRowDTO {
    private String rowType;
    private Long mainId;
    private Long id;
    private String name;
    private String perPrice;
    private Long count;
    private String discountPrice;
    private String totalPrice;
}
