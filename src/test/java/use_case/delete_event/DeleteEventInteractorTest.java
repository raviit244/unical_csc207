package use_case.delete_event;

import data_access.CalendarDataAccessObjectFactory;
import data_access.DeleteEventDataAccessInterface;
import entity.Calendar;
import entity.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DeleteEventInteractorTest {
    private DeleteEventInteractor interactor;
    private CalendarDataAccessObjectFactory mockFactory;
    private DeleteEventOutputBoundary mockPresenter;
    private DeleteEventDataAccessInterface mockDAO;
    private Calendar mockCalendar;
    private Event mockEvent;

    @BeforeEach
    void setUp() {
        mockFactory = mock(CalendarDataAccessObjectFactory.class);
        mockPresenter = mock(DeleteEventOutputBoundary.class);
        mockDAO = mock(DeleteEventDataAccessInterface.class);
        mockCalendar = mock(Calendar.class);
        mockEvent = mock(Event.class);

        when(mockEvent.getCalendarApi()).thenReturn(mockCalendar);
        when(mockFactory.getCalendarDataAccessObject(any(Calendar.class)))
                .thenReturn(mockDAO);

        interactor = new DeleteEventInteractor(mockFactory, mockPresenter);
    }

    @Test
    void execute_SuccessfulDeletion() {
        when(mockDAO.deleteEvent(mockEvent)).thenReturn(true);

        DeleteEventInputData inputData = new DeleteEventInputData(mockEvent);
        interactor.execute(inputData);

        verify(mockDAO).deleteEvent(mockEvent);
        verify(mockPresenter).prepareSuccessView(any(DeleteEventOutputData.class));
        verify(mockPresenter, never()).prepareFailView(anyString());
    }

    @Test
    void execute_FailedDeletion() {
        when(mockDAO.deleteEvent(mockEvent)).thenReturn(false);

        DeleteEventInputData inputData = new DeleteEventInputData(mockEvent);
        interactor.execute(inputData);

        verify(mockDAO).deleteEvent(mockEvent);
        verify(mockPresenter).prepareFailView("Failed to delete event from calendar");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }

    @Test
    void execute_ExceptionThrown() {
        when(mockDAO.deleteEvent(mockEvent)).thenThrow(new RuntimeException("Test error"));

        DeleteEventInputData inputData = new DeleteEventInputData(mockEvent);
        interactor.execute(inputData);

        verify(mockPresenter).prepareFailView("Error deleting event: Test error");
        verify(mockPresenter, never()).prepareSuccessView(any());
    }
}