package managers;

import data.*;
import data_structures.ProductLinkedList;
import data_structures.QueueOrders;
import data_structures.UndoStack;
import models.products.Product;
import utils.Utility;

import java.util.ArrayList;

public class OrderManager {
    private final QueueOrders queueOrders = new QueueOrders();
    private final UndoStack undoStack = new UndoStack();
    private ProductManager productManager;
    private String adminUsername;
    private int adminUserID;

    /**
     * Constructs an OrderManager and loads pending orders from the file into the queue.
     */
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

    /**
     * Sets the admin user ID for logging purposes.
     *
     * @param adminUserID the admin's user ID
     */
    public void setAdminUserID(int adminUserID) {
        this.adminUserID = adminUserID;
    }

    /**
     * Sets the admin username for logging purposes.
     *
     * @param adminUsername the admin's username
     */
    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    /**
     * Adds a new order to the queue and saves it to the file.
     *
     * @param customerID   the ID of the customer placing the order
     * @param productID    the ID of the product ordered
     * @param productPrice the price of the product
     * @param quantity     the quantity ordered
     * @param subtotal     the subtotal for the order
     */
    public void addOrder(int customerID, int productID, double productPrice, int quantity, double subtotal) {
        QueueOrders.Queue queue = queueOrders.enqueue(customerID, productID, productPrice, quantity, subtotal);
        saveToFile(queue, quantity);
    }

    /**
     * Sets the ProductManager for stock validation and updates.
     *
     * @param productManager the ProductManager instance
     */
    public void setProductManager(ProductManager productManager) {
        this.productManager = productManager;
    }

    /**
     * Displays all pending orders in the queue.
     */
    public void seeOrders() {
        queueOrders.display();
    }

    /**
     * Fulfills the front order in the queue if stock is available.
     * Updates product stock, logs the action, and pushes to undo stack.
     */
    public void fulfillOrder() {
        QueueOrders.Queue currentQueueOrder = queueOrders.getOrder();
        if (currentQueueOrder == null) {
            System.out.println("No orders to fulfill.");
            return;
        }
        boolean enoughStock = validateStock(currentQueueOrder);
        Product product = productManager.findProduct(currentQueueOrder.productID);
        if (product == null) {
            System.out.println("Error: Product is not found. De-Queuing the product.");
            queueOrders.dequeue();
            Utility.stopper();
            return;
        }
        int productStock = product.getProductStock();
        if (!enoughStock) {
            return;
        }
        undoStack.push(new UndoStack.UndoAction(currentQueueOrder, productStock));
        queueOrders.dequeue();
        updateProductStock(currentQueueOrder);
        LogHistory.addLog(adminUserID, adminUsername, ActionType.ORDER_FULFILLED, TargetType.ORDER);
        FileManager.updateFile(FilePaths.PENDING_ORDERS, FileManager.pendingOrderHeader, convertQueueOrderTo2D());
        FileManager.updateFile(FilePaths.PRODUCTS, FileManager.productHeader, productManager.convertProductListTo2D());
    }

    /**
     * Undo the last fulfilled order by restoring stock and re-enqueueing the order.
     * Updates files and logs the action.
     */
    public void undoFulfillOrder() {
        if (undoStack.isEmpty()) {
            System.out.println("No recent fulfillments to undo.");
            Utility.stopper();
            return;
        }

        UndoStack.UndoAction lastAction = undoStack.pop();
        QueueOrders.Queue orderToRestore = lastAction.getFulfilledOrder();

        Product product = productManager.findProduct(orderToRestore.productID);
        if (product != null) {
            product.setProductStock(lastAction.getPreviousStock());
        }

        queueOrders.enqueue(orderToRestore.customerID, orderToRestore.productID,
                orderToRestore.productPrice, orderToRestore.quantity, orderToRestore.subtotal);

        FileManager.updateFile(FilePaths.PENDING_ORDERS, FileManager.pendingOrderHeader, convertQueueOrderTo2D());
        FileManager.updateFile(FilePaths.PRODUCTS, FileManager.productHeader, productManager.convertProductListTo2D());

        LogHistory.addLog(adminUserID, adminUsername, ActionType.ORDER_FULFILLED, TargetType.ORDER,
                orderToRestore.orderID + " (UNDONE)", null, null);
        System.out.println("Last fulfillment undone. Order restored to queue.");
        Utility.stopper();
    }

    /**
     * Validates if there is enough stock for the given order.
     *
     * @param current the order to validate
     * @return true if stock is sufficient, false otherwise
     */
    private boolean validateStock(QueueOrders.Queue current) {
        Product product = productManager.findProduct(current.productID);
        return product != null && product.getProductStock() >= current.quantity;
    }

    public void seeFrontOrder() {
        System.out.println(queueOrders.peek());
    }

    /**
     * Converts the current queue orders to a 2D ArrayList for file updates.
     * Commonly use with {@link FileManager#updateFile(String, String, ArrayList)}
     *
     * @return a 2D ArrayList representation of the orders
     */
    private ArrayList<ArrayList<String>> convertQueueOrderTo2D() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
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

    /**
     * Saves a new order to the pending orders file.
     *
     * @param order    the order to save
     * @param quantity the quantity of the order
     */
    private void saveToFile(QueueOrders.Queue order, int quantity) {
        FileManager.appendToFile(FilePaths.PENDING_ORDERS,
                order.orderID + Utility.DIVIDER
                        + order.customerID + Utility.DIVIDER
                        + order.productID + Utility.DIVIDER
                        + order.productPrice + Utility.DIVIDER
                        + quantity + Utility.DIVIDER
                        + (quantity * order.productPrice));
    }

    /**
     * Updates the stock of the product associated with the fulfilled order.
     *
     * @param currentQueueOrder the order that was fulfilled
     */
    private void updateProductStock(QueueOrders.Queue currentQueueOrder) {
        ProductLinkedList.Node currentProduct = productManager.getProductLists().getHead();
        while (currentProduct != null) {
            if (currentQueueOrder.productID == currentProduct.product.getProductID()) {
                int newProductStock = currentProduct.product.getProductStock() - currentQueueOrder.quantity;
                currentProduct.product.setProductStock(newProductStock);
            }
            currentProduct = currentProduct.next;
        }
    }
}
