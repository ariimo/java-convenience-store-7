package store.model;

import store.ProductCategory;
import store.Promotion;

public class Product {

    private final ProductCategory productCategory;
    private int quantity = 0;
    private int promotionQuantity = 0;
    private Promotion promotion = Promotion.NONE;

    public Product(ProductCategory productCategory, int quantity) {
        this.productCategory = productCategory;
        this.quantity = quantity;
    }

    public Product(ProductCategory productCategory, int promotionQuantity, Promotion promotion) {
        this.productCategory = productCategory;
        this.promotionQuantity = promotionQuantity;
        this.promotion = promotion;
    }

    public int getTotalQuantity() {
        return this.quantity + this.promotionQuantity;
    }

    public Promotion getPromotion() {
        return promotion;
    }

    public String getName() {
        return productCategory.getName();
    }

    public int getPrice() {
        return productCategory.getPrice();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPromotionQuantity() {
        return promotionQuantity;
    }

    public String getPromotionName() {
        return promotion.getName();
    }

    public int getPromotionBenefit() {
        return promotionQuantity / promotion.getPromotionUnit();
    }

    public void addPromotionQuantity(int quantity) {
        this.promotionQuantity += quantity;
    }

    public void addRegularQuantity(int quantity) {
        this.quantity += quantity;
    }

    public void updatePromotionQuantity(int purchasedQuantity) {
        this.promotionQuantity -= purchasedQuantity;
    }

    public void updateQuantity(int purchasedQuantity) {
        this.quantity -= purchasedQuantity;
    }
}
