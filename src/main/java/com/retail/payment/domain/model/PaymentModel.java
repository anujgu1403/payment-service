package com.retail.payment.domain.model;

import com.retail.payment.application.model.Method;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Map;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentModel {
    private Method method;
    private BigDecimal amount;
    private String currency;
    private Map<String, String> payload;
    private Long paymentId;
    private String status;
    private String message;
    private OffsetDateTime processedAt;
}
