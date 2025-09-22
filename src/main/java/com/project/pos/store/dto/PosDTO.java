package com.project.pos.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PosDTO {
    private Long id;
    @NotNull(message = "{notNull.number}")
    private Long number;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    @NotBlank(message = "{notBlank.machine_id}")
    private String machineId;
    private String location;
    @NotNull(message = "{notNull.status}")
    private Boolean status;
    @NotNull(message = "{notNull.deleted}")
    private Boolean deleted;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
}
