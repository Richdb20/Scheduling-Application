package Controllers;

import Database.*;
import Models.Appointments;
import Models.Contacts;
import Models.Customers;
import Models.Users;
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
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;


public class ModifyAppointmentController implements Initializable {

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

    int appID;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        CustomerID.setItems(DBCustomers.getCustomers());
        ContactID.setItems(DBContacts.getContacts());
        UserID.setItems(DBUsers.getUsers());

        LocalTime start = LocalTime.of(8, 0);
        LocalTime end = LocalTime.of(22, 0);

        while (start.isBefore(end.plusSeconds(1))) {
            startTimeComboBox.getItems().add(start);
            endTimeComboBox.getItems().add(start);
            start = start.plusMinutes(15);

            startTimeComboBox.getSelectionModel().select(LocalTime.of(8,0));
            endTimeComboBox.getSelectionModel().select(LocalTime.of(22,0));
        }
    }

    /**
     *
     * @param appointmentID Appointment ID of selected Appointment
     * @throws IOException
     */
    public void selectedAppointment(int appointmentID) throws IOException {

        AppointmentID.setText(Integer.toString(appointmentID));

        try {
            //Sql Select Query
            String databaseQuery = "SELECT * FROM Appointments WHERE Appointment_ID=" + appointmentID;

            //Create the Java statement
            Statement statement = JDBC.connection.createStatement();

            //Execute the query statement
            ResultSet rs = statement.executeQuery(databaseQuery);

            while (rs.next()) {
                appID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                int contactID = rs.getInt("Contact_ID");

                Title.setText(title);
                Description.setText(description);
                Location.setText(location);
                Type.setText(type);
                datePicker.setValue(start.toLocalDate());
                startTimeComboBox.setValue(start.toLocalTime());
                endTimeComboBox.setValue(end.toLocalTime());
                CustomerID.setValue(DBCustomers.getCustomerID(customerID));
                UserID.setValue(DBUsers.getUserID(userID));
                ContactID.setValue(DBContacts.getContactID(contactID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param event Update Appointment in the Database
     * @throws IOException
     */
    public void saveUpdateAppointment(ActionEvent event) throws IOException {

        LocalDateTime startTime = LocalDateTime.of(datePicker.getValue(), startTimeComboBox.getValue());
        LocalDateTime endTime = LocalDateTime.of(datePicker.getValue(), endTimeComboBox.getValue());

        Customers customer = CustomerID.getValue();
        Users user = UserID.getValue();
        Contacts contact = ContactID.getValue();

        int appID = Integer.parseInt(AppointmentID.getText());

        if (startTime == null || endTime == null || endTime.isBefore(startTime)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Times");
            alert.setContentText("End time must be after start time.");
            alert.show();
            return;
        }

        if (Location.getText().isEmpty() || Title.getText().isEmpty() || Description.getText().isEmpty() || Type.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText("Fill all Fields");
            alert.setContentText("All Fields Must be Contain a Value");
            alert.showAndWait();
            return;
        }

        if (AddAppointmentController.checkOverlap(startTime, endTime, customer.getCustomerID(), appID)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Modify Appointment");
            alert.setContentText("There is an Appointment Overlap, Appointment can't be modified.");
            alert.showAndWait();
            return;
        }

        Appointments a = new Appointments (appID, Title.getText(), Description.getText(), Location.getText(), Type.getText(), startTime, endTime,
                                          CustomerID.getValue().getCustomerID(), UserID.getValue().getUserID(), ContactID.getValue().getContactID());

        DBAppointments.modifyAppointments(a);

        Parent parent = FXMLLoader.load(getClass().getResource("/Views/MainScreen.fxml"));
        Scene parentScene = new Scene(parent);
        Stage window = (Stage) SaveButton.getScene().getWindow();
        window.setScene(parentScene);
        window.show();
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

    public void appointmentDate(ActionEvent event) {

    }
}
