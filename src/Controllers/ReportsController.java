package Controllers;

import Database.DBContacts;
import Database.DBReports;
import Database.Generate;
import Models.Appointments;
import Models.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ReportsController implements Initializable {

    @FXML private ComboBox<String> MonthComboBox;
    @FXML private ComboBox<String> TypeComboBox;
    @FXML private ComboBox<Contacts> ScheduleComboBox;
    @FXML private ComboBox<Contacts> ContactComboBox;
    @FXML private TableView<Appointments> ReportTableOne;
    @FXML private TableColumn<Appointments, Integer> AppointmentID;
    @FXML private TableColumn<Appointments, String> Title;
    @FXML private TableColumn<Appointments, String> Type;
    @FXML private TableColumn<Appointments, String> Description;
    @FXML private TableColumn<Appointments, String> StartDate;
    @FXML private TableColumn<Appointments, String> EndDate;
    @FXML private TableColumn<Appointments, Integer> CustomerID;
    @FXML private TableColumn<Appointments, Integer> contactID;
    @FXML private TableView<Contacts> ReportTableTwo;
    @FXML private TableColumn<Contacts, Integer> ContactID;
    @FXML private TableColumn<Contacts, String> ContactName;
    @FXML private TableColumn<Contacts, String> ContactEmail;
    @FXML private Label ValueLabel;
    @FXML private Button ExitButton;

    //Observable list of all months to load into combo box.
    private static final ObservableList<String> allMonths = FXCollections.observableArrayList("January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December");

    public static ObservableList<String> getAllMonths() {
        return allMonths;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        MonthComboBox.setItems(getAllMonths());
        TypeComboBox.setItems(DBReports.getAppointmentType());
        ScheduleComboBox.setItems(DBContacts.getContacts());
        ContactComboBox.setItems(DBContacts.getContacts());

        ReportTableOne.setItems(DBContacts.getContactSchedule());

        AppointmentID.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        Title.setCellValueFactory(new PropertyValueFactory<>("title"));
        Type.setCellValueFactory(new PropertyValueFactory<>("type"));
        Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        StartDate.setCellValueFactory(new PropertyValueFactory<>("start"));
        EndDate.setCellValueFactory(new PropertyValueFactory<>("end"));
        CustomerID.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        contactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));

        ContactID.setCellValueFactory(new PropertyValueFactory<>("contactID"));
        ContactName.setCellValueFactory(new PropertyValueFactory<>("contactName"));
        ContactEmail.setCellValueFactory(new PropertyValueFactory<>("contactEmail"));
    }

    /**
     * Lambda expression used for faster and cleaner code.
     * @param event get appointments by type for selected month.
     */
    public void onGenerate(ActionEvent event) {

        final String  month = MonthComboBox.getValue();
        final String  type = TypeComboBox.getValue();

        if (month == null || type == null) {
            return;
        }

        Generate g = () -> {

            int count = DBReports.getAppointmentMonth(month, type);
            ValueLabel.setText(String.valueOf(count));
        };
        g.generate();
    }

    public void onContactSelected (ActionEvent event) {

        int contactId = ScheduleComboBox.getValue().getContactID();

        ObservableList<Appointments> filteredAppointments = DBContacts.getContactSchedule(contactId);

        ReportTableOne.setItems(filteredAppointments);
    }

    /**
     * Lambda expression used within method to filter contactID through all contacts
     * @param event filter through allContacts and get contact based on contactID
     */
    public void onViewContactSelected (ActionEvent event) {

        int contactId = ContactComboBox.getValue().getContactID();

        ObservableList<Contacts> allContacts = DBContacts.getContacts();

        //Lambda Expression to filter through all contacts.
        ObservableList<Contacts> filteredContacts = allContacts.filtered(c -> {
        if (c.getContactID() == contactId)
                return true;
            return false;
        });
        ReportTableTwo.setItems(filteredContacts);
    }

    /**
     *
     * @param event Return to main screen
     * @throws IOException
     */
    public void exitToMain(ActionEvent event) throws IOException {
        Stage stage = (Stage) ExitButton.getScene().getWindow();
        stage.close();
    }
}
