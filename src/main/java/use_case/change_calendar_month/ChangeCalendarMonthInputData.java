package use_case.change_calendar_month;

import entity.Calendar;
import java.util.List;

public class ChangeCalendarMonthInputData {
    private final List<Calendar> calendarList;
    private final String date;

    public ChangeCalendarMonthInputData(List<Calendar> calendarList, String date) {
        if (calendarList == null) {
            throw new NullPointerException("Calendar list cannot be null");
        }
        if (date == null) {
            throw new NullPointerException("Date cannot be null");
        }
        this.calendarList = calendarList;
        this.date = date;
    }

    public List<Calendar> getCalendarList() {
        return calendarList;
    }

    public String getDate() {
        return date;
    }
}