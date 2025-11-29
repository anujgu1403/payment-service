package com.retail.payment;

import com.retail.payment.application.mapper.PaymentModelToPaymentMapper;
import com.retail.payment.application.mapper.PaymentToPaymentModelMapper;
import com.retail.payment.application.model.Payment;
import com.retail.payment.application.service.impl.PaymentServiceImpl;
import com.retail.payment.domain.model.PaymentModel;
import com.retail.payment.infrastructure.repository.PaymentRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepositoryAdapter paymentRepositoryAdapter;

    @Mock
    private PaymentToPaymentModelMapper paymentToPaymentModelMapper;

    @Mock
    private PaymentModelToPaymentMapper paymentModelToPaymentMapper;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessPayment_success() {
        // Given
        Payment payment = new Payment();
        PaymentModel mappedModel = new PaymentModel();
        PaymentModel returnedModel = new PaymentModel();
        Payment mappedBackPayment = new Payment();

        when(paymentToPaymentModelMapper.apply(payment)).thenReturn(mappedModel);
        when(paymentRepositoryAdapter.processPayment(mappedModel)).thenReturn(returnedModel);
        when(paymentModelToPaymentMapper.apply(returnedModel)).thenReturn(mappedBackPayment);

        // When
        Payment result = paymentService.processPayment(payment);

        // Then
        assertNotNull(result);
        assertEquals(mappedBackPayment, result);

        verify(paymentToPaymentModelMapper).apply(payment);
        verify(paymentRepositoryAdapter).processPayment(mappedModel);
        verify(paymentModelToPaymentMapper).apply(returnedModel);
    }

    @Test
    void testGetPayment_success() {
        // Given
        Long id = 10L;
        PaymentModel paymentModel = new PaymentModel();
        Payment expectedPayment = new Payment();

        when(paymentRepositoryAdapter.getPayment(id)).thenReturn(paymentModel);
        when(paymentModelToPaymentMapper.apply(paymentModel)).thenReturn(expectedPayment);

        // When
        Payment result = paymentService.getPayment(id);

        // Then
        assertNotNull(result);
        assertEquals(expectedPayment, result);

        verify(paymentRepositoryAdapter).getPayment(id);
        verify(paymentModelToPaymentMapper).apply(paymentModel);
    }

    @Test
    void testGetPayment_nullResponse() {
        // Given
        Long id = 20L;

        when(paymentRepositoryAdapter.getPayment(id)).thenReturn(null);
        when(paymentModelToPaymentMapper.apply(null)).thenReturn(null);

        // When
        Payment result = paymentService.getPayment(id);

        // Then
        assertNull(result);

        verify(paymentRepositoryAdapter).getPayment(id);
        verify(paymentModelToPaymentMapper).apply(null);
    }
}
