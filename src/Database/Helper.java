package Database;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.*;

//Helper class to convert Time Zones from the user local time to the database UTC time.
public class Helper {

    public static final LocalTime estStartBusinessHours = LocalTime.of(8,0);
    public static final LocalTime estEndBusinessHours = LocalTime.of(22,0);
    private static ObservableList<LocalTime> localBusinessHoursStart = FXCollections.observableArrayList();
    private static ObservableList<LocalTime> localBusinessHoursEnd = FXCollections.observableArrayList();


    private static void createBusinessHoursLists() {

        ZonedDateTime estStart = ZonedDateTime.of(LocalDate.now(), estStartBusinessHours, ZoneId.of("America/New_York"));
        ZonedDateTime estEnd = ZonedDateTime.of(LocalDate.now(), estEndBusinessHours, ZoneId.of("America/New_York"));
        ZonedDateTime start = estStart.withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime end = estEnd.withZoneSameInstant(ZoneId.systemDefault());

        while (start.isBefore(end)) {
            localBusinessHoursStart.add(start.toLocalTime());
            start = start.plusMinutes(15);
            localBusinessHoursEnd.add(start.toLocalTime());
        }
    }

    public static ObservableList<LocalTime> getLocalBusinessHoursStart() {
        if (localBusinessHoursStart.size() == 0) {
            createBusinessHoursLists();
        }
        return localBusinessHoursStart;
    }

    public static ObservableList<LocalTime> getLocalBusinessHoursEnd() {
        if (localBusinessHoursEnd.size() == 0) {
            createBusinessHoursLists();
        }
        return localBusinessHoursEnd;
    }
}
