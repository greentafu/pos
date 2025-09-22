package com.project.pos.option.dto.responseDTO;

import com.project.pos.option.dto.OptionDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OptionArrangementDTO {
    private List<OptionDTO> nonArrayList;
    private List<OptionDTO> arrayList;
}
