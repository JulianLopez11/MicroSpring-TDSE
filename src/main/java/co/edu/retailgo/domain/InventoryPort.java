package co.edu.retailgo.domain;

public interface InventoryPort {
    boolean hasStock(String sku, int quantity);
}
