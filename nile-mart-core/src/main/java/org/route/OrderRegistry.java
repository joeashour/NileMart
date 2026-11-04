package org.route;

import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderRegistry {
    private final List<Order> orders = new ArrayList<>();
    private int orderCounter = 1;

    public synchronized String generateOrderNumber() {
        String dateStr = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String formattedSeq = String.format("%05d", orderCounter++);
        return "NM-" + dateStr + "-" + formattedSeq;
    }

    public void save(Order order) {
        orders.add(order);
    }

    @PreDestroy
    public void destroy() {
        BigDecimal totalRevenue = orders.stream()
                .map(Order::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
        System.out.println("End of day: " + orders.size() + " orders, revenue " + totalRevenue + " EGP");
    }
}
