package use_case.merge_calendars;

/**
 * Output Boundary interface for the Merge Calendars use-case.
 */
public interface MergeCalendarsOutputBoundary {
    /**
     * Prepares the success view for the Merge Calendars use-case.
     * @param mergeCalendarsOutputData the output data from the Merge Calendars use-case
     */
    void prepareSuccessView(MergeCalendarsOutputData mergeCalendarsOutputData);

    /**
     * Prepares the fail view for the Merge Calendars use-case.
     * @param error the error message
     */
    void prepareFailView(String error);
}
