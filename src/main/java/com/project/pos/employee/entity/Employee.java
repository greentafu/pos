package com.project.pos.employee.entity;

import com.project.pos.home.entity.BaseEntity;
import com.project.pos.home.entity.Login;
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
public class Employee extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long number;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String telNumber;
    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted=false;

    @OneToOne
    @JoinColumn(name = "login_id", nullable = false)
    private Login login;
    @ManyToOne
    @JoinColumn(name = "job_title_id", nullable = false)
    private JobTitle jobTitle;
}
