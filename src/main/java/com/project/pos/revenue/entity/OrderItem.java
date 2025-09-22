package com.project.pos.revenue.entity;

import com.project.pos.store.entity.Store;
import com.project.pos.product.entity.Product;
import com.project.pos.home.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItem extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Long productPrice;
    @Column(nullable = false)
    private Long productCost;
    @Column(nullable = false)
    private Long productCount;
    @Column(nullable = false)
    private Long productPerUnit;
    @Column(nullable = false)
    private Long totalOption;
    @Column(nullable = false)
    private Long totalDiscount;
    @Column(nullable = false)
    private Long totalPerUnit;
    @Column(nullable = false)
    private Long totalPayment;
    private Long copyLocation;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @ManyToOne
    @JoinColumn(name = "order_id",  nullable = false)
    private Orders orders;

    @ToString.Exclude
    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderItemOption> orderItemOptionList = new ArrayList<>();
    @ToString.Exclude
    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderItemDiscount> orderItemDiscountList = new ArrayList<>();
}
