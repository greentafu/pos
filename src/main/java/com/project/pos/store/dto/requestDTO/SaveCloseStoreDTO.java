package com.project.pos.store.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveCloseStoreDTO {
    private Long total;
    @NotNull(message = "{notNull.record}")
    private Long recordId;
    @NotBlank(message = "{notBlank.login_id}")
    private String employeeId;
    @NotBlank(message = "{notBlank.login_pw}")
    private String employeePw;
}
