package models.users;

import data.ActionType;
import data.LogHistory;
import data.TargetType;
import data_structures.ProductLinkedList;
import data_structures.QueueOrders;
import managers.OrderManager;
import managers.ProductManager;
import models.products.Product;
import models.products.ProductCategory;
import utils.Utility;

public class Customer extends User {
    private final ProductManager productManager;
    private final QueueOrders queueOrders;
    private final OrderManager orderManager;

    /**
     * Constructs a Customer with the given credentials and managers.
     *
     * @param username the customer's username
     * @param password the customer's password
     * @param address the customer's address
     * @param productManager the ProductManager instance
     * @param queueOrders the QueueOrders instance
     * @param orderManager the OrderManager instance
     */
    public Customer(String username, String password, String address, ProductManager productManager, QueueOrders queueOrders, OrderManager orderManager) {
        super(username, password, address);
        this.productManager = productManager;
        this.queueOrders = queueOrders;
        this.orderManager = orderManager;
    }

    @Override
    public void showMenu() {
        String[] choices = {"Log Out", "Browse Shop", "Edit Info"};
        while (true) {
            Utility.centralizeHeading("CUSTOMER MENU");
            showUserInfo();
            Utility.printUserChoices(choices);
            int userChoice = Utility.isInputInteger();
            switch (userChoice) {
                case -1:
                    continue;
                case 0:
                    return;
                case 1:
                    printProducts();
            }
        }
    }

    @Override
    protected void showUserInfo() {
        System.out.println("Username: " + username + " (ID #" + userID + ")");
        System.out.println("-".repeat(Utility.TOTAL_WIDTH));
    }

    @Override
    protected void printProducts() {
        while (true) {
            Utility.centralizeHeading("🛒 BROWSE SHOP");
            Utility.printUserChoices(Utility.productChoices);
            int userChoice = Utility.isInputInteger();
            ProductCategory categoryChoice = productManager.categoryChoices(userChoice);
            if (categoryChoice == null) {
                continue;
            }
            if (categoryChoice == ProductCategory.NULL) {
                return;
            }
            Utility.centralizeHeading(String.valueOf(categoryChoice));
            ProductLinkedList filteredProduct = productManager.getProductByCategory(categoryChoice);
            productManager.printProductByCategory(filteredProduct);
            userAction(filteredProduct);
        }
    }

    /**
     * Handles user actions on the filtered product list, such as buying.
     *
     * @param filteredProduct the list of products to show
     */

    private void userAction(ProductLinkedList filteredProduct) {
        String[] choices = {"🔙 Go Back", "Buy Product"};
        if (filteredProduct.isEmpty()) {
            Utility.stopper();
            return;
        }
        while (true) {
            Utility.centralizeHeading("Choose Action");
            Utility.printUserChoices(choices);
            int userChoice = Utility.isInputInteger();
            switch (userChoice) {
                case -1:
                    continue;
                case 0:
                    return;
                case 1:
                    buyProduct(filteredProduct);
            }
        }
    }

    /**
     * Handles the product selection and purchase process.
     *
     * @param filteredProduct the list of products to choose from
     */
    private void buyProduct(ProductLinkedList filteredProduct) {
        while (true) {
            Utility.centralizeHeading("Choose item to checkout");
            productManager.printProductByCategory(filteredProduct);
            System.out.println("━".repeat(25));
            System.out.println("[0] Go Back\n[1-" + filteredProduct.size() + "] Product Number");
            int userChoice = Utility.isInputInRange(filteredProduct.size());
            switch (userChoice) {
                case -1:
                    continue;
                case 0:
                    return;
            }
            Product chosenProduct = filteredProduct.get(userChoice - 1);
            if (chosenProduct.getProductStock() == 0) {
                System.out.println("\nOops! No stock available for [" + chosenProduct.getProductName() + "].");
                Utility.stopper();
                continue;
            }
            purchaseProcess(chosenProduct);
        }
    }

    /**
     * Handles the purchase process for the selected product, including quantity and confirmation.
     *
     * @param chosenProduct the product to purchase
     */
    private void purchaseProcess(Product chosenProduct) {
        while (true) {
            Utility.centralizeHeading("Purchasing Process");
            System.out.println("Item: " + chosenProduct.getProductName());
            System.out.println("Description: " + chosenProduct.getProductDescription());
            System.out.println("Price: ₱ " + chosenProduct.getProductPrice());
            System.out.println("Stock left: " + chosenProduct.getProductStock());

            int quantity = Utility.isInputInteger("Enter quantity to buy");
            if (quantity <= 0) {
                System.out.println("\nOops! You tried to buy in negative integer or zero. Please input a positive integer.");
                Utility.stopper();
                continue;
            }
            if (quantity > chosenProduct.getProductStock()) {
                System.out.println("\nOops! You tried to buy [" + quantity + "], but only [" + chosenProduct.getProductStock() + "] is available.");
                Utility.stopper();
                continue;
            }
            if (!isPurchasedConfirm(chosenProduct, quantity)) {
                System.out.println("\nPurchase cancelled by user.");
                return;

            }
            System.out.println("You order is being processed. Thank you, dear Customer!");
//            chosenProduct.setProductStock(chosenProduct.getProductStock() - quantity);
            double subtotal = chosenProduct.getProductPrice() * quantity;
            orderManager.addOrder(this.getUserID(), chosenProduct.getProductID(), chosenProduct.getProductPrice(), quantity, subtotal);
            LogHistory.addLog(userID, username, ActionType.ORDER_CREATE, TargetType.ORDER);
            return;
        }
    }

    /**
     * Confirms the purchase with the user.
     *
     * @param chosenProduct the product to confirm
     * @param quantity the quantity to confirm
     * @return {@code true} if confirmed, {@code false} otherwise
     */
    private boolean isPurchasedConfirm(Product chosenProduct, int quantity) {
        while (true) {
            Utility.centralizeHeading("Purchase Confirmation");
            System.out.println("Item: " + chosenProduct.getProductName());
            System.out.println("Description: " + chosenProduct.getProductDescription());
            System.out.println("Quantity: " + quantity);
            System.out.println("Total price: ₱ " + chosenProduct.getProductPrice() * quantity);
            System.out.println("━".repeat(25));
            System.out.println("[0] Cancel\n[1] Proceed to checkout");
            int userChoice = Utility.isInputInteger();
            switch (userChoice) {
                case -1:
                    continue;
                case 0:
                    return false;
                case 1:
                    return true;
            }
        }
    }
}
