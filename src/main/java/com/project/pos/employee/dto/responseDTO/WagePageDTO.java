package com.project.pos.employee.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WagePageDTO {
    private Long id;
    private String name;
    private Long perWage;
    private Long employeeCount;
}
