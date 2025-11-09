package managers;

import products.Product;
import users.Customer;

public class QueueOrders {
    Queue head;

    public void enqueue(Product product, Customer customer, int quantity) {
        Queue queue = new Queue(product, customer, quantity);
        if (head == null) {
            head = queue;
            return;
        }
        Queue temp = head;
        while (temp.next != null) {
            temp = temp.next;
        }
        temp.next = queue;
    }

    public void dequeue() {
        if (head == null) {
            System.out.println("Order queue is empty.");
            return;
        }
        head = head.next;
    }

    public static class Queue {
        public Product product;
        public Queue next;
        public int quantity;
        public Customer customer;

        public Queue(Product product, Customer customer, int quantity) {
            this.product = product;
            this.quantity = quantity;
            this.customer = customer;
            next = null;
        }
    }

}
