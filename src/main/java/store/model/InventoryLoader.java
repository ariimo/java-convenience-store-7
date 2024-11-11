package store.model;

import static store.ProductCategory.getProductCategoryByName;
import static store.Promotion.NONE;
import static store.Promotion.getPromotionByName;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.EnumMap;
import java.util.Map;
import store.ProductCategory;
import store.Promotion;

public class InventoryLoader {

    private static final String FILE_PATH = "/Users/moarim/Documents/Woowa/java-convenience-store-7-ariimo/src/main/resources/products.md";
    private final Map<ProductCategory, Product> inventory = new EnumMap<>(ProductCategory.class);

    public InventoryLoader() {
        initInventory();
    }

    public Map<ProductCategory, Product> getInventory() {
        return inventory;
    }

    private void initInventory() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_PATH));
            String line = br.readLine();// 첫 줄(속성 이름)은 건너뜁니다.
            while ((line = br.readLine()) != null) {
                putProduct(line);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void putProduct(String line) {
        String[] parts = line.split(",");
        ProductCategory productCategory = getValidProductCategory(parts[0]);
        int quantity = getValidQuantity(parts[2]);
        Promotion promotion = getValidPromotion(parts[3]);

        Product product = inventory.get(productCategory);
        createOrUpdateProduct(product, productCategory, quantity, promotion);
    }

    private void createOrUpdateProduct(Product product, ProductCategory productCategory,
        int quantity, Promotion promotion) {
        if (product == null) {
            createProduct(productCategory, quantity, promotion);
        }
        if (product != null) {
            updateProduct(product, quantity);
        }
    }

    private void createProduct(ProductCategory productCategory, int quantity, Promotion promotion) {
        if (promotion != NONE) {
            inventory.put(productCategory, new Product(productCategory, quantity, promotion));
        }
        if (promotion == NONE) {
            inventory.put(productCategory, new Product(productCategory, quantity));
        }
    }

    private void updateProduct(Product product, int quantity) {
        product.setQuantity(quantity);
    }

    private ProductCategory getValidProductCategory(String target) {
        ProductCategory productCategory = getProductCategoryByName(target);
        if (productCategory == null) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다.");
        }
        return productCategory;
    }

    private int getValidQuantity(String target) {
        try {
            int quantity = Integer.parseInt(target);
            validatePositiveNumber(quantity);
            return quantity;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 수량이 숫자가 아닙니다. ");
        }
    }

    private void validatePositiveNumber(int target) {
        if (target <= 0) {
            throw new IllegalArgumentException("[ERROR] 수량이 양수가 아닙니다. ");
        }
    }

    private Promotion getValidPromotion(String target) {
        Promotion promotion = getPromotionByName(target);
        if (promotion == null) {
            throw new IllegalStateException("[ERROR] 존재하지 않는 프로모션입니다.");
        }
        return promotion;
    }
}
