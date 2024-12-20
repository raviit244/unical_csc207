package use_case.merge_calendars;

import java.util.List;

import entity.Calendar;

/**
 * Input Data for the Merge Calendars use-case.
 */
public class MergeCalendarsInputData {
    private final List<Calendar> calendars;
    private final String date;

    public MergeCalendarsInputData(List<Calendar> calendars, String date) {
        this.calendars = calendars;
        this.date = date;
    }

    public List<Calendar> getCalendars() {
        return calendars;
    }

    public String getDate() {
        return date;
    }
}
