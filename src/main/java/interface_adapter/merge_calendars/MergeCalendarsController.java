package interface_adapter.merge_calendars;

import entity.Calendar;
import use_case.merge_calendars.MergeCalendarsInputBoundary;
import use_case.merge_calendars.MergeCalendarsInputData;

import java.util.List;

public class MergeCalendarsController {
    private final MergeCalendarsInputBoundary mergeCalendarsUseCaseInteractor;

    public MergeCalendarsController(MergeCalendarsInputBoundary mergeCalendarsUseCaseInteractor) {
        this.mergeCalendarsUseCaseInteractor = mergeCalendarsUseCaseInteractor;
    }

    public void execute(List<Calendar> calendars, String date) {
        MergeCalendarsInputData inputData = new MergeCalendarsInputData(calendars, date);
        mergeCalendarsUseCaseInteractor.execute(inputData);
    }
}
