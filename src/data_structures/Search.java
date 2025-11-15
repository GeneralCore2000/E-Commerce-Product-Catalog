package data_structures;

import utils.Utility;

public class Search {

    public static void linearSearch(String productName, ProductLinkedList productLists) {
        ProductLinkedList.Node current = productLists.getHead();
        int counter = 1;
        while (current != null) {
            if (current.product.getProductName().contains(productName)) {
                printFoundProduct(counter, current);
                counter++;
                System.out.println("-".repeat(Utility.TOTAL_WIDTH));
            }
            current = current.next;
        }
    }

    public static void linearSearch(int productID, ProductLinkedList productLists) {
        ProductLinkedList.Node current = productLists.getHead();
        int counter = 1;
        while (current != null) {
            if (current.product.getProductID() == (productID)) {
                printFoundProduct(counter, current);
                System.out.println("-".repeat(Utility.TOTAL_WIDTH));
                return;
            }
            current = current.next;
        }
    }

    private static void printFoundProduct(int counter, ProductLinkedList.Node current) {
        System.out.println("[" + counter + "]\nProduct name: " + current.product.getProductName());
        System.out.println("ID: " + current.product.getProductID());
        System.out.println("Price: " + current.product.getProductPrice());
        System.out.println("Stock : " + current.product.getProductStock());
    }
}
