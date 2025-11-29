package com.retail.payment.application.mapper;

import com.retail.payment.application.model.Payment;
import com.retail.payment.domain.model.PaymentModel;
import org.springframework.stereotype.Component;

@Component
public class PaymentToPaymentModelMapper {

    public PaymentModel apply(Payment payment){
        return PaymentModel.builder()
                .paymentId(payment.getPaymentId())
                .message(payment.getMessage())
                .status(payment.getStatus())
                .processedAt(payment.getProcessedAt())
                .method(payment.getMethod())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .payload(payment.getPayload())
                .build();
    }
}
