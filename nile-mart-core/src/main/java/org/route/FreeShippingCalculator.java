package org.route;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component("freeShipping")
@Qualifier("freeShipping")
public class FreeShippingCalculator implements ShippingCalculator {
    @Override
    public BigDecimal calculate(Governorate g) {
        return BigDecimal.ZERO;
    }
}
