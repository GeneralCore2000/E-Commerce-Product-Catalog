package managers;

import models.products.Product;
import models.users.Customer;

public class QueueOrders {
    Queue head;

    public Queue enqueue(Product product, Customer customer, int quantity) {
        Queue queue = new Queue(product, customer, quantity);
        if (head == null) {
            head = queue;
        } else {
            Queue temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = queue;
        }
        return queue;
    }

    public void dequeue() {
        if (head == null) {
            System.out.println("Order queue is empty.");
            return;
        }
        head = head.next;
    }

    public void display() {
        if (head == null) {
            System.out.println("No orders currently.");
            return;
        }
        Queue temp = head;
        int orderNumber = 1;
        while (temp != null) {
            System.out.println(orderNumber + ". " + temp.orderID
                    + " | " + temp.customer.getUserID()
                    + " | " + temp.product.getProductID()
                    + " | " + temp.product.getProductPrice()
                    + temp.quantity
                    + " | " + temp.product.getProductPrice() * temp.quantity);
            temp = temp.next;
        }
    }

    public static class Queue {
        public static int NEXT_ORDER_ID = 4000;
        public int orderID;
        public Product product;
        public Queue next;
        public int quantity;
        public Customer customer;

        public Queue(Product product, Customer customer, int quantity) {
            this.product = product;
            this.quantity = quantity;
            this.customer = customer;
            next = null;
            orderID = NEXT_ORDER_ID++;
        }
    }

}
