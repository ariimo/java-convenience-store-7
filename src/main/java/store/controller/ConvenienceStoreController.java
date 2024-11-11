package store.controller;

import java.util.Map;
import store.ProductCategory;
import store.model.Calculator;
import store.model.InventoryManager;
import store.model.Product;
import store.model.PurchaseManager;
import store.view.InputView;
import store.view.OutputView;

public class ConvenienceStoreController {

    static final int ASK_PROMOTION_ADDITION = 0;
    static final int ASK_REGULAR_PURCHASE = 1;

    private final InputView inputView;
    private final OutputView outputView;
    private final InventoryManager inventoryManager;
    private PurchaseManager purchaseManager;
    private Calculator calculator;

    public ConvenienceStoreController() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.inventoryManager = new InventoryManager();
    }

    public void run() {
        boolean runNext = true;
        while (runNext) {
            Map<ProductCategory, Product> inventory = inventoryManager.getInventory();
            outputView.printInventory(inventory);
            controlPurchase(inventory);
            Map<ProductCategory, Product> purchase = purchaseManager.getPurchase();
            inventoryManager.update(purchase);
            controlCalculation(purchase);
            runNext = inputView.askAdditionalPurchase();
        }
    }

    private void controlPurchase(Map<ProductCategory, Product> inventory) {
        Map<ProductCategory, Integer> purchaseProposal = inputView.getPurchaseProposal(
            inventory);
        purchaseManager = new PurchaseManager(inventory, purchaseProposal);
        setPurchaseProducts(purchaseProposal);
    }

    private void controlCalculation(Map<ProductCategory, Product> purchase) {
        calculator = new Calculator(purchase);
        boolean membershipApply = inputView.askMembershipDiscount();
        if (membershipApply) {
            calculator.setMembershipDiscount();
        }
        outputView.printReceipt(calculator);
    }

    private void setPurchaseProducts(
        Map<ProductCategory, Integer> purchaseProposal) {
        for (ProductCategory productCategory : purchaseProposal.keySet()) {
            if (purchaseManager.isValidPeriod(productCategory)) {
                int status = purchaseManager.getPromotionStatus(productCategory);
                startAskLogicBy(status, productCategory);
            }
            if (!purchaseManager.isValidPeriod(productCategory)) {
                purchaseManager.putNonePromotionProduct(productCategory);
            }
        }
    }

    private void startAskLogicBy(int status, ProductCategory productCategory) {
        if (status == ASK_PROMOTION_ADDITION) {
            boolean answer = inputView.askPromotionAddition(productCategory.getName());
            if (answer) {
                purchaseManager.addPromotionQuantity(productCategory);
            }
        }
        if (status == ASK_REGULAR_PURCHASE) {
            int noPromotionQuantity = purchaseManager.getNoPromotionQuantity(productCategory);
            boolean answer = inputView.askRegularPurchase(productCategory.getName(),
                noPromotionQuantity);
            if (answer) {
                purchaseManager.addNoPromotionQuantity(productCategory, noPromotionQuantity);
            }
        }
    }
}
