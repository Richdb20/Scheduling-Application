package Database;

import Models.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUsers {

    //Query to get all User parameters
    public static ObservableList<Users> getUsers() {

        ObservableList<Users> users = FXCollections.observableArrayList();

        String query = "SELECT * FROM Users";

        try {
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("User_ID");
                String name = rs.getString("User_Name");
                Users u = new Users(id, name);

                users.add(u);
            }

        } catch (
                SQLException throwables) {
            throwables.printStackTrace();
        }
        return users;
    }

    //Query to get User ID
    public static Users getUserID (int userID) {

        String query = "SELECT User_ID, User_Name FROM Users WHERE User_ID = " + userID;
        int userId = 0;
        String userName;
        Users u = null;

        try {
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {

                userId = rs.getInt("User_ID");
                userName =  rs.getString("User_Name");

                u = new Users(userId, userName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }
}
