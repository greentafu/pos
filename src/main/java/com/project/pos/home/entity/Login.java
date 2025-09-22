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
public class Login extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String userId;
    @Column(nullable = false)
    private String userPw;
    @Column(nullable = false)
    @Builder.Default
    private Boolean typeValue=false;
    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted=false;
}
