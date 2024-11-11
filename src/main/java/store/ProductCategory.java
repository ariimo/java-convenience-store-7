package store;

import java.util.Arrays;

public enum ProductCategory {
    COLA("콜라", 1000),
    CIDER("사이다", 1000),
    ORANGE_JUICE("오렌지주스", 1800),
    SPARKLING_WATER("탄산수", 1200),
    WATER("물", 500),
    VITAMIN_WATER("비타민워터", 1500),
    CHIPS("감자칩", 1500),
    CHOCOLATE_BAR("초코바", 1200),
    ENERGY_BAR("에너지바", 2000),
    MEAL_BOX("정식도시락", 6400),
    CUP_RAMEN("컵라면", 1700);

    private final String name;
    private final int price;

    ProductCategory(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public static ProductCategory getProductCategoryByName(final String name) {
        return Arrays.stream(ProductCategory.values())
            .filter(productCategory -> productCategory.getName().equals(name))
            .findFirst()
            .orElse(null);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
