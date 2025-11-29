
package com.retail.payment.infrastructure.gateway;

import com.retail.payment.application.model.Payment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Random;

@Component
public class CreditCardGateway implements PaymentGateway {

    private final Random rnd = new Random();

    @Override
    public PaymentResult process(Payment request) {
        BigDecimal a = request.getAmount();
        if (a == null) return PaymentResult.error("Missing amount");
        if (a.compareTo(BigDecimal.valueOf(1000)) > 0) return PaymentResult.declined("Amount exceeds limit for credit card");
        boolean approved = rnd.nextInt(100) < 90;
        return approved ? PaymentResult.approved("Credit card approved (simulated)") : PaymentResult.declined("Credit card declined (simulated)");
    }

    @Override
    public Method getSupportedMethod() {
        return Method.CREDIT_CARD;
    }
}
