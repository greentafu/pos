package com.project.pos.revenue.entity;

import com.project.pos.home.entity.BaseEntity;
import com.project.pos.store.entity.Pos;
import com.project.pos.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Receipt extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long totalAmount;
    private String cardCompany;
    private String cardNumber;
    private Integer cardMonth;
    private Boolean cashReceiptType;
    private String cashReceiptNumber;
    private String receiptNumber;
    private String authorizationNumber;
    private LocalDateTime authorizationDate;
    @Column(nullable = false)
    @Builder.Default
    private Boolean status=true;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;
    @ManyToOne
    @JoinColumn(name = "pos_id", nullable = false)
    private Pos pos;

    @PrePersist
    public void prePersist() {
        if(this.receiptNumber == null) this.receiptNumber = generateReceipt();
        if(this.authorizationNumber == null) this.authorizationNumber = generateAuthorizationNumber();
        if(this.authorizationDate == null) this.authorizationDate = LocalDateTime.now();
    }
    private String generateReceipt() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuid = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return date + "-" + uuid;
    }
    private String generateAuthorizationNumber() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int random = (int)(Math.random() * 900000) + 100000;
        return date + random;
    }
}
