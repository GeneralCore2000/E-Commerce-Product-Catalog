package managers;

import products.*;
import utils.FilePaths;
import utils.Utility;

import java.util.ArrayList;
import java.util.Scanner;

public class ProductManager {
    private final ArrayList<Product> productLists = new ArrayList<>();
    private final Scanner in = new Scanner(System.in);
    private double productPrice;
    private int productStock;
    private String productName, productDescription;

    /**
     * Construct a {@code ProductManager} and initializes the {@code productLists} from the file {@code products.txt}
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
     * @param categoryChoice the name of the category to filter the products
     * @return an {@code ArrayList} containing all the products in {@code productLists} that matches the
     * {@code categoryChoice}; an empty list if none.
     */
    public ArrayList<Product> getProductByCategory(String categoryChoice) {
        ArrayList<Product> filteredProduct = new ArrayList<>();
        for (Product product : productLists) {
            if (product.getProductCategory().equals(categoryChoice)) {
                filteredProduct.add(product);
            }
        }
        return filteredProduct;
    }

    /**
     * Searches for a specific product inside {@code productLists} by its name ignoring case sensitivity.
     *
     * @param productName the name of the product to find
     * @return {@code Product} object if is existing; {@code null} if non-existing
     */
    private Product findProduct(String productName) {
        for (Product product : productLists) {
            if (product.getProductName().equalsIgnoreCase(productName)) {
                return product;
            }
        }
        return null;
    }

    /**
     * Prints out the products filtered by the chosen category. This method is used in {@link users.Admin} to include
     * product IDs at print.
     * <p>
     * If {@code showID} is {@code true}, each products is displayed using {@link Product#adminDisplay()}, otherwise
     * {@link Product#customerDisplay()}.
     * </p>
     *
     * @param filteredProduct the list of products to display specified by category
     * @param showID          {@code true} to include product IDs, otherwise {@code false} for customer display
     */
    public void printProductByCategory(ArrayList<Product> filteredProduct, boolean showID) {
        if (filteredProduct.isEmpty()) {
            System.out.println("There is nothing to see here. 🫣");
            return;
        }
        int productNumber = 0;
        for (Product product : filteredProduct) {
            productNumber++;
            if (showID) {
                System.out.println(productNumber + ". " + product.adminDisplay());
            } else {
                System.out.println(productNumber + ". " + product.customerDisplay());
            }
        }
    }

    /**
     * This is an overload method of {@link #printProductByCategory(ArrayList, boolean)} and is use for Customer menu
     * hiding the product IDs.
     *
     * <p>
     * This is a convenience method that calls {@link #printProductByCategory(ArrayList, boolean)} with {@code showID}
     * is set to {@code false}.
     * </p>
     *
     * @param filteredProduct arraylist of products of a specified category to be printed
     */
    public void printProductByCategory(ArrayList<Product> filteredProduct) {
        printProductByCategory(filteredProduct, false);
    }

    /**
     * Add product to a specified category,
     * <p>
     * Calls {@link #addProductQuestions()} internally to gather the product information.
     * </p>
     * <p>
     * If the adding process is successful, the new Product will be added to the {@code productLists} as well as to
     * the {@code products.txt}
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

        FileManager.appendToFile(FilePaths.PRODUCTS, category + "##" + product.getProductID() + "##" + productName + "##" + productPrice + "##" + productStock + "##" + productDescription);
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
            String[] userChoicesList = {"Go Back", "Product Name", "Product Description", "Product Price", "Product Category", "Product Stock"};
            Utility.printUserChoices(userChoicesList);
            int userChoice = Utility.isInputInteger();
            switch (userChoice) {
                case 0 -> {
                    return;
                }
                case 1 -> {
                    System.out.print("Enter new product name for [" + updateIndex.getProductName() + "] >>: ");
                    updateIndex.setProductName(in.nextLine());
                }
                case 2 -> {
                    System.out.print("Enter new product description for [" + updateIndex.getProductDescription() + "] >>: ");
                    updateIndex.setProductDescription(in.nextLine());
                }
                case 3 -> {
//TODO: Add validation if the input is integer
                    System.out.print("Enter new product price for [" + updateIndex.getProductPrice() + "] >>: ");
                    updateIndex.setProductPrice(Double.parseDouble(in.nextLine()));
                }
                case 4 -> {
                    System.out.print("Enter new product category for [" + updateIndex.getProductCategory() + "] >>: ");
                    updateIndex.setProductCategory(in.nextLine());
                }
                case 5 -> {
//TODO: Add validation if the input is integer
                    System.out.print("Enter new product stock for [" + updateIndex.getProductStock() + "] >>: ");
                    updateIndex.setProductStock(Integer.parseInt(in.nextLine()));
                }
            }
        }
    }

    /**
     * Delete the product from the specified category.
     *
     * @param chosenCategory the category of the products to be display using {@link #printProductByCategory(ArrayList, boolean)}
     *                       as well as to be deleted.
     *
     */
    public void deleteProducts(String chosenCategory) {
        while (true) {
            Utility.centralizeHeading(chosenCategory);
            ArrayList<Product> filteredProduct = getProductByCategory(chosenCategory);
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
                    System.out.println("\nInvalid input: Product price cannot be set to negative or ₱ 0." +
                            "\nPress any key to continue...");
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

    public String categoryChoices(int userChoice) {
        return switch (userChoice) {
            case 0 -> "";
            case 1 -> "Accessories";
            case 2 -> "Laptops";
            case 3 -> "Smartphones";
            case 4 -> "Tablets";
            default -> null;
        };
    }
}
