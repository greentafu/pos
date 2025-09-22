package com.project.pos.store.dto;

import com.project.pos.employee.dto.JobTitleDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ScreenAuthorityDTO {
    private Long jobTitleId;
    private Long screenId;
    @NotNull(message = "{notNull.authority}")
    private Boolean authority;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.jobTitle}")
    private JobTitleDTO jobTitleDTO;
    @NotNull(message = "{notNull.screen}")
    private ScreenDTO screenDTO;
}
