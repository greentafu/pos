package com.project.pos.option.entity;

import com.project.pos.home.entity.BaseEntity;
import com.project.pos.stock.entity.Stock;
import com.project.pos.store.entity.ScreenAuthorityID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OptionReceipt extends BaseEntity {
    @EmbeddedId
    private OptionReceiptID id;
    @Column(nullable = false)
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;
    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    public OptionReceipt(Option option, Stock stock, Long quantity) {
        this.id = new OptionReceiptID(option.getId(), stock.getId());
        this.option = option;
        this.stock = stock;
        this.quantity = quantity;
    }
}
