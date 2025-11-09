package data;

import utils.Utility;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FileManager {

    /**
     * Read and store the content of the files in an ArrayList
     * <p>
     * The file must contain a {@code ##} as a divider of information
     * </p>
     * <p>
     * Prompts an error if the file does not exist
     * </p>
     *
     * @param filePath The path of the file to read
     * @return {@code 2D ArrayList} containing all content of the file whether it's empty or not
     */
    public static ArrayList<ArrayList<String>> readFile(String filePath) {
        ArrayList<ArrayList<String>> fileArray = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                fileArray.add(new ArrayList<>(Arrays.asList(line.split(Utility.DIVIDER))));
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return fileArray;
    }

    /**
     * Update the whole file. This method is convenient for updating a row that is within the first and last row
     *
     * @param filePath       The path of the file to be updated or append to
     * @param updatedContent The {@code 2D ArrayList} of the content to replace the current content of the file
     */
    public static void updateFile(String filePath, ArrayList<ArrayList<String>> updatedContent) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            for (int row = 0; row < updatedContent.size(); row++) {
                ArrayList<String> updatedContentArray = updatedContent.get(row);
                for (String column : updatedContentArray) {
                    bw.write(column + Utility.DIVIDER);
                }
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating file: " + e.getMessage());
        }
    }

    /**
     * This is used to add the text to the last part of the file.
     * <p>
     * If the file does not exist, it will create automatically
     * </p>
     *
     * @param filePath   the path of the file to be updated or append to
     * @param appendText the text to add at the end of the file
     */
    public static void appendToFile(String filePath, String appendText) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
            bw.write(appendText);
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Error appending to file: " + e.getMessage());
        }
    }

    /**
     * Initializes the necessary files for the program to run correctly.
     * <p>
     * This method uses the {@link File} class to create files whose paths are defined
     * in the {@link FilePaths#ALL_FILES} array. Each file in the array will be created
     * if it does not already exist.
     * </p>
     */

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
