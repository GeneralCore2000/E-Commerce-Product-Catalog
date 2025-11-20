package managers;

import data.*;
import data_structures.ProductLinkedList;
import data_structures.Search;
import data_structures.Sort;
import models.products.*;
import utils.Utility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class ProductManager {
    private final ProductLinkedList productLists = new ProductLinkedList();
    private final Scanner in = new Scanner(System.in);

    private double productPrice;
    private int productStock;
    private LocalDate unavailableDate;
    private String productName, productDescription;

    private String adminUsername;
    private int adminUserID;

    /**
     * Constructs a ProductManager and loads products from the file, filtering out expired ones.
     * Active products are added to the list, and expired ones are archived.
     */
    public ProductManager() {
        ArrayList<ArrayList<String>> products = FileManager.readFile(FilePaths.PRODUCTS);
        ArrayList<ArrayList<String>> activeProducts = new ArrayList<>();

        int ID = 1, NAME = 2, PRICE = 3, STOCK = 4, DESCRIPTION = 5, UNAVAILABLE_DATE = 6;

        for (ArrayList<String> row : products) {
            LocalDate unavailableDate = LocalDate.parse(row.get(UNAVAILABLE_DATE));

            String category = row.getFirst();
            String name = row.get(NAME);
            String desc = row.get(DESCRIPTION);
            double price = Double.parseDouble(row.get(PRICE));
            int stock = Integer.parseInt(row.get(STOCK));
            int id = Integer.parseInt(row.get(ID));

            Product tempProduct = null;
            switch (category.toLowerCase()) {
                case "accessories" -> tempProduct = new Accessories(id, name, desc, price, stock, unavailableDate);
                case "laptops" -> tempProduct = new Laptops(id, name, desc, price, stock, unavailableDate);
                case "smartphones" -> tempProduct = new Smartphones(id, name, desc, price, stock, unavailableDate);
                case "tablets" -> tempProduct = new Tablets(id, name, desc, price, stock, unavailableDate);
            }
            productLists.add(tempProduct);
            if (!LocalDate.now().isBefore(unavailableDate)) {
                FileManager.appendToFile(FilePaths.ARCHIVE,
                        category + "," +
                                id + "," +
                                name + "," +
                                price + "," +
                                stock + "," +
                                desc + "," +
                                unavailableDate);
                productLists.remove(tempProduct);
                continue;
            }
            activeProducts.add(row);
        }
        FileManager.updateFile(FilePaths.PRODUCTS, FileManager.productHeader, activeProducts);
    }

    /**
     * Sets the admin username for logging purposes.
     *
     * @param adminUsername the admin's username
     */
    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }
    /**
     * Sets the admin user ID for logging purposes.
     *
     * @param adminUserID the admin's user ID
     */
    public void setAdminUserID(int adminUserID) {
        this.adminUserID = adminUserID;
    }

    /**
     * Returns a list of products filtered by the specified category.
     *
     * @param categoryChoice the category to filter by
     * @return a ProductLinkedList of products in the category
     */
    public ProductLinkedList getProductByCategory(ProductCategory categoryChoice) {
        ProductLinkedList filteredProducts = new ProductLinkedList();
        ProductLinkedList.Node current = productLists.getHead();

        while (current != null) {
            if (current.product.getProductCategory() == categoryChoice) {
                filteredProducts.add(current.product);
            }
            current = current.next;
        }
        return filteredProducts;
    }

    /**
     * Finds a product by name (case-insensitive).
     *
     * @param productName the name to search for
     * @return the Product if found, null otherwise
     */
    public Product findProduct(String productName) {
        ProductLinkedList.Node current = productLists.getHead();
        while (current != null) {
            if (current.product.getProductName().equalsIgnoreCase(productName)) {
                return current.product;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Finds a product by ID.
     *
     * @param productID the ID to search for
     * @return the Product if found, null otherwise
     */
    public Product findProduct(int productID) {
        ProductLinkedList.Node current = productLists.getHead();
        while (current != null) {
            if (current.product.getProductID() == productID) {
                return current.product;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Searches for all products containing the given name using linear search.
     *
     * @param productName the name to search for
     */
    public void findAllProduct(String productName) {
        Search.linearSearch(productName, productLists);
    }

    /**
     * Searches for a product by SKU using binary search after sorting by ID.
     *
     * @param productSKU the SKU to search for
     */
    public void findProductSKU(int productSKU) {
        Sort.bubbleSortByID(productLists);
        Search.binarySearch(productSKU, productLists);
    }

    /**
     * Prints products from the filtered list, with or without IDs.
     *
     * @param filteredProduct the list of products to print
     * @param showID true to show product IDs, false otherwise
     */
    public void printProductByCategory(ProductLinkedList filteredProduct, boolean showID) {
        if (productLists.isEmpty()) {
            System.out.println("There is nothing to see here. 🫣");
            return;
        }
        ProductLinkedList.Node current = filteredProduct.getHead();

        int productNumber = 0;

        while (current != null) {
            productNumber++;
            if (showID) {
                System.out.println(productNumber + ". " + current.product.adminDisplay());
            } else {
                System.out.println(productNumber + ". " + current.product.customerDisplay());
            }
            current = current.next;
        }
    }

    /**
     * Displays all products with pagination and sorting options.
     *
     * @param showID true to show product IDs, false otherwise
     */
    public void displayAllProduct(boolean showID) {
        ProductLinkedList.Node current = productLists.getHead();
        int pageNumber = 1;
        int productNumber = 0;

        if (productLists.isEmpty()) {
            System.out.println("There is nothing to see here. 🫣");
            return;
        }

        while (current != null) {
            productNumber++;
            if (showID) {
                System.out.println(productNumber + ". " + current.product.adminDisplay());
            } else {
                System.out.println(productNumber + ". " + current.product.customerDisplay());
            }
            current = current.next;
            if (productNumber % 5 == 0 && productNumber != productLists.size()) {
                System.out.println("\nPg. " + pageNumber + " of " + productLists.size() / 5);
                outerloop:
                while (true) {
                    System.out.println("-".repeat(Utility.TOTAL_WIDTH));
                    Utility.printUserChoices("🔙 Go Back", "⏭️ Next page", "\uD83D\uDD00 Sort by name",
                            "\uD83D\uDD00 Sort by ID", "\uD83D\uDD00 Sort by Price");
                    int userChoice = Utility.isInputInteger();
                    switch (userChoice) {
                        case -1:
                            continue;
                        case 0:
                            return;
                        case 1:
                            System.out.println();
                            pageNumber++;
                            break outerloop;
                        case 2:
                            Sort.bubbleSortByName(productLists);
                            current = productLists.getHead();
                            productNumber = 0;
                            pageNumber = 1;
                            break outerloop;
                        case 3:
                            Sort.bubbleSortByID(productLists);
                            current = productLists.getHead();
                            productNumber = 0;
                            pageNumber = 1;
                            break outerloop;
                        case 4:
                            Sort.bubbleSortByPrice(productLists);
                            current = productLists.getHead();
                            productNumber = 0;
                            pageNumber = 1;
                            break outerloop;
                    }
                }
            }
        }
        Utility.stopper();
    }

    /**
     * Prints products from the filtered list without IDs.
     * This is used with {@link models.users.Customer} print product menu as customer does not often need
     * technical information.
     * @param filteredProduct the list of products to print
     */
    public void printProductByCategory(ProductLinkedList filteredProduct) {
        printProductByCategory(filteredProduct, false);
    }

    /**
     * Adds a new product to the specified category after prompting for details.
     *
     * @param addToProductCategory the category number to add to
     */
    public void addProducts(int addToProductCategory) {
        if (!addProductQuestions()) {
            return;
        }
        Product product;
        String category;
        switch (addToProductCategory) {
            case 1 -> {
                product = new Accessories(productName, productDescription, productPrice, productStock, unavailableDate);
                category = "Accessories";
            }
            case 2 -> {
                product = new Laptops(productName, productDescription, productPrice, productStock, unavailableDate);
                category = "Laptops";
            }
            case 3 -> {
                product = new Smartphones(productName, productDescription, productPrice, productStock, unavailableDate);
                category = "Smartphones";
            }
            case 4 -> {
                product = new Tablets(productName, productDescription, productPrice, productStock, unavailableDate);
                category = "Tablets";
            }
            default -> {
                System.out.println("Invalid Input: Not in the choices.");
                return;
            }
        }

        FileManager.appendToFile(FilePaths.PRODUCTS, category + Utility.DIVIDER
                + product.getProductID() + Utility.DIVIDER
                + "\"" + productName + "\"" + Utility.DIVIDER
                + productPrice + Utility.DIVIDER
                + productStock + Utility.DIVIDER
                + "\"" + productDescription + "\"" + Utility.DIVIDER
                + unavailableDate);
        productLists.add(product);
    }

    /**
     * Updates the details of the specified product.
     *
     * @param updateIndex the product to update
     * @param userID the ID of the user performing the update
     * @param username the username of the user performing the update
     */
    public void updateProducts(Product updateIndex, int userID, String username) {
        while (true) {
            String[] userChoicesList = {"Go Back", "Product Name", "Product Description", "Product Price", "Product Stock"};
            Utility.printUserChoices(userChoicesList);
            int userChoice = Utility.isInputInteger();
            switch (userChoice) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    updateProductName(updateIndex);
                }
                case 2 -> {
                    updateProductDescription(updateIndex);
                }
                case 3 -> {
                    updateProductPrice(updateIndex);
                }
                case 4 -> {
                    updateProductStock(updateIndex);
                }
            }
            FileManager.updateFile(FilePaths.PRODUCTS, FileManager.productHeader, convertProductListTo2D());

        }
    }

    private void updateProductName(Product updateIndex) {
        System.out.print("Enter new product name for [" + updateIndex.getProductName() + "] >>: ");
        String productName = in.nextLine();
        LogHistory.addLog(adminUserID, adminUsername, ActionType.PRODUCT_UPDATE, TargetType.PRODUCT, updateIndex.getProductID() + "", updateIndex.getProductName(), productName);
        updateIndex.setProductName(productName);
    }

    private void updateProductDescription(Product updateIndex) {
        System.out.print("Enter new product description for [" + updateIndex.getProductDescription() + "] >>: ");
        String productDescription = in.nextLine();
        LogHistory.addLog(adminUserID, adminUsername, ActionType.PRODUCT_UPDATE, TargetType.PRODUCT, updateIndex.getProductID() + "", updateIndex.getProductDescription(), productDescription);
        updateIndex.setProductDescription(productDescription);
    }

    private void updateProductPrice(Product updateIndex) {
        while (true) {
            int newPrice = Utility.isInputInteger("Enter new product price for [" + updateIndex.getProductPrice() + "]");
            if (newPrice < 0) {
                System.out.println("\n" + "~".repeat(42));
                System.out.println("Invalid input: Cannot be negative stock.");
                System.out.println("~".repeat(42));
                continue;
            }
            LogHistory.addLog(adminUserID, adminUsername, ActionType.PRODUCT_UPDATE, TargetType.PRODUCT, updateIndex.getProductID() + "", updateIndex.getProductPrice() + "", newPrice + "");
            updateIndex.setProductPrice(newPrice);
            break;
        }
    }

    private void updateProductStock(Product updateIndex) {
        while (true) {
            int newStock = Utility.isInputInteger("Enter new product stock for [" + updateIndex.getProductStock() + "]");
            if (newStock < 0) {
                System.out.println("\n" + "~".repeat(42));
                System.out.println("Invalid input: Cannot be negative stock.");
                System.out.println("~".repeat(42));
                continue;
            }
            LogHistory.addLog(adminUserID, adminUsername, ActionType.PRODUCT_UPDATE, TargetType.PRODUCT, updateIndex.getProductID() + "", updateIndex.getProductStock() + "", newStock + "");
            updateIndex.setProductStock(newStock);
            break;
        }
    }

    /**
     * Converts the product list to a 2D ArrayList for file updates.
     * This is use with {@link FileManager#updateFile(String, String, ArrayList)}
     * @return a 2D ArrayList representation of the products
     */
    public ArrayList<ArrayList<String>> convertProductListTo2D() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        ProductLinkedList.Node current = productLists.getHead();
        while (current != null) {
            ArrayList<String> row = new ArrayList<>();
            row.add(current.product.getProductCategory() + "");
            row.add(current.product.getProductID() + "");
            row.add(current.product.getProductName());
            row.add(current.product.getProductPrice() + "");
            row.add(current.product.getProductStock() + "");
            row.add(current.product.getProductDescription());
            row.add(current.product.getUnavailableDate() + "");
            data.add(row);
            current = current.next;
        }
        return data;
    }

    /**
     * Deletes the specified product and archives it.
     *
     * @param deleteIndex the product to delete
     * @param userID the ID of the user performing the deletion
     * @param username the username of the user performing the deletion
     */
    public void deleteProducts(Product deleteIndex, int userID, String username) {
        LogHistory.addLog(userID, username, ActionType.PRODUCT_DELETE, TargetType.PRODUCT, deleteIndex.getProductID() + "", null, null);
        FileManager.appendToFile(FilePaths.ARCHIVE,
                deleteIndex.getProductCategory() + ","
                        + deleteIndex.getProductID() + ","
                        + deleteIndex.getProductName() + ","
                        + deleteIndex.getProductPrice() + ","
                        + deleteIndex.getProductDescription() + ","
                        + deleteIndex.getUnavailableDate());
        productLists.remove((deleteIndex));
        FileManager.updateFile(FilePaths.PRODUCTS, FileManager.productHeader, convertProductListTo2D());
        Utility.stopper();
    }

    /**
     * Prompts the user to continue adding a product if it already exists.
     *
     * @param product the existing product
     * @return {@code true} to continue, {@code false} to cancel
     */
    private boolean continueAddProduct(Product product) {
        while (true) {
            Utility.centralizeHeading("Product existing already");
            System.out.println("Product [" + product.getProductName() + "] is already existing");
            System.out.println("Product ID: " + product.getProductID());
            System.out.println("Product Category: " + product.getProductCategory());
            System.out.println("\nWould you like to update the product stock instead?");
            System.out.println("\n\t[0] Cancel add");
            System.out.println("\t[1] Continue [proceed to create new product ID]");
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

    /**
     * Prompts for product details and validates input.
     *
     * @return {@code true} if input is valid, {@code false} otherwise
     */
    private boolean addProductQuestions() {
        System.out.print("Enter product name >>: ");
        productName = in.nextLine();
        Product product = findProduct(productName);
        if (product != null && !continueAddProduct((product))) {
            return false;
        }
        System.out.print("Enter product description >>: ");
        productDescription = in.nextLine();

        while (true) {
            System.out.print("Enter product price >>: ");
            try {
                productPrice = Double.parseDouble(in.nextLine());
                if (productPrice <= 0) {
                    System.out.println("\nInvalid input: Product price cannot be set to negative or ₱ 0." + "\nPress any key to continue...");
                    in.nextLine();
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("\n" + "~".repeat(42));
                System.out.print("Invalid input: Non-integer is not allowed.\nPress any key to continue...");
                System.out.println("\n" + "~".repeat(42));
                in.nextLine();
            }
        }
        while (true) {
            System.out.print("How many of the product will be added? >>: ");
            try {
                productStock = Integer.parseInt(in.nextLine());
                productStock = (productStock < 0) ? 1 : productStock;
                break;
            } catch (NumberFormatException ae) {
                System.out.println("\n" + "~".repeat(42));
                System.out.print("Invalid input: Non-integer is not allowed.\nPress any key to continue...");
                System.out.println("\n" + "~".repeat(42));
                in.nextLine();
            }
        }
        while (true) {
            unavailableDate = null;
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            System.out.print("Enter deactivation date (yyyy-MM-dd) >>: ");
            String date = in.nextLine();
            try {
                unavailableDate = LocalDate.parse(date, dtf);
                break;
            } catch (Exception e) {
                System.out.println("\n" + "~".repeat(42));
                System.out.println("Invalid input: Date format is not correct.\nPress any key to continue...");
                System.out.println("\n" + "~".repeat(42));
                in.nextLine();
            }

        }
        return true;
    }

    /**
     * Validates the input if it is within the range of available products.
     *
     * @param productIndex the chosen index of the product
     * @param maxIndex the number of products in a specific category
     * @return {@code true} if the input is valid, otherwise {@code false}.
     */
    public boolean isProductNumberValid(int productIndex, int maxIndex) {
        if (productIndex < 0) {
            return false;
        } else if (productIndex == 0) {
            return false;
        } else if (productIndex > maxIndex) {
            System.out.println("\nInvalid input: Not existing product for #" + productIndex);
            Utility.stopper();
            return false;
        }
        return true;
    }

    public ProductCategory categoryChoices(int userChoice) {
        return switch (userChoice) {
            case 0 -> ProductCategory.NULL;
            case 1 -> ProductCategory.ACCESSORIES;
            case 2 -> ProductCategory.LAPTOPS;
            case 3 -> ProductCategory.SMARTPHONES;
            case 4 -> ProductCategory.TABLETS;
            default -> null;
        };
    }

    public ProductLinkedList getProductLists() {
        return productLists;
    }
}
