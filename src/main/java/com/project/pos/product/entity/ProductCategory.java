package com.project.pos.product.entity;

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
public class ProductCategory extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    private Long arrangement;
    @Builder.Default
    private Boolean deleted=false;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
