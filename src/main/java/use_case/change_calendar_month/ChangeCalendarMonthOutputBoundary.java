package use_case.change_calendar_month;

/**
 * The output boundary for the Login Use Case.
 */
public interface ChangeCalendarMonthOutputBoundary {
    /**
     * Prepares the success view for the Login Use Case.
     * @param changeCalendarMonthOutputData the output data
     */
    void prepareSuccessView(ChangeCalendarMonthOutputData changeCalendarMonthOutputData);

    /**
     * Prepares the fail view for the Login Use Case.
     * @param error the error generated that is causing us to prepare the fail view
     */
    void prepareFailView(String error);
}
