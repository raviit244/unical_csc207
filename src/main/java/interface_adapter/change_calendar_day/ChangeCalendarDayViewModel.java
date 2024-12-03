package interface_adapter.change_calendar_day;

import java.time.LocalDate;

import entity.Event;
import interface_adapter.ViewModel;

/**
 * The view model for Change Day Calendar Use Case.
 */
public class ChangeCalendarDayViewModel extends ViewModel<ChangeCalendarDayState> {
    public static final String TITLE_LABEL = "Day View";
    public static final String ADD_EVENT_BUTTON_LABEL = "Add Event";
    public static final String DELETE_EVENT_BUTTON_LABEL = "Delete Event";
    public static final String BACK_TO_MONTH_BUTTON_LABEL = "Back to Month View";
    public static final String NO_EVENTS_MESSAGE = "No events for this day";
    public static final String ERROR_NO_SELECTION = "Please select an event";
    public static final String DELETE_CONFIRM_MESSAGE = "Delete selected event?";

    public ChangeCalendarDayViewModel() {
        super("calendar_day");
        setState(new ChangeCalendarDayState());
    }

    // Add these methods to handle event modifications
    /**
     * Adds Event.
     *
     * @param event Event to be Added
     */
    public void addEvent(Event event) {
        final ChangeCalendarDayState state = getState();
        state.addEvent(event);
        setState(state);
        firePropertyChanged();
    }

    /**
     * Removes Event.
     *
     * @param event Event to be Removed
     */
    public void removeEvent(Event event) {
        final ChangeCalendarDayState state = getState();
        state.removeEvent(event);
        setState(state);
        firePropertyChanged();
    }

    // Additional convenience methods for state management
    /**
     * Checks if Events are present.
     * @return if Has Event.
     */
    public boolean hasEvents() {
        return getState().hasEvents();
    }

    public int getEventCount() {
        return getState().getEventCount();
    }

    /**
     * Edits the Current Date.
     *
     * @param date Current Date
     */
    public void setCurrentDate(LocalDate date) {
        ChangeCalendarDayState state = getState();
        state = new ChangeCalendarDayState(state);
        state.setDate(date);
        setState(state);
        firePropertyChanged();
    }

    public LocalDate getCurrentDate() {
        return getState().getDate();
    }
}
