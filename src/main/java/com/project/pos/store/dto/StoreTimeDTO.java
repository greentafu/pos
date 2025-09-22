package com.project.pos.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StoreTimeDTO {
    private Long storeId;
    private Long dayOfWeekId;
    @NotBlank(message = "{notBlank.start_time}")
    private String startTime;
    @NotBlank(message = "{notBlank.end_time}")
    private String endTime;
    @NotNull(message = "{notNull.status}")
    private Boolean statusValue;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
    private DayOfWeekDTO dayOfWeekDTO;
}
