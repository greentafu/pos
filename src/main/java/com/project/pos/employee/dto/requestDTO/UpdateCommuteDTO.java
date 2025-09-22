package com.project.pos.employee.dto.requestDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCommuteDTO {
    private Long id;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
}
