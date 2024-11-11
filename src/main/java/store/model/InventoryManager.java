package store.model;

import java.util.Map;
import store.ProductCategory;

public class InventoryManager {

    private final Map<ProductCategory, Product> inventory;

    public InventoryManager() {
        InventoryLoader inventoryLoader = new InventoryLoader();
        this.inventory = inventoryLoader.getInventory();
    }

    public Map<ProductCategory, Product> getInventory() {
        return inventory;
    }

    public void update(Map<ProductCategory, Product> purchase) {
        for (ProductCategory productCategory : purchase.keySet()) {
            Product purchaseProduct = purchase.get(productCategory);
            updatePromotionQuantity(productCategory, purchaseProduct.getPromotionQuantity());
            updateQuantity(productCategory, purchaseProduct.getQuantity());
        }
    }

    private void updatePromotionQuantity(ProductCategory productCategory, int purchasedQuantity) {
        Product inventoryProduct = inventory.get(productCategory);
        inventoryProduct.updatePromotionQuantity(purchasedQuantity);
    }

    private void updateQuantity(ProductCategory productCategory, int purchasedQuantity) {
        Product inventoryProduct = inventory.get(productCategory);
        inventoryProduct.updateQuantity(purchasedQuantity);
    }
}
