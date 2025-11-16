package users;

import managers.EditUserInfos;
import managers.FileManager;
import managers.ProductManager;
import products.Product;
import products.ProductCategory;
import utils.*;

import java.util.ArrayList;

public class Admin extends User implements AdminPrivilege {
    private final ProductManager productManager;

    public Admin(String username, String password, String address, ProductManager productManager, EditUserInfos editUserInfos) {
        super(username, password, address, editUserInfos);
        this.productManager = productManager;
    }

    @Override
    public void showMenu() {
        String[] choices = {"🔙 Log Out", "📦 Manage Inventory", "⌛ View Log History", "📝 Edit Info"};
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
                    break;
                case 2:
                    viewLogHistory();
                    break;
                case 3:
                    editInfo();
            }
        }
    }

    public void viewLogHistory() {
        ArrayList<ArrayList<String>> historyList = FileManager.readFile(FilePaths.LOG_HISTORY);
        for (ArrayList<String> row : historyList) {
            System.out.println(row);
        }
    }

    @Override
    public void showUserInfo() {
        System.out.println("Username: " + username + " (ID #" + userID + ")");
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
            ProductCategory categoryChoice = productManager.categoryChoices(userChoice);
            if (categoryChoice == null) {
                continue;
            }
            if (categoryChoice == ProductCategory.NULL) {
                return;
            }
            Utility.centralizeHeading(String.valueOf(categoryChoice));
            ArrayList<Product> filteredProduct = productManager.getProductByCategory(categoryChoice);
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
            if (productManager.addProducts(userChoice)) {
                LogHistory.add(userID, username, ActionLog.PRODUCT_ADD, RemarksLog.SUCCESSFUL);
                return;
            }
        }
    }

    @Override
    public void deleteProduct() {
        while (true) {
            Utility.centralizeHeading("🗑️📦 DELETE PRODUCT");
            Utility.printUserChoices(Utility.productChoices);
            int userChoice = Utility.isInputInteger("Enter Category");
            ProductCategory chosenCategory = productManager.categoryChoices(userChoice);
            if (chosenCategory == null) {
                continue;
            }
            if (chosenCategory == ProductCategory.NULL) {
                return;
            }
            if (productManager.deleteProducts(chosenCategory)) {
                LogHistory.add(userID, username, ActionLog.PRODUCT_DELETE, RemarksLog.SUCCESSFUL);
                return;
            }
        }
    }

    @Override
    public void updateProduct() {
        while (true) {
            Utility.centralizeHeading("🔁📦 UPDATE PRODUCT");
            Utility.printUserChoices(Utility.productChoices);
            int userChoice = Utility.isInputInteger("Enter Category");
            ProductCategory chosenCategory = productManager.categoryChoices(userChoice);
            if (chosenCategory == null) {
                continue;
            }
            if (chosenCategory == ProductCategory.NULL) {
                return;
            }
            Utility.centralizeHeading(String.valueOf(chosenCategory));
            ArrayList<Product> filteredProduct = productManager.getProductByCategory(chosenCategory);
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
            if (productManager.updateProducts(updateIndex)) {
                LogHistory.add(userID, username, ActionLog.PRODUCT_UPDATE, RemarksLog.SUCCESSFUL);
                return;
            }
        }
    }
}