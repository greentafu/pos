package com.project.pos.store.dto;

import com.project.pos.home.dto.OwnerDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StoreDTO {
    private Long id;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    @NotBlank(message = "{notBlank.location}")
    private String location;
    @NotBlank(message = "{notBlank.tel_number}")
    private String telNumber;
    @NotNull(message = "{notNull.percent}")
    private Long pointPercent;
    @NotNull(message = "{notNull.status}")
    private Boolean status;
    @NotNull(message = "{notNull.deleted}")
    private Boolean deleted;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.owner}")
    private OwnerDTO ownerDTO;
}
