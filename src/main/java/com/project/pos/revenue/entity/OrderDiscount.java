package com.project.pos.revenue.entity;

import com.project.pos.discount.entity.Discount;
import com.project.pos.home.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderDiscount extends BaseEntity {
    @EmbeddedId
    private OrderDiscountID id;
    @Column(nullable = false)
    private Long discountPrice;

    @MapsId("orderKey")
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Orders orders;
    @MapsId("discountKey")
    @ManyToOne
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;

    public OrderDiscount(Long discountPrice, Orders orders, Discount discount) {
        this.id = new OrderDiscountID(orders.getId(), discount.getId());
        this.discountPrice = discountPrice;
        this.orders = orders;
        this.discount = discount;
    }
}
