package models.products;

import java.time.LocalDate;

public class Smartphones extends Product {

    public Smartphones(String productName, String productDescription, double productPrice, int productStock, LocalDate unavailableDate) {
        super(productName, productDescription, productPrice, ProductCategory.SMARTPHONES, productStock, unavailableDate);
    }
    public Smartphones(int productID, String productName, String productDescription, double productPrice, int productStock, LocalDate unavailableDate) {
        super(productID, productName, productDescription, productPrice, ProductCategory.SMARTPHONES, productStock, unavailableDate);
    }
}
