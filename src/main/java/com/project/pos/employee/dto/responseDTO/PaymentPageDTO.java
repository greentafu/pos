package com.project.pos.employee.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentPageDTO {
    private Long id;
    private Long number;
    private String name;
    private String wageName;
    private Long perWage;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
