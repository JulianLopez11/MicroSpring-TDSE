package co.edu.retailgo.adapters;

import co.edu.retailgo.domain.NotificationPort;
import co.edu.retailgo.minispring.RGComponent;

@RGComponent
public class ConsoleNotificationAdapter implements NotificationPort {
    @Override
    public void send(String customerId, String message) {
        System.out.printf("Notificación a %s: %s%n", customerId, message);
    }
}
