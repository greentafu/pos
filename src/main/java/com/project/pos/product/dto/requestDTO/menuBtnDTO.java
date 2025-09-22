package com.project.pos.product.dto.requestDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class menuBtnDTO {
    private Integer page;
    private Integer index;
    private Integer color;
    private Integer size;
    private String name;
    private String price;
}
