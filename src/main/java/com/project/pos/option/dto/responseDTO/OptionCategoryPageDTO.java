package com.project.pos.option.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OptionCategoryPageDTO {
    private Long id;
    private String name;
    private Long count;
}
