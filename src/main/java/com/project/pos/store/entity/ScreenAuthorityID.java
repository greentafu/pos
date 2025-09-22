package com.project.pos.store.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ScreenAuthorityID implements Serializable {
    private Long jobTitleKey;
    private Long screenKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScreenAuthorityID)) return false;
        ScreenAuthorityID that = (ScreenAuthorityID) o;
        return Objects.equals(jobTitleKey, that.jobTitleKey) && Objects.equals(screenKey, that.screenKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jobTitleKey, screenKey);
    }
}