package managers;

import utils.FilePaths;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FileManager {

    public static ArrayList<ArrayList<String>> readFile(String filePath) {
        ArrayList<ArrayList<String>> fileArray = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                fileArray.add(new ArrayList<>(Arrays.asList(line.split("##"))));
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return fileArray;
    }

    public static void updateFile(String filePath, ArrayList<ArrayList<String>> updatedContent, boolean append) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, append))) {
            for (int row = 0; row < updatedContent.size(); row++) {
                ArrayList<String> updatedContentArray = updatedContent.get(row);
                for (String column : updatedContentArray) {
                    bw.write(column + "##");
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating file: " + e.getMessage());
        }
    }

    public static void appendToFile(String filePath, String appendText) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(appendText);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error appending to fil: " + e.getMessage());
        }
    }

    public static void initializeFiles() {
        for (String path : FilePaths.ALL_FILES) {
            try {
                File file = new File(path);
                if (file.createNewFile()) {
                    System.out.print("\rFile " + file.getName() + " created successfully at: " + file.getAbsolutePath());
                }
            } catch (IOException ae) {
                System.out.println("Problem creating the file: " + ae.getMessage());
            }
        }
        System.out.println();
    }
}
