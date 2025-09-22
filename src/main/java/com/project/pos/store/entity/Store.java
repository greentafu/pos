package com.project.pos.store.entity;

import com.project.pos.home.entity.BaseEntity;
import com.project.pos.home.entity.Owner;
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
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String location;
    @Column(nullable = false)
    private String telNumber;
    @Column(nullable = false)
    private Long pointPercent;
    @Column(nullable = false)
    @Builder.Default
    private Boolean status = false;
    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;
}
