package products;

public class Smartphones extends Product {

    public Smartphones(String productName, String productDescription, double productPrice, int productStock) {
        super(productName, productDescription, productPrice, ProductCategory.SMARTPHONES, productStock);
    }
}
