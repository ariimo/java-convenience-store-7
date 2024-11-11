package store.view;

import camp.nextstep.edu.missionutils.Console;
import java.util.List;
import java.util.Map;
import store.ProductCategory;
import store.model.Product;

public class InputView {

    private final InputValidator inputValidator;

    public InputView() {
        this.inputValidator = new InputValidator();
    }

    public Map<ProductCategory, Integer> getPurchaseProposal(
        Map<ProductCategory, Product> inventory) {
        while (true) {
            try {
                System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
                List<String> products = getSplitProducts(Console.readLine());
                return inputValidator.getValidProductsToBuy(products, inventory);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private List<String> getSplitProducts(String input) {
        String[] items = input.split(",");
        for (String item : items) {
            item = item.trim();
            inputValidator.validateProductFormat(item);
        }
        return List.of(items);
    }

    public boolean askPromotionAddition(String productName) {
        while (true) {
            try {
                System.out.printf("현재 %s은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)%n", productName);
                return inputValidator.getValidAnswer(Console.readLine());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean askRegularPurchase(String productName, int noPromotionQuantity) {
        while (true) {
            try {
                System.out.printf("현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)%n",
                    productName,
                    noPromotionQuantity);
                return inputValidator.getValidAnswer(Console.readLine());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean askMembershipDiscount() {
        while (true) {
            try {
                System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
                return inputValidator.getValidAnswer(Console.readLine());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean askAdditionalPurchase() {
        while (true) {
            try {
                System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
                return inputValidator.getValidAnswer(Console.readLine());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
