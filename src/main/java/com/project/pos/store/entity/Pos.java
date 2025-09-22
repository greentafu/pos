package com.project.pos.store.entity;

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
public class Pos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long number;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String machineId;
    private String location;
    @Column(nullable = false)
    @Builder.Default
    private Boolean status = false;
    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted = false;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
