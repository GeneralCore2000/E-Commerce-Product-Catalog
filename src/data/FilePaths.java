package data;

/**
 * Contains constant file paths used throughout the project for data storage and retrieval.
 * These paths point to CSV files for user accounts, products, orders, logs, and archives.
 * All paths are relative to the project root.
 */
public class FilePaths {
    public static final String USER_ACCOUNTS = "src\\data\\repositories\\user_accounts.csv";
    public static final String PRODUCTS = "src\\data\\repositories\\products.csv";
    public static final String PENDING_ORDERS = "src\\data\\repositories\\pending_orders.csv";
    public static final String LOG_HISTORY = "src\\data\\repositories\\log_history.csv";
    public static final String ARCHIVE = "src\\data\\repositories\\archive.csv";
    public static final String[] ALL_FILES = {USER_ACCOUNTS, PRODUCTS, PENDING_ORDERS, LOG_HISTORY, ARCHIVE};
}
