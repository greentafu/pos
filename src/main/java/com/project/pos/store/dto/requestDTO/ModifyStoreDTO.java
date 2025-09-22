package com.project.pos.store.dto.requestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModifyStoreDTO {
    @NotBlank(message = "{notBlank.name}")
    private String name;
    @NotBlank(message = "{notBlank.location}")
    private String location;
    @NotBlank(message = "{notBlank.tel_number}")
    private String telNumber;
    @NotBlank(message = "{notBlank.percent}")
    private String pointPercent;
    @NotNull(message = "{notNull.status}")
    private Boolean status;
}
