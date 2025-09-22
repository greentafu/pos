package com.project.pos.employee.entity;

import com.project.pos.home.entity.BaseEntity;
import com.project.pos.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Commute extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Column(nullable = false)
    private String jobTitle;
    @Column(nullable = false)
    private Long perWage;
    private String notes;
    @Column(nullable = false)
    @Builder.Default
    private Boolean modified=false;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}
