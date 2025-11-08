package products;

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

    private class Node {
        Product product;
        Node next;

        public Node(Product product) {
            this.product = product;
            next = null;
        }
    }

}
