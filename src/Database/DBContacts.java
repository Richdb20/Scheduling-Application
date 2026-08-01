package Database;

import Models.Appointments;
import Models.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

public class DBContacts {

    //Query to get all Contacts
    public static ObservableList<Contacts> getContacts() {

        ObservableList<Contacts> contacts = FXCollections.observableArrayList();

        String query = "SELECT * FROM contacts";

        try {
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("Contact_ID");
                String name = rs.getString("Contact_Name");
                String email = rs.getString("Email");
                Contacts c = new Contacts(id, name, email);

                contacts.add(c);
            }

        } catch (
                SQLException throwables) {
            throwables.printStackTrace();
        }
        return contacts;
    }

    //Query to get a contact ID
    public static Contacts getContactID (int contactID) {

        String query = "SELECT Contact_ID, Contact_Name FROM Contacts WHERE Contact_ID = " + contactID;
        int contactId = 0;
        String contactName;
        Contacts c = null;

        try {
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {

                contactId = rs.getInt("Contact_ID");
                contactName = rs.getString("Contact_Name");

                c = new Contacts(contactId, contactName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    //Query to get a Contacts Schedule from Contact ID
    public static ObservableList<Appointments> getContactSchedule (int contactID) {

        ObservableList<Appointments> ContactAppointments = FXCollections.observableArrayList();

        String query = "SELECT Appointment_ID, Title, Type, Description, Start, End, Customer_ID, Contact_ID FROM Appointments WHERE Contact_ID = " + contactID;
        int contactId = 0;
        Appointments a = null;

        try {
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {

                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String type = rs.getString("Type");
                String description = rs.getString("Description");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                contactId = rs.getInt("Contact_ID");

                ContactAppointments.add(new Appointments(appointmentId, title, type, description, start, end, customerId, contactId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ContactAppointments;
    }


    //Query to get all Contact Appointment information
    public static ObservableList<Appointments> getContactSchedule () {

        ObservableList<Appointments> ContactAppointments = FXCollections.observableArrayList();

        String query = "SELECT Appointment_ID, Title, Type, Description, Start, End, Customer_ID, Contact_ID FROM Appointments";
        int contactId = 0;
        Appointments a = null;

        try {
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {

                int appointmentId = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String type = rs.getString("Type");
                String description = rs.getString("Description");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerId = rs.getInt("Customer_ID");
                contactId = rs.getInt("Contact_ID");

                ContactAppointments.add(new Appointments(appointmentId, title, type, description, start, end, customerId, contactId));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ContactAppointments;
    }
}

