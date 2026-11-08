package com.project.ecommerce_app.service;

import com.project.ecommerce_app.dto.AddressDto;
import com.project.ecommerce_app.entity.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    Payment processPayment(Integer customerId, AddressDto address);
    Optional<Payment> getPaymentById(Integer paymentId);
    List<Payment> getAllPayments();

    Payment markPaymentSuccess(Integer paymentId);

    Payment markPaymentFailed(Integer paymentId);
}
