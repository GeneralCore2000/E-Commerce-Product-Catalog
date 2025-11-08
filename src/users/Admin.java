package users;

import managers.ProductManager;
import products.Product;
import products.ProductLinkedList;
import utils.Utility;

import java.util.ArrayList;

public class Admin extends User implements AdminPrivilege {
    private final ProductManager productManager;

    public Admin(String username, String password, String address, ProductManager productManager) {
        super(username, password, address);
        this.productManager = productManager;
    }

    @Override
    public void showMenu() {
        String[] choices = {"🔙 Log Out", "📦 Manage Inventory"};
        while (true) {
            Utility.centralizeHeading("ADMIN MENU");
            showUserInfo();
            Utility.printUserChoices(choices);
            int userChoice = Utility.isInputInteger();
            switch (userChoice) {
                case -1:
                    continue;
                case 0:
                    return;
                case 1:
                    manageInventory();
            }
        }
    }

    @Override
    public void showUserInfo() {
        System.out.println("Username: " + username + " (ID #" + userID + ")");
        System.out.println("-".repeat(Utility.TOTAL_WIDTH));
    }

    private void manageInventory() {
        String[] choices = {"🔙 Go Back", "🔍 View Products", "➕ Add Products", "✏️ Update Products", "🗑️ Delete Products"};

        while (true) {
            Utility.centralizeHeading("MANAGE INVENTORY");
            Utility.printUserChoices(choices);
            int userChoice = Utility.isInputInteger();
            switch (userChoice) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    printProducts();
                }
                case 2 -> {
                    addProduct();
                }
                case 3 -> {
                    updateProduct();
                }
                case 4 -> {
                    deleteProduct();
                }
            }
        }
    }

    @Override
    protected void printProducts() {
        while (true) {
            Utility.centralizeHeading("📝📦 PRODUCTS CATALOG");
            Utility.printUserChoices(Utility.productChoices);
            int userChoice = Utility.isInputInteger();
            String categoryChoice = productManager.categoryChoices(userChoice);
            if (categoryChoice == null) {
                continue;
            }
            if (categoryChoice.isBlank()) {
                return;
            }
            Utility.centralizeHeading(categoryChoice);
            ProductLinkedList filteredProduct = productManager.getProductByCategory(categoryChoice);
            productManager.printProductByCategory(filteredProduct, true);
            Utility.stopper();
        }
    }

    @Override
    public void addProduct() {
        while (true) {
            Utility.centralizeHeading("➕📦 ADD PRODUCT");
            Utility.printUserChoices(Utility.productChoices);
            int userChoice = Utility.isInputInteger();
            if (userChoice == 0) {
                return;
            }
            productManager.addProducts(userChoice);
        }
    }

    @Override
    public void deleteProduct() {
        while (true) {
            Utility.centralizeHeading("🗑️📦 DELETE PRODUCT");
            Utility.printUserChoices(Utility.productChoices);
            int userChoice = Utility.isInputInteger("Enter Category");
            String chosenCategory = productManager.categoryChoices(userChoice);
            if (chosenCategory == null) {
                continue;
            }
            if (chosenCategory.isBlank()) {
                return;
            }
            productManager.deleteProducts(chosenCategory);
        }
    }

    @Override
    public void updateProduct() {
        while (true) {
            Utility.centralizeHeading("🔁📦 UPDATE PRODUCT");
            Utility.printUserChoices(Utility.productChoices);
            int userChoice = Utility.isInputInteger("Enter Category");
            String chosenCategory = productManager.categoryChoices(userChoice);
            if (chosenCategory == null) {
                continue;
            }
            if (chosenCategory.isBlank()) {
                return;
            }
            Utility.centralizeHeading(chosenCategory);
            ProductLinkedList filteredProduct = productManager.getProductByCategory(chosenCategory);
            productManager.printProductByCategory(filteredProduct, true);
            if (filteredProduct.isEmpty()) {
                break;
            }
            int updateProduct = Utility.isInputInteger("Enter product number to update [0 to go back]");
            if (updateProduct == 0) {
                break;
            }
            if (!productManager.isProductNumberValid(updateProduct, filteredProduct.size())) {
                continue;
            }
            Product updateIndex = filteredProduct.get(updateProduct - 1);
            productManager.updateProducts(updateIndex);
        }
    }
}