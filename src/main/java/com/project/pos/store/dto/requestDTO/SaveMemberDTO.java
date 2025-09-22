package com.project.pos.store.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SaveMemberDTO {
    private Long id;
    @NotBlank(message = "{notBlank.tel_number}")
    private String phoneNumber;
    @NotNull(message = "{notNull.mail}")
    private Boolean mail;
}
