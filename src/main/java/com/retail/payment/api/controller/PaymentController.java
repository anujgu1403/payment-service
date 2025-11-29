package com.retail.payment.api.controller;

import com.retail.payment.application.model.Payment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface PaymentController {

    @PostMapping
    ResponseEntity<Payment> process(@RequestBody Payment request);

    @GetMapping("/{id}")
    ResponseEntity<Payment> get(@PathVariable Long id);
}
