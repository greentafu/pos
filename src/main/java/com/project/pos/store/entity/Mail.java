package com.project.pos.store.entity;

import com.project.pos.home.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Mail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String content;
    private String notes;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
}
