package com.project.pos.stock.entity;

import com.project.pos.store.entity.Store;
import com.project.pos.home.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StockMovement extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long movementAmount;
    @Column(nullable = false)
    private Long presentAmount;
    @Column(nullable = false)
    private Long stockCost;
    @Column(nullable = false, columnDefinition = "INT")
    private Integer typeValue;
    @Column(nullable = false)
    private Boolean plusType;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne
    @JoinColumn(name = "stock_id", nullable = false)
    private Stock stock;
}
