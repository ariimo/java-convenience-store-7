package store.view;

import static store.ProductCategory.getProductCategoryByName;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import store.ProductCategory;
import store.model.Product;

public class InputValidator {

    private static final String PRODUCT_FORMAT = "\\[([a-zA-Z가-힣0-9]+)-([a-zA-Z가-힣-0-9]+)\\]";

    public void validateProductFormat(String input) {
        if (!isValidFormat(input)) {
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }

    private boolean isValidFormat(String product) {
        Pattern pattern = Pattern.compile(PRODUCT_FORMAT);
        Matcher matcher = pattern.matcher(product);
        return matcher.matches();
    }

    public Map<ProductCategory, Integer> getValidProductsToBuy(
        List<String> products, Map<ProductCategory, Product> inventory) {
        Map<ProductCategory, Integer> productsToBuy = new EnumMap<>(ProductCategory.class);
        for (String product : products) {
            product = product.substring(1, product.length() - 1);
            String[] target = product.split("-");
            ProductCategory productCategory = getValidProductCategory(target[0]);
            int quantity = getValidQuantity(target[1]);
            validateProductQuantity(inventory, productCategory, quantity);
            productsToBuy.put(productCategory, quantity);
        }
        return productsToBuy;
    }

    private void validateProductQuantity(
        Map<ProductCategory, Product> inventory, ProductCategory productCategory, int quantity) {
        int totalQuantity = inventory.get(productCategory).getTotalQuantity();
        if (totalQuantity < quantity) {
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
    }

    private ProductCategory getValidProductCategory(String target) {
        ProductCategory productCategory = getProductCategoryByName(target);
        if (productCategory == null) {
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }
        return productCategory;
    }

    private int getValidQuantity(String target) {
        try {
            int quantity = Integer.parseInt(target);
            validatePositiveNumber(quantity);
            return quantity;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    private void validatePositiveNumber(int target) {
        if (target <= 0) {
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    public boolean getValidAnswer(String input) {
        validateAnswer(input);
        return input.equals("Y");
    }

    private void validateAnswer(String input) {
        if (!input.equals("Y") && !input.equals("N")) {
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }
}
