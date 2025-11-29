package com.retail.payment.infrastructure.mapper;

import com.retail.payment.domain.model.PaymentModel;
import com.retail.payment.infrastructure.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentModelToPaymentEntityMapper {

    public PaymentEntity apply(PaymentModel paymentModel) {
        return PaymentEntity.builder()
                .method(paymentModel.getMethod().name())
                .amount(paymentModel.getAmount())
                .currency(paymentModel.getCurrency())
                .build();
    }
}
