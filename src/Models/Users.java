package Models;

public class Users {

    private int userID;
    private String name;
    private String password;
    private static String newUser;

    //Constructor to obtain user ID, name, and password
    public Users(int userID, String name, String password, String newUser) {
        this.userID = userID;
        this.name = name;
        this.password = password;
    }

    //Constructor used to get user ID and Name to authenticate into the database
    public Users(int userID, String name) {
        this.userID = userID;
        this.name = name;
    }

    //Getters
    public int getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public static String getnewUser() {
        return newUser;
    }

    //Setters
    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void setNewUser(String newUser) {
        Users.newUser = newUser;
    }

    @Override
    public String toString() {
        String str = String.valueOf(getUserID()) + " - " + getName();
        return str;
    }
}
