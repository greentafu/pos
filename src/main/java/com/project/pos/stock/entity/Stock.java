package com.project.pos.stock.entity;

import com.project.pos.store.entity.Store;
import com.project.pos.home.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Where(clause = "deleted = false")
public class Stock extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long number;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Long stockCost;
    @Column(nullable = false)
    private String unit;
    @Column(nullable = false)
    private Long quantity;
    @Column(nullable = false)
    @Builder.Default
    private Boolean soldOut = false;
    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne
    @JoinColumn(name = "stock_category_id")
    private StockCategory stockCategory;
}
