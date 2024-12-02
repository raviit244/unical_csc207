package use_case.merge_calendars;

import data_access.CalendarDataAccessObjectFactory;
import data_access.GetEventsDataAccessInterface;
import entity.Calendar;
import entity.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MergeCalendarsInteractorTest {
    private MergeCalendarsInteractor interactor;
    private CalendarDataAccessObjectFactory mockFactory;
    private MergeCalendarsOutputBoundary mockPresenter;
    private GetEventsDataAccessInterface mockDAO;

    @BeforeEach
    void setUp() {
        mockFactory = mock(CalendarDataAccessObjectFactory.class);
        mockPresenter = mock(MergeCalendarsOutputBoundary.class);
        mockDAO = mock(GetEventsDataAccessInterface.class);

        when(mockFactory.getCalendarDataAccessObject(any(Calendar.class)))
                .thenReturn(mockDAO);

        interactor = new MergeCalendarsInteractor(mockFactory, mockPresenter);
    }

    @Test
    void execute_SuccessfulMerge() {
        List<Calendar> calendarList = new ArrayList<>();
        Calendar mockCalendar1 = mock(Calendar.class);
        Calendar mockCalendar2 = mock(Calendar.class);
        calendarList.add(mockCalendar1);
        calendarList.add(mockCalendar2);
        String date = "2024-01-01";

        Event mockEvent1 = mock(Event.class);
        Event mockEvent2 = mock(Event.class);

        when(mockEvent1.getDate()).thenReturn(LocalDate.parse(date));
        when(mockEvent2.getDate()).thenReturn(LocalDate.parse(date));
        when(mockEvent1.getStartTime()).thenReturn(LocalTime.of(9, 0));
        when(mockEvent2.getStartTime()).thenReturn(LocalTime.of(10, 0));
        when(mockEvent1.getEndTime()).thenReturn(LocalTime.of(10, 0));
        when(mockEvent2.getEndTime()).thenReturn(LocalTime.of(11, 0));

        ArrayList<Event> events1 = new ArrayList<>();
        events1.add(mockEvent1);
        ArrayList<Event> events2 = new ArrayList<>();
        events2.add(mockEvent2);

        when(mockDAO.fetchEventsMonth(any(LocalDate.class)))
                .thenReturn(events1)
                .thenReturn(events2);

        MergeCalendarsInputData inputData = new MergeCalendarsInputData(calendarList, date);
        interactor.execute(inputData);

        verify(mockDAO, times(2)).fetchEventsMonth(LocalDate.parse(date));
        verify(mockPresenter).prepareSuccessView(any(MergeCalendarsOutputData.class));
        verify(mockPresenter, never()).prepareFailView(anyString());
    }

    @Test
    void execute_EmptyCalendarList_SucceedsWithNoEvents() {
        List<Calendar> calendarList = new ArrayList<>();
        String date = "2024-01-01";

        MergeCalendarsInputData inputData = new MergeCalendarsInputData(calendarList, date);
        interactor.execute(inputData);

        verify(mockDAO, never()).fetchEventsMonth(any(LocalDate.class));
        verify(mockPresenter).prepareSuccessView(any(MergeCalendarsOutputData.class));
        verify(mockPresenter, never()).prepareFailView(anyString());
    }

    @Test
    void execute_InvalidDateFormat_CallsFailView() {
        List<Calendar> calendarList = new ArrayList<>();
        calendarList.add(mock(Calendar.class));
        String invalidDate = "invalid-date";

        MergeCalendarsInputData inputData = new MergeCalendarsInputData(calendarList, invalidDate);
        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("Error merging calendars: Text 'invalid-date' could not be parsed at index 0");
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

        MergeCalendarsInputData inputData = new MergeCalendarsInputData(calendarList, date);
        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("Error merging calendars: DAO error");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_SingleCalendar_ProcessesCorrectly() {
        List<Calendar> calendarList = new ArrayList<>();
        Calendar mockCalendar = mock(Calendar.class);
        calendarList.add(mockCalendar);
        String date = "2024-01-01";

        Event mockEvent = mock(Event.class);
        when(mockEvent.getDate()).thenReturn(LocalDate.parse(date));
        when(mockEvent.getStartTime()).thenReturn(LocalTime.of(9, 0));
        when(mockEvent.getEndTime()).thenReturn(LocalTime.of(10, 0));

        ArrayList<Event> events = new ArrayList<>();
        events.add(mockEvent);
        when(mockDAO.fetchEventsMonth(any(LocalDate.class))).thenReturn(events);

        MergeCalendarsInputData inputData = new MergeCalendarsInputData(calendarList, date);
        interactor.execute(inputData);

        verify(mockDAO, times(1)).fetchEventsMonth(LocalDate.parse(date));
        verify(mockPresenter).prepareSuccessView(any(MergeCalendarsOutputData.class));
        verify(mockPresenter, never()).prepareFailView(anyString());
    }
}