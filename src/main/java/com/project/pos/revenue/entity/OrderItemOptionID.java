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
public class OrderItemOptionID implements Serializable {
    private Long orderItemKey;
    private Long optionKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItemOptionID)) return false;
        OrderItemOptionID that = (OrderItemOptionID) o;
        return Objects.equals(orderItemKey, that.orderItemKey) && Objects.equals(optionKey, that.optionKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderItemKey, optionKey);
    }
}