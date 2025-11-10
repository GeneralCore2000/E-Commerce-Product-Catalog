package managers;

import data.FileManager;
import data.FilePaths;
import models.products.Product;
import models.users.Customer;
import utils.Utility;

public class OrderManager {
    private final QueueOrders queueOrders = new QueueOrders();

    public void addOrder(Product product, Customer customer, int quantity) {
        QueueOrders.Queue queue = queueOrders.enqueue(product, customer, quantity);
        saveToFile(queue, quantity);
    }

    public void seeOrders() {
        queueOrders.display();
    }

    private void saveToFile(QueueOrders.Queue order, int quantity) {
        FileManager.appendToFile(FilePaths.PENDING_ORDERS,
                order.orderID + Utility.DIVIDER
                        + order.customer.getUserID() + Utility.DIVIDER
                        + order.product.getProductID() + Utility.DIVIDER
                        + order.product.getProductPrice() + Utility.DIVIDER
                        + quantity + Utility.DIVIDER
                        + (quantity * order.product.getProductPrice()));
    }
}
