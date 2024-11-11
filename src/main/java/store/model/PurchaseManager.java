package store.model;

import static camp.nextstep.edu.missionutils.DateTimes.now;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;
import store.ProductCategory;
import store.Promotion;

public class PurchaseManager {

    static final int ASK_PROMOTION_ADDITION = 0;
    static final int ASK_REGULAR_PURCHASE = 1;
    static final int ASK_NONE = 2;

    private final LocalDate today;
    Map<ProductCategory, Product> inventory;
    Map<ProductCategory, Integer> purchaseProposal;
    Map<ProductCategory, Product> purchase;

    public PurchaseManager(Map<ProductCategory, Product> inventory,
        Map<ProductCategory, Integer> purchaseProposal) {
        this.inventory = inventory;
        this.purchaseProposal = purchaseProposal;
        this.today = now().toLocalDate();
        this.purchase = new EnumMap<>(ProductCategory.class);
    }

    public Map<ProductCategory, Product> getPurchase() {
        return purchase;
    }

    public boolean isValidPeriod(ProductCategory productCategory) {
        Product product = inventory.get(productCategory);
        return product.getPromotion() != Promotion.NONE &&
            product.getPromotion().isBetweenPromotionPeriod(today);
    }

    public void putNonePromotionProduct(ProductCategory productCategory) {
        int consumerQuantity = purchaseProposal.get(productCategory);
        purchase.put(productCategory, new Product(productCategory, consumerQuantity));
    }

    public int getPromotionStatus(ProductCategory productCategory) {
        Product stockProduct = inventory.get(productCategory);
        int promotionUnit = stockProduct.getPromotion().getPromotionUnit();
        int consumerQuantity = purchaseProposal.get(productCategory);
        int availableQuantity = getAvailablePromotionQuantity(productCategory);

        if (consumerQuantity <= availableQuantity) {
            if (consumerQuantity % promotionUnit == promotionUnit - 1) {
                purchase.put(productCategory, new Product(productCategory, consumerQuantity,
                    stockProduct.getPromotion()));
                return ASK_PROMOTION_ADDITION;
            }
            if (consumerQuantity % promotionUnit == 0) {
                purchase.put(productCategory, new Product(productCategory, consumerQuantity,
                    stockProduct.getPromotion()));
                return ASK_NONE;
            }
            purchase.put(productCategory,
                new Product(productCategory, (consumerQuantity / promotionUnit) * promotionUnit,
                    stockProduct.getPromotion()));
            return ASK_REGULAR_PURCHASE;
        }

        purchase.put(productCategory,
            new Product(productCategory, availableQuantity, stockProduct.getPromotion()));
        return ASK_REGULAR_PURCHASE;
    }

    private int getAvailablePromotionQuantity(ProductCategory productCategory) {
        Product stockProduct = inventory.get(productCategory);
        int promotionUnit = stockProduct.getPromotion().getPromotionUnit();
        int stockPromotionQuantity = stockProduct.getPromotionQuantity();
        return (stockPromotionQuantity / promotionUnit) * promotionUnit;
    }

    public int getNoPromotionQuantity(ProductCategory productCategory) {
        Product purchaseProduct = purchase.get(productCategory);
        int consumerQuantity = purchaseProposal.get(productCategory);
        return consumerQuantity - purchaseProduct.getPromotionQuantity();
    }

    public void addPromotionQuantity(ProductCategory productCategory) {
        Product purchaseProduct = purchase.get(productCategory);
        purchaseProduct.addPromotionQuantity(1);
    }

    public void addNoPromotionQuantity(ProductCategory productCategory, int quantity) {
        Product purchaseProduct = purchase.get(productCategory);
        int remainStockPromotionQuantity = inventory.get(productCategory).getPromotionQuantity()
            - purchaseProduct.getPromotionQuantity();
        if (remainStockPromotionQuantity <= quantity) {
            purchaseProduct.addPromotionQuantity(remainStockPromotionQuantity);
            quantity -= remainStockPromotionQuantity;
        }
        purchaseProduct.addRegularQuantity(quantity);
    }
}
