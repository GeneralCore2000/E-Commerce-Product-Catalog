package main;

import data.FileManager;
import managers.AccountManager;
import utils.Utility;

import java.util.Scanner;

public class Main {
    private final Scanner in = new Scanner(System.in);
    private final AccountManager accountManager = new AccountManager();

    static void main() {
        new Main().menu();
    }

    private void menu() {
        String[] choices = {"🔙 Exit", "🔑 Log In", "📝 Register", "📂 Load Files", "❔ About"};
        while (true) {
            banner();
            Utility.centralizeHeading("MAIN MENU");
            Utility.printUserChoices(choices);
            int userChoice = Utility.isInputInteger();
            System.out.println();
            switch (userChoice) {
                case -1:
                    continue;
                case 0:
                    return;
                case 1:
                    if (FileManager.areFilesExisting()) {
                        accountManager.login();
                    }
                    break;
                case 2:
                    if (FileManager.areFilesExisting()) {
                        accountManager.register();
                    }
                    break;
                case 3:
                    FileManager.initializeFiles();
                    accountManager.loadAccounts();
                    break;
                case 4:
                    aboutPage();
            }
        }
    }

    private void aboutPage() {
        System.out.println("\n" + "~".repeat(112));
        System.out.println("About PRODEX\n" +
                "\n" +
                "Welcome to PRODEX, an E-commerce models.products.Product Catalog made by John Lloyd E. Vargas for OOP 1 under Sir " +
                "Jayson Batoon.\n" +
                "This project was developed as a two-week individual activity for the A.Y. 2025–2026, 1st Semester.\n" +
                "\n" +
                "PRODEX lets you view, organize, and manage models.products easily — just like in a real online store!\n" +
                "It’s built in Java and showcases Object-Oriented Programming concepts such as classes, inheritance, " +
                "and abstraction.\n" +
                "\n" +
                "Simple, clean, and made to show what OOP can do in action. \uD83D\uDE80");
        System.out.print("\nPress any key to continue...");
        System.out.println("\n" + "~".repeat(112));
        in.nextLine();
    }

    private void banner() {
        System.out.println("╔═══╗   ╔════╗  ╔═════╗  ╔════╗    ╔═════╗    ╔════╗   ╔═╗\n" +
                "║   ╚═══╝    ╚══╝     ╚══╝    ╚════╝     ╚════╝    ╚═══╝ ║\n" +
                "║                                                        ║\n" +
                "║  ██████╗ ██████╗  ██████╗    ██████╗ ███████╗██╗  ██╗  ║\n" +
                "║  ██╔══██╗██╔══██╗██╔═══██╗   ██╔══██╗██╔════╝╚██╗██╔╝  ║\n" +
                "║  ██████╔╝██████╔╝██║   ██║   ██║  ██║█████╗   ╚███╔╝   ║\n" +
                "║  ██╔═══╝ ██╔══██╗██║   ██║   ██║  ██║██╔══╝   ██╔██╗   ║\n" +
                "║  ██║     ██║  ██║╚██████╔╝██╗██████╔╝███████╗██╔╝ ██╗  ║\n" +
                "║  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝╚═════╝ ╚══════╝╚═╝  ╚═╝  ║\n" +
                "║                                                        ║\n" +
                "╚════════════════════════════════════════════════════════╝\n" +
                "  ┃   ╔════════════════════════════════════════════╗   ┃\n" +
                "  ╽╔══╝          WELCOME TO PRODUCT INDEX          ╚══╗╽\n" +
                "   ╚══╗           by Vargas, John Lloyd E.         ╔══╝\n" +
                "      ╚════════════════════════════════════════════╝  ");
    }
}