package use_case.change_calendar_month;

import data_access.CalendarDataAccessObjectFactory;
import data_access.GetEventsDataAccessInterface;
import entity.Calendar;
import entity.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChangeCalendarMonthInteractorTest {
    private ChangeCalendarMonthInteractor interactor;
    private CalendarDataAccessObjectFactory mockFactory;
    private ChangeCalendarMonthOutputBoundary mockPresenter;
    private GetEventsDataAccessInterface mockDAO;

    @BeforeEach
    void setUp() {
        mockFactory = mock(CalendarDataAccessObjectFactory.class);
        mockPresenter = mock(ChangeCalendarMonthOutputBoundary.class);
        mockDAO = mock(GetEventsDataAccessInterface.class);

        when(mockFactory.getCalendarDataAccessObject(any(Calendar.class)))
                .thenReturn(mockDAO);

        interactor = new ChangeCalendarMonthInteractor(mockFactory, mockPresenter);
    }

    @Test
    void execute_SuccessfulFetch() {
        List<Calendar> calendarList = new ArrayList<>();
        Calendar mockCalendar = mock(Calendar.class);
        calendarList.add(mockCalendar);
        String date = "2024-01-01";

        ArrayList<Event> events = new ArrayList<>();
        events.add(mock(Event.class));
        when(mockDAO.fetchEventsMonth(any(LocalDate.class))).thenReturn(events);

        ChangeCalendarMonthInputData inputData = new ChangeCalendarMonthInputData(calendarList, date);
        interactor.execute(inputData);

        verify(mockDAO).fetchEventsMonth(LocalDate.parse(date));
        verify(mockPresenter).prepareSuccessView(any(ChangeCalendarMonthOutputData.class));
        verify(mockPresenter, never()).prepareFailView(anyString());
    }

    @Test
    void execute_EmptyCalendarList_SucceedsWithNoEvents() {
        List<Calendar> calendarList = new ArrayList<>();
        String date = "2024-01-01";

        ChangeCalendarMonthInputData inputData = new ChangeCalendarMonthInputData(calendarList, date);
        interactor.execute(inputData);

        verify(mockDAO, never()).fetchEventsMonth(any(LocalDate.class));
        verify(mockPresenter).prepareSuccessView(any(ChangeCalendarMonthOutputData.class));
        verify(mockPresenter, never()).prepareFailView(anyString());
    }

    @Test
    void execute_InvalidDateFormat_CallsFailView() {
        List<Calendar> calendarList = new ArrayList<>();
        calendarList.add(mock(Calendar.class));
        String invalidDate = "invalid-date";

        ChangeCalendarMonthInputData inputData = new ChangeCalendarMonthInputData(calendarList, invalidDate);
        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView(contains("Error fetching calendar data"));
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_DAOThrowsException_CallsFailView() {
        List<Calendar> calendarList = new ArrayList<>();
        Calendar mockCalendar = mock(Calendar.class);
        calendarList.add(mockCalendar);
        String date = "2024-01-01";

        when(mockDAO.fetchEventsMonth(any(LocalDate.class)))
                .thenThrow(new RuntimeException("DAO error"));

        ChangeCalendarMonthInputData inputData = new ChangeCalendarMonthInputData(calendarList, date);
        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView(contains("Error fetching calendar data"));
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_MultipleCalendars_MergesEvents() {
        List<Calendar> calendarList = new ArrayList<>();
        calendarList.add(mock(Calendar.class));
        calendarList.add(mock(Calendar.class));
        String date = "2024-01-01";

        ArrayList<Event> events1 = new ArrayList<>();
        events1.add(mock(Event.class));
        ArrayList<Event> events2 = new ArrayList<>();
        events2.add(mock(Event.class));

        when(mockDAO.fetchEventsMonth(any(LocalDate.class)))
                .thenReturn(events1)
                .thenReturn(events2);

        ChangeCalendarMonthInputData inputData = new ChangeCalendarMonthInputData(calendarList, date);
        interactor.execute(inputData);

        verify(mockDAO, times(2)).fetchEventsMonth(LocalDate.parse(date));
        verify(mockPresenter).prepareSuccessView(any(ChangeCalendarMonthOutputData.class));
        verify(mockPresenter, never()).prepareFailView(anyString());
    }
}