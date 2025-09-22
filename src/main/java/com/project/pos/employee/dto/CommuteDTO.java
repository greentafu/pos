package com.project.pos.employee.dto;

import com.project.pos.store.dto.StoreDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommuteDTO {
    private Long id;
    @NotNull(message = "{notNull.start_time}")
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @NotBlank(message = "{notBlank.position}")
    private String jobTitle;
    @NotNull(message = "{notNull.per_wage}")
    private Long perWage;
    private String notes;
    @NotNull(message = "{notNull.modify}")
    private Boolean modified;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
    @NotNull(message = "{notNull.employee}")
    private EmployeeDTO employeeDTO;
}
