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
public class OrderDiscountID implements Serializable {
    private Long orderKey;
    private Long discountKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDiscountID)) return false;
        OrderDiscountID that = (OrderDiscountID) o;
        return Objects.equals(orderKey, that.orderKey) && Objects.equals(discountKey, that.discountKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderKey, discountKey);
    }
}