package com.project.pos.product.dto.requestDTO;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class SaveMenuBtnDTO {
    private Map<String, menuBtnDTO> menuMap;
    private Long searchCategory;
}
