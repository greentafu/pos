package com.project.pos.employee.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveCommuteDTO {
    private Long id;
    @NotBlank(message = "{notBlank.login_pw}")
    private String userPw;
}
