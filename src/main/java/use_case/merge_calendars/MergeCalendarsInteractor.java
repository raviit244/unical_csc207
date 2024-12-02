package use_case.merge_calendars;

import data_access.CalendarDataAccessObjectFactory;
import data_access.GetEventsDataAccessInterface;
import entity.Calendar;
import entity.Event;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MergeCalendarsInteractor implements MergeCalendarsInputBoundary {
    private final CalendarDataAccessObjectFactory calendarDataAccessObjectFactory;
    private final MergeCalendarsOutputBoundary mergeCalendarsPresenter;

    public MergeCalendarsInteractor(
            CalendarDataAccessObjectFactory calendarDataAccessObjectFactory,
            MergeCalendarsOutputBoundary mergeCalendarsPresenter) {
        this.calendarDataAccessObjectFactory = calendarDataAccessObjectFactory;
        this.mergeCalendarsPresenter = mergeCalendarsPresenter;
    }

    @Override
    public void execute(MergeCalendarsInputData inputData) {
        try {
            List<Event> allEvents = new ArrayList<>();
            List<Calendar> calendars = inputData.getCalendars();

            for (Calendar calendar : calendars) {
                GetEventsDataAccessInterface getEventsDataAccessObject =
                        (GetEventsDataAccessInterface) calendarDataAccessObjectFactory
                                .getCalendarDataAccessObject(calendar);

                List<Event> calendarEvents = getEventsDataAccessObject.fetchEventsMonth(
                        LocalDate.parse(inputData.getDate()));
                allEvents.addAll(calendarEvents);
            }

            // Sort events by date and time
            allEvents.sort((e1, e2) -> {
                int dateCompare = e1.getDate().compareTo(e2.getDate());
                if (dateCompare != 0) {
                    return dateCompare;
                }
                return e1.getStartTime().compareTo(e2.getStartTime());
            });

            MergeCalendarsOutputData outputData = new MergeCalendarsOutputData(calendars, allEvents, false);
            mergeCalendarsPresenter.prepareSuccessView(outputData);
        } catch (Exception e) {
            mergeCalendarsPresenter.prepareFailView("Error merging calendars: " + e.getMessage());
        }
    }
}