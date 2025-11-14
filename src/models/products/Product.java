package models.products;

public abstract class Product {

    private static int PRODUCT_ID = 5000;
    private final int productID;
    private int productStock;
    private String productName;
    private String productDescription;
    private ProductCategory productCategory;
    private double productPrice;

    public Product(String productName, String productDescription, double productPrice, ProductCategory productCategory, int productStock) {
        this.productName = productName;
        this.productDescription = (productDescription.length() > 80) ? productDescription.substring(0, 80) + "..." : productDescription;
        this.productPrice = productPrice;
        this.productCategory = productCategory;
        this.productStock = productStock;
        productID = PRODUCT_ID++;
    }

    public String customerDisplay() {
        return productName + " - " + productDescription + "\n" + "\t₱ " + productPrice + "\n" + "\tStock: " + productStock + "x";
    }

    public String adminDisplay() {
        return productName + " - " + productDescription + "\n" + "\t₱ " + productPrice + "\n" + "\tStock: " + productStock + "x\n" + "\tProduct ID: " + productID;
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

    @Override
    public String toString() {
        return productName + " - " + productDescription + "\n" + "\t₱ " + productPrice + "\n" + "\tStock: " + productStock + "x\n" + "\tProduct ID: " + productID;
    }
}
