package org.route;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    void main(){
        var context = new AnnotationConfigApplicationContext(AppConfig.class);
        ProductService productService = context.getBean(ProductService.class);
        CheckoutService checkoutService = context.getBean(CheckoutService.class);

        Map<String, ShoppingCart> carts = new HashMap<>();
        Scanner scan = new Scanner(System.in);

        System.out.println("========== NILEMART ==========");

        boolean running = true;
        while (running) {
            System.out.println("\n1. Show products");
            System.out.println("2. Create a cart");
            System.out.println("3. Add a product to a cart");
            System.out.println("4. Show a cart");
            System.out.println("5. Check out a cart");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            String choice = scan.nextLine();

            try {
                switch (choice) {
                    case "1" -> {
                        System.out.println("\n--- Product Catalog ---");
                        productService.findAll().forEach(p ->
                                System.out.println(p.getSku() + " | " + p.getName() + " | " + p.getPrice() + " EGP | Stock: " + p.getStockQuantity())
                        );
                    }
                    case "2" -> {
                        System.out.print("Enter cart ID (e.g. cart1): ");
                        String cartId = scan.nextLine();
                        System.out.print("Enter customer full name: ");
                        String name = scan.nextLine();
                        System.out.print("Enter customer phone: ");
                        String phone = scan.nextLine();
                        System.out.print("Enter customer email (optional): ");
                        String email = scan.nextLine();

                        Customer customer = new Customer(name, phone, email.isBlank() ? null : email);

                        ShoppingCart cart = context.getBean(ShoppingCart.class, cartId, customer);
                        carts.put(cartId, cart);
                        System.out.println("Cart " + cartId + " created successfully for " + name);
                    }
                    case "3" -> {
                        System.out.print("Enter cart ID: ");
                        String cartId = scan.nextLine();
                        ShoppingCart cart = carts.get(cartId);
                        if (cart == null) {
                            throw new StoreException("Cart not found.");
                        }
                        System.out.print("Enter Product SKU: ");
                        String sku = scan.nextLine();
                        System.out.print("Enter Quantity: ");
                        int qty = Integer.parseInt(scan.nextLine());

                        cart.addItem(sku, qty);
                        System.out.println("Product added successfully to cart " + cartId);
                    }
                    case "4" -> {
                        System.out.print("Enter cart ID: ");
                        String cartId = scan.nextLine();
                        ShoppingCart cart = carts.get(cartId);
                        if (cart == null) {
                            throw new StoreException("Cart not found.");
                        }
                        System.out.println("\n--- Cart " + cartId + " (" + cart.getCustomer().fullName() + ") ---");
                        if (cart.isEmpty()) {
                            System.out.println("Cart is empty.");
                        } else {
                            cart.getItems().forEach((sku, qty) ->
                                    System.out.println("- " + sku + ": " + qty)
                            );
                            System.out.println("Subtotal: " + cart.subtotal() + " EGP");
                        }
                    }
                    case "5" -> {
                        System.out.print("Enter cart ID: ");
                        String cartId = scan.nextLine();
                        ShoppingCart cart = carts.get(cartId);
                        if (cart == null) {
                            throw new StoreException("Cart not found.");
                        }
                        System.out.print("Enter Governorate (e.g. GIZA, CAIRO, ALEXANDRIA, ASWAN): ");
                        Governorate gov = Governorate.valueOf(scan.nextLine().toUpperCase().trim());

                        Order order = checkoutService.placeOrder(cart, gov);
                        System.out.println("Order placed successfully! Order Number: " + order.orderNumber() + ", Total: " + order.total() + " EGP");
                    }
                    case "0" -> {
                        running = false;
                        System.out.println("Exiting NileMart...");
                    }
                    default -> System.out.println("Invalid option, please try again.");
                }
            } catch (Exception e) {
                System.out.println("[Error] " + e.getMessage());
            }
        }


    }
}
