package Database;

import Models.Countries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBCountries {

    //Query to get all Countries
    public static ObservableList<Countries> getAllCountries() {

        ObservableList<Countries> countries = FXCollections.observableArrayList();

        String query = "SELECT * FROM countries";

        try {
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("Country_ID");
                String name = rs.getString("Country");
                Countries c = new Countries(id, name);

                countries.add(c);
            }

        } catch (
                SQLException throwables) {
            throwables.printStackTrace();
        }
        return countries;
    }

    //Query to get Country name
    public static Countries getCountryName (int countryID) {

        String query = "SELECT * FROM countries WHERE Country_ID = " + countryID;
        String name = "";
        Countries c = null;

        try {
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                name = rs.getString("Country");
                c = new Countries(name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    //Query to get Country ID
    public static int getCountryID (int divisionID) {

        String query = "SELECT * FROM first_level_divisions WHERE Division_ID = " + divisionID;
        int c = 0;
        try {
            Statement statement = JDBC.connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                c = rs.getInt("COUNTRY_ID");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }
}
