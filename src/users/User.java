package users;

abstract public class User {

    protected static int NEXT_ID = 1000;
    protected String username;
    protected String password;
    protected String address;
    protected int userID;

    public User(String username, String password, String address) {
        this.userID = NEXT_ID++;
        this.username = username;
        this.password = password;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "ID: " + userID + "\nName: " + username + "\nPassword: " + password;
    }

    public abstract void showMenu();

    protected abstract void printProducts();

    protected abstract void showUserInfo();
}
