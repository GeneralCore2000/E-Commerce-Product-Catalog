package data_structures;

import models.products.Product;

public class ProductLinkedList {

    private Node head;

    public void add(Product product) {
        Node newNode = new Node(product);
        if (head == null) {
            head = newNode;
            return;
        }
        Node temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = newNode;
    }

    public Node getHead() {
        return head;
    }

    public void remove(Product product) {
        if (head == null) {
            return;
        }
        if (head.product.equals(product)) {
            head = head.next;
            return;
        }

        Node current = head;
        while (current.next != null) {
            if (current.next.product.equals(product)) {
                current.next = current.next.next;
                return;
            }
            current = current.next;
        }
    }

    public void setHead(Node head) {
        this.head = head;
    }

    public int size() {
        int count = 0;
        Node current = head;
        while (current != null) {
            count++;
            current = current.next;
        }
        return count;
    }

    public Product get(int node) {
        if (node < 0) {
            return null;
        }
        int iterate = 0;
        Node current = head;
        while (iterate < node) {
            iterate++;
            current = current.next;
        }
        return (current != null) ? current.product : null;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public static class Node {
        public Product product;
        public Node next;

        public Node(Product product) {
            this.product = product;
            next = null;
        }
    }

}
