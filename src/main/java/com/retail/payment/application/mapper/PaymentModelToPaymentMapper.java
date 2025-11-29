package com.retail.payment.application.mapper;

import com.retail.payment.application.model.Payment;
import com.retail.payment.domain.model.PaymentModel;
import org.springframework.stereotype.Component;

@Component
public class PaymentModelToPaymentMapper {

    public Payment apply(PaymentModel paymentModel) {
        return Payment.builder()
                .paymentId(paymentModel.getPaymentId())
                .message(paymentModel.getMessage())
                .status(paymentModel.getStatus())
                .processedAt(paymentModel.getProcessedAt())
                .method(paymentModel.getMethod())
                .amount(paymentModel.getAmount())
                .currency(paymentModel.getCurrency())
                .build();
    }
}
