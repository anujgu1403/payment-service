
package com.retail.payment.infrastructure.gateway;

import com.retail.payment.application.model.Payment;

public interface PaymentGateway {
    PaymentResult process(Payment request);
    Method getSupportedMethod();
}