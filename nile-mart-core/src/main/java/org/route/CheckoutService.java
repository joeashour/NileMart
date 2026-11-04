package org.route;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Component
@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final ProductService productService;
    private final ShippingQuoteService shippingQuoteService;
    private final TaxCalculator taxCalculator;
    private final OrderRegistry orderRegistry;
    private final List<NotificationSender> notificationSenders;

    public Order placeOrder(ShoppingCart cart, Governorate governorate) {
        if (cart.isEmpty()) {
            throw new StoreException("Cannot checkout an empty cart.");
        }

        for (Map.Entry<String, Integer> entry : cart.getItems().entrySet()) {
            Product product = productService.findBySku(entry.getKey());
            if (product.getStockQuantity() < entry.getValue()) {
                throw new StoreException("Checkout failed. Insufficient stock for product: " + product.getName());
            }
        }

        BigDecimal subtotal = cart.subtotal().setScale(2, RoundingMode.HALF_UP);
        BigDecimal vat = taxCalculator.calculateVat(subtotal).setScale(2, RoundingMode.HALF_UP);
        BigDecimal shippingFee = shippingQuoteService.quote(governorate, subtotal).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(vat).add(shippingFee).setScale(2, RoundingMode.HALF_UP);

        for (Map.Entry<String, Integer> entry : cart.getItems().entrySet()) {
            productService.getPrice(entry.getKey(), entry.getValue());
        }

        String orderNumber = orderRegistry.generateOrderNumber();
        Order order = new Order(
                orderNumber,
                cart.getCustomer(),
                governorate,
                new HashMap<>(cart.getItems()),
                subtotal,
                vat,
                shippingFee,
                total
        );
        orderRegistry.save(order);
        String message = "Order " + orderNumber + " confirmed. Total " + total + " EGP";
        for (NotificationSender sender : notificationSenders) {
            sender.send(cart.getCustomer(), message);
        }
        cart.clear();

        return order;
    }
}