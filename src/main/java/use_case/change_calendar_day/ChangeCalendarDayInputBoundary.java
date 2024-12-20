package use_case.change_calendar_day;

/**
 * Input Boundary for actions which are related to Changing the Month Calendar.
 */
public interface ChangeCalendarDayInputBoundary {
    /**
     * Executes the login use case.
     * @param changeCalendarDayInputData the input data
     */
    void execute(ChangeCalendarDayInputData changeCalendarDayInputData);
}
