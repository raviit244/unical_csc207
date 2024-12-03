package use_case.merge_calendars;

import java.util.List;

import entity.Calendar;
import entity.Event;

/**
 * Output Data for the Merge Calendars use-case.
 */
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
