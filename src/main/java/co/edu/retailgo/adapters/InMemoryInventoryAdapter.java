package co.edu.retailgo.adapters;

import co.edu.retailgo.domain.InventoryPort;
import co.edu.retailgo.minispring.RGComponent;

@RGComponent
public class InMemoryInventoryAdapter implements InventoryPort {
    @Override
    public boolean hasStock(String sku, int quantity) {
        return quantity <= 5 && !"SKU-OUT".equals(sku);
    }
}
