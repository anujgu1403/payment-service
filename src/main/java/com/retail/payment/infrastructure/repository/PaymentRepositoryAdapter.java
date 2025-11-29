package com.retail.payment.infrastructure.repository;

import com.retail.payment.domain.model.PaymentModel;
import com.retail.payment.infrastructure.entity.PaymentEntity;
import com.retail.payment.infrastructure.gateway.Method;
import com.retail.payment.infrastructure.gateway.PaymentGateway;
import com.retail.payment.infrastructure.gateway.PaymentResult;
import com.retail.payment.infrastructure.mapper.PaymentEntityToPaymentModelMapper;
import com.retail.payment.infrastructure.mapper.PaymentModelToPaymentEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class PaymentRepositoryAdapter implements PaymentRepository {

    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    private final Map<Method, PaymentGateway> gatewayMap = new HashMap<>();

    public PaymentRepositoryAdapter(PaymentJpaRepository paymentJpaRepository, List<PaymentGateway> gateways) {
        this.paymentJpaRepository = paymentJpaRepository;
        gateways.forEach(g -> gatewayMap.put(g.getSupportedMethod(), g));
    }

    @Autowired
    private PaymentModelToPaymentEntityMapper paymentModelToPaymentEntityMapper;

    @Autowired
    private PaymentEntityToPaymentModelMapper paymentEntityToPaymentModelMapper;

    @Override
    public PaymentModel processPayment(PaymentModel paymentModel) {
        PaymentEntity paymentEntity = paymentModelToPaymentEntityMapper.apply(paymentModel);
        try {
            String payload = paymentModel.getPayload() == null ? "{}" : new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(paymentModel.getPayload());
            paymentEntity.setPayload(payload);
        } catch (Exception ex) {
            paymentEntity.setPayload("{}");
        }

        PaymentGateway gateway = gatewayMap.get(Method.valueOf(paymentModel.getMethod().name()));
        PaymentResult result = gateway == null ? PaymentResult.error("No gateway for method: " + paymentModel.getMethod()) : gateway.process(paymentModel);

        paymentEntity.setStatus(result.isApproved() ? "APPROVED" : result.isError() ? "ERROR" : "DECLINED");
        paymentEntity.setMessage(result.getMessage());
        paymentEntity.setProcessedAt(OffsetDateTime.now());
        paymentJpaRepository.save(paymentEntity);

        return paymentEntityToPaymentModelMapper.apply(paymentEntity);
    }

    @Override
    public PaymentModel getPayment(Long id) {
        return paymentJpaRepository.findById(id).map(paymentEntity -> {
            return paymentEntityToPaymentModelMapper.apply(paymentEntity);
        }).orElse(null);
    }
}
