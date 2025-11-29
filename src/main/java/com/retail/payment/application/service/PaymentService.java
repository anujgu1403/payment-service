
package com.retail.payment.application.service;

import com.retail.payment.application.model.Payment;

public interface PaymentService {
    Payment processPayment(Payment request);
    Payment getPayment(Long id);
}