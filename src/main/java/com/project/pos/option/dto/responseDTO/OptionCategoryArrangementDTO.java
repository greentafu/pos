package com.project.pos.option.dto.responseDTO;

import com.project.pos.option.dto.OptionCategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OptionCategoryArrangementDTO {
    private List<OptionCategoryDTO> nonArrayList;
    private List<OptionCategoryDTO> arrayList;
}
