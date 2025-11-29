
package com.retail.payment.infrastructure.gateway;

import com.retail.payment.domain.model.PaymentModel;

public interface PaymentGateway {
    PaymentResult process(PaymentModel paymentModel);
    Method getSupportedMethod();
}