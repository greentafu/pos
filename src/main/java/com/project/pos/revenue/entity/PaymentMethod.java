package com.project.pos.revenue.entity;

import com.project.pos.store.entity.Pos;
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
public class PaymentMethod extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, columnDefinition = "INT")
    private Integer typeValue;
    @Column(nullable = false)
    private Long paymentAmount;
    private Long received;
    private Long changeValue;
    @Column(nullable = false)
    @Builder.Default
    private Boolean status=true;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne
    @JoinColumn(name = "pos_id", nullable = false)
    private Pos pos;
    @ManyToOne
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;
}
