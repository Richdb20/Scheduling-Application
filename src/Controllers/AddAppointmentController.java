package Controllers;

import Database.*;
import Models.Appointments;
import Models.Contacts;
import Models.Customers;
import Models.Users;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;


public class AddAppointmentController implements Initializable {

    @FXML private TextField AppointmentID;
    @FXML private ComboBox<Customers> CustomerID;
    @FXML private ComboBox<Users> UserID;
    @FXML private TextField Location;
    @FXML private ComboBox<Contacts> ContactID;
    @FXML private Button SaveButton;
    @FXML private Button ExitButton;
    @FXML private TextField Title;
    @FXML private TextField Description;
    @FXML private TextField Type;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<LocalTime> startTimeComboBox;
    @FXML private ComboBox<LocalTime> endTimeComboBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CustomerID.setItems(DBCustomers.getCustomers());
        ContactID.setItems(DBContacts.getContacts());
        UserID.setItems(DBUsers.getUsers());

        startTimeComboBox.setItems(Helper.getLocalBusinessHoursStart());
        endTimeComboBox.setItems(Helper.getLocalBusinessHoursEnd());
    }

    /**
     *
     * @param event Save new Appointment
     * @throws IOException
     */
    public void saveNewAppointment(ActionEvent event) throws IOException {

        try {
            LocalTime start = startTimeComboBox.getValue();
            LocalTime end = endTimeComboBox.getValue();
            LocalDate appointmentDate = datePicker.getValue();

            if (start == null || end == null || end.isBefore(start)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Times");
                alert.setContentText("End time must be after start time.");
                alert.show();
                return;
            }

            if (appointmentDate == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Date");
                alert.setContentText("Please pick a Date.");
                alert.show();
                return;
            }

            Customers customer = CustomerID.getValue();
            Users user = UserID.getValue();
            Contacts contact = ContactID.getValue();

            if (customer == null || user == null || contact == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Entries");
                alert.setContentText("Customer, Contact, and User are Required, Please select.");
                alert.show();
                return;
            }

            LocalDateTime startTime = LocalDateTime.of(appointmentDate, (LocalTime) startTimeComboBox.getSelectionModel().getSelectedItem());
            LocalDateTime endTime = LocalDateTime.of(appointmentDate, (LocalTime) endTimeComboBox.getSelectionModel().getSelectedItem());

            String title = Title.getText();
            String description = Description.getText();
            String location = Location.getText();
            String type = Type.getText();

            if (Location.getText().isEmpty() || Title.getText().isEmpty() || Description.getText().isEmpty() || Type.getText().isEmpty()) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText("Fill all Fields");
                alert.setContentText("All Fields Must be Contain a Value");
                alert.showAndWait();
                return;
            }

            if (checkOverlap(startTime, endTime, customer.getCustomerID(), -1)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Add Appointment");
                alert.setContentText("There is an Appointment Overlap, Appointment can't be added.");
                alert.showAndWait();
                return;
            }

            DBAppointments.newAppointment(new Appointments(title, description, location, type, startTime, endTime, customer.getCustomerID(), user.getUserID(), contact.getContactID()));

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
     * @param start
     * @param end
     * @param customerID
     * @param appID
     * @return Check for Overlapping Appointments
     */
    public static boolean checkOverlap(LocalDateTime start, LocalDateTime end, int customerID, int appID) {

        ObservableList<Appointments> list = DBAppointments.overlappingAppointments(customerID);

        if (list.size() == 0) {
            return false;
        }

        for (Appointments a : list) {
            if (a.getAppointmentID() == appID)
                continue;
            if (start.equals(a.getStart()) || end.equals(a.getEnd()))
                return true;
            else if (start.isBefore(a.getStart()) && end.isAfter(a.getEnd()))
                return true;
            else if (start.isAfter(a.getStart()) && start.isBefore(a.getEnd()))
                return true;
            else if (end.isAfter(a.getStart()) && end.isBefore(a.getEnd()))
                return true;
            else if (start.isAfter(a.getStart()) && end.isBefore(a.getEnd()))
                return true;
        }
        return false;
    }

    /**
     *
     * @param event Return to the main screen
     * @throws IOException
     */
    public void exitToMain(ActionEvent event) throws IOException {

        Parent parent = FXMLLoader.load(getClass().getResource("/Views/MainScreen.fxml"));
        Scene parentScene = new Scene(parent);
        Stage window = (Stage) ExitButton.getScene().getWindow();
        window.setScene(parentScene);
        window.show();
    }

    public void appointmentDate(ActionEvent event) {

    }
}
