
package com.retail.payment.infrastructure.gateway;

import com.retail.payment.application.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class GiftCardGateway implements PaymentGateway {

    @Override
    public PaymentResult process(Payment request) {
        var payload = request.getPayload();
        if (payload == null || payload.get("giftCode") == null) return PaymentResult.error("Missing giftCode");
        String code = payload.get("giftCode");
        if ("GIFT-VALID".equalsIgnoreCase(code)) {
            return PaymentResult.approved("Gift card approved (simulated)");
        } else {
            return PaymentResult.declined("Invalid gift card code");
        }
    }

    @Override
    public Method getSupportedMethod() {
        return Method.GIFT_CARD;
    }
}
