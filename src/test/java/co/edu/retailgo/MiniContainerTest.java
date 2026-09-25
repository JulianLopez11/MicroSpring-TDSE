package co.edu.retailgo;

import co.edu.retailgo.adapters.ConsoleNotificationAdapter;
import co.edu.retailgo.adapters.InMemoryInventoryAdapter;
import co.edu.retailgo.domain.InventoryPort;
import co.edu.retailgo.domain.PromotionService;
import co.edu.retailgo.minispring.ContainerException;
import co.edu.retailgo.minispring.MiniContainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MiniContainerTest {
    @Test
    void createsAndInjectsPromotionService() {
        MiniContainer container = standardContainer();
        PromotionService service = container.getBean(PromotionService.class);

        assertEquals("PROMOTION_APPLIED", service.apply("C-1", "SKU-100", 2));
    }

    @Test
    void reusesSingletons() {
        MiniContainer container = standardContainer();

        assertSame(
                container.getBean(PromotionService.class),
                container.getBean(PromotionService.class)
        );
    }

    @Test
    void reportsMissingImplementation() {
        MiniContainer container = new MiniContainer(PromotionService.class);
        ContainerException error = assertThrows(
                ContainerException.class,
                () -> container.getBean(PromotionService.class)
        );

        assertEquals(true, error.getMessage().contains("No existe componente"));
    }

    @Test
    void detectsCircularDependency() {
        MiniContainer container = new MiniContainer(CircularA.class, CircularB.class);

        assertThrows(ContainerException.class, () -> container.getBean(CircularA.class));
    }

    private MiniContainer standardContainer() {
        return new MiniContainer(
                PromotionService.class,
                InMemoryInventoryAdapter.class,
                ConsoleNotificationAdapter.class
        );
    }
}
