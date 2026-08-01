package Controllers;

import Database.DBCountries;
import Database.DBCustomers;
import Database.DBDivisions;
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
import java.util.ResourceBundle;


public class AddCustomerController implements Initializable {

    @FXML private ComboBox<Countries> CustomerCountry;
    @FXML private ComboBox<Divisions> CustomerState;
    @FXML private TextField CustomerID;
    @FXML private TextField CustomerName;
    @FXML private TextField CustomerAddress;
    @FXML private TextField CustomerZip;
    @FXML private TextField CustomerPhone;
    @FXML private Button SaveButton;
    @FXML private Button ExitButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CustomerCountry.setItems(DBCountries.getAllCountries());
        CustomerState.setItems(DBDivisions.getDivision());
    }

    /**
     *
     * @param event Saves a new Customer object
     * @throws IOException
     */
    public void saveNewCustomer(ActionEvent event) throws IOException {

        try {
            String customerName = CustomerName.getText();
            String customerAddress = CustomerAddress.getText();
            String postalCode = CustomerZip.getText();
            String phoneNumber = CustomerPhone.getText();
            Divisions division = CustomerState.getValue();

            if (division == null) {
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

            DBCustomers.newCustomer(new Customers(customerName, customerAddress, postalCode, phoneNumber, division.getDivisionID()));

        } catch (Exception e) {
            e.printStackTrace();
        }
        Parent parent = FXMLLoader.load(getClass().getResource("/Views/MainScreen.fxml"));
        Scene parentScene = new Scene(parent);
        Stage window = (Stage) SaveButton.getScene().getWindow();
        window.setScene(parentScene);
        window.show();
    }

    /**
     *
     * @param actionEvent Filters states based on Country selected
     */
    public void onCountryClicked(ActionEvent actionEvent) {
        //changed from states to DBDivisions.getDivision
        ObservableList<Divisions> filterStates = FXCollections.observableArrayList();
        int countryId = CustomerCountry.getSelectionModel().getSelectedItem().getCountryID();

        for (Divisions div : DBDivisions.getDivision()) {
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
