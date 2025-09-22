package com.project.pos.store.entity;

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
public class StoreTimeID implements Serializable {
    private Long storeKey;
    private Long dayOfWeekKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreTimeID)) return false;
        StoreTimeID that = (StoreTimeID) o;
        return Objects.equals(storeKey, that.storeKey) && Objects.equals(dayOfWeekKey, that.dayOfWeekKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeKey, dayOfWeekKey);
    }
}