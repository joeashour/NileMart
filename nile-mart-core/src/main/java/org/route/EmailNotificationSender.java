package org.route;

import org.springframework.stereotype.Component;

@Component
public class EmailNotificationSender implements NotificationSender{
    @Override
    public void send(Customer customer, String message) {
        System.out.println("[EMAIL] to"+customer.email()+": "+message);
    }
}
