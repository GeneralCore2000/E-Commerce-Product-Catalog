package models.products;

import java.time.LocalDate;

public class Tablets extends Product {

    public Tablets(String productName, String productDescription, double productPrice, int productStock, LocalDate unavailableDate) {
        super(productName, productDescription, productPrice, ProductCategory.TABLETS, productStock, unavailableDate);
    }
    public Tablets(int productID, String productName, String productDescription, double productPrice, int productStock, LocalDate unavailableDate) {
        super(productID, productName, productDescription, productPrice, ProductCategory.TABLETS, productStock, unavailableDate);
    }
}
