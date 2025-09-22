package com.project.pos.employee.entity;

import com.project.pos.home.entity.BaseEntity;
import com.project.pos.store.entity.Store;
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
public class Wage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Long perWage;
    private String notes;
    @Builder.Default
    private Boolean deleted=false;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
