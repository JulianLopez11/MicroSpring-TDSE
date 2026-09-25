package co.edu.retailgo;

import co.edu.retailgo.adapters.ConsoleNotificationAdapter;
import co.edu.retailgo.adapters.InMemoryInventoryAdapter;
import co.edu.retailgo.domain.PromotionService;
import co.edu.retailgo.minispring.MiniContainer;

public class App {
    public static void main(String[] args) {
        MiniContainer container = new MiniContainer(
                PromotionService.class,
                InMemoryInventoryAdapter.class,
                ConsoleNotificationAdapter.class
        );

        PromotionService service = container.getBean(PromotionService.class);
        System.out.println(service.apply("CLI-1042", "SKU-100", 2));
    }
}
