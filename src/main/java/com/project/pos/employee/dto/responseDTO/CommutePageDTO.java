package com.project.pos.employee.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommutePageDTO {
    private Long id;
    private Long number;
    private String name;
    private String jobTitle;
    private String startTime;
    private String endTime;
    private String notes;
}
