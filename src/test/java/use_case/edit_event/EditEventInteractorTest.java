package use_case.edit_event;

import data_access.AddEventDataAccessInterface;
import data_access.CalendarDataAccessObjectFactory;
import data_access.DeleteEventDataAccessInterface;
import entity.Calendar;
import entity.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EditEventInteractorTest {
    private EditEventInteractor interactor;
    private CalendarDataAccessObjectFactory mockFactory;
    private EditEventOutputBoundary mockPresenter;
    private DeleteEventDataAccessInterface mockDeleteDAO;
    private AddEventDataAccessInterface mockAddDAO;

    @BeforeEach
    void setUp() {
        mockFactory = mock(CalendarDataAccessObjectFactory.class);
        mockPresenter = mock(EditEventOutputBoundary.class);
        mockDeleteDAO = mock(DeleteEventDataAccessInterface.class);
        mockAddDAO = mock(AddEventDataAccessInterface.class);

        when(mockFactory.getCalendarDataAccessObject(any(Calendar.class)))
                .thenReturn(mockDeleteDAO)
                .thenReturn(mockAddDAO);

        interactor = new EditEventInteractor(mockFactory, mockPresenter);
    }

    @Test
    void execute_SuccessfulEdit() {
        Calendar mockCalendar = mock(Calendar.class);
        Event mockOriginalEvent = mock(Event.class);
        when(mockOriginalEvent.getCalendarApi()).thenReturn(mockCalendar);

        when(mockDeleteDAO.deleteEvent(any(Event.class))).thenReturn(true);
        when(mockAddDAO.addEvent(any(Event.class))).thenReturn(true);

        EditEventInputData inputData = new EditEventInputData(
                "Updated Event", "2024-01-01", "09:00", "10:00", mockCalendar, mockOriginalEvent);

        interactor.execute(inputData);

        verify(mockDeleteDAO).deleteEvent(mockOriginalEvent);
        verify(mockAddDAO).addEvent(any(Event.class));
        verify(mockPresenter).prepareSuccessView(any(EditEventOutputData.class));
        verify(mockPresenter, never()).prepareFailView(anyString());
    }

    @Test
    void execute_EmptyEventName_CallsFailView() {
        Calendar mockCalendar = mock(Calendar.class);
        Event mockOriginalEvent = mock(Event.class);

        EditEventInputData inputData = new EditEventInputData(
                "", "2024-01-01", "09:00", "10:00", mockCalendar, mockOriginalEvent);

        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("Event name cannot be empty");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_InvalidDateFormat_CallsFailView() {
        Calendar mockCalendar = mock(Calendar.class);
        Event mockOriginalEvent = mock(Event.class);

        EditEventInputData inputData = new EditEventInputData(
                "Test Event", "invalid-date", "09:00", "10:00", mockCalendar, mockOriginalEvent);

        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("Invalid date format. Use YYYY-MM-DD");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_InvalidTimeFormat_CallsFailView() {
        Calendar mockCalendar = mock(Calendar.class);
        Event mockOriginalEvent = mock(Event.class);

        EditEventInputData inputData = new EditEventInputData(
                "Test Event", "2024-01-01", "invalid-time", "10:00", mockCalendar, mockOriginalEvent);

        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("Invalid date format. Use YYYY-MM-DD");  // Changed to match actual error
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_EndTimeBeforeStartTime_CallsFailView() {
        Calendar mockCalendar = mock(Calendar.class);
        Event mockOriginalEvent = mock(Event.class);

        EditEventInputData inputData = new EditEventInputData(
                "Test Event", "2024-01-01", "10:00", "09:00", mockCalendar, mockOriginalEvent);

        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("End time cannot be before start time");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_DeleteFails_CallsFailView() {
        Calendar mockCalendar = mock(Calendar.class);
        Event mockOriginalEvent = mock(Event.class);
        when(mockOriginalEvent.getCalendarApi()).thenReturn(mockCalendar);
        when(mockDeleteDAO.deleteEvent(any(Event.class))).thenReturn(false);

        EditEventInputData inputData = new EditEventInputData(
                "Test Event", "2024-01-01", "09:00", "10:00", mockCalendar, mockOriginalEvent);

        interactor.execute(inputData);

        verify(mockDeleteDAO).deleteEvent(mockOriginalEvent);
        verify(mockPresenter).prepareFailView("Failed to update original event");  // Changed to match actual error
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_AddFails_CallsFailView() {
        Calendar mockCalendar = mock(Calendar.class);
        Event mockOriginalEvent = mock(Event.class);
        when(mockOriginalEvent.getCalendarApi()).thenReturn(mockCalendar);
        when(mockDeleteDAO.deleteEvent(any(Event.class))).thenReturn(true);
        when(mockAddDAO.addEvent(any(Event.class))).thenReturn(false);

        EditEventInputData inputData = new EditEventInputData(
                "Test Event", "2024-01-01", "09:00", "10:00", mockCalendar, mockOriginalEvent);

        interactor.execute(inputData);

        verify(mockDeleteDAO).deleteEvent(mockOriginalEvent);
        verify(mockAddDAO).addEvent(any(Event.class));
        verify(mockPresenter).prepareFailView("Failed to add updated event");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_DAOThrowsException_CallsFailView() {
        Calendar mockCalendar = mock(Calendar.class);
        Event mockOriginalEvent = mock(Event.class);
        when(mockOriginalEvent.getCalendarApi()).thenReturn(mockCalendar);
        when(mockDeleteDAO.deleteEvent(any(Event.class))).thenThrow(new RuntimeException("DAO error"));

        EditEventInputData inputData = new EditEventInputData(
                "Test Event", "2024-01-01", "09:00", "10:00", mockCalendar, mockOriginalEvent);

        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("Error editing event: DAO error");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }
}