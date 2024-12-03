package data_access;

import java.time.LocalDate;
import java.util.ArrayList;

import entity.Event;

/**
 * Interface for fetching events from a calendar.
 */
public interface GetEventsDataAccessInterface {

    /**
     * Fetches all events from the calendar.
     *
     * @param date any day of the month
     * @return a list of events.
     */
    ArrayList<Event> fetchEventsMonth(LocalDate date);

    /**
     * Fetches all events from the calendar.
     *
     * @param date the date.
     * @return a list of events.
     */
    ArrayList<Event> fetchEventsDay(LocalDate date);
}

