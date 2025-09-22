package com.project.pos.store.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TimeDTO {
    @NotBlank(message = "{notBlank.start_time}")
    private String startTime;
    @NotBlank(message = "{notBlank.end_time}")
    private String endTime;
    @NotNull(message = "{notNull.status}")
    private Boolean statusValue;
}
