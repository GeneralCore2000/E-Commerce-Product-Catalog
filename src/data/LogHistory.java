package data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogHistory {

    public static void addLog(int userID,
                              String username,
                              ActionType actionType,
                              TargetType targetType,
                              String targetID,
                              String oldValue,
                              String newValue) {

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        targetID = targetID == null ? "" : targetID;
        oldValue = oldValue == null ? "" : oldValue;
        newValue = newValue == null ? "" : newValue;

        String log = timestamp + ", " + userID + ", " + username + ", " + actionType + ", " + targetType
                + ", " + targetID + ", " + oldValue + ", " + newValue;
        FileManager.appendToFile(FilePaths.LOG_HISTORY, log);
    }

    public static void addLog(int userID, String username, ActionType actionType, TargetType targetType) {
        addLog(userID, username, actionType, targetType, "", "", "");
    }
}
