package Controllers;

import Database.DBCountries;
import Database.DBCustomers;
import Database.DBDivisions;
import Database.JDBC;
import Models.Countries;
import Models.Customers;
import Models.Divisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;


public class ModifyCustomerController implements Initializable {

    @FXML private ComboBox<Countries> CustomerCountry;
    @FXML private ComboBox<Divisions> CustomerState;
    @FXML private TextField CustomerID;
    @FXML private TextField CustomerName;
    @FXML private TextField CustomerAddress;
    @FXML private TextField CustomerZip;
    @FXML private TextField CustomerPhone;
    @FXML private Button SaveButton;
    @FXML private Button ExitButton;

    ObservableList<Divisions> states = FXCollections.observableArrayList();
    ObservableList<Countries> countries = FXCollections.observableArrayList();

    int custID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        String query1 = "SELECT * FROM countries";
        String query2 = "SELECT * FROM first_level_divisions";

        try {
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(query2);

            while (rs.next()) {
                int id = rs.getInt("Division_ID");
                String name = rs.getString("Division");
                int countryId = rs.getInt("COUNTRY_ID");
                Divisions d = new Divisions(id, name, countryId);

                states.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(query1);

            while (rs.next()) {
                int id = rs.getInt("Country_ID");
                String name = rs.getString("Country");
                Countries c = new Countries(id, name);

                countries.add(c);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     *
     * @param customerID Customer ID of selected Customer
     * @throws IOException
     */
    public void selectedCustomer(int customerID) throws IOException {

        CustomerID.setText(Integer.toString(customerID));

        try {
            //Sql Select Query
            String databaseQuery = "SELECT * FROM Customers WHERE Customer_ID=" + customerID;

            //Create the Java statement
            Statement statement = JDBC.connection.createStatement();

            //Execute the query statement
            ResultSet rs = statement.executeQuery(databaseQuery);

            while (rs.next()) {
                custID = rs.getInt("Customer_ID");
                String name = rs.getString("Customer_Name");
                String address = rs.getString("Address");
                String postalcode = rs.getString("Postal_Code");
                String phone = rs.getString("Phone");
                int divID = rs.getInt("Division_ID");

                CustomerName.setText(name);
                CustomerAddress.setText(address);
                CustomerZip.setText(postalcode);
                CustomerPhone.setText(phone);
                CustomerState.setItems(states);
                CustomerCountry.setItems(countries);
                CustomerState.setValue(DBDivisions.getDivisionName(divID));
                int countryId = DBCountries.getCountryID(divID);
                CustomerCountry.setValue(DBCountries.getCountryName(countryId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param event Updates Customer in the Database
     * @throws IOException
     */
    public void saveUpdateCustomer(ActionEvent event) throws IOException {

        Divisions divID = CustomerState.getSelectionModel().getSelectedItem();

        if (divID == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Fill All Fields");
            alert.setContentText("All Fields Must be Contain a Value");
            alert.show();
            return;
        }

        if (CustomerName.getText().isEmpty() || CustomerAddress.getText().isEmpty() || CustomerZip.getText().isEmpty() || CustomerPhone.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Fill all Fields");
            alert.setContentText("All Fields Must be Contain a Value");
            alert.showAndWait();
            return;
        }

        Customers c = new Customers (custID, CustomerName.getText(), CustomerAddress.getText(), CustomerZip.getText(), CustomerPhone.getText(), CustomerState.getValue().getDivisionID());
        DBCustomers.modifyCustomer(c);

        Parent parent = FXMLLoader.load(getClass().getResource("/Views/MainScreen.fxml"));
        Scene parentScene = new Scene(parent);
        Stage window = (Stage) SaveButton.getScene().getWindow();
        window.setScene(parentScene);
        window.show();
    }

    //Filters states based on country selected
    public void onCountryClicked(ActionEvent actionEvent) {

        ObservableList<Divisions> filterStates = FXCollections.observableArrayList();
        int countryId = CustomerCountry.getSelectionModel().getSelectedItem().getCountryID();

        for (Divisions div : states) {
            if (div.getCountryID() == countryId) {
                filterStates.add(div);
            }
        }
        CustomerState.setItems(filterStates);
    }

    /**
     *
     * @param event Return to main screen
     * @throws IOException
     */
    public void exitToMain(ActionEvent event) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("/Views/MainScreen.fxml"));
        Scene parentScene = new Scene(parent);
        Stage window = (Stage) ExitButton.getScene().getWindow();
        window.setScene(parentScene);
        window.show();
    }
}
