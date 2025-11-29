
package com.retail.payment.infrastructure.gateway;

import com.retail.payment.application.model.Payment;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DebitCardGateway implements PaymentGateway {

    @Override
    public PaymentResult process(Payment request) {
        var payload = request.getPayload();
        if (payload == null || payload.get("pin") == null) return PaymentResult.error("Missing pin for debit card");
        BigDecimal a = request.getAmount();
        if (a == null) return PaymentResult.error("Missing amount");
        if (a.compareTo(BigDecimal.valueOf(2000)) > 0) return PaymentResult.declined("Insufficient funds (simulated)");
        return PaymentResult.approved("Debit card approved (simulated)");
    }

    @Override
    public Method getSupportedMethod() {
        return Method.DEBIT_CARD;
    }
}
