package org.route;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ShippingQuoteService {

    private final ShippingCalculator zoneShippingCalculator;
    private final ShippingCalculator freeShippingCalculator;
    private final BigDecimal freeShippingThreshold;

    public ShippingQuoteService(
            ShippingCalculator zoneShippingCalculator,
            @Qualifier("freeShipping") ShippingCalculator freeShippingCalculator,
            @Value("${store.free-shipping-threshold}") BigDecimal freeShippingThreshold) {
        this.zoneShippingCalculator = zoneShippingCalculator;
        this.freeShippingCalculator = freeShippingCalculator;
        this.freeShippingThreshold = freeShippingThreshold;
    }

    public BigDecimal quote(Governorate governorate, BigDecimal subtotal) {
        boolean isEligibleForFreeShipping = subtotal.compareTo(freeShippingThreshold) >= 0
                && governorate.getZone().isFreeShippingEligible();

        if (isEligibleForFreeShipping) {
            return freeShippingCalculator.calculate(governorate);
        } else {
            return zoneShippingCalculator.calculate(governorate);
        }
    }
}