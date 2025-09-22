package com.project.pos.store.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DayOfWeekDTO {
    private Long id;
    private String dayText;
}
