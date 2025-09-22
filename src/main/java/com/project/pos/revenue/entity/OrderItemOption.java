package com.project.pos.revenue.entity;

import com.project.pos.store.entity.Store;
import com.project.pos.option.entity.Option;
import com.project.pos.home.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemOption extends BaseEntity {
    @EmbeddedId
    private OrderItemOptionID id;
    @Column(nullable = false)
    private String optionName;
    @Column(nullable = false)
    private Long optionPrice;
    @Column(nullable = false)
    private Long optionCost;

    @MapsId("orderItemKey")
    @ManyToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;
    @MapsId("optionKey")
    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    public OrderItemOption(String optionName, Long optionPrice, Long optionCost, OrderItem orderItem, Option option) {
        this.id = new OrderItemOptionID(orderItem.getId(), option.getId());
        this.optionName = optionName;
        this.optionPrice = optionPrice;
        this.optionCost = optionCost;
        this.orderItem = orderItem;
        this.option = option;
    }
}
