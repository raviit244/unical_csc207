package entity;

/**
 * The representation of a user in our program.
 */
public interface User {

    /**
     * Returns the username of the user.
     * @return the username of the user.
     */
    String getName();

    /**
     * Returns the password of the user.
     * @return the password of the user.
     */
    String getPassword();

    /**
     * Adds a calendar to the user's list of calendars.
     * @param calendar the calendar to be added
     */
    void addCalendar(Calendar calendar);
}
