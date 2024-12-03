package use_case.change_calendar_day;

import java.util.ArrayList;

import entity.Calendar;

/**
 * Input Data for the Calendar Day View use-case.
 */
public class ChangeCalendarDayInputData {
    private final ArrayList<Calendar> calendarList;
    private final String date;

    public ChangeCalendarDayInputData(ArrayList<Calendar> calendarList, String date) {
        if (calendarList == null) {
            throw new NullPointerException("Calendar list cannot be null");
        }
        if (date == null) {
            throw new NullPointerException("Date cannot be null");
        }
        this.calendarList = new ArrayList<>(calendarList);
        this.date = date;
    }

    public ArrayList<Calendar> getCalendarList() {
        return new ArrayList<>(calendarList);
    }

    public String getDate() {
        return date;
    }
}
