package entity;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents an event in the system.
 */
public class Event {
    private final String eventName;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Calendar calendarApi;

    /**
     * Creates a new Event.
     * @param eventName The name of the event (non-null, non-empty)
     * @param date The date of the event (non-null)
     * @param startTime The start time of the event (non-null)
     * @param endTime The end time of the event (non-null, must be after startTime)
     * @param calendarApi The calendar this event belongs to (non-null)
     * @throws IllegalArgumentException if eventName is empty or endTime is before startTime
     * @throws NullPointerException if any parameter is null
     */
    public Event(String eventName, LocalDate date, LocalTime startTime, LocalTime endTime, Calendar calendarApi) {
        if (eventName == null || date == null || startTime == null || endTime == null || calendarApi == null) {
            throw new NullPointerException("All parameters must be non-null");
        }
        if (eventName.trim().isEmpty()) {
            throw new IllegalArgumentException("Event name cannot be empty");
        }
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("End time cannot be before start time");
        }

        this.eventName = eventName.trim();
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.calendarApi = calendarApi;
    }

    public String getEventName() {
        return eventName;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Calendar getCalendarApi() {
        return calendarApi;
    }
}
