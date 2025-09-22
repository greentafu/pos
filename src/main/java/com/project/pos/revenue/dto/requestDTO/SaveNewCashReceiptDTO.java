package com.project.pos.revenue.dto.requestDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveNewCashReceiptDTO {
    private Map<String, Long> receiptMap;
    @NotNull(message = "{notNull.number}")
    private Long receiptNumber;
    @NotNull(message = "{notNull.type}")
    private Boolean receiptType;
    @NotNull(message = "{notNull.price}")
    private Long totalPrice;
}
