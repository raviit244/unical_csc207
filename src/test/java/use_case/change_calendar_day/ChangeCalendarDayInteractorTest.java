package use_case.change_calendar_day;

import data_access.CalendarDataAccessObjectFactory;
import data_access.GetEventsDataAccessInterface;
import entity.Calendar;
import entity.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChangeCalendarDayInteractorTest {
    private ChangeCalendarDayInteractor interactor;
    private CalendarDataAccessObjectFactory mockFactory;
    private ChangeCalendarDayOutputBoundary mockPresenter;
    private GetEventsDataAccessInterface mockDAO;

    @BeforeEach
    void setUp() {
        mockFactory = mock(CalendarDataAccessObjectFactory.class);
        mockPresenter = mock(ChangeCalendarDayOutputBoundary.class);
        mockDAO = mock(GetEventsDataAccessInterface.class);

        when(mockFactory.getCalendarDataAccessObject(any(Calendar.class)))
                .thenReturn(mockDAO);

        interactor = new ChangeCalendarDayInteractor(mockFactory, mockPresenter);
    }

    @Test
    void execute_SuccessfulFetch() {
        ArrayList<Calendar> calendarList = new ArrayList<>();
        Calendar mockCalendar = mock(Calendar.class);
        calendarList.add(mockCalendar);
        String date = "2024-01-01";

        ArrayList<Event> events = new ArrayList<>();
        events.add(mock(Event.class));
        when(mockDAO.fetchEventsDay(any(LocalDate.class))).thenReturn(events);

        ChangeCalendarDayInputData inputData = new ChangeCalendarDayInputData(calendarList, date);
        interactor.execute(inputData);

        verify(mockDAO).fetchEventsDay(LocalDate.parse(date));
        verify(mockPresenter).prepareSuccessView(any(ChangeCalendarDayOutputData.class));
        verify(mockPresenter, never()).prepareFailView(anyString());
    }

    @Test
    void execute_EmptyCalendarList_StillSucceeds() {
        ArrayList<Calendar> calendarList = new ArrayList<>();
        String date = "2024-01-01";

        ChangeCalendarDayInputData inputData = new ChangeCalendarDayInputData(calendarList, date);
        interactor.execute(inputData);

        verify(mockDAO, never()).fetchEventsDay(any(LocalDate.class));
        verify(mockPresenter).prepareSuccessView(any(ChangeCalendarDayOutputData.class));
        verify(mockPresenter, never()).prepareFailView(anyString());
    }

    @Test
    void execute_InvalidDateFormat_CallsFailView() {
        ArrayList<Calendar> calendarList = new ArrayList<>();
        calendarList.add(mock(Calendar.class));
        String invalidDate = "invalid-date";

        ChangeCalendarDayInputData inputData = new ChangeCalendarDayInputData(calendarList, invalidDate);
        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView(contains("Error fetching calendar data"));
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_DAOThrowsException_CallsFailView() {
        ArrayList<Calendar> calendarList = new ArrayList<>();
        Calendar mockCalendar = mock(Calendar.class);
        calendarList.add(mockCalendar);
        String date = "2024-01-01";

        when(mockDAO.fetchEventsDay(any(LocalDate.class)))
                .thenThrow(new RuntimeException("DAO error"));

        ChangeCalendarDayInputData inputData = new ChangeCalendarDayInputData(calendarList, date);
        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView(contains("Error fetching calendar data"));
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_MultipleCalendars_MergesEvents() {
        ArrayList<Calendar> calendarList = new ArrayList<>();
        calendarList.add(mock(Calendar.class));
        calendarList.add(mock(Calendar.class));
        String date = "2024-01-01";

        ArrayList<Event> events1 = new ArrayList<>();
        events1.add(mock(Event.class));
        ArrayList<Event> events2 = new ArrayList<>();
        events2.add(mock(Event.class));

        when(mockDAO.fetchEventsDay(any(LocalDate.class)))
                .thenReturn(events1)
                .thenReturn(events2);

        ChangeCalendarDayInputData inputData = new ChangeCalendarDayInputData(calendarList, date);
        interactor.execute(inputData);

        verify(mockDAO, times(2)).fetchEventsDay(LocalDate.parse(date));
        verify(mockPresenter).prepareSuccessView(any(ChangeCalendarDayOutputData.class));
        verify(mockPresenter, never()).prepareFailView(anyString());
    }
}