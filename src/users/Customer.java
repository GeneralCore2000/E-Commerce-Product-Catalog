package users;

import managers.EditUserInfos;
import managers.FileManager;
import managers.ProductManager;
import products.Product;
import products.ProductCategory;
import utils.*;

import java.util.ArrayList;

public class Customer extends User {
    private ProductManager productManager;

    public Customer(String username, String password, String address, ProductManager productManager, EditUserInfos editUserInfos) {
        super(username, password, address, editUserInfos);
        this.productManager = productManager;
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
                    break;
                case 2:
                    editInfo();
            }
        }
    }

    @Override
    public void showUserInfo() {
        System.out.println("Username: " + username + " (ID #" + userID + ")");
        System.out.println("Address: " + address);
        System.out.println("-".repeat(Utility.TOTAL_WIDTH));
    }

    @Override
    protected void editInfo() {
        while (true) {
            Utility.centralizeHeading("EDIT INFO");
            Utility.printUserChoices("Go Back", "Edit name", "Edit password", "Edit address");
            int userChoice = Utility.isInputInteger();
            switch (userChoice) {
                case -1:
                    continue;
                case 0:
                    return;
                case 1:
                    editUserInfos.updateUsername(this);
                    break;
                case 2:
                    editUserInfos.updatePassword(this);
                    break;
                case 3:
                    editUserInfos.updateAddress(this);
                    break;
            }
        }
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
            ArrayList<Product> filteredProduct = productManager.getProductByCategory(categoryChoice);
            productManager.printProductByCategory(filteredProduct);
            userAction(filteredProduct);
        }
    }

    private void userAction(ArrayList<Product> filteredProduct) {
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

    private void buyProduct(ArrayList<Product> filteredProduct) {
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

    private void purchaseProcess(Product chosenProduct) {
        while (true) {
            Utility.centralizeHeading("Purchasing Process");
            System.out.println("Item: " + chosenProduct.getProductName());
            System.out.println("Description: " + chosenProduct.getProductDescription());
            System.out.println("Price: ₱ " + chosenProduct.getProductPrice());
            System.out.println("Stock left: " + chosenProduct.getProductStock());

            int quantity = Utility.isInputInteger("Enter quantity to buy");
            if (quantity < 0) {
                System.out.println("\nOops! You tried to buy in negative integer. Please input a positive integer.");
                Utility.stopper();
                continue;
            }
            if (quantity > chosenProduct.getProductStock()) {
                System.out.println("\nOops! You tried to buy [" + quantity + "], but only [" + chosenProduct.getProductStock() + "] is available.");
                Utility.stopper();
                continue;
            }
            if (!isPurchasedConfirm(chosenProduct, quantity)) {
                LogHistory.add(userID, username, ActionLog.CHECKOUT, RemarksLog.CANCELLED);
                System.out.println("\nPurchase cancelled by user.");
                return;
            }
            System.out.println("Thank you for your purchase, dear Customer!");
            chosenProduct.setProductStock(chosenProduct.getProductStock() - quantity);
            LogHistory.add(userID, username, ActionLog.CHECKOUT, RemarksLog.SUCCESSFUL);
            FileManager.updateFile(FilePaths.PRODUCTS, productManager.convertProductTo2DList());
            return;
        }
    }

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
