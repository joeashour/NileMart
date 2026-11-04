package org.route;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
@Component
public interface ProductRepository {
    Optional<Product> findBySku(String sku);
    List<Product> findAll();

}
