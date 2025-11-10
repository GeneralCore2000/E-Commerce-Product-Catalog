package managers;

import data.FileManager;
import data.FilePaths;
import utils.Utility;

import java.util.ArrayList;
import java.util.Arrays;

public class OrderManager {
    private final QueueOrders queueOrders = new QueueOrders();
    private ProductManager productManager;

    public OrderManager() {
        ArrayList<ArrayList<String>> pendingOrders = FileManager.readFile(FilePaths.PENDING_ORDERS);
        for (ArrayList<String> row : pendingOrders) {
            int customerID = Integer.parseInt(row.get(1));
            int productID = Integer.parseInt(row.get(2));
            double productPrice = Double.parseDouble(row.get(3));
            int quantity = Integer.parseInt(row.get(4));
            double subtotal = Double.parseDouble(row.get(5));

            queueOrders.enqueue(customerID, productID, productPrice, quantity, subtotal);
        }
    }

    public void addOrder(int customerID, int productID, double productPrice, int quantity, double subtotal) {
        QueueOrders.Queue queue = queueOrders.enqueue(customerID, productID, productPrice, quantity, subtotal);
        saveToFile(queue, quantity);
    }

    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    public void seeOrders() {
        queueOrders.display();
    }

    public void fulfillOrder() {
        QueueOrders.Queue currentQueueOrder = queueOrders.getOrder();
        if (currentQueueOrder == null) {
            System.out.println("No orders to fulfill.");
            return;
        }
        boolean enoughStock = validateStock(currentQueueOrder);
        int productStock = productManager.findProduct(currentQueueOrder.productID).getProductStock();
        if (!enoughStock) {
            System.out.println("Uh oh! Unable to fulfill order: Not enough stock for quantity [" + currentQueueOrder.quantity + "]");
            System.out.println("Product ID: " + currentQueueOrder.productID + " only has [" + productStock + "]");
            Utility.stopper();
            return;
        }
        queueOrders.dequeue();
        FileManager.updateFile(FilePaths.PENDING_ORDERS, convertQueueOrderTo2D());
    }

    private boolean validateStock(QueueOrders.Queue current) {
        ArrayList<ArrayList<String>> products = FileManager.readFile(FilePaths.PENDING_ORDERS);
        for (ArrayList<String> row : products) {
            if (Integer.parseInt(row.get(2)) == current.productID) {
                int stock = productManager.findProduct(current.productID).getProductStock();
                return stock >= current.quantity;
            }
        }
        return false;
    }

    public void seeFrontOrder() {
        System.out.println(queueOrders.peek());
    }

    private ArrayList<ArrayList<String>> convertQueueOrderTo2D() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        data.add(new ArrayList<>(Arrays.asList("Order ID", "Customer ID", "Product ID", "Product Price", "Quantity", "Subtotal")));
        QueueOrders.Queue current = queueOrders.getOrder();

        while (current != null) {
            ArrayList<String> row = new ArrayList<>();
            row.add(current.orderID + "");
            row.add(current.customerID + "");
            row.add(current.productID + "");
            row.add(current.productPrice + "");
            row.add(current.quantity + "");
            row.add(current.subtotal + "");
            data.add(row);
            current = current.next;
        }
        return data;
    }

    private void saveToFile(QueueOrders.Queue order, int quantity) {
        FileManager.appendToFile(FilePaths.PENDING_ORDERS,
                order.orderID + Utility.DIVIDER
                        + order.customerID + Utility.DIVIDER
                        + order.productID + Utility.DIVIDER
                        + order.productPrice + Utility.DIVIDER
                        + quantity + Utility.DIVIDER
                        + (quantity * order.productPrice));
    }
}
