package com.project.pos.home.dto;

import com.project.pos.store.dto.StoreDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginStoreDTO {
    private Long id;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
    @NotNull(message = "{notNull.login}")
    private LoginDTO loginDTO;
}
