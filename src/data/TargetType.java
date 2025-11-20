package data;

/**
 * Represents the types of targets affected by actions.
 * Used in logging to specify what entity was impacted, such as accounts, products, orders, or the system itself.
 * This is commonly used with {@link LogHistory}.
 */
public enum TargetType {
    ACCOUNT,
    PRODUCT,
    ORDER,
    SYSTEM;
}
