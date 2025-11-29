package com.retail.payment.infrastructure.mapper;

import com.retail.payment.application.model.Method;
import com.retail.payment.domain.model.PaymentModel;
import com.retail.payment.infrastructure.entity.PaymentEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentEntityToPaymentModelMapper {
    public PaymentModel apply(PaymentEntity paymentEntity){
        return PaymentModel.builder()
                .paymentId(paymentEntity.getId())
                .method(Method.valueOf(paymentEntity.getMethod()))
                .amount(paymentEntity.getAmount())
                .currency(paymentEntity.getCurrency())
                .message(paymentEntity.getMessage())
                .processedAt(paymentEntity.getProcessedAt())
                .status(paymentEntity.getStatus())
                .build();
    }
}
