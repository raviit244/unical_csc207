package interface_adapter.change_calendar_day;

import java.util.ArrayList;
import java.util.List;

import entity.Calendar;
import use_case.change_calendar_day.ChangeCalendarDayInputBoundary;
import use_case.change_calendar_day.ChangeCalendarDayInputData;

/**
 * The controller for Change Day Calendar Use Case.
 */
public class ChangeCalendarDayController {
    private final ChangeCalendarDayInputBoundary changeCalendarDayUseCaseInteractor;

    public ChangeCalendarDayController(ChangeCalendarDayInputBoundary changeCalendarDayUseCaseInteractor) {
        this.changeCalendarDayUseCaseInteractor = changeCalendarDayUseCaseInteractor;
    }

    /**
     * Executes the change calendar day use case with the given calendars and date.
     *
     * @param calendarList list of calendars to fetch events from
     * @param date         the date in YYYY-MM-DD format
     */
    public void execute(List<Calendar> calendarList, String date) {
        final ChangeCalendarDayInputData inputData = new ChangeCalendarDayInputData(
                new ArrayList<>(calendarList),
                date
        );

        changeCalendarDayUseCaseInteractor.execute(inputData);
    }

    /**
     * Convenience method for single calendar execution.
     * @param calendar calendar to be used.
     * @param date date of the events to be fetched.
     */
    public void execute(Calendar calendar, String date) {
        final ArrayList<Calendar> calendars = new ArrayList<>();
        calendars.add(calendar);
        execute(calendars, date);
    }
}
