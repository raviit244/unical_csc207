package use_case.merge_calendars;

public interface MergeCalendarsOutputBoundary {
    void prepareSuccessView(MergeCalendarsOutputData mergeCalendarsOutputData);
    void prepareFailView(String error);
}