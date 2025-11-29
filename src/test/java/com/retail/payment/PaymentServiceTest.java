package com.retail.payment;

import com.retail.payment.application.model.Method;
import com.retail.payment.application.model.Payment;

import com.retail.payment.application.service.impl.PaymentServiceImpl;
import com.retail.payment.infrastructure.entity.PaymentEntity;
import com.retail.payment.infrastructure.gateway.PaymentGateway;
import com.retail.payment.infrastructure.gateway.PaymentResult;
import com.retail.payment.infrastructure.mapper.PaymentEntityToPaymentMapper;
import com.retail.payment.infrastructure.mapper.PaymentToPaymentEntityMapper;
import com.retail.payment.infrastructure.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentEntityToPaymentMapper entityToModelMapper;

    @Mock
    private PaymentToPaymentEntityMapper modelToEntityMapper;

    @Mock
    private PaymentGateway creditCardGateway;

    @MockBean
    private PaymentServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // mock gateway supports CREDIT_CARD
        when(creditCardGateway.getSupportedMethod()).thenReturn(com.retail.payment.infrastructure.gateway.Method.CREDIT_CARD);

        service = new PaymentServiceImpl(
                paymentRepository,
                List.of(creditCardGateway)
        );

        // Inject mappers (Autowired fields)
        service.paymentEntityToPaymentMapper = entityToModelMapper;
        service.paymentToPaymentEntityMapper = modelToEntityMapper;
    }

    @Test
    void testProcessPayment_success() {
        // Given
        Payment payment = new Payment();
        payment.setMethod(Method.CREDIT_CARD);
        payment.setAmount(BigDecimal.TEN);
        payment.setPayload(Map.of("order", "123"));

        PaymentEntity entity = new PaymentEntity();
        when(modelToEntityMapper.apply(payment)).thenReturn(entity);

        PaymentResult gatewayResult = PaymentResult.approved("OK");
        when(creditCardGateway.process(payment)).thenReturn(gatewayResult);

        Payment returnedPayment = new Payment();
        when(entityToModelMapper.apply(entity)).thenReturn(returnedPayment);

        when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // When
        Payment result = service.processPayment(payment);

        // Then
        assertNotNull(result);
        assertEquals(returnedPayment, result);

        assertEquals("APPROVED", entity.getStatus());
        assertEquals("OK", entity.getMessage());
        assertNotNull(entity.getProcessedAt());
        assertEquals("{\"order\":\"123\"}", entity.getPayload());
    }

    @Test
    void testProcessPayment_noGateway() {
        // Given
        Payment payment = new Payment();
        payment.setMethod(Method.DEBIT_CARD);

        PaymentEntity entity = new PaymentEntity();
        when(modelToEntityMapper.apply(payment)).thenReturn(entity);
        when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));
        when(entityToModelMapper.apply(entity)).thenReturn(new Payment());

        // When
        Payment result = service.processPayment(payment);

        // Then
        assertNotNull(result);
        assertEquals("ERROR", entity.getStatus());
        assertTrue(entity.getMessage().contains("No gateway for method"));
    }

    @Test
    void testProcessPayment_invalidPayloadSerialization() throws Exception {
        // Given
        Payment payment = mock(Payment.class);
        when(payment.getMethod()).thenReturn(Method.CREDIT_CARD);

        // Force an invalid payload (ObjectMapper exception)
        when(payment.getPayload()).thenReturn(new HashMap<>() {{
            put("bad", "");
        }});

        PaymentEntity entity = new PaymentEntity();
        when(modelToEntityMapper.apply(payment)).thenReturn(entity);

        when(creditCardGateway.process(payment)).thenReturn(PaymentResult.approved("OK"));
        when(entityToModelMapper.apply(entity)).thenReturn(new Payment());
        when(paymentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        // When
        Payment result = service.processPayment(payment);

        // Then
        assertNotNull(result);
        assertEquals("{\"bad\":\"\"}", entity.getPayload());   // fallback
    }

    @Test
    void testGetPayment_found() {
        // Given
        PaymentEntity entity = new PaymentEntity();
        Payment model = new Payment();

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(entityToModelMapper.apply(entity)).thenReturn(model);

        // When
        Payment result = service.getPayment(1L);

        // Then
        assertNotNull(result);
        assertEquals(model, result);
    }

    @Test
    void testGetPayment_notFound() {
        // Given
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Payment result = service.getPayment(1L);

        // Then
        assertNull(result);
    }
}