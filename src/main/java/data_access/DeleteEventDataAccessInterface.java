package data_access;

import entity.Event;

/**
 * Interface for deleting events from a calendar.
 */
public interface DeleteEventDataAccessInterface {
    /**
     * Deletes an event from the calendar.
     *
     * @param event the event to delete.
     * @return true if the operation was successful, false otherwise.
     */
    boolean deleteEvent(Event event);
}
