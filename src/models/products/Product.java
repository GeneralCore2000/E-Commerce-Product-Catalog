package models.products;

import java.time.LocalDate;

public abstract class Product {

    private static int PRODUCT_ID = 5000;
    private int productID;
    private int productStock;
    private String productName;
    private String productDescription;
    private ProductCategory productCategory;
    private double productPrice;
    private LocalDate unavailableDate;

    public Product(String productName, String productDescription, double productPrice, ProductCategory productCategory, int productStock, LocalDate unavailableDate) {
        this.productName = productName;
        this.productDescription = (productDescription.length() > 80) ? productDescription.substring(0, 80) + "..." : productDescription;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.productStock = productStock;
        this.unavailableDate = unavailableDate;
        productID = PRODUCT_ID++;
    }

    public Product(int productID, String productName, String productDescription, double productPrice, ProductCategory productCategory, int productStock, LocalDate unavailableDate) {
        this(productName, productDescription, productPrice, productCategory, productStock, unavailableDate);
        this.productID = productID;
    }

    public String customerDisplay() {
        return productName + " - " + productDescription + "\n" + "\t₱ " + productPrice + "\n" + "\tStock: " + productStock + "x" + "\n\tAvailable until: " + unavailableDate;
    }

    public String adminDisplay() {
        return productName + " - " + productDescription + "\n" + "\t₱ " + productPrice + "\n" + "\tStock: " + productStock + "x - "+ stockFlag() + "\n" + "\tProduct ID: " + productID + "\n\tAvailable until: " + unavailableDate;
    }


    public int getProductID() {
        return productID;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public LocalDate getUnavailableDate() {
        return unavailableDate;
    }

    public void setUnavailableDate(LocalDate unavailableDate) {
        this.unavailableDate = unavailableDate;
    }

    public boolean isAvailable() {
        return LocalDate.now().isBefore(unavailableDate);
    }

    public String stockFlag(){
        if(productStock < 20){
            return "LOW ON STOCK";
        }
        return "";
    }

    @Override
    public String toString() {
        return productName + " - " + productDescription + "\n" + "\t₱ " + productPrice + "\n" + "\tStock: " + productStock + "x - "+ stockFlag() + "\n" + "\tProduct ID: " + productID + "\n\tAvailable until: " + unavailableDate;
    }
}
