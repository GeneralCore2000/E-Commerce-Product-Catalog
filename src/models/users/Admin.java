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

import java.util.Scanner;

public class Admin extends User implements AdminPrivilege {
    private final ProductManager productManager;
    private final QueueOrders queueOrders;
    private final OrderManager orderManager;
    private final Scanner in = new Scanner(System.in);

    public Admin(String username, String password, String address, ProductManager productManager, QueueOrders queueOrders, OrderManager orderManager) {
        super(username, password, address);
        this.productManager = productManager;
        this.queueOrders = queueOrders;
        this.orderManager = orderManager;
    }

    @Override
    public void showMenu() {
        productManager.setAdminUsername(username);
        productManager.setAdminUserID(userID);
        String[] choices = {"🔙 Log Out", "📦 Manage Inventory", "📃 See Orders"};
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
                    seeOrders();
            }
        }
    }

    private void seeOrders() {
        String[] choices = {"Go Back", "Fulfill Orders"};
        while (true) {
            Utility.centralizeHeading("ORDERS QUEUE");
            orderManager.seeOrders();
            System.out.println("━".repeat(Utility.TOTAL_WIDTH));
            Utility.printUserChoices(choices);
            int userChoice = Utility.isInputInteger();
            switch (userChoice) {
                case -1:
                    continue;
                case 0:
                    return;
                case 1:
                    fulfillOrders();
                    break;
                default:
                    System.err.println("Invalid input: Not in the choices.");
                    Utility.stopper();
            }
        }
    }

    private void fulfillOrders() {
        String[] choices = {"Go back", "Continue fulfill order", "Undo Last Fulfillment"};
        while (true) {
            Utility.centralizeHeading("FULFILL ORDERS");
            orderManager.seeOrders();
            Utility.centralizeHeading("Fulfill Current Order");
            orderManager.seeFrontOrder();
            System.out.println("━".repeat(Utility.TOTAL_WIDTH));
            Utility.printUserChoices(choices);
            int userChoice = Utility.isInputInteger();
            switch (userChoice) {
                case -1:
                    continue;
                case 0:
                    return;
                case 1:
                    orderManager.fulfillOrder();
                    break;
                case 2:
                    orderManager.undoFulfillOrder();
                    break;
            }
        }
    }

    @Override
    public void showUserInfo() {
        System.out.println("Username: " + username + " (ID #" + userID + ")");
        System.out.println("-".repeat(Utility.TOTAL_WIDTH));
    }

    private void manageInventory() {
        String[] choices = {"🔙 Go Back", "🔍 View All Products", "🔍 Search product", "➕ Add Products",
                "✏️ Update Products", "🗑️ Delete Products"};

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
                    searchProduct();
                }
                case 3 -> {
                    addProduct();
                }
                case 4 -> {
                    updateProduct();
                }
                case 5 -> {
                    deleteProduct();
                }
            }
        }
    }

    @Override
    protected void printProducts() {
        productManager.displayAllProduct(true);
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
            LogHistory.addLog(userID, username, ActionType.PRODUCT_ADD, TargetType.PRODUCT);
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
            Utility.centralizeHeading(String.valueOf(chosenCategory));
            ProductLinkedList filteredProduct = productManager.getProductByCategory(chosenCategory);
            productManager.printProductByCategory(filteredProduct, true);
            if (filteredProduct.isEmpty()) {
                Utility.stopper();
                break;
            }
            int deleteProduct = Utility.isInputInteger("Enter product number to delete [0 to go back]");
            if (deleteProduct == 0) {
                break;
            }
            if (!productManager.isProductNumberValid(deleteProduct, filteredProduct.size())) {
                continue;
            }
            Product deleteIndex = filteredProduct.get(deleteProduct - 1);
            System.out.println("Deleting: " + " " + deleteIndex);
            productManager.deleteProducts(deleteIndex, userID, username);
            return;
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
            productManager.updateProducts(updateIndex, userID, username);
        }
    }

    @Override
    public void searchProduct() {
        while (true) {
            Utility.centralizeHeading("SEARCH PRODUCT");
            Utility.printUserChoices("Go back", "Search by SKU (Binary)", "Search by name (Linear)");
            int userChoice = Utility.isInputInteger();
            switch (userChoice) {
                case -1:
                    continue;
                case 0:
                    return;
                case 1:
                    searchSKU();
                    break;
                case 2:
                    searchName();

            }
        }
    }

    private void searchName() {
        System.out.print("Enter product name >>: ");
        String findProductName = in.nextLine();
        System.out.println();
        productManager.findAllProduct(findProductName);
        Utility.stopper();
    }

    private void searchSKU() {
        int findProductSKU;
        do {
            findProductSKU = Utility.isInputInteger("Enter SKU");
            System.out.println();
        } while (findProductSKU == -1);
        productManager.findProductSKU(findProductSKU);
    }
}