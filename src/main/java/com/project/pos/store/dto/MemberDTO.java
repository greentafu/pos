package com.project.pos.store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MemberDTO {
    private Long id;
    @NotBlank(message = "{notBlank.tel_number}")
    private String phoneNumber;
    @NotNull(message = "{notNull.total_payment}")
    private Long totalPayment;
    @NotNull(message = "{notNull.points}")
    private Long points;
    @NotNull(message = "{notNull.mail}")
    private Boolean mail;
    @NotNull(message = "{notNull.deleted}")
    private Boolean deleted;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
}
