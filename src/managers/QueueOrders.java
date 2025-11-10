package managers;

public class QueueOrders {
    Queue head;

    public Queue enqueue(int customerID, int productID, double productPrice, int quantity, double subtotal) {
        Queue queue = new Queue(customerID, productID, productPrice, quantity, subtotal);
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

    public Queue getHead() {
        return head;
    }

    public void dequeue() {
        if (head == null) {
            System.out.println("Order queue is empty.");
            return;
        }
        head = head.next;
    }

    public Queue peek() {
        return head;
    }

    public void display() {
        if (head == null) {
            System.out.println("No orders currently.");
            return;
        }
        Queue temp = head;
        int orderNumber = 1;
        while (temp != null) {
            System.out.println(orderNumber + ". " + temp.orderID + " | " + temp.customerID + " | " + temp.productID
                    + " | " + temp.productPrice + " | " + temp.quantity + " | " + temp.productPrice * temp.quantity);
            temp = temp.next;
            orderNumber++;
        }
    }

    public int size() {
        int size = 0;
        Queue temp = head;
        while (temp != null) {
            size++;
            temp = temp.next;
        }
        return size;
    }

    public static class Queue {
        public static int NEXT_ORDER_ID = 4000;
        public Queue next;
        public int orderID;

        public int customerID;
        public int productID;
        public double productPrice;
        public int quantity;
        public double subtotal;

        public Queue(int customerID, int productID, double productPrice, int quantity, double subtotal) {
            this.customerID = customerID;
            this.productID = productID;
            this.productPrice = productPrice;
            this.quantity = quantity;
            this.subtotal = subtotal;

            next = null;
            orderID = NEXT_ORDER_ID++;
        }

        @Override
        public String toString() {
            return orderID + " | " + customerID + " | " + productID
                    + " | " + productPrice + " | " + quantity + " | " + productPrice * quantity;
        }
    }

}
