package com.retail.payment.infrastructure.mapper;

import com.retail.payment.application.model.Payment;
import com.retail.payment.infrastructure.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentToPaymentEntityMapper {

    public PaymentEntity apply(Payment payment) {
        return PaymentEntity.builder()
                .method(payment.getMethod().name())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .build();
    }
}
