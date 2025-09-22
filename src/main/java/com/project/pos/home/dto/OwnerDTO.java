package com.project.pos.home.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OwnerDTO {
    private Long id;
    @NotBlank(message = "{notBlank.name}")
    private String name;
    @NotBlank(message = "{notBlank.reg_number}")
    private String regNumber;
    @NotBlank(message = "{notBlank.owner_code}")
    private String ownerCode;
    @NotNull(message = "{notNull.quantity}")
    private Long storeCount;
    @NotNull(message = "{notNull.deleted}")
    private Boolean deleted;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.login}")
    private LoginDTO loginDTO;
}
