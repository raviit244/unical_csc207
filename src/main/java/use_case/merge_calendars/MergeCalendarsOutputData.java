package use_case.merge_calendars;

import entity.Calendar;
import entity.Event;

import java.util.List;

public class MergeCalendarsOutputData {
    private final List<Calendar> calendars;
    private final List<Event> events;
    private final boolean useCaseFailed;

    public MergeCalendarsOutputData(List<Calendar> calendars, List<Event> events, boolean useCaseFailed) {
        this.calendars = calendars;
        this.events = events;
        this.useCaseFailed = useCaseFailed;
    }

    public List<Calendar> getCalendars() {
        return calendars;
    }

    public List<Event> getEvents() {
        return events;
    }

    public boolean isUseCaseFailed() {
        return useCaseFailed;
    }
}
