package co.edu.retailgo.domain;

import co.edu.retailgo.minispring.RGComponent;
import co.edu.retailgo.minispring.RGInject;

@RGComponent
public class PromotionService {
    private final InventoryPort inventory;
    private final NotificationPort notifications;

    @RGInject
    public PromotionService(InventoryPort inventory, NotificationPort notifications) {
        this.inventory = inventory;
        this.notifications = notifications;
    }

    public String apply(String customerId, String sku, int quantity) {
        if (!inventory.hasStock(sku, quantity)) {
            return "PROMOTION_REJECTED_NO_STOCK";
        }
        notifications.send(customerId, "Promoción RetailGo aplicada para " + sku);
        return "PROMOTION_APPLIED";
    }
}
