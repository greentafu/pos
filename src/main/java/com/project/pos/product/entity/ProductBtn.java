package com.project.pos.product.entity;

import com.project.pos.home.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductBtn extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Integer page;
    @Column(nullable = false)
    private Integer indexValue;
    @Column(nullable = false)
    private Integer color;
    @Column(nullable = false)
    @Builder.Default
    private Integer size = 1;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
