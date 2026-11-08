package com.project.ecommerce_app.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer payment_id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    @JsonManagedReference
    private Order order;


    private String paymentMethod; // e.g., Credit Card, PayPal

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // e.g., SUCCESS, PENDING, FAILED

    private String transactionId;
    private LocalDateTime paymentDate;

//    private String razorpayOrderId;
}
