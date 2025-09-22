package com.project.pos.revenue.repository;

import com.project.pos.revenue.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReceiptMethodRepository extends JpaRepository<ReceiptMethod, ReceiptMethodID> {
    @Query("select r from ReceiptMethod r where r.paymentMethod=:paymentMethod")
    List<ReceiptMethod> getAllReceiptMethodByPaymentMethod(@Param("paymentMethod") PaymentMethod paymentMethod);
    @Query("select r from ReceiptMethod r where r.paymentMethod=:paymentMethod and r.status=true")
    List<ReceiptMethod> getTrueReceiptMethodByPaymentMethod(@Param("paymentMethod") PaymentMethod paymentMethod);

    @Query("select distinct r.receipt from ReceiptMethod r " +
            "where r.paymentMethod.payment=:payment and r.receipt.cashReceiptNumber is not null and r.receipt.status=true")
    List<Receipt> getCashReceiptListByPayment(@Param("payment") Payment payment);
    @Query("select distinct r.receipt from ReceiptMethod r " +
            "where r.paymentMethod.payment=:payment and r.receipt.cashReceiptNumber is not null and r.receipt.status=false")
    List<Receipt> getCancelCashReceiptListByPayment(@Param("payment") Payment payment);

    @Query("select distinct r.receipt from ReceiptMethod r " +
            "where r.paymentMethod.payment=:payment and r.receipt.cardNumber is not null and r.receipt.status=true")
    List<Receipt> getCardReceiptListByPayment(@Param("payment") Payment payment);
    @Query("select distinct r.receipt from ReceiptMethod r " +
            "where r.paymentMethod.payment=:payment and r.receipt.cardNumber is not null and r.receipt.status=false")
    List<Receipt> getCancelCardReceiptListByPayment(@Param("payment") Payment payment);

    @Query("select r from ReceiptMethod r where r.receipt=:receipt")
    List<ReceiptMethod> getReceiptMethodByReceipt(@Param("receipt") Receipt receipt);
}
