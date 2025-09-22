package com.project.pos.option.dto.queryDTO;

import com.project.pos.option.dto.OptionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderOptionDTO {
    private Long id;
    private String name;
    private Long arrangement;
    private Boolean multi;
    private List<OptionDTO> optionList;
}
