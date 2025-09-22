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
public class ScreenArrangementID implements Serializable {
    private Long storeKey;
    private Long screenKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScreenArrangementID)) return false;
        ScreenArrangementID that = (ScreenArrangementID) o;
        return Objects.equals(storeKey, that.storeKey) && Objects.equals(screenKey, that.screenKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeKey, screenKey);
    }
}