package managers;

import products.Product;

public class QueueOrders {
    Queue head;

    public void enqueue(Product product) {
        Queue queue = new Queue(product);
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

    public class Queue {
        public Product product;
        public Queue next;

        public Queue(Product product) {
            this.product = product;
            next = null;
        }
    }

}
