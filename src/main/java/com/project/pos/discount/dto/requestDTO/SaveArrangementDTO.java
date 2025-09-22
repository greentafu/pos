package com.project.pos.discount.dto.requestDTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SaveArrangementDTO {
    private List<Long> nonArrangementList;
    private List<Long> arrangementList;
}
