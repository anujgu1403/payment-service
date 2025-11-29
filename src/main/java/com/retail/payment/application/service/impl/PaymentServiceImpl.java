package com.retail.payment.application.service.impl;

import com.retail.payment.application.mapper.PaymentModelToPaymentMapper;
import com.retail.payment.application.mapper.PaymentToPaymentModelMapper;
import com.retail.payment.application.model.Payment;
import com.retail.payment.application.service.PaymentService;
import com.retail.payment.domain.model.PaymentModel;
import com.retail.payment.infrastructure.repository.PaymentRepositoryAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepositoryAdapter paymentRepositoryAdapter;

    @Autowired
    private PaymentToPaymentModelMapper paymentToPaymentModelMapper;

    @Autowired
    private PaymentModelToPaymentMapper paymentModelToPaymentMapper;


    @Override
    public Payment processPayment(Payment payment) {

        PaymentModel paymentModel = paymentRepositoryAdapter.processPayment(paymentToPaymentModelMapper.apply(payment));
        return paymentModelToPaymentMapper.apply(paymentModel);
    }

    @Override
    public Payment getPayment(Long id) {
        return paymentModelToPaymentMapper.apply(paymentRepositoryAdapter.getPayment(id));
    }
}