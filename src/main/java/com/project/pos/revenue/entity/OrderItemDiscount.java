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
public class OrderItemDiscount extends BaseEntity {
    @EmbeddedId
    private OrderItemDiscountID id;
    @Column(nullable = false)
    private Long discountPrice;

    @MapsId("orderItemKey")
    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;
    @MapsId("discountKey")
    @ManyToOne
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;

    public OrderItemDiscount(Long discountPrice, OrderItem orderItem, Discount discount) {
        this.id = new OrderItemDiscountID(orderItem.getId(), discount.getId());
        this.discountPrice = discountPrice;
        this.orderItem = orderItem;
        this.discount = discount;
    }
}
