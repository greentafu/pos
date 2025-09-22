package com.project.pos.revenue.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReceiptMethodID implements Serializable {
    private Long receiptKey;
    private Long paymentMethodKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReceiptMethodID)) return false;
        ReceiptMethodID that = (ReceiptMethodID) o;
        return Objects.equals(receiptKey, that.receiptKey) && Objects.equals(paymentMethodKey, that.paymentMethodKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiptKey, paymentMethodKey);
    }
}