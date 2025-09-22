package com.project.pos.stock.dto.requestDTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SaveSoldOutDTO {
    private List<String> trueList;
    private List<String> falseList;
}
