package models.products;

import java.time.LocalDate;

public class Laptops extends Product {

    public Laptops(String productName, String productDescription, double productPrice, int productStock, LocalDate unavailableDate) {
        super(productName, productDescription, productPrice, ProductCategory.LAPTOPS, productStock, unavailableDate);
    }
    public Laptops(int productID, String productName, String productDescription, double productPrice, int productStock, LocalDate unavailableDate) {
        super(productID, productName, productDescription, productPrice, ProductCategory.LAPTOPS, productStock, unavailableDate);
    }
}
