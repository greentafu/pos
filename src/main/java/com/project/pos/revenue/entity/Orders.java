package com.project.pos.revenue.entity;

import com.project.pos.store.entity.Pos;
import com.project.pos.store.entity.Store;
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
public class Orders extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long orderAmount;
    @Column(nullable = false)
    private Long orderDiscountAmount;
    @Column(nullable = false)
    private Long totalDiscountAmount;
    @Column(nullable = false)
    private Long totalPaymentAmount;
    @Column(columnDefinition = "INT")
    private Integer waiting;
    private String receiptNumber;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne
    @JoinColumn(name = "pos_id", nullable = false)
    private Pos pos;

    @ToString.Exclude
    @OneToMany(mappedBy = "orders", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderItem> orderItemList = new ArrayList<>();
    @ToString.Exclude
    @OneToMany(mappedBy = "orders", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<OrderDiscount> orderDiscountList = new ArrayList<>();
}
