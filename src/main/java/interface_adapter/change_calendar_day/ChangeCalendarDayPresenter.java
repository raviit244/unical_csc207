package interface_adapter.change_calendar_day;

import java.time.LocalTime;
import java.util.List;

import entity.Event;
import use_case.change_calendar_day.ChangeCalendarDayOutputBoundary;
import use_case.change_calendar_day.ChangeCalendarDayOutputData;

/**
 * The presenter for Change Day Calendar Use Case.
 */
public class ChangeCalendarDayPresenter implements ChangeCalendarDayOutputBoundary {
    private final ChangeCalendarDayViewModel changeCalendarDayViewModel;

    public ChangeCalendarDayPresenter(ChangeCalendarDayViewModel viewModel) {
        this.changeCalendarDayViewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ChangeCalendarDayOutputData outputData) {
        final ChangeCalendarDayState state = new ChangeCalendarDayState();
        state.setCalendarList(outputData.getCalendarList());
        state.setEventList(outputData.getEventList());
        state.setUseCaseFailed(false);
        state.setError("");

        // Sort events by time
        sortEvents(state.getEventList());

        changeCalendarDayViewModel.setState(state);
        changeCalendarDayViewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        final ChangeCalendarDayState state = new ChangeCalendarDayState();
        state.setError(error);
        state.setUseCaseFailed(true);
        changeCalendarDayViewModel.setState(state);
        changeCalendarDayViewModel.firePropertyChanged();
    }

    private void sortEvents(List<Event> events) {
        events.sort((eventOne, eventTwo) -> {
            // First compare by date
            final int dateCompare = eventOne.getDate().compareTo(eventTwo.getDate());
            if (dateCompare != 0) {
                return dateCompare;
            }

            // If dates are equal, compare by start time
            final LocalTime time1 = eventOne.getStartTime();
            final LocalTime time2 = eventTwo.getStartTime();
            final int timeCompare = time1.compareTo(time2);
            if (timeCompare != 0) {
                return timeCompare;
            }

            // If start times are equal, compare by end time
            final int endTimeCompare = eventOne.getEndTime().compareTo(eventTwo.getEndTime());
            if (endTimeCompare != 0) {
                return endTimeCompare;
            }

            // If all times are equal, sort by event name
            return eventOne.getEventName().compareTo(eventTwo.getEventName());
        });
    }
}
