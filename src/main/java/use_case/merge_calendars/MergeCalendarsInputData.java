package use_case.merge_calendars;

import entity.Calendar;

import java.util.List;

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
