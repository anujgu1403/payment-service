
package com.retail.payment.infrastructure.gateway;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@Builder
public class PaymentResult {
    private final boolean approved;
    private final boolean error;
    private final String message;

    public static PaymentResult approved(String message) {
        return new PaymentResult(true, false, message);
    }

    public static PaymentResult declined(String message) {
        return new PaymentResult(false, false, message);
    }

    public static PaymentResult error(String message) {
        return new PaymentResult(false, true, message);
    }

}
