package use_case.change_calendar_day;

import java.time.LocalDate;
import java.util.ArrayList;

import data_access.CalendarDataAccessObjectFactory;
import data_access.GetEventsDataAccessInterface;
import entity.Calendar;
import entity.Event;

/**
 * Use-case interactor for the Calendar Day View use-case.
 */
public class ChangeCalendarDayInteractor implements ChangeCalendarDayInputBoundary {
    private final CalendarDataAccessObjectFactory calendarDataAccessObjectFactory;
    private final ChangeCalendarDayOutputBoundary changeCalendarDayPresenter;

    public ChangeCalendarDayInteractor(
            CalendarDataAccessObjectFactory calendarDataAccessObjectFactory,
            ChangeCalendarDayOutputBoundary changeCalendarDayPresenter) {
        this.calendarDataAccessObjectFactory = calendarDataAccessObjectFactory;
        this.changeCalendarDayPresenter = changeCalendarDayPresenter;
    }

    @Override
    public void execute(ChangeCalendarDayInputData inputData) {
        try {
            final ArrayList<Event> events = new ArrayList<>();
            final ArrayList<Calendar> calendars = inputData.getCalendarList();

            for (Calendar calendar : calendars) {
                final GetEventsDataAccessInterface getEventsDataAccessObject =
                        (GetEventsDataAccessInterface) calendarDataAccessObjectFactory
                                .getCalendarDataAccessObject(calendar);

                events.addAll(getEventsDataAccessObject.fetchEventsDay(LocalDate.parse(inputData.getDate())));
            }

            final ChangeCalendarDayOutputData outputData = new ChangeCalendarDayOutputData(
                    calendars, events, false);
            changeCalendarDayPresenter.prepareSuccessView(outputData);
        }
        catch (Exception exception) {
            changeCalendarDayPresenter.prepareFailView("Error fetching calendar data: " + exception.getMessage());
        }
    }
}
