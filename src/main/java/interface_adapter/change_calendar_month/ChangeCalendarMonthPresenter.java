package interface_adapter.change_calendar_month;

import use_case.change_calendar_month.ChangeCalendarMonthOutputBoundary;
import use_case.change_calendar_month.ChangeCalendarMonthOutputData;

/**
 * The presenter for Change Month Calendar Use Case.
 */
public class ChangeCalendarMonthPresenter implements ChangeCalendarMonthOutputBoundary {
    private final ChangeCalendarMonthViewModel viewModel;

    public ChangeCalendarMonthPresenter(ChangeCalendarMonthViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ChangeCalendarMonthOutputData outputData) {
        final ChangeCalendarMonthState state = viewModel.getState();
        state.setCurrCalendarList(outputData.getCalendarList());
        state.setCurrEvents(outputData.getEventList());
        state.setError("");
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final ChangeCalendarMonthState state = viewModel.getState();
        state.setError(error);
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}
