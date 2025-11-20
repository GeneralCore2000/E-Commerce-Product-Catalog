package data;

import utils.Utility;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class FileManager {
    public static String productHeader = "CATEGORY,ID,NAME,PRICE,STOCK,DESCRIPTION,UNAVAILABLE DATE";
    public static String pendingOrderHeader = "ORDER ID,CUSTOMER ID,PRODUCT ID,PRODUCT PRICE,QUANTITY,SUBTOTAL";

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
    public static void updateFile(String filePath, String header, ArrayList<ArrayList<String>> updatedContent) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            bw.write(header);
            bw.newLine();
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
                    initializeTxtHeaders(path);
                    if (path.equals(FilePaths.PRODUCTS)) {
                        initializeProductsContent();
                    }
                    if (path.equals(FilePaths.USER_ACCOUNTS)) {
                        initializeAccountsContent();
                    }
                }
            } catch (IOException ae) {
                System.out.println("Problem creating the file: " + ae.getMessage());
            }
        }
        System.out.println();
    }

    public static boolean areFilesExisting() {
        for (String path : FilePaths.ALL_FILES) {
            File file = new File(path);
            if (!file.exists()) {
                System.out.println("Make sure to load Files first using [📂 Load Files].");
                Utility.stopper();
                return false;
            }
        }
        return true;
    }

    private static void initializeProductsContent() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FilePaths.PRODUCTS, true))) {
            bw.write("ACCESSORIES,5000,Wireless Mouse,599.0,35,\"Ergonomic wireless mouse with silent clicks and long battery life.\",2024-12-31\n");
            bw.write("LAPTOPS,5001,Acer Aspire 5,34999.0,8,\"15.6\" FHD laptop with Ryzen 5 processor and 8GB RAM.\",2024-12-31\n");
            bw.write("SMARTPHONES,5002,Samsung Galaxy A54,23999.0,15,\"5G smartphone with AMOLED display and triple camera setup.\",2024-12-31\n");
            bw.write("TABLETS,5003,iPad 10th Gen,27999.0,9,\"10.9\" Liquid Retina display with A14 Bionic chip and Touch ID.\",2026-12-31\n");
            bw.write("ACCESSORIES,5004,Laptop Sleeve 15.6\",449.0,20,\"Soft neoprene sleeve to protect laptops from scratches and dust.\",2024-12-31\n");
            bw.write("LAPTOPS,5005,Lenovo IdeaPad Slim 3,32499.0,10,\"Lightweight laptop with Intel Core i3 and SSD storage.\",2024-12-31\n");
            bw.write("SMARTPHONES,5006,iPhone 14,56999.0,8,\"Apple smartphone with A15 Bionic chip and Super Retina XDR display.\",2024-12-31\n");
            bw.write("TABLETS,5007,Samsung Galaxy Tab S9,45999.0,7,\"Flagship Android tablet with AMOLED screen and S Pen support.\",2024-12-31\n");
            bw.write("ACCESSORIES,5008,USB-C Hub 6-in-1,999.0,15,\"Multiport adapter with HDMI, USB 3.0, and SD card support.\",2026-12-31\n");
            bw.write("LAPTOPS,5009,ASUS TUF Gaming F15,5994.59,90,\"Gaming laptop pa rin ba to?\"\n");
            bw.write("SMARTPHONES,5010,Xiaomi Redmi Note 13,12499.0,20,\"Mid-range phone with 108MP camera and fast charging.\",2026-12-31\n");
            bw.write("TABLETS,5011,Lenovo Tab M10 Plus,12999.0,11,\"Affordable tablet for study and entertainment use.\",2026-12-31\n");
            bw.write("ACCESSORIES,5012,Mechanical Keyboard,1899.0,12,\"RGB backlit mechanical keyboard with blue switches.\",2026-12-31\n");
            bw.write("LAPTOPS,5013,HP Pavilion 14,41299.0,6,\"Compact laptop with Intel Core i5 and sleek aluminum design.\",2026-12-31\n");
            bw.write("SMARTPHONES,5014,Redmi note 8 Pro,-9500.0,-5,\"Stylish design with 200MP camera and curved AMOLED screen.\",2026-12-31\n");
            bw.write("TABLETS,5015,Huawei MatePad 11,24499.0,10,\"11\" 120Hz display tablet with multi-screen collaboration.\",2026-12-31\n");
            bw.write("ACCESSORIES,5016,Noise-Cancelling Headphones,2499.0,10,\"Comfortable over-ear headphones with active noise cancellation.\",2026-12-31\n");
            bw.write("LAPTOPS,5017,MacBook Air M2,69999.0,4,\"Apple M2 chip with 8GB RAM and 256GB SSD, ultra-thin build.\",2026-12-31\n");
            bw.write("SMARTPHONES,5018,OnePlus Nord CE 3,21499.0,10,\"Smooth 120Hz display with Snapdragon 782G processor.\",2026-12-31\n");
            bw.write("TABLETS,5019,Xiaomi Pad 6,18999.0,14,\"Snapdragon 870-powered tablet with high refresh rate display.\",2026-12-31\n");
        } catch (IOException ae) {
            System.out.println("Error adding content to products.csv: " + ae.getMessage());
        }
    }

    private static void initializeAccountsContent() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FilePaths.USER_ACCOUNTS, true))) {
            bw.write("ADMIN,1000,admin1,admin,\"admin address 1\"\n");
            bw.write("CUSTOMER,1001,customer1,customer,\"customer address 1\"\n");
        } catch (IOException ae) {
            System.out.println("Error adding content to user_accounts.csv: " + ae.getMessage());
        }
    }

    private static void initializeTxtHeaders(String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
            if (filePath.equals(FilePaths.PRODUCTS)) {
                bw.write("CATEGORY,ID,NAME,PRICE,STOCK,DESCRIPTION");
                bw.newLine();
            }
            if (filePath.equals(FilePaths.USER_ACCOUNTS)) {
                bw.write("ACCOUNT TYPE,USER ID,USER NAME,PASSWORD,ADDRESS");
                bw.newLine();
            }
            if (filePath.equals(FilePaths.PENDING_ORDERS)) {
                bw.write("ORDER ID,CUSTOMER ID,PRODUCT ID,PRODUCT PRICE,QUANTITY,SUBTOTAL");
                bw.newLine();
            }
            if (filePath.equals(FilePaths.LOG_HISTORY)) {
                bw.write("TIMESTAMP,USER ID,USERNAME,ACTION TYPE,TARGET TYPE,TARGET ID,OLD VALUE,NEW VALUE");
                bw.newLine();
            }
        } catch (IOException ae) {
            System.out.println("Error adding headers to " + filePath + " : " + ae.getMessage());
        }
    }
}
