package store.view;

import java.text.DecimalFormat;
import java.util.Map;
import store.ProductCategory;
import store.model.Calculator;
import store.model.Product;

public class OutputView {

    static final int ZERO_QUANTITY = 0;
    static final String WELCOME_GREETING = "안녕하세요. W편의점입니다.\n현재 보유하고 있는 상품입니다.\n";
    private final DecimalFormat df = new DecimalFormat("###,###");

    public void printInventory(Map<ProductCategory, Product> inventory) {
        System.out.println(WELCOME_GREETING);
        for (Product product : inventory.values()) {
            printProductInfo(product);
        }
    }

    private void printProductInfo(Product product) {
        if (product.getPromotion() != null) {
            printPromotionProductInfo(product);
        }
        printRegularProductInfo(product);
    }

    private void printPromotionProductInfo(Product product) {
        if (product.getPromotionQuantity() != ZERO_QUANTITY) {
            System.out.printf("- %s %s원 %d개 %s%n",
                product.getName(), df.format(product.getPrice()),
                product.getPromotionQuantity(), product.getPromotionName());
        }
        if (product.getPromotionQuantity() == ZERO_QUANTITY) {
            System.out.printf("- %s %s원 재고 없음 %s%n",
                product.getName(), df.format(product.getPrice()),
                product.getPromotionName());
        }
    }

    private void printRegularProductInfo(Product product) {
        if (product.getQuantity() != ZERO_QUANTITY) {
            System.out.printf("- %s %s원 %d개%n",
                product.getName(), df.format(product.getPrice()), product.getQuantity());
        }
        if (product.getQuantity() == ZERO_QUANTITY) {
            System.out.printf("- %s %s원 재고 없음%n", product.getName(), df.format(product.getPrice()));
        }
    }

    public void printReceipt(Calculator calculator) {
        System.out.println("=============W 편의점================");
        Map<ProductCategory, Product> purchase = calculator.getPurchase();
        printQuantityInfo(purchase);
        printPromotionBenefitInfo(purchase);
        printPurchaseInfo(calculator);
    }

    private void printQuantityInfo(Map<ProductCategory, Product> purchase) {
        System.out.println("상품명         수량         금액");
        for (ProductCategory category : purchase.keySet()) {
            Product product = purchase.get(category);
            String productName = category.getName();
            int quantity = product.getTotalQuantity();
            int price = category.getPrice() * quantity;
            System.out.printf("%-10s %4d %12s%n", productName, quantity, df.format(price));
        }
    }

    private void printPromotionBenefitInfo(Map<ProductCategory, Product> purchase) {
        System.out.println("=============증    정===============");
        for (ProductCategory category : purchase.keySet()) {
            Product product = purchase.get(category);
            if (product.getPromotionQuantity() > 0) {
                System.out.printf("%-10s %15d%n", category.getName(),
                    product.getPromotionBenefit());
            }
        }
    }

    private void printPurchaseInfo(Calculator calculator) {
        System.out.println("===================================");
        int totalPayment = calculator.getTotalPayment();
        int promotionDiscount = calculator.getPromotionDiscount();
        int membershipDiscount = calculator.getMembershipDiscount();
        int finalPayment = calculator.getFinalPayment();
        System.out.printf("총구매액 %9d %12s%n", calculator.getTotalQuantity(),
            df.format(totalPayment));
        System.out.printf("행사할인%23s%n", "-" + df.format(promotionDiscount));
        System.out.printf("멤버십할인%18s%n", "-" + df.format(membershipDiscount));
        System.out.printf("내실돈%24s%n", df.format(finalPayment));
    }
}
