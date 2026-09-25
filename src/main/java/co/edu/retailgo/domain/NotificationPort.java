package co.edu.retailgo.domain;

public interface NotificationPort {
    void send(String customerId, String message);
}
