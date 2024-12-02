package use_case.edit_event;

import entity.Event;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class EditEventOutputDataTest {
    @Test
    void constructor_ValidInput_CreatesObject() {
        Event mockOldEvent = mock(Event.class);
        Event mockNewEvent = mock(Event.class);
        boolean useCaseFailed = false;

        EditEventOutputData outputData = new EditEventOutputData(mockOldEvent, mockNewEvent, useCaseFailed);

        assertEquals(mockOldEvent, outputData.getOldEvent());
        assertEquals(mockNewEvent, outputData.getNewEvent());
        assertFalse(outputData.isUseCaseFailed());
    }

    @Test
    void constructor_WithFailedStatus_CreatesObject() {
        Event mockOldEvent = mock(Event.class);
        Event mockNewEvent = mock(Event.class);
        boolean useCaseFailed = true;

        EditEventOutputData outputData = new EditEventOutputData(mockOldEvent, mockNewEvent, useCaseFailed);

        assertEquals(mockOldEvent, outputData.getOldEvent());
        assertEquals(mockNewEvent, outputData.getNewEvent());
        assertTrue(outputData.isUseCaseFailed());
    }
}