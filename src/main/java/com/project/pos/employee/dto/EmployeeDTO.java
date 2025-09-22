package com.project.pos.employee.dto;

import com.project.pos.home.dto.LoginDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
public class EmployeeDTO {
    private Long id;
    @NotNull(message = "{notNull.number}")
    private Long number;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    @NotBlank(message = "{notBlank.tel_number}")
    private String telNumber;
    @NotNull(message = "{notNull.deleted}")
    private Boolean deleted;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.login}")
    private LoginDTO loginDTO;
    @NotNull(message = "{notNull.position}")
    private JobTitleDTO job_titleDTO;
}
