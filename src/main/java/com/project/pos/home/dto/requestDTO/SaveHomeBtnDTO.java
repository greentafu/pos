package com.project.pos.home.dto.requestDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveHomeBtnDTO {
    private Long id;
    private Integer page;
    private Integer index;
}
