package interface_adapter.merge_calendars;

import java.util.List;

import entity.Calendar;
import use_case.merge_calendars.MergeCalendarsInputBoundary;
import use_case.merge_calendars.MergeCalendarsInputData;

/**
 * The controller for Merge Calendar Use Case.
 */
public class MergeCalendarsController {
    private final MergeCalendarsInputBoundary mergeCalendarsUseCaseInteractor;

    public MergeCalendarsController(MergeCalendarsInputBoundary mergeCalendarsUseCaseInteractor) {
        this.mergeCalendarsUseCaseInteractor = mergeCalendarsUseCaseInteractor;
    }

    /**
     * Executes the change calendar day use case with the given calendars and date.
     *
     * @param calendars list of calendars to fetch events from
     * @param date Date
     */
    public void execute(List<Calendar> calendars, String date) {
        final MergeCalendarsInputData inputData = new MergeCalendarsInputData(calendars, date);
        mergeCalendarsUseCaseInteractor.execute(inputData);
    }
}
