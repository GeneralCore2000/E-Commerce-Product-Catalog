package data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Handles logging of user and system actions in the PRODEX system.
 * Logs are appended to a CSV file for auditing purposes, including timestamps, user details, action types, and changes.
 */

public class LogHistory {

    /**
     * Adds a detailed log entry to the log history file.
     * Includes old and new values for tracking changes
     *
     * @param userID     the ID of the user performing the action
     * @param username   the username of the user performing the action
     * @param actionType the type of action performed (e.g., LOGIN, PRODUCT_UPDATE)
     * @param targetType the type of target affected (e.g., ACCOUNT, PRODUCT)
     * @param targetID   the ID of the target (e.g., product ID); can be null or empty
     * @param oldValue   the previous value before the change; can be null or empty
     * @param newValue   the new value after the change; can be null or empty
     */
    public static void addLog(int userID, String username, ActionType actionType, TargetType targetType, String targetID, String oldValue, String newValue) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        targetID = targetID == null ? "" : targetID;
        oldValue = oldValue == null ? "" : oldValue;
        newValue = newValue == null ? "" : newValue;

        String log = timestamp + ", " + userID + ", " + username + ", " + actionType + ", " + targetType
                + ", " + targetID + ", " + oldValue + ", " + newValue;
        FileManager.appendToFile(FilePaths.LOG_HISTORY, log);
    }
    /**
     * Adds a basic log entry to the log history file without old/new values.
     * Used for actions that don't involve value changes (e.g., LOGIN, LOGOUT).
     *
     * @param userID     the ID of the user performing the action
     * @param username   the username of the user performing the action
     * @param actionType the type of action performed
     * @param targetType the type of target affected
     */
    public static void addLog(int userID, String username, ActionType actionType, TargetType targetType) {
        addLog(userID, username, actionType, targetType, "", "", "");
    }
}
