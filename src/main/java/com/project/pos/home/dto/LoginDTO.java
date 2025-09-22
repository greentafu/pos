package com.project.pos.home.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Builder
@ToString(exclude = "userPw")
public class LoginDTO {
    private Long id;
    @NotBlank(message = "{notBlank.user_id}")
    private String userId;
    @NotBlank(message = "{notBlank.user_pw}")
    private String userPw;
    @NotNull(message = "{notNull.type}")
    private Boolean typeValue;
    @NotNull(message = "{notNull.deleated}")
    private Boolean deleted;

    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
