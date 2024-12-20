package use_case.change_calendar_month;

/**
 * Input Boundary for actions which are related to Changing the Month Calendar.
 */
public interface ChangeCalendarMonthInputBoundary {
    /**
     * Executes the login use case.
     * @param changeCalendarMonthInputData the input data
     */
    void execute(ChangeCalendarMonthInputData changeCalendarMonthInputData);
}
