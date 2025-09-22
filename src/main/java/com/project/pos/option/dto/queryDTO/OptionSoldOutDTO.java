package com.project.pos.option.dto.queryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OptionSoldOutDTO {
    private Long id;
    private Long soldOutCount;
}
