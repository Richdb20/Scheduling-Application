package Controllers;

import Database.DBAppointments;
import Database.JDBC;
import Models.Appointments;
import Models.Users;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class LoginController implements Initializable {

    @FXML private TextField userName;
    @FXML private TextField password;
    @FXML private Button loginButton;
    @FXML private Button exitButton;
    @FXML private Label  SchedulingAppLabel;
    @FXML private Label  loginPageLabel;
    @FXML private Label  usernameLabel;
    @FXML private Label  passwordLabel;
    @FXML private Label zoneID;
    private static String aTitle;
    private static String aHeader;
    private static String aContext;
    private static Users loggedinUser;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Locale locale = Locale.getDefault();
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId zoneId = ZoneId.systemDefault();
        zoneID.setText(zoneId.toString());

        ResourceBundle rb = ResourceBundle.getBundle("Languages/Language", Locale.getDefault());
        String scheduleLabel = null;
        String loginLabel = null;

        if (Locale.getDefault().getLanguage().equals("fr")) {
            scheduleLabel = rb.getString("Scheduling") + " " + rb.getString("Application");
            loginLabel = rb.getString("Login") + " " + rb.getString("Page");
            SchedulingAppLabel.setText(scheduleLabel);
            loginPageLabel.setText(loginLabel);
            usernameLabel.setText(rb.getString("Username"));
            passwordLabel.setText(rb.getString("Password"));
            loginButton.setText(rb.getString("Login"));
            exitButton.setText(rb.getString("Exit"));
            aTitle = rb.getString("aTitle");
            aHeader = rb.getString("aHeader");
            aContext = rb.getString("aContext");
        }
    }

    /**
     *
     * @param event Get username and password and login to the database
     * @throws SQLException
     * @throws IOException
     */
        public void login (ActionEvent event) throws SQLException, IOException {
            String usernameField, passwordField;
            usernameField = userName.getText();
            passwordField = password.getText();
            boolean verify = authenticate(usernameField, passwordField);

            if (verify) {
                Users.setNewUser(usernameField);
                appointmentNotify();
                LogUser(usernameField, true);
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/MainScreen.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) loginButton.getScene().getWindow();
                    stage.setTitle("Main Screen");
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                LogUser(usernameField, false);
            }
        }

    /**
     *
     * @param username
     * @param success
     */
    public static void LogUser(String username, boolean success) {

        Logger logText = Logger.getLogger("login_activity.txt");

        try {
            FileHandler fileHandler = new FileHandler("login_activity.txt", true);
            SimpleFormatter format = new SimpleFormatter();
            fileHandler.setFormatter(format);
            logText.addHandler(fileHandler);
            ZonedDateTime zoneDateTime = ZonedDateTime.now();

            logText.log(Level.INFO, "User=" + username + " Time of Login=" + zoneDateTime);
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param usernameField
     * @param passwordField
     * @return True or False, checks the username and password against the database
     * @throws SQLException
     */
        public static boolean authenticate(String usernameField, String passwordField) throws SQLException {

        if (Locale.getDefault().getLanguage().equals("en")) {
            try {
                JDBC.openConnection();
                PreparedStatement pst = JDBC.connection.prepareStatement("SELECT * FROM users WHERE user_Name=? AND password=?");
                pst.setString(1, usernameField);
                pst.setString(2, passwordField);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    int userId = rs.getInt("User_ID");
                    String userName =  rs.getString("User_Name");

                    loggedinUser = new Users(userId, userName);

                    return true;
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Username or Password Missing");
                    alert.setContentText("Please Enter Username & Password");
                    alert.showAndWait();
                    return false;
                }

            } catch (Exception e) {
                return false;
            }
        }
        else if (Locale.getDefault().getLanguage().equals("fr"))

            try {
                JDBC.openConnection();
                PreparedStatement pst = JDBC.connection.prepareStatement("SELECT * FROM users WHERE user_Name=? AND password=?");
                pst.setString(1, usernameField);
                pst.setString(2, passwordField);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    return true;
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle(aTitle);
                    alert.setHeaderText(aHeader);
                    alert.setContentText(aContext);
                    alert.showAndWait();
                    return false;
                }

            } catch (Exception e) {
                return false;
            }
        return false;
    }

    public static void appointmentNotify() {
        String message = "";

        ObservableList<Appointments> appointments = DBAppointments.getUserUpcomingAppointments(loggedinUser.getUserID());
        if (appointments.size() > 0) {
            for (Appointments app : appointments) {
                message = message + "Appointment ID:" + app.getAppointmentID() + " StartTime:" + app.getStart() + "\n";
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Appointment Notification");
            alert.setContentText("There is a scheduled Appointment within 15 minutes \n" + message);
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Appointment Notification");
            alert.setContentText("There are no scheduled Appointments." + message);
            alert.showAndWait();
        }
    }

    public void ExitApp(ActionEvent event) {

        System.exit(0);
    }
}
