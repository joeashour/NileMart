package org.route;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
@Getter
@AllArgsConstructor
public enum Zone {
    GREATER_CAIRO(BigDecimal.valueOf(50.00),true),
    ALEX_DELTA (BigDecimal.valueOf(65.00),true),
    CANAL (BigDecimal.valueOf(70.00),true),
    UPPER_EGYPT (BigDecimal.valueOf(85.00),true),
    REMOTE(BigDecimal.valueOf(120.00),false);

    private final BigDecimal fee;
    private final boolean freeShippingEligible;


}
