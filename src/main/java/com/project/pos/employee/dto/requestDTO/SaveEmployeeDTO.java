package com.project.pos.employee.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveEmployeeDTO {
    private Long id;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    @NotNull(message = "{notNull.position}")
    private Long jobTitle;
    @NotBlank(message = "{notBlank.user_id}")
    private String userId;
    private String userPw;
    @NotBlank(message = "{notBlank.tel_number}")
    private String telNumber;
}
