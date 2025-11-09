package models.products;

public class Laptops extends Product {

    public Laptops(String productName, String productDescription, double productPrice, int productStock) {
        super(productName, productDescription, productPrice, ProductCategory.LAPTOPS, productStock);
    }
}
