package com.project.pos.option.dto.queryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OptionQuantityDTO {
    private Long id;
    private Long cost;
    private Long quantity;
}
