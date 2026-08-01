package Database;

import Models.Divisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DBDivisions {

    //Query to get all Division parameters
    public static ObservableList<Divisions> getDivision() {

        ObservableList<Divisions> states = FXCollections.observableArrayList();

        String query = "SELECT * FROM first_level_divisions";

        try {
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                String name = rs.getString("Division");
                int id = rs.getInt("Division_ID");
                int countryId = rs.getInt("COUNTRY_ID");
                Divisions d = new Divisions(id, name, countryId);

                states.add(d);
            }
        } catch (
                SQLException e) {
            e.printStackTrace();
        }
        return states;
    }

    //Query to get Division Name
    public static Divisions getDivisionName (int divisionID) {

        String query = "SELECT * FROM first_level_divisions WHERE Division_ID = " + divisionID;
        String name = "";
        int id = 0;
        int country = 0;
        Divisions d = null;

        try {
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                name = rs.getString("Division");
                id = rs.getInt("Division_ID");
                country = rs.getInt("COUNTRY_ID");
                d = new Divisions(id, name, country);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }
}
