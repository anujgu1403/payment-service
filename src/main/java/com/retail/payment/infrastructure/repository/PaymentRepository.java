package com.retail.payment.infrastructure.repository;


import com.retail.payment.domain.model.PaymentModel;

public interface PaymentRepository {
    PaymentModel processPayment(PaymentModel paymentModel);
    PaymentModel getPayment(Long id);
}
