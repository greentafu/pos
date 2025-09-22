package com.project.pos.store.dto.requestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SaveStoreDTO {
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
    @Valid
    private List<TimeDTO> timeList;
}
