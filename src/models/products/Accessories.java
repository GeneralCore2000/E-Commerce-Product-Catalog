package models.products;

import java.time.LocalDate;

public class Accessories extends Product {

    public Accessories(String productName, String productDescription, double productPrice, int productStock, LocalDate unavailableDate) {
        super(productName, productDescription, productPrice, ProductCategory.ACCESSORIES, productStock, unavailableDate);
    }

    public Accessories(int productID, String productName, String productDescription, double productPrice, int productStock, LocalDate unavailableDate) {
        super(productID, productName, productDescription, productPrice, ProductCategory.ACCESSORIES, productStock, unavailableDate);
    }
}
