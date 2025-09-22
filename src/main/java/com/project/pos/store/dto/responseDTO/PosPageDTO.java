package com.project.pos.store.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PosPageDTO {
    private Long id;
    private Long number;
    private String name;
    private String location;
    private Boolean status;
}
