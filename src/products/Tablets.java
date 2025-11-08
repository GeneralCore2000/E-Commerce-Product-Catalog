package products;

public class Tablets extends Product {

    public Tablets(String productName, String productDescription, double productPrice, int productStock) {
        super(productName, productDescription, productPrice, ProductCategory.TABLETS, productStock);
    }
}
