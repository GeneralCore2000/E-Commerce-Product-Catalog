package data_structures;

import models.products.Product;

public class Sort {

    public static void bubbleSortByName(ProductLinkedList productLinkedList) {
        ProductLinkedList.Node head = productLinkedList.getHead();
        if (head == null || head.next == null) return;
        boolean swapped;
        do {
            swapped = false;
            ProductLinkedList.Node current = head;

            while (current.next != null) {
                Product currentProduct = current.product;
                Product nextProduct = current.next.product;

                if (current.product.getProductName().compareToIgnoreCase(current.next.product.getProductName()) > 0) {
                    current.product = nextProduct;
                    current.next.product = currentProduct;
                    swapped = true;
                }
                current = current.next;
            }

        } while (swapped);
    }

    public static void bubbleSortByID(ProductLinkedList productLinkedList) {
        ProductLinkedList.Node head = productLinkedList.getHead();
        if (head == null || head.next == null) return;
        boolean swapped;
        do {
            swapped = false;
            ProductLinkedList.Node current = head;

            while (current.next != null) {
                Product currentProduct = current.product;
                Product nextProduct = current.next.product;

                if (current.product.getProductID() > current.next.product.getProductID()) {
                    current.product = nextProduct;
                    current.next.product = currentProduct;
                    swapped = true;
                }
                current = current.next;
            }

        } while (swapped);
    }

    public static void bubbleSortByPrice(ProductLinkedList productLinkedList) {
        ProductLinkedList.Node head = productLinkedList.getHead();
        if (head == null || head.next == null) return;
        boolean swapped;
        do {
            swapped = false;
            ProductLinkedList.Node current = head;

            while (current.next != null) {
                Product currentProduct = current.product;
                Product nextProduct = current.next.product;

                if (current.product.getProductPrice() > current.next.product.getProductPrice()) {
                    current.product = nextProduct;
                    current.next.product = currentProduct;
                    swapped = true;
                }
                current = current.next;
            }

        } while (swapped);
    }
}
