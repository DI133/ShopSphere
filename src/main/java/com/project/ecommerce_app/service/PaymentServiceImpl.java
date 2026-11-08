package com.project.ecommerce_app.service;

import com.project.ecommerce_app.dto.AddressDto;
import com.project.ecommerce_app.entity.Order;
import com.project.ecommerce_app.entity.Payment;
import com.project.ecommerce_app.entity.PaymentStatus;
import com.project.ecommerce_app.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class PaymentServiceImpl implements PaymentService{
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderService orderService;

    @Override
    public Payment processPayment(Integer customerId, AddressDto address) {
        // Set payment status to PENDING initially
        Order newOrder = orderService.placeOrder(customerId, address);
        Payment payment = new Payment();
        payment.setOrder(newOrder);
        payment.setPaymentStatus(PaymentStatus.pending);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPaymentMethod("Card");
        payment.setTransactionId(UUID.randomUUID().toString());
        paymentRepository.save(payment);
        return payment;
    }

    @Override
    public Optional<Payment> getPaymentById(Integer paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment markPaymentSuccess(Integer paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setPaymentStatus(PaymentStatus.success);

        return paymentRepository.save(payment);
    }

    @Override
    public Payment markPaymentFailed(Integer paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setPaymentStatus(PaymentStatus.failed);

        orderService.cancelOrder(payment.getPayment_id());
        return paymentRepository.save(payment);
    }
}
