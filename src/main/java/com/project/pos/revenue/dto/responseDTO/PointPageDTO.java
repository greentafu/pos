package com.project.pos.revenue.dto.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PointPageDTO {
    private Long id;
    private LocalDateTime regDate;
    private Long changingPoint;
    private Boolean typeValue;
    private Long remainingPoint;
}
