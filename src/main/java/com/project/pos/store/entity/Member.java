package com.project.pos.store.entity;

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
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private Long totalPayment;
    @Column(nullable = false)
    private Long points;
    @Column(nullable = false)
    @Builder.Default
    private Boolean mail = true;
    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
