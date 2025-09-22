package com.project.pos.employee.dto.queryDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class WorkTimeDTO {
    private Long id;
    private String date;
    private Integer time;
    private Integer wage;
}
