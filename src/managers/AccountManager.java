package managers;

import users.Admin;
import users.Customer;
import users.User;
import utils.FilePaths;
import utils.Utility;

import java.util.ArrayList;
import java.util.Scanner;

public class AccountManager implements EditUserInfos {
    private ArrayList<User> accountLists = new ArrayList<>();
    private ProductManager productManager = new ProductManager();
    private Scanner in = new Scanner(System.in);
    private String name, password, address;
    private int userChoice;

    /**
     * Constructs an {@code AccountManager} instance and automatically reading the {@code useraccounts.txt} using
     * {@link FilePaths#USER_ACCOUNTS} where it stores the String path of {@code user_accounts.txt}
     *
     * <p>
     * The {@code user_accounts.txt} is expected to have rows containing the information of the users with
     * {@code ,} as dividers.
     * </p>
     *  <ul>
     *      <li>Index 0: Account type ("Customer" or "Admin")</li>
     *      <li>Index 1: Account ID that is automatically incremented by 1</li>
     *      <li>Index 2: Username</li>
     *      <li>Index 3: Password</li>
     *     <li>Index 4: Address</li>
     * </ul>
     *
     * @see FileManager#readFile(String)
     * @see FilePaths#USER_ACCOUNTS
     * @see Customer
     * @see Admin
     */
    public AccountManager() {
        ArrayList<ArrayList<String>> useraccounts = FileManager.readFile(FilePaths.USER_ACCOUNTS);
        int USERNAME = 2, PASSWORD = 3, ADDRESS = 4;

        for (ArrayList<String> useraccountRow : useraccounts) {
            AccountType accountType = AccountType.valueOf(useraccountRow.getFirst());
            if (accountType == AccountType.CUSTOMER) {
                accountLists.add(new Customer(useraccountRow.get(USERNAME),
                        useraccountRow.get(PASSWORD),
                        useraccountRow.get(ADDRESS),
                        productManager, this));
            } else {
                accountLists.add(new Admin(useraccountRow.get(USERNAME),
                        useraccountRow.get(PASSWORD),
                        useraccountRow.get(ADDRESS),
                        productManager, this));
            }
        }
    }

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
                        user.showMenu();
                    } else {
                        System.out.println("\nInvalid credentials. Try again");
                        Utility.stopper();
                        System.out.println();
                    }
            }
        }
    }

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
     * Prompt the users for credentials and validate if it is existing or not
     *
     * @return {@link User} object that matches all the entered credentials; {@code null} if at least one credential is
     * wrong
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

        for (User user : accountLists) {
            if (user.getUserID() == id && user.getUsername().equals(name) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * Creates a new account of the specified {@link AccountType}
     * <p>
     * This method collects {@link #generalInformation()} and create a User object of either {@link Admin}
     * or {@link Customer} and save it to {@code accountLists}. It also appends the newly created account details to the
     * user accounts file path defined in {@link FilePaths#USER_ACCOUNTS}
     * </p>
     *
     * @param accountType should be either "Customer" or "Admin"
     * @see AccountType
     */
    private void createAccount(AccountType accountType) {
        Utility.centralizeHeading("REGISTER " + accountType + " ACCOUNT");
        generalInformation();
        User user;

        if (accountType == AccountType.CUSTOMER) {
            user = new Customer(name, password, address, productManager, this);
        } else if (accountType == AccountType.ADMIN) {
            user = new Admin(name, password, address, productManager, this);
        } else {
            System.out.println("Invalid account type.");
            return;
        }
        System.out.println("\n" + "━".repeat(Utility.TOTAL_WIDTH));
        System.out.println("Successfully created " + accountType + " account\n" + user + "\n");

        accountLists.add(user);
        FileManager.appendToFile(FilePaths.USER_ACCOUNTS,
                accountType + ","
                        + user.getUserID() + ","
                        + name + ","
                        + password + ","
                        + address);
    }

    private void generalInformation() {
        System.out.print("Enter name >>:");
        name = in.nextLine();
        System.out.print("Enter password >>: ");
        password = in.nextLine();
        System.out.print("Enter shipping address >>: ");
        address = in.nextLine();
    }

    private ArrayList<ArrayList<String>> convertUsersTo2D() {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        for (User account : accountLists) {
            ArrayList<String> row = new ArrayList<>();
            row.add(account.getClass().getSimpleName().toUpperCase());
            row.add(account.getUserID() + "");
            row.add(account.getUsername());
            row.add(account.getPassword());
            row.add(account.getAddress());
            data.add(row);
        }
        return data;
    }

    public void updateUsername(User user) {
        System.out.println("Current username: " + user.getUsername());
        System.out.print("Enter new username >>: ");
        user.setUsername(in.nextLine());
        FileManager.updateFile(FilePaths.USER_ACCOUNTS, convertUsersTo2D());
    }

    public void updatePassword(User user) {
        System.out.print("Enter new password >>: ");
        user.setPassword(in.nextLine());
        FileManager.updateFile(FilePaths.USER_ACCOUNTS, convertUsersTo2D());
    }

    public void updateAddress(User user) {
        System.out.println("Current address: " + user.getPassword());
        System.out.print("Enter new address >>: ");
        user.setAddress(in.nextLine());
        FileManager.updateFile(FilePaths.USER_ACCOUNTS, convertUsersTo2D());
    }
}

