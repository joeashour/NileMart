package org.route;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
public interface ShippingCalculator {
    BigDecimal calculate(Governorate g);
}
