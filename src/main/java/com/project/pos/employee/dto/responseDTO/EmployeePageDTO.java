package com.project.pos.employee.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EmployeePageDTO {
    private Long id;
    private Long number;
    private String name;
    private String jobTitle;
    private LocalDateTime regDate;
    private String telNumber;
}
