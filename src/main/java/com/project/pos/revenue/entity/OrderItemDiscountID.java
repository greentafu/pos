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
public class OrderItemDiscountID implements Serializable {
    private Long orderItemKey;
    private Long discountKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItemDiscountID)) return false;
        OrderItemDiscountID that = (OrderItemDiscountID) o;
        return Objects.equals(orderItemKey, that.orderItemKey) && Objects.equals(discountKey, that.discountKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemKey, discountKey);
    }
}