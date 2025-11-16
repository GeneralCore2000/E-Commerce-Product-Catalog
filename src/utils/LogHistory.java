package utils;

import managers.FileManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogHistory {

    public static void add(int userID, String username, ActionLog action, String info, RemarksLog remarks) {
        String timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String log = timeStamp + "," + userID + "," + username + "," + action + "," + info + "," + remarks;
        FileManager.appendToFile(FilePaths.LOG_HISTORY, log);
    }

    public static void add(int userID, String username, ActionLog action, RemarksLog remarks) {
        add(userID, username, action, "", remarks);
    }
    //DATE TIME, USER ID, USER NAME, ACTION, ADDITIONAL INFO, REMARKS
}
