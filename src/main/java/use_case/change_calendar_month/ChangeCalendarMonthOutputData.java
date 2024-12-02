package use_case.change_calendar_month;

import entity.Calendar;
import entity.Event;
import java.util.List;

public class ChangeCalendarMonthOutputData {
    private final List<Calendar> calendarList;
    private final List<Event> eventList;

    public ChangeCalendarMonthOutputData(List<Calendar> calendarList, List<Event> eventList) {
        if (calendarList == null) {
            throw new NullPointerException("Calendar list cannot be null");
        }
        if (eventList == null) {
            throw new NullPointerException("Event list cannot be null");
        }
        this.calendarList = calendarList;
        this.eventList = eventList;
    }

    public List<Calendar> getCalendarList() {
        return calendarList;
    }

    public List<Event> getEventList() {
        return eventList;
    }
}
