package com.project.pos.home.dto.responseDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString(exclude = "userPw")
public class LoginProcessDTO {
    @NotBlank(message = "{notBlank.login_id}")
    private String userId;
    @NotBlank(message = "{notBlank.login_pw}")
    private String userPw;
}
