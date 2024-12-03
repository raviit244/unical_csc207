package use_case.change_calendar_day;

import java.util.ArrayList;

import entity.Calendar;
import entity.Event;

/**
 * Output Data for the Calendar Day View use-case.
 */
public class ChangeCalendarDayOutputData {
    private final ArrayList<Calendar> calendarList;
    private final ArrayList<Event> eventList;
    private final boolean useCaseFailed;

    public ChangeCalendarDayOutputData(ArrayList<Calendar> calendarList,
                                       ArrayList<Event> eventList,
                                       boolean useCaseFailed) {
        this.calendarList = new ArrayList<>(calendarList);
        this.eventList = new ArrayList<>(eventList);
        this.useCaseFailed = useCaseFailed;
    }

    public ArrayList<Calendar> getCalendarList() {
        return new ArrayList<>(calendarList);
    }

    public ArrayList<Event> getEventList() {
        return new ArrayList<>(eventList);
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
