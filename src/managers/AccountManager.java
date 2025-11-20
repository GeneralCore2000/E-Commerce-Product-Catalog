package managers;

import data.*;
import data_structures.QueueOrders;
import data_structures.UserLinkedList;
import models.users.Admin;
import models.users.Customer;
import models.users.User;
import utils.Utility;

import java.util.ArrayList;
import java.util.Scanner;

public class AccountManager {

    private final Scanner in = new Scanner(System.in);
    private final UserLinkedList accountLists = new UserLinkedList();
    private final ProductManager productManager;
    private final OrderManager orderManager;
    private final QueueOrders queueOrders;

    private String name, password, address;
    private int userChoice;

    /**
     * Constructs an AccountManager and initializes product, order, and queue managers.
     * Automatically loads user accounts from the file.
     */
    public AccountManager() {
        productManager = new ProductManager();
        orderManager = new OrderManager();
        queueOrders = new QueueOrders();
        orderManager.setProductManager(productManager);
        loadAccounts();
    }

    /**
     * Loads user accounts from the user accounts file into the account list.
     * Clears existing accounts to reset (just in case) and populates with Customer or Admin objects based on the file data.
     */
    public void loadAccounts() {
        accountLists.clear();
        ArrayList<ArrayList<String>> userAccounts = FileManager.readFile(FilePaths.USER_ACCOUNTS);
        int USERNAME = 2, PASSWORD = 3, ADDRESS = 4;

        for (ArrayList<String> userAccountRow : userAccounts) {
            if (userAccountRow.getFirst().equalsIgnoreCase("Customer")) {
                accountLists.add(new Customer(userAccountRow.get(USERNAME), userAccountRow.get(PASSWORD), userAccountRow.get(ADDRESS), productManager, queueOrders, orderManager));
            } else {
                accountLists.add(new Admin(userAccountRow.get(USERNAME), userAccountRow.get(PASSWORD), userAccountRow.get(ADDRESS), productManager, queueOrders, orderManager));
            }
        }
    }

    /**
     * Displays the login menu and handles user authentication.
     * Prompts for credentials and logs the user in if valid, then shows the user's menu.
     */
    public void login() {
        String[] choices = {"🔙 Go Back", "🔑 Log In"};

        while (true) {
            Utility.centralizeHeading("LOG IN");
            Utility.printUserChoices(choices);
            userChoice = Utility.isInputInteger();
            switch (userChoice) {
                case -1:
                    continue;
                case 0:
                    return;
                case 1:
                    User user = validateCredentials();
                    if (user != null) {
                        LogHistory.addLog(user.getUserID(), user.getUsername(), ActionType.LOGIN, TargetType.ACCOUNT);
                        orderManager.setAdminUserID(user.getUserID());
                        orderManager.setAdminUsername(user.getUsername());
                        user.showMenu();
                        LogHistory.addLog(user.getUserID(), user.getUsername(), ActionType.LOGOUT, TargetType.ACCOUNT);
                    } else {
                        System.out.println("\nInvalid credentials. Try again");
                        Utility.stopper();
                        System.out.println();
                    }
            }
        }
    }

    /**
     * Displays the registration menu and handles account creation.
     * Allows creating Customer or Admin accounts.
     */
    public void register() {
        String[] choices = {"🔙 Go Back", "👤 Create customer account", "🧑‍💼 Create admin account"};
        while (true) {
            Utility.centralizeHeading("REGISTER");
            Utility.printUserChoices(choices);
            userChoice = Utility.isInputInteger();
            switch (userChoice) {
                case -1:
                    continue;
                case 0:
                    return;
                case 1:
                    createAccount(AccountType.CUSTOMER);
                    break;
                case 2:
                    createAccount(AccountType.ADMIN);
                    break;
            }
        }
    }

    /**
     * Validates user credentials by checking against the loaded account list.
     *
     * @return the {@link User} object if credentials match, or null if invalid
     */
    private User validateCredentials() {
        int id;
        while (true) {
            System.out.print("Enter user ID >>: ");
            try {
                id = Integer.parseInt(in.nextLine());
                break;
            } catch (NumberFormatException ae) {
                System.out.println("\n" + "~".repeat(42));
                System.out.print("Invalid input: Non-integer is not allowed.\nPress enter to continue...");
                System.out.println("\n" + "~".repeat(42));
                in.nextLine();
            }
        }

        System.out.print("Enter user name >>: ");
        name = in.nextLine();

        System.out.print("Enter password >>: ");
        password = in.nextLine();

        UserLinkedList.Node current = accountLists.getHead();
        while (current != null) {
            if (current.user.getUserID() == id && current.user.getUsername().equals(name) && current.user.getPassword().equals(password)) {
                return current.user;
            }
            current = current.next;
        }
        return null;
    }

    /**
     * Creates a new account of the specified type and adds it to the system.
     * Prompts for user information, creates the account, and logs the action.
     *
     * @param accountType the type of account to create (CUSTOMER or ADMIN)
     */
    private void createAccount(AccountType accountType) {
        Utility.centralizeHeading("REGISTER " + accountType + " ACCOUNT");
        generalInformation();
        User user;

        if (accountType == AccountType.CUSTOMER) {
            user = new Customer(name, password, address, productManager, queueOrders, orderManager);
        } else if (accountType == AccountType.ADMIN) {
            user = new Admin(name, password, address, productManager, queueOrders, orderManager);
        } else {
            System.out.println("Invalid account type.");
            return;
        }
        System.out.println("\n" + "━".repeat(Utility.TOTAL_WIDTH));
        System.out.println("Successfully created " + accountType + " account\n" + user + "\n");

        accountLists.add(user);
        FileManager.appendToFile(FilePaths.USER_ACCOUNTS, accountType + Utility.DIVIDER + user.getUserID() + Utility.DIVIDER + "\"" + name + "\"" + Utility.DIVIDER + password + Utility.DIVIDER + "\"" + address + "\"");
        LogHistory.addLog(user.getUserID(), user.getUsername(), ActionType.ACCOUNT_CREATE, TargetType.SYSTEM);
    }

    private void generalInformation() {
        System.out.print("Enter name >>:");
        name = in.nextLine();
        System.out.print("Enter password >>: ");
        password = in.nextLine();
        System.out.print("Enter shipping address >>: ");
        address = in.nextLine();
    }
}

