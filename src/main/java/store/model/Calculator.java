package store.model;

import java.util.Map;
import store.ProductCategory;
import store.Promotion;

public class Calculator {

    private static final float MEMBERSHIP_DISCOUNT_RATE = 0.3F;
    private static final int MAX_MEMBERSHIP_AMOUNT = 8000;

    private final Map<ProductCategory, Product> purchase;
    private final int totalPayment;
    private final int totalQuantity;
    private final int promotionDiscount;
    private int membershipDiscount = 0;

    public Calculator(Map<ProductCategory, Product> purchase) {
        this.purchase = purchase;
        this.totalPayment = initializeTotalPayment();
        this.totalQuantity = initializeTotalQuantity();
        this.promotionDiscount = initializePromotionDiscount();
    }

    public Map<ProductCategory, Product> getPurchase() {
        return purchase;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getPromotionDiscount() {
        return promotionDiscount;
    }

    public int getMembershipDiscount() {
        return membershipDiscount;
    }

    public int getFinalPayment() {
        return totalPayment - promotionDiscount - membershipDiscount;
    }

    private int initializeTotalPayment() {
        int totalPayment = 0;
        for (ProductCategory productCategory : purchase.keySet()) {
            Product purchaseProduct = purchase.get(productCategory);
            totalPayment += purchaseProduct.getTotalQuantity() * productCategory.getPrice();
        }
        return totalPayment;
    }

    private int initializeTotalQuantity() {
        int totalQuantity = 0;
        for (ProductCategory productCategory : purchase.keySet()) {
            Product purchasaeProduct = purchase.get(productCategory);
            totalQuantity += purchasaeProduct.getTotalQuantity();
        }
        return totalQuantity;
    }

    private int initializePromotionDiscount() {
        int promotionDiscount = 0;
        for (ProductCategory productCategory : purchase.keySet()) {
            int promotionApplyAmount = getValidPromotionApplyAmount(productCategory);
            promotionDiscount += promotionApplyAmount;
        }
        return promotionDiscount;
    }

    private int getValidPromotionApplyAmount(ProductCategory productCategory) {
        Product purchasaeProduct = purchase.get(productCategory);
        if (purchasaeProduct.getPromotion() != Promotion.NONE) {
            int promotionUnit = purchasaeProduct.getPromotion().getPromotionUnit();
            int promotionQuantity = purchasaeProduct.getPromotionQuantity();
            return (promotionQuantity / promotionUnit) * productCategory.getPrice();
        }
        return 0;
    }

    public void setMembershipDiscount() {
        int nonPromotionPayment = 0;
        for (ProductCategory productCategory : purchase.keySet()) {
            Product regularProduct = purchase.get(productCategory);
            int productPrice = regularProduct.getPrice();
            nonPromotionPayment += regularProduct.getQuantity() * productPrice;
        }
        this.membershipDiscount = getValidMembershipApplyAmount(nonPromotionPayment);
    }

    private int getValidMembershipApplyAmount(int nonPromotionPayment) {
        int membershipDiscountApplyAmount = (int) (nonPromotionPayment * MEMBERSHIP_DISCOUNT_RATE);
        return Math.min(membershipDiscountApplyAmount, MAX_MEMBERSHIP_AMOUNT);
    }
}
