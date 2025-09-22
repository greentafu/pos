package com.project.pos.revenue.dto.requestDTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SaveOrderOptionDTO {
    private Integer waiting;
    private Long product;
    private List<Long> optionList;

}
