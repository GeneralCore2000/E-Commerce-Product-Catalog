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

    public void setAdminUserID(int adminUserID) {
        this.adminUserID = adminUserID;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
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

            return;
        }
        undoStack.push(new UndoStack.UndoAction(currentQueueOrder, productStock));
        queueOrders.dequeue();
        updateProductStock(currentQueueOrder);
        FileManager.updateFile(FilePaths.PENDING_ORDERS, FileManager.pendingOrderHeader, convertQueueOrderTo2D());
        FileManager.updateFile(FilePaths.PRODUCTS, FileManager.productHeader, productManager.convertProductListTo2D());
    }

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

    private boolean validateStock(QueueOrders.Queue current) {
        Product product = productManager.findProduct(current.productID);
        return product != null && product.getProductStock() >= current.quantity;
    }

    public void seeFrontOrder() {
        System.out.println(queueOrders.peek());
    }

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

    private void saveToFile(QueueOrders.Queue order, int quantity) {
        FileManager.appendToFile(FilePaths.PENDING_ORDERS,
                order.orderID + Utility.DIVIDER
                        + order.customerID + Utility.DIVIDER
                        + order.productID + Utility.DIVIDER
                        + order.productPrice + Utility.DIVIDER
                        + quantity + Utility.DIVIDER
                        + (quantity * order.productPrice));
    }

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
