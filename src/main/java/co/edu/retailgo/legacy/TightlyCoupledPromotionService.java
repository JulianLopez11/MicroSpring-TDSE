package co.edu.retailgo.legacy;

import co.edu.retailgo.adapters.ConsoleNotificationAdapter;
import co.edu.retailgo.adapters.InMemoryInventoryAdapter;

public class TightlyCoupledPromotionService {
    private final InMemoryInventoryAdapter inventory = new InMemoryInventoryAdapter();
    private final ConsoleNotificationAdapter notifications = new ConsoleNotificationAdapter();

    public String apply(String customerId, String sku, int quantity) {
        if (!inventory.hasStock(sku, quantity)) {
            return "PROMOTION_REJECTED_NO_STOCK";
        }
        notifications.send(customerId, "Promoción RetailGo aplicada para " + sku);
        return "PROMOTION_APPLIED";
    }
}
