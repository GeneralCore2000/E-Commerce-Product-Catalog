package managers;

import users.Admin;
import users.Customer;
import users.User;
import utils.FilePaths;
import utils.Utility;

import java.util.ArrayList;
import java.util.Scanner;

public class AccountManager {
    private ArrayList<User> accountLists = new ArrayList<>();
    private ProductManager productManager = new ProductManager();
    private Scanner in = new Scanner(System.in);
    private String name, password, address;
    private int userChoice;

    public AccountManager() {
        ArrayList<ArrayList<String>> useraccounts = FileManager.readFile(FilePaths.USER_ACCOUNTS);
        int USERNAME = 2, PASSWORD = 3, ADDRESS = 4;

        for (ArrayList<String> useraccountRow : useraccounts) {
            if (useraccountRow.getFirst().equals("Customer")) {
                accountLists.add(new Customer(useraccountRow.get(USERNAME),
                        useraccountRow.get(PASSWORD),
                        useraccountRow.get(ADDRESS),
                        productManager));
            } else {
                accountLists.add(new Admin(useraccountRow.get(USERNAME),
                        useraccountRow.get(PASSWORD),
                        useraccountRow.get(ADDRESS),
                        productManager));
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
                    createCustomerAccount();
                    break;
                case 2:
                    createAdminAccount();
                    break;
            }
        }
    }

    private User validateCredentials() {
        ArrayList<ArrayList<String>> useraccounts = FileManager.readFile(FilePaths.USER_ACCOUNTS);
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
        for (int row = 0; row < useraccounts.size(); row++) {
            if (useraccounts.get(row).get(1).equals(id + "")) {
                System.out.println("FINDJDIJDIJID");
            }

        }
        for (User user : accountLists) {
            if (user.getUserID() == id && user.getUsername().equals(name) && user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    private void createCustomerAccount() {
        Utility.centralizeHeading("REGISTER CUSTOMER ACCOUNT");
        generalInformation();
        User customer = new Customer(name, password, address, productManager);
        accountLists.add(customer);
        System.out.println("Successfully created customer account\n" + customer);
        System.out.println();
        System.out.println(customer.getClass());
        FileManager.appendToFile(FilePaths.USER_ACCOUNTS,
                "Customer" + "##"
                        + customer.getUserID() + "##"
                        + name + "##"
                        + password + "##"
                        + address);
    }

    private void createAdminAccount() {
        Utility.centralizeHeading("REGISTER ADMIN ACCOUNT");
        generalInformation();
        User admin = new Admin(name, password, address, productManager);
        accountLists.add(admin);
        System.out.println("Successfully created admin account\n" + admin);
        System.out.println();
        FileManager.appendToFile(FilePaths.USER_ACCOUNTS,
                "Admin" + "##"
                        + admin.getUserID() + "##"
                        + name + "##"
                        + password + "##"
                        + address);
    }

    //User type ## ID ## Username ## Address ## Password

    private void generalInformation() {
        System.out.print("Enter name >>:");
        name = in.nextLine();
        System.out.print("Enter password >>: ");
        password = in.nextLine();
        System.out.print("Enter shipping address >>: ");
        address = in.nextLine();
    }
}

