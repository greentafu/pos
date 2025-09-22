package com.project.pos.store.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveOpenStoreDTO {
    private Long total;
    @NotNull(message = "{notNull.pos}")
    private Long pos;
    @NotBlank(message = "{notBlank.login_id}")
    private String employeeId;
    @NotBlank(message = "{notBlank.login_pw}")
    private String employeePw;
}
