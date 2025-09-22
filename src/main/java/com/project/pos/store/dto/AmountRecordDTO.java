package com.project.pos.store.dto;

import com.project.pos.employee.dto.EmployeeDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AmountRecordDTO {
    private Long id;
    @NotNull(message = "{notNull.amount}")
    private Long amount;
    private Long estimate;
    private Long finished;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
    @NotNull(message = "{notNull.pos}")
    private PosDTO posDTO;
    @NotNull(message = "{notNull.employee}")
    private EmployeeDTO openEmployeeDTO;
    @NotNull(message = "{notNull.employee}")
    private EmployeeDTO closeEmployeeDTO;
}
