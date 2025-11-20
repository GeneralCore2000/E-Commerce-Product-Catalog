package main;

import data.FileManager;
import managers.AccountManager;
import utils.Utility;

public class Main {
    private final AccountManager accountManager = new AccountManager();

    static void main() {
        new Main().menu();
    }

    private void menu() {
        String[] choices = {"🔙 Exit", "🔑 Log In", "📝 Register", "📂 Load Files"};
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
            }
        }
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