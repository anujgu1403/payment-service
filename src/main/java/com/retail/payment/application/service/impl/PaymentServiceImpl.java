package com.retail.payment.application.service.impl;

import com.retail.payment.application.model.Payment;
import com.retail.payment.application.service.PaymentService;
import com.retail.payment.infrastructure.entity.PaymentEntity;
import com.retail.payment.infrastructure.gateway.Method;
import com.retail.payment.infrastructure.mapper.PaymentEntityToPaymentMapper;
import com.retail.payment.infrastructure.mapper.PaymentToPaymentEntityMapper;
import com.retail.payment.infrastructure.repository.PaymentRepository;
import com.retail.payment.infrastructure.gateway.PaymentGateway;
import com.retail.payment.infrastructure.gateway.PaymentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final Map<com.retail.payment.infrastructure.gateway.Method, PaymentGateway> gatewayMap = new HashMap<>();

    @Autowired
    public PaymentEntityToPaymentMapper paymentEntityToPaymentMapper;

    @Autowired
    public PaymentToPaymentEntityMapper paymentToPaymentEntityMapper;

    public PaymentServiceImpl(PaymentRepository paymentRepository, List<PaymentGateway> gateways) {
        this.paymentRepository = paymentRepository;
        gateways.forEach(g -> gatewayMap.put(g.getSupportedMethod(), g));
    }

    @Override
    public Payment processPayment(Payment payment) {

        PaymentEntity paymentEntity = paymentToPaymentEntityMapper.apply(payment);
        try {
            String payload = payment.getPayload() == null ? "{}" : new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(payment.getPayload());
            paymentEntity.setPayload(payload);
        } catch (Exception ex) {
            paymentEntity.setPayload("{}");
        }

        PaymentGateway gateway = gatewayMap.get(Method.valueOf(payment.getMethod().name()));
        PaymentResult result = gateway == null ? PaymentResult.error("No gateway for method: " + payment.getMethod()) : gateway.process(payment);

        paymentEntity.setStatus(result.isApproved() ? "APPROVED" : result.isError() ? "ERROR" : "DECLINED");
        paymentEntity.setMessage(result.getMessage());
        paymentEntity.setProcessedAt(OffsetDateTime.now());
        paymentRepository.save(paymentEntity);

       return paymentEntityToPaymentMapper.apply(paymentEntity);
    }

    @Override
    public Payment getPayment(Long id) {
        return paymentRepository.findById(id).map(paymentEntity -> {
           return paymentEntityToPaymentMapper.apply(paymentEntity);
        }).orElse(null);
    }
}