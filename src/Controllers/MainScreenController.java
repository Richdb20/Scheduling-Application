package Controllers;

import Database.DBAppointments;
import Database.DBCustomers;
import Database.JDBC;
import Models.Appointments;
import Models.Customers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;


public class MainScreenController implements Initializable {

    @FXML private TableView<Appointments> AppointmentTable;
    @FXML private TableColumn<Appointments, Integer> appointmentIDCol;
    @FXML private TableColumn<Appointments, String> titleCol;
    @FXML private TableColumn<Appointments, String> descriptionCol;
    @FXML private TableColumn<Appointments, String> locationCol;
    @FXML private TableColumn<Appointments, String> typeCol;
    @FXML private TableColumn<Appointments, String> startCol;
    @FXML private TableColumn<Appointments, String> endCol;
    @FXML private TableColumn<Appointments, String> customerIDCol;
    @FXML private TableColumn<Appointments, String> contactIDCol;
    @FXML private TableColumn<Appointments, String> userIDCol;
    @FXML private RadioButton viewAllRadioButton;
    @FXML private RadioButton viewMonthRadioButton;
    @FXML private RadioButton viewWeekRadioButton;
    @FXML private TableView<Customers> CustomerTable;
    @FXML private TableColumn<Customers, Integer> cIDCol;
    @FXML private TableColumn<Customers, String> cnameCol;
    @FXML private TableColumn<Customers, String> cAddressCol;
    @FXML private TableColumn<Customers, String> cPostalCodeCol;
    @FXML private TableColumn<Customers, String> cPhoneNumberCol;
    @FXML private TableColumn<Customers, String> cStateCol;
    @FXML private TableColumn<Customers, String> cCountryCol;
    @FXML private Button addCustomerButton;
    @FXML private Button updateCustomerButton;
    @FXML private Button deleteCustomerButton;
    @FXML private Button addAppointmentButton;
    @FXML private Button updateAppointmentButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private Button reportsButton;
    @FXML private ToggleGroup AppointmentToggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

            //Initialize and set the Customer table parameters
            CustomerTable.setItems(DBCustomers.getAllCustomers());

            cIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            cnameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            cAddressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
            cPostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            cPhoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            cStateCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
            cCountryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));

            //Initialize and set the Customer table parameters
            AppointmentTable.setItems(DBAppointments.getAllAppointments());

            appointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            customerIDCol.setCellValueFactory(new PropertyValueFactory<>("customerID"));
            userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
            contactIDCol.setCellValueFactory(new PropertyValueFactory<>("contactID"));
    }

    /**
     *
     * @param event Open add appointment screen
     * @throws IOException
     */
    public void addAppointment(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/AddAppointment.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) addAppointmentButton.getScene().getWindow();;
        stage.setTitle("Add Appointment");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     *
     * @param event Open add customer screen
     * @throws IOException
     */
    public void addCustomer(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/AddCustomer.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) addCustomerButton.getScene().getWindow();;
        stage.setTitle("Add Customer");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     *
     * @param event Delete selected appointment
     */
    public void deleteAppointment(ActionEvent event) {

        Appointments selectedAppointment;
        selectedAppointment = AppointmentTable.getSelectionModel().getSelectedItem();

        if (selectedAppointment != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirm to Delete Appointment");
            alert.setContentText("Click ok to Confirm Deletion of Appointment");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                DBAppointments.deleteAppointment(selectedAppointment.getAppointmentID());

                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Confirmation");
                alert1.setContentText("Appointment ID " + selectedAppointment.getAppointmentID() + " Type " + selectedAppointment.getType() + " Was Deleted ");
                alert1.showAndWait();

                AppointmentTable.setItems(DBAppointments.getAllAppointments());
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Appointment Must be Selected");
            alert.setContentText("Please Select Appointment Before Removing.");
            alert.showAndWait();
        }
    }

    /**
     *
     * @param event Delete selected Customer
     */
    public void deleteCustomer(ActionEvent event) {

        Customers selectedCustomer;
        selectedCustomer = CustomerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirm to Delete Customer & Appointments");
            alert.setContentText("WARNING: Clicking ok will delete the Customer and all Associated Appointments for this Customer. This action can't be undone.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                DBCustomers.deleteCustomer(selectedCustomer.getCustomerID());

                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("Information");
                alert1.setContentText("Customer ID " + selectedCustomer.getCustomerID() + " " + selectedCustomer.getCustomerName() + " Was Deleted ");
                alert1.showAndWait();

                CustomerTable.setItems(DBCustomers.getAllCustomers());
                AppointmentTable.setItems(DBAppointments.getAllAppointments());
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Customer Must be Selected");
            alert.setContentText("Please Select Customer Before Removing.");
            alert.showAndWait();
        }
    }

    /**
     *
     * @param event Open modify appointment screen
     * @throws IOException
     */
    public void updateAppointment(ActionEvent event) throws IOException {

        if(AppointmentTable.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Selection Required");
            alert.setHeaderText("Selection Required");
            alert.setContentText("Please Select an Appointment");
            alert.showAndWait();
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyAppointment.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) updateAppointmentButton.getScene().getWindow();;
            stage.setTitle("Modify Appointment");
            stage.setScene(new Scene(root));

            int appointmentID = AppointmentTable.getSelectionModel().getSelectedItem().getAppointmentID();
            ModifyAppointmentController modifyAppointment = loader.getController();
            modifyAppointment.selectedAppointment(appointmentID);

            stage.show();
        }
    }

    /**
     *
     * @param event Open modify customer screen
     * @throws IOException
     */
    public void updateCustomer(ActionEvent event) throws IOException {

        if(CustomerTable.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Selection Required");
            alert.setHeaderText("Selection Required");
            alert.setContentText("Please Select a Customer");
            alert.showAndWait();
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ModifyCustomer.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) updateCustomerButton.getScene().getWindow();;
            stage.setTitle("Modify Customer");
            stage.setScene(new Scene(root));
            stage.show();

            int customerID = CustomerTable.getSelectionModel().getSelectedItem().getCustomerID();
            ModifyCustomerController modifyCustomer = loader.getController();
            modifyCustomer.selectedCustomer(customerID);
        }
    }

    /**
     *
     * @param event Open reports screen
     * @throws IOException
     */
    public void toReports(ActionEvent event) throws IOException {

        Stage stage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("/Views/Reports.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param event View all appointments radio button
     */
    public void viewAllAppointments(ActionEvent event) {

        if (viewAllRadioButton.isSelected()) {
            AppointmentTable.setItems(DBAppointments.getAllAppointments());
        }
    }

    /**
     *
     * @param event View appointments by month radio button
     */
    public void viewByMonth(ActionEvent event) {

        if (viewMonthRadioButton.isSelected()) {
            AppointmentTable.setItems(DBAppointments.getMonthlyAppointments());
        }
    }

    /**
     * View appointments by week radio button
     * @param event
     */
    public void viewByWeek(ActionEvent event) {

        ObservableList<Appointments> weeklyAppointments = FXCollections.observableArrayList();

        if (viewWeekRadioButton.isSelected()) {

            try {
                //Sql Select Query
                String databaseQuery = "SELECT * FROM appointments WHERE Week(start) = Week(now()) AND Week(end) = Week(now()) ORDER by Appointment_ID, start";

                //Create the Java statement
                Statement statement = JDBC.connection.createStatement();

                //Execute the query statement
                ResultSet rs = statement.executeQuery(databaseQuery);

                //Iterate through the java Resultset
                while (rs.next()) {
                    int aptID = rs.getInt("Appointment_ID");
                    String title = rs.getString("Title");
                    String description = rs.getString("Description");
                    String location = rs.getString("Location");
                    String type = rs.getString("Type");
                    LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                    LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                    int custID = rs.getInt("Customer_ID");
                    int usrID = rs.getInt("User_ID");
                    int contID = rs.getInt("Contact_ID");

                    weeklyAppointments.add(new Appointments(aptID, title, description, location, type, start, end, custID, usrID, contID));
                    AppointmentTable.setItems(weeklyAppointments);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
