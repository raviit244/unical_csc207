package use_case.add_event;

import data_access.AddEventDataAccessInterface;
import data_access.CalendarDataAccessObjectFactory;
import entity.Calendar;
import entity.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AddEventInteractorTest {
    private AddEventInteractor interactor;
    private CalendarDataAccessObjectFactory mockFactory;
    private AddEventOutputBoundary mockPresenter;
    private AddEventDataAccessInterface mockDAO;
    private Calendar mockCalendar;

    @BeforeEach
    void setUp() {
        mockFactory = mock(CalendarDataAccessObjectFactory.class);
        mockPresenter = mock(AddEventOutputBoundary.class);
        mockDAO = mock(AddEventDataAccessInterface.class);
        mockCalendar = mock(Calendar.class);

        when(mockFactory.getCalendarDataAccessObject(any(Calendar.class)))
                .thenReturn(mockDAO);

        interactor = new AddEventInteractor(mockFactory, mockPresenter);
    }

    @Test
    void execute_SuccessfulEventAddition() {
        when(mockDAO.addEvent(any(Event.class))).thenReturn(true);

        AddEventInputData inputData = new AddEventInputData(
                "Test Event",
                "2024-01-01",
                "09:00",
                "10:00",
                mockCalendar
        );

        interactor.execute(inputData);

        verify(mockDAO).addEvent(any(Event.class));
        verify(mockPresenter).prepareSuccessView(any(AddEventOutputData.class));
        verify(mockPresenter, never()).prepareFailView(anyString());
    }

    @Test
    void execute_EmptyEventName_CallsFailView() {
        AddEventInputData inputData = new AddEventInputData(
                "",
                "2024-01-01",
                "09:00",
                "10:00",
                mockCalendar
        );

        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("Event name cannot be empty");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_InvalidDateFormat_CallsFailView() {
        AddEventInputData inputData = new AddEventInputData(
                "Test Event",
                "invalid-date",
                "09:00",
                "10:00",
                mockCalendar
        );

        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("Invalid date format. Use YYYY-MM-DD");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_InvalidTimeFormat_CallsFailView() {
        // Use a valid date but invalid time format
        AddEventInputData inputData = new AddEventInputData(
                "Test Event",
                "2024-01-01",  // Valid date format
                "invalid-time", // Invalid time format
                "10:00",
                mockCalendar
        );

        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("Invalid time format. Use HH:mm (24-hour format)");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_EndTimeBeforeStartTime_CallsFailView() {
        AddEventInputData inputData = new AddEventInputData(
                "Test Event",
                "2024-01-01",
                "10:00",
                "09:00",
                mockCalendar
        );

        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("End time cannot be before start time");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_DAOFailure_CallsFailView() {
        when(mockDAO.addEvent(any(Event.class))).thenReturn(false);

        AddEventInputData inputData = new AddEventInputData(
                "Test Event",
                "2024-01-01",
                "09:00",
                "10:00",
                mockCalendar
        );

        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("Failed to add event to calendar");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_VerifyEventCreation() {
        when(mockDAO.addEvent(any(Event.class))).thenReturn(true);

        String eventName = "Test Event";
        String date = "2024-01-01";
        String startTime = "09:00";
        String endTime = "10:00";

        AddEventInputData inputData = new AddEventInputData(eventName, date, startTime, endTime, mockCalendar);

        interactor.execute(inputData);

        ArgumentCaptor<Event> eventCaptor = ArgumentCaptor.forClass(Event.class);
        verify(mockDAO).addEvent(eventCaptor.capture());

        Event capturedEvent = eventCaptor.getValue();
        assertEquals(eventName, capturedEvent.getEventName());
        assertEquals(LocalDate.parse(date), capturedEvent.getDate());
        assertEquals(LocalTime.parse(startTime), capturedEvent.getStartTime());
        assertEquals(LocalTime.parse(endTime), capturedEvent.getEndTime());
        assertEquals(mockCalendar, capturedEvent.getCalendarApi());
    }
}