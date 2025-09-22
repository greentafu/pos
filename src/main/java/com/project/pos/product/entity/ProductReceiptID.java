package com.project.pos.product.entity;

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
public class ProductReceiptID implements Serializable {
    private Long productKey;
    private Long stockKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductReceiptID)) return false;
        ProductReceiptID that = (ProductReceiptID) o;
        return Objects.equals(productKey, that.productKey) && Objects.equals(stockKey, that.stockKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productKey, stockKey);
    }
}
