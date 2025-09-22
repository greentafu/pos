package com.project.pos.option.entity;

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
public class OptionReceiptID implements Serializable {
    private Long optionKey;
    private Long stockKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OptionReceiptID)) return false;
        OptionReceiptID that = (OptionReceiptID) o;
        return Objects.equals(optionKey, that.optionKey) && Objects.equals(stockKey, that.stockKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(optionKey, stockKey);
    }
}
