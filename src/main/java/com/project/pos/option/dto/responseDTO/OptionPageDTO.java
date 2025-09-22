package com.project.pos.option.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OptionPageDTO {
    private Long id;
    private Long number;
    private String name;
    private String category;
    private Long optionPrice;
}
