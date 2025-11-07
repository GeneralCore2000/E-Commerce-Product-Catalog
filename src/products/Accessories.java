package products;

public class Accessories extends Product {

    public Accessories(String productName, String productDescription, double productPrice, int productStock) {
        super(productName, productDescription, productPrice, "Accessories", productStock);
    }
}
