package com.project.pos.product.entity;

import com.project.pos.home.entity.BaseEntity;
import com.project.pos.stock.entity.Stock;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductReceipt extends BaseEntity {
    @EmbeddedId
    private ProductReceiptID id;
    @Column(nullable = false)
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    public ProductReceipt(Product product, Stock stock, Long quantity) {
        this.id = new ProductReceiptID(product.getId(), stock.getId());
        this.product = product;
        this.stock = stock;
        this.quantity = quantity;
    }
}
