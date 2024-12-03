package use_case.merge_calendars;

/**
 * Input Boundary for the Merge Calendars use-case.
 */
public interface MergeCalendarsInputBoundary {
    /**
     * Execute method for the Merge Calendars use-case.
     * @param mergeCalendarsInputData input data for the Merge Calendars use-case.
     */
    void execute(MergeCalendarsInputData mergeCalendarsInputData);
}
