package Database;

import Models.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCustomers {

    public static ObservableList<Customers> customers = FXCollections.observableArrayList();

    //Query to save new Customer
    public static void newCustomer(Customers customer) {

        try {
            //Sql Statement
            String query = "INSERT into customers (Customer_Name, Address, Postal_Code, Phone, Division_ID)" + "VALUES (?, ?, ?, ?, ?)";
            //Prepared statement
            PreparedStatement pst = JDBC.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            //Add Values

            pst.setString(1, customer.getCustomerName());
            pst.setString(2, customer.getCustomerAddress());
            pst.setString(3, customer.getPostalCode());
            pst.setString(4, customer.getPhoneNumber());
            pst.setInt(5, customer.getDivision());

            pst.execute();

            customers.add(customer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Query to update existing Customer
    public static void modifyCustomer(Customers customer) {

        try {
            //Sql Statement
            String query = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
            //Prepared statement
            PreparedStatement pst = JDBC.connection.prepareStatement(query);
            //Add Values
            pst.setInt(6, customer.getCustomerID());
            pst.setString(1, customer.getCustomerName());
            pst.setString(2, customer.getCustomerAddress());
            pst.setString(3, customer.getPostalCode());
            pst.setString(4, customer.getPhoneNumber());
            pst.setInt(5, customer.getDivision());

            pst.execute();

            customers.add(customer);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Query to delete existing Customer
    public static void deleteCustomer(int Customer_ID) {

        try {
            //Sql Statement
            String query = "DELETE FROM appointments WHERE Customer_ID = ?";
            //Prepared statement
            PreparedStatement pst = JDBC.connection.prepareStatement(query);

            pst.setInt(1, Customer_ID);
            pst.execute();

            //Sql Statement
            String queryTwo = "DELETE FROM customers WHERE Customer_ID = ?";
            //Prepared statement
            PreparedStatement pstTwo = JDBC.connection.prepareStatement(queryTwo);

            pstTwo.setInt(1, Customer_ID);
            pstTwo.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Query to get all Customer information
    public static ObservableList<Customers> getAllCustomers() {

        ObservableList<Customers> allCustomers = FXCollections.observableArrayList();

        try {
            //Sql Select Query
            String databaseQueryOne = "SELECT Customer_ID, Customer_Name, Address, Postal_code, Phone, customers.Division_ID, first_level_divisions.Division, countries.country \n" +
                    "FROM client_schedule.customers JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID \n" +
                    "JOIN countries ON countries.Country_ID = first_level_divisions.country_ID;";

            //Create the Java statement
            Statement statement = JDBC.connection.createStatement();

            //Execute the query statement
            ResultSet rs = statement.executeQuery(databaseQueryOne);

            //Iterate through the java Resultset
            while (rs.next()) {
                int custID = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalcode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divID = rs.getInt("Division_ID");
                String divName = rs.getString("Division");
                String countryName = rs.getString("Country");

                allCustomers.add(new Customers(custID, name, address, postalcode, phone, divID, divName, countryName));
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allCustomers;
    }

    //Query to get Customer ID and Name
    public static ObservableList<Customers> getCustomers() {

        ObservableList<Customers> CustomerID = FXCollections.observableArrayList();

        String query = "SELECT * FROM customers";

        try {
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                Customers c = new Customers(id, name);

                CustomerID.add(c);
            }

        } catch (
                SQLException throwables) {
            throwables.printStackTrace();
        }
        return CustomerID;
    }

    //Query to get Customer ID
    public static Customers getCustomerID (int customerID) {

        String query = "SELECT Customer_ID, Customer_Name FROM Customers WHERE Customer_ID = " + customerID;
        int customerId = 0;
        String customerName;
        Customers c = null;


        try {
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {

                customerId = rs.getInt("Customer_ID");
                customerName = rs.getString("Customer_Name");

                c = new Customers(customerId, customerName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }
}
