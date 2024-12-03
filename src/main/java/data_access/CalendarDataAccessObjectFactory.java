package data_access;

import entity.Calendar;
import entity.GoogleCalendar;
import entity.NotionCalendar;

/**
 * Factory for creating the appropriate Calendar Data Access Object based on the Calendar type.
 */
public class CalendarDataAccessObjectFactory {

    /**
     * Returns the appropriate Data Access Object for the given Calendar entity.
     *
     * @param calendar the Calendar entity
     * @return the corresponding Calendar DAO
     * @throws IllegalArgumentException if the Calendar type is not supported
     */
    public Object getCalendarDataAccessObject(Calendar calendar) {
        Object dataAccessObject = null;
        switch (calendar.getCalendarApiName()) {
            case "GoogleCalendar":
                dataAccessObject = new GoogleCalendarDataAccessObject((GoogleCalendar) calendar);
                break;
            case "NotionCalendar":
                dataAccessObject = new NotionCalendarDataAccessObject((NotionCalendar) calendar);
                break;
            default:
                throw new IllegalArgumentException("Unsupported calendar type: " + calendar.getCalendarApiName());
        }
        return dataAccessObject;
    }
}

