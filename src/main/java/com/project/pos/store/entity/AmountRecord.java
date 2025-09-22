package com.project.pos.store.entity;

import com.project.pos.employee.entity.Employee;
import com.project.pos.home.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AmountRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Builder.Default
    private Long amount=0L;
    private Long estimate;
    private Long finished;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne
    @JoinColumn(name = "pos_id", nullable = false)
    private Pos pos;
    @ManyToOne
    @JoinColumn(name = "open_employee_id", nullable = false)
    private Employee openEmployee;
    @ManyToOne
    @JoinColumn(name = "close_employee_id")
    private Employee closeEmployee;
}
