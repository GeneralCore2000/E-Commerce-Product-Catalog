package data_structures;

import models.products.Product;
import utils.Utility;

public class Search {

    public static void linearSearch(String productName, ProductLinkedList productLists) {
        ProductLinkedList.Node current = productLists.getHead();
        int counter = 1;
        while (current != null) {
            if (current.product.getProductName().toLowerCase().contains(productName.toLowerCase())) {
                printFoundProduct(counter, current.product);
                counter++;
                System.out.println("-".repeat(Utility.TOTAL_WIDTH));
            }
            current = current.next;
        }
    }

    public static void binarySearch(int productID, ProductLinkedList productLinkedList) {
        int left = 0;
        int right = productLinkedList.size() - 1;
        while (left <= right) {
            int middle = left + (right - left) / 2;
            Product middleProduct = productLinkedList.get(middle);
            int middleProductID = middleProduct.getProductID();

            if (middleProductID == productID) {
                printFoundProduct(1, middleProduct);
                return;
            }
            if (middleProductID < productID) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }
    }

    private static void printFoundProduct(int counter, Product current) {
        System.out.println("[" + counter + "]\nProduct name: " + current.getProductName());
        System.out.println("ID: " + current.getProductID());
        System.out.println("Price: " + current.getProductPrice());
        System.out.println("Stock : " + current.getProductStock());
    }
}
