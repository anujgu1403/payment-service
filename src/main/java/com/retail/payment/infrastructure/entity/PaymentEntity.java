package com.retail.payment.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String message;
    private OffsetDateTime processedAt;
    @Column(columnDefinition = "text")
    private String payload;
}
