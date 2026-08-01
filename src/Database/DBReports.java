package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBReports {

    //Query to get Appointments by type
    public static ObservableList<String> getAppointmentType() {

        ObservableList<String> appointmentTypes = FXCollections.observableArrayList();

        try {
            //Sql Select Query
            String databaseQuery = "SELECT DISTINCT Type FROM appointments";

            //Create the Java statement
            Statement statement = JDBC.connection.createStatement();

            //Execute the query statement
            ResultSet rs = statement.executeQuery(databaseQuery);

            //Iterate through the java Resultset
            while (rs.next()) {

                String type = rs.getString("type");

                appointmentTypes.add(type);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointmentTypes;
    }

    //Query to get appointments by Month
    public static int getAppointmentMonth(String monthName, String appType) {

        int appCount = 0;

        try {
            //Sql Select Query
            String databaseQuery = "SELECT COUNT(*) AS CNT FROM appointments WHERE monthname(start) = ? AND Type = ?";

            PreparedStatement pst = JDBC.connection.prepareStatement(databaseQuery);

            pst.setString(1, monthName);
            pst.setString(2, appType);
            ResultSet rs = pst.executeQuery();

            //Iterate through the java Resultset
            while (rs.next()) {

                appCount = rs.getInt("CNT");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appCount;
    }
}
