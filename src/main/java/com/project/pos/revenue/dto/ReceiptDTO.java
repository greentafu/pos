package com.project.pos.revenue.dto;

import com.project.pos.store.dto.PosDTO;
import com.project.pos.store.dto.StoreDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReceiptDTO {
    private Long id;
    @NotNull(message = "{notNull.price}")
    private Long totalAmount;
    private String cardCompany;
    private String cardNumber;
    private Integer cardMonth;
    private Boolean cashReceiptType;
    private String cashReceiptNumber;
    private String receiptNumber;
    private String authorizationNumber;
    private LocalDateTime authorizationDate;
    @NotNull(message = "{notNull.status}")
    private Boolean status;

    private LocalDateTime regDate;
    private LocalDateTime modDate;

    @NotNull(message = "{notNull.store}")
    private StoreDTO storeDTO;
    @NotNull(message = "{notNull.pos}")
    private PosDTO posDTO;
}
