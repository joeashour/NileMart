package org.route;

import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
@Component
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Product {
    private String name;
    private String sku;
    private BigDecimal price;
    private String category;
    private boolean isActive;
    private int stockQuantity;
   }
