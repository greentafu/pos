package com.project.pos.option.dto.requestDTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SaveCategoryArrangementDTO {
    private List<Long> nonArrangementList;
    private List<Long> arrangementList;
}
