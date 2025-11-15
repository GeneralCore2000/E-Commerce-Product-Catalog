package users;

import managers.FileManager;
import managers.ProductManager;
import products.Product;
import products.ProductCategory;
import utils.FilePaths;
import utils.Utility;

import java.util.ArrayList;

public class Customer extends User {
    private ProductManager productManager;

    public Customer(String username, String password, String address, ProductManager productManager) {
        super(username, password, address);
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
            }
        }
    }

    @Override
    public void showUserInfo() {
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
                System.out.println("\nPurchase cancelled by user.");
                return;

            }
            System.out.println("Thank you for your purchase, dear Customer!");
            chosenProduct.setProductStock(chosenProduct.getProductStock() - quantity);
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
