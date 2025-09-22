package com.project.pos.revenue.entity;

import com.project.pos.home.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReceiptMethod extends BaseEntity {
    @EmbeddedId
    private ReceiptMethodID id;
    @Column(nullable = false)
    private Long issuePrice;
    @Column(nullable = false)
    @Builder.Default
    private Boolean status=true;

    @MapsId("receiptKey")
    @ManyToOne
    @JoinColumn(name = "receipt_id", nullable = false)
    private Receipt receipt;
    @MapsId("paymentMethodKey")
    @ManyToOne
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    public ReceiptMethod(Long issuePrice, Receipt receipt, PaymentMethod paymentMethod) {
        this.id = new ReceiptMethodID(receipt.getId(), paymentMethod.getId());
        this.issuePrice = issuePrice;
        this.receipt = receipt;
        this.paymentMethod = paymentMethod;
    }
}
