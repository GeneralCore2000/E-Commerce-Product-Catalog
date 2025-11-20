package data;

/**
 * Represents the types of action performed for logging purposes.
 * Each action type corresponds to a user event.
 * These are used in {@link LogHistory} to track and record activities
 */
public enum ActionType {
    LOGIN,
    LOGOUT,
    ACCOUNT_CREATE,
    PRODUCT_ADD,
    PRODUCT_UPDATE,
    PRODUCT_DELETE,
    ORDER_CREATE,
    ORDER_FULFILLED;
}
