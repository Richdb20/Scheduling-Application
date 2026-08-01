package Database;

import Models.Appointments;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDateTime;

public class DBAppointments {


    public static ObservableList<Appointments> appointments = FXCollections.observableArrayList();

    //Query to get all Appointments
    public static ObservableList<Appointments> getAllAppointments() {

        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();

        try {
            //Sql Select Query
            String databaseQuery = "SELECT * FROM appointments";

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

                allAppointments.add(new Appointments(aptID, title, description, location, type, start, end, custID, usrID, contID));
            }
            statement.close();
        }
        catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return allAppointments;
    }

    //Query to get Monthly Appointments
    public static ObservableList<Appointments> getMonthlyAppointments() {

        ObservableList<Appointments> monthlyAppointments = FXCollections.observableArrayList();

            try {
                //Sql Select Query
                String databaseQuery = "SELECT * FROM appointments WHERE Month(start) = Month(now()) AND Month(end) = Month(now()) ORDER by Appointment_ID, start";

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

                    monthlyAppointments.add(new Appointments(aptID, title, description, location, type, start, end, custID, usrID, contID));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return monthlyAppointments;
    }

    //Query to create new Appointments
    public static void newAppointment(Appointments appointment) {

        try {
            //Sql Statement
            String query = "INSERT into appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID)" + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            //Prepared statement
            PreparedStatement pst = JDBC.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            //Add Values
            pst.setString(1, appointment.getTitle());
            pst.setString(2, appointment.getDescription());
            pst.setString(3, appointment.getLocation());
            pst.setString(4, appointment.getType());
            pst.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
            pst.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
            pst.setInt(7, appointment.getCustomerID());
            pst.setInt(8, appointment.getUserID());
            pst.setInt(9, appointment.getContactID());

            pst.execute();

            appointments.add(appointment);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Query to Modify existing Appointments
    public static void modifyAppointments(Appointments appointment) {

        try {
            //Sql Statement
            String query = "UPDATE Appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            //Prepared statement
            PreparedStatement pst = JDBC.connection.prepareStatement(query);
            //Add Values
            pst.setInt(10, appointment.getAppointmentID());
            pst.setString(1, appointment.getTitle());
            pst.setString(2, appointment.getDescription());
            pst.setString(3, appointment.getLocation());
            pst.setString(4, appointment.getType());
            pst.setTimestamp(5, Timestamp.valueOf(appointment.getStart()));
            pst.setTimestamp(6, Timestamp.valueOf(appointment.getEnd()));
            pst.setInt(7, appointment.getCustomerID());
            pst.setInt(8, appointment.getUserID());
            pst.setInt(9, appointment.getContactID());

            pst.execute();

            appointments.add(appointment);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Query to Delete existing Appointments
    public static void deleteAppointment(int Appointment_ID) {

        try {
            //Sql Statement
            String query = "DELETE FROM appointments WHERE Appointment_ID = ?";
            //Prepared statement
            PreparedStatement pst = JDBC.connection.prepareStatement(query);

            pst.setInt(1, Appointment_ID);
            pst.execute();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Query to get Appointments in the next 15 minutes for the current user
    public static ObservableList<Appointments> getUserUpcomingAppointments(int userID) {

        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();

        try {
            //Sql Select Query
            String databaseQuery = "SELECT * FROM appointments where User_ID = ? AND Start between now() AND date_add(now(), interval 15 minute)";

            //Create the Java statement
//            Statement statement = JDBC.connection.createStatement();
            PreparedStatement ps = JDBC.connection.prepareStatement(databaseQuery);
            ps.setInt(1,userID);
            //Execute the query statement
//            ResultSet rs = statement.executeQuery(databaseQuery);
                ResultSet rs = ps.executeQuery();
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

                allAppointments.add(new Appointments(aptID, title, description, location, type, start, end, custID, usrID, contID));
            }
            ps.close();
        }
        catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return allAppointments;
    }

    //Query to check for overlapping appointments
    public static ObservableList<Appointments> overlappingAppointments(int customerID) {

        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();

        try {
            String databaseQuery = "SELECT * FROM appointments WHERE Customer_ID = ?";

            PreparedStatement ps = JDBC.connection.prepareStatement(databaseQuery);
            ps.setInt(1,customerID);

            ResultSet rs = ps.executeQuery();

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

                allAppointments.add(new Appointments(aptID, title, description, location, type, start, end, custID, usrID, contID));
            }
            ps.close();
        }
        catch(SQLException throwables){
            throwables.printStackTrace();
        }
        return allAppointments;
    }
}
