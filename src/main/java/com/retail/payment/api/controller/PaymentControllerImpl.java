package com.retail.payment.api.controller;

import com.retail.payment.application.service.PaymentService;
import com.retail.payment.application.model.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentControllerImpl implements PaymentController{

    private final PaymentService paymentService;

    public PaymentControllerImpl(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public ResponseEntity<Payment> process(@RequestBody Payment request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }

    public ResponseEntity<Payment> get(@PathVariable Long id) {
        Payment resp = paymentService.getPayment(id);
        return resp == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(resp);
    }
}