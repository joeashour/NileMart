package org.route;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@Scope("prititype")
@Getter
public class ShoppingCart {
    private final String cartNumber;
    private final Customer customer;
    private final Map<String,Integer> items =new HashMap<>();
    private final ProductService productService;
    private final PricingService pricingService;
    private final int maxQuantityPerProduct;

    public ShoppingCart(
            String cartNumber,
            Customer customer,
            ProductService productService,
            PricingService pricingService,
            @Value("${store.max-quantity-per-product}") int maxQuantityPerProduct) {
        this.cartNumber = cartNumber;
        this.customer = customer;
        this.productService = productService;
        this.pricingService = pricingService;
        this.maxQuantityPerProduct = maxQuantityPerProduct;
    }
    public void addItem(String sku,int qty){
        Product product=productService.findBySku(sku);
        if (product.getStockQuantity()<=0){
            throw new StoreException("Product " +product.getName() + " not found.");
        }
        int currentQty = items.getOrDefault(sku, 0);
        int newQty = currentQty + qty;
        if (newQty > maxQuantityPerProduct) {
            throw new StoreException("Cannot add items. Maximum allowed per product is " + maxQuantityPerProduct + ".");
        }
        if (newQty > product.getStockQuantity()) {
            throw new StoreException("Cannot add items. Available stock is only " + product.getStockQuantity() + ".");
        }
        items.put(sku, newQty);
    }
    public BigDecimal subtotal() {
        BigDecimal totalSubtotal = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            Product product = productService.findBySku(entry.getKey());
            int qty = entry.getValue();
            BigDecimal linePrice = pricingService.getPrice(product, qty);
            totalSubtotal = totalSubtotal.add(linePrice);
        }
        return totalSubtotal;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();

}
}
