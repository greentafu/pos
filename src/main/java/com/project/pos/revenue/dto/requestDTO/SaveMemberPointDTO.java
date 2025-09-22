package com.project.pos.revenue.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveMemberPointDTO {
    @NotNull(message = "{notNull.id}")
    private Long id;
    @NotBlank(message = "{notBlank.tel_number}")
    private String phoneNumber;
}
