package interface_adapter.merge_calendars;

import interface_adapter.change_calendar_month.ChangeCalendarMonthState;
import interface_adapter.change_calendar_month.ChangeCalendarMonthViewModel;
import use_case.merge_calendars.MergeCalendarsOutputBoundary;
import use_case.merge_calendars.MergeCalendarsOutputData;

/**
 * The presenter for Merge Calendar Use Case.
 */
public class MergeCalendarsPresenter implements MergeCalendarsOutputBoundary {
    private final ChangeCalendarMonthViewModel monthViewModel;

    public MergeCalendarsPresenter(ChangeCalendarMonthViewModel monthViewModel) {
        this.monthViewModel = monthViewModel;
    }

    @Override
    public void prepareSuccessView(MergeCalendarsOutputData outputData) {
        final ChangeCalendarMonthState state = monthViewModel.getState();
        state.setCurrCalendarList(outputData.getCalendars());
        state.setCurrEvents(outputData.getEvents());
        state.setMergedView(true);
        state.setError("");
        monthViewModel.setState(state);
        monthViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final ChangeCalendarMonthState state = monthViewModel.getState();
        state.setError(error);
        monthViewModel.setState(state);
        monthViewModel.firePropertyChanged();
    }
}
