package managers;

import data.FileManager;
import data.FilePaths;
import data_structures.ProductLinkedList;
import data_structures.Search;
import data_structures.Sort;
import models.products.*;
import utils.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ProductManager {
    private final ProductLinkedList productLists = new ProductLinkedList();
    private final Scanner in = new Scanner(System.in);
    private double productPrice;
    private int productStock;
    private String productName, productDescription;

    /**
     * Construct a {@code ProductManager} and initializes the {@code productLists} from the file {@code models.products.txt}
     * path defined in {@link FilePaths#PRODUCTS}
     * <p>
     * Each line inside the file represents a product in format: <br>
     * {@code Category ## Product ID ## Name ## Price ## Stock ## Description}
     * </p>
     *
     * @see FileManager#readFile(String)
     */
    public ProductManager() {
        ArrayList<ArrayList<String>> products = FileManager.readFile(FilePaths.PRODUCTS);
        int NAME = 2, PRICE = 3, STOCK = 4, DESCRIPTION = 5;

        for (ArrayList<String> row : products) {
            String category = row.getFirst();
            String name = row.get(NAME);
            String desc = row.get(DESCRIPTION);
            double price = Double.parseDouble(row.get(PRICE));
            int stock = Integer.parseInt(row.get(STOCK));

            switch (category.toLowerCase()) {
                case "accessories" -> productLists.add(new Accessories(name, desc, price, stock));
                case "laptops" -> productLists.add(new Laptops(name, desc, price, stock));
                case "smartphones" -> productLists.add(new Smartphones(name, desc, price, stock));
                case "tablets" -> productLists.add(new Tablets(name, desc, price, stock));
            }
        }
    }

    /**
     * Return a list of product object in an arraylist that belongs to the specified group.
     *
     * @param categoryChoice the name of the category to filter the models.products
     * @return an {@code ArrayList} containing all the models.products in {@code productLists} that matches the
     * {@code categoryChoice}; an empty list if none.
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
     * Searches for a specific product inside {@code productLists} by its name ignoring case sensitivity.
     *
     * @param productName the name of the product to find
     * @return {@code Product} object if is existing; {@code null} if non-existing
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

    public void findAllProduct(String productName) {
        Search.linearSearch(productName, productLists);
    }

    public void findProductSKU(int productSKU) {
        sortByID();
        Search.binarySearch(productSKU, productLists);
    }

    public void sortByName() {
        Sort.bubbleSortByName(productLists);
    }

    public void sortByID() {
        Sort.bubbleSortByID(productLists);
    }

    public void sortByPrice() {
        Sort.bubbleSortByPrice(productLists);
    }

    /**
     * Prints out the models.products filtered by the chosen category. This method is used in {@link models.users.Admin} to include
     * product IDs at print.
     * <p>
     * If {@code showID} is {@code true}, each models.products is displayed using {@link Product#adminDisplay()}, otherwise
     * {@link Product#customerDisplay()}.
     * </p>
     *
     * @param filteredProduct the list of models.products to display specified by category
     * @param showID          {@code true} to include product IDs, otherwise {@code false} for customer display
     * @see #printProductByCategory(ProductLinkedList)
     */
    public void printProductByCategory(ProductLinkedList filteredProduct, boolean showID) {
        ProductLinkedList.Node current = filteredProduct.getHead();
        if (productLists.isEmpty()) {
            System.out.println("There is nothing to see here. 🫣");
            return;

        }

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
                            sortByName();
                            current = productLists.getHead();
                            productNumber = 0;
                            pageNumber = 1;
                            break outerloop;
                        case 3:
                            sortByID();
                            current = productLists.getHead();
                            productNumber = 0;
                            pageNumber = 1;
                            break outerloop;
                        case 4:
                            sortByPrice();
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
     * This is an overload method of {@link #printProductByCategory(ProductLinkedList, boolean)} and is use for Customer menu
     * hiding the product IDs.
     *
     * <p>
     * This is a convenience method that calls {@link #printProductByCategory(ProductLinkedList)} with {@code showID}
     * is set to {@code false}.
     * </p>
     *
     * @param filteredProduct arraylist of models.products of a specified category to be printed
     */
    public void printProductByCategory(ProductLinkedList filteredProduct) {
        printProductByCategory(filteredProduct, false);
    }

    /**
     * Add product to a specified category,
     * <p>
     * Calls {@link #addProductQuestions()} internally to gather the product information.
     * </p>
     * <p>
     * If the adding process is successful, the new Product will be added to the {@code productLists} as well as to
     * the {@code models.products.txt}
     * </p>
     *
     * @param addToProductCategory represents the product category
     */
    public void addProducts(int addToProductCategory) {
        if (!addProductQuestions()) {
            return;
        }
        Product product;
        String category;
        switch (addToProductCategory) {
            case 1 -> {
                product = new Accessories(productName, productDescription, productPrice, productStock);
                category = "Accessories";
            }
            case 2 -> {
                product = new Laptops(productName, productDescription, productPrice, productStock);
                category = "Laptops";
            }
            case 3 -> {
                product = new Smartphones(productName, productDescription, productPrice, productStock);
                category = "Smartphones";
            }
            case 4 -> {
                product = new Tablets(productName, productDescription, productPrice, productStock);
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
                + "\"" + productDescription + "\"");
        productLists.add(product);
    }

    /**
     * Allows the user to update the details of a given product
     * <p>
     * The user can choose to update the Product's name, description, price, category, or stock.
     * </p>
     *
     * @param updateIndex the {@link Product} object to be updated
     */
    public void updateProducts(Product updateIndex) {
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
            FileManager.updateFile(FilePaths.PRODUCTS, FileManager.productHeader, convertProductTo2DList());
        }
    }

    private void updateProductName(Product updateIndex) {
        System.out.print("Enter new product name for [" + updateIndex.getProductName() + "] >>: ");
        updateIndex.setProductName(in.nextLine());
    }

    private void updateProductDescription(Product updateIndex) {
        System.out.print("Enter new product description for [" + updateIndex.getProductDescription() + "] >>: ");
        updateIndex.setProductDescription(in.nextLine());
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
            updateIndex.setProductStock(newStock);
            break;
        }
    }

    /**
     * This method converts {@code productLists} into 2D ArrayList.
     * <p>
     * This is use in combination of {@link FileManager#updateFile(String, String, ArrayList)} as it has an param of 2D arraylist.
     * </p>
     *
     * @return 2D arraylist version of productLists
     */
    private ArrayList<ArrayList<String>> convertProductTo2DList() {
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
            data.add(row);
            current = current.next;
        }
        return data;
    }

    /**
     * Delete the product from the specified category.
     *
     * @param chosenCategory the category of the models.products to be display using {@link #printProductByCategory(ProductLinkedList, boolean)}
     *                       as well as to be deleted.
     *
     */
    public void deleteProducts(ProductCategory chosenCategory) {
        while (true) {
            Utility.centralizeHeading(String.valueOf(chosenCategory));
            ProductLinkedList filteredProduct = getProductByCategory(chosenCategory);
            printProductByCategory(filteredProduct, true);
            if (filteredProduct.isEmpty()) {
                Utility.stopper();
                break;
            }
            int deleteProduct = Utility.isInputInteger("Enter product number to delete [0 to go back]");
            if (deleteProduct == 0) {
                break;
            }
            if (!isProductNumberValid(deleteProduct, filteredProduct.size())) {
                continue;
            }
            Product removeIndex = filteredProduct.get(deleteProduct - 1);
            System.out.println("Deleting: " + " " + removeIndex);
            productLists.remove((removeIndex));
            FileManager.updateFile(FilePaths.PRODUCTS, FileManager.productHeader, convertProductTo2DList());
            Utility.stopper();
        }
    }

    /**
     * This method is used to ask whether the user would like to continue to add the product if it is existing, or stop.
     *
     * @param product the existing product that conflicts with the new product to be created.
     * @return {@code true} to continue adding the product; {@code false} to discontinue
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
     * Queries the user for the information of the product to be added and performs input validation.
     * <p>
     * If product name entered already exist {@link #continueAddProduct(Product)} is called to ask the user whether
     * to continue. Will return {@code false} if the user chooses not to.
     * </p>
     * <p>
     * The price must be positive number and cannot be set to {@code 0}.
     * If invalid, the user is prompted to enter a valid price.
     * </p>
     *
     * @return {@code true} to continue the adding process;
     * {@code false} to discontinue the adding process
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
        return true;
    }

    /**
     * Checks whether an inputted product index is within the allowed range.
     * <p>
     * A product index is considered invalid if it is less than 1, or exceed the specified maximum index.
     * If it exceeds the maximum index, a warning is prompted and {@link Utility#stopper()} is called to pause the
     * program.
     * </p>
     *
     * @param productIndex the product number to validate
     * @param maxIndex     the size of an arraylist
     * @return {@code true} if the product index is valid; {@code false} if invalid
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

    public ArrayList<ArrayList<String>> convertProductListTo2D() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        data.add(new ArrayList<>(Arrays.asList("CATEGORY", "ID", "NAME", "PRICE", "STOCK", "DESCRIPTION")));
        ProductLinkedList.Node current = productLists.getHead();
        while (current != null) {
            ArrayList<String> row = new ArrayList<>();
            row.add(current.product.getProductCategory() + "");
            row.add(current.product.getProductID() + "");
            row.add(current.product.getProductName());
            row.add(current.product.getProductPrice() + "");
            row.add(current.product.getProductStock() + "");
            row.add(current.product.getProductDescription());
            data.add(row);
            current = current.next;
        }
        return data;
    }
}
