package utils;

import java.util.Scanner;

public class Utility {
    public static final int TOTAL_WIDTH = 58;
    public static String[] productChoices = {"🔙 Go Back", "🎧 Accessories", "💻 Laptops", "📱 Smartphones", "📲 Tablets"};
    private static Scanner in = new Scanner(System.in);

    /**
     * This method centralizes the text {@code heading} by subtracting the length of {@code heading} from the predefined
     * {@code TOTAL_WIDTH} and dividing to two. Then the result is the padding or the amount of spaces to center
     * the {@code heading}.
     *
     * @param heading is the text to display
     */
    public static void centralizeHeading(String heading) {
        int padding = (TOTAL_WIDTH - heading.length()) / 2;
        System.out.println();
        System.out.println("═".repeat(TOTAL_WIDTH));
        System.out.println(" ".repeat(padding) + heading.toUpperCase());
        System.out.println("═".repeat(TOTAL_WIDTH));
    }

    /**
     * This method prompts the user and validates if the input is an integer or not.
     *
     * @return the integer entered by user, or {@code -1} if the input is not a valid integer.
     */
    public static int isInputInteger() {
        System.out.println("━".repeat(25));
        System.out.print("Enter choice >>: ");
        try {
            return Integer.parseInt(in.nextLine());
        } catch (NumberFormatException ae) {
            System.out.println("\n" + "~".repeat(42));
            System.out.print("Invalid input: Non-integer is not allowed.\nPress enter to continue...");
            System.out.println("\n" + "~".repeat(42));
            in.nextLine();
            return -1;
        }
    }

    /**
     * This method validate the input of the user if it is an integer or not. Unlike {@link #isInputInteger()} that
     * has predefined prompts for the user to input, this method allows for customized prompt. Take note that the
     * {@code >>:} is already predefined, therefore, only the prompt text must be entered.
     *
     * @return the integer entered by user, or {@code -1} if the input is not a valid integer.
     */
    public static int isInputInteger(String prompt) {
        System.out.println("━".repeat(25));
        System.out.print(prompt.trim() + " >>: ");
        try {
            return Integer.parseInt(in.nextLine());
        } catch (NumberFormatException ae) {
            System.out.println("\n" + "~".repeat(42));
            System.out.print("Invalid input: Non-integer is not allowed.\nPress enter to continue...");
            System.out.println("\n" + "~".repeat(42));
            in.nextLine();
            return -1;
        }
    }

    /**
     * This method prints out the choices as a formatted menu to user. The number starts from {@code 0}, and must always
     * preserve it for exit, go back, or log out.
     *
     * @param choices is a {@code String} array that contains the possible choices for the user.
     */
    public static void printUserChoices(String[] choices) {
        for (int i = 0; i < choices.length; i++) {
            System.out.println("\t[" + i + "] " + choices[i]);
        }
    }

    public static int isInputInRange(int maxRange) {
        int userInput = isInputInteger();
        if (userInput > maxRange) {
            System.out.println("\n" + "~".repeat(42));
            System.out.print("Invalid input: Not within the range.\nPress enter to continue...");
            System.out.println("\n" + "~".repeat(42));
            in.nextLine();
            return -1;
        }
        return userInput;
    }

    /**
     * This method pauses the program until any key is pressed by the user to give them time to read the display on the
     * screen before proceeding.
     */
    public static void stopper() {
        System.out.print("\nPress enter to continue...");
        in.nextLine();
    }
}