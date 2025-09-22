package com.project.pos.home.entity;

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
public class Owner extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String regNumber;
    @Column(nullable = false)
    private String ownerCode;
    @Column(nullable = false)
    private Long storeCount;
    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted = false;

    @OneToOne
    @JoinColumn(name = "login_id", nullable = false)
    private Login login;
}
