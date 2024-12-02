package use_case.delete_event;

import entity.Event;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DeleteEventOutputDataTest {
    @Test
    void constructor_ValidInput_CreatesObject() {
        Event mockEvent = mock(Event.class);
        boolean useCaseFailed = false;

        DeleteEventOutputData outputData = new DeleteEventOutputData(mockEvent, useCaseFailed);

        assertEquals(mockEvent, outputData.getEvent());
        assertFalse(outputData.isUseCaseFailed());
    }

    @Test
    void constructor_WithFailedStatus_CreatesObject() {
        Event mockEvent = mock(Event.class);
        boolean useCaseFailed = true;

        DeleteEventOutputData outputData = new DeleteEventOutputData(mockEvent, useCaseFailed);

        assertEquals(mockEvent, outputData.getEvent());
        assertTrue(outputData.isUseCaseFailed());
    }

    @Test
    void constructor_NullEvent_ThrowsException() {
        Exception exception = assertThrows(NullPointerException.class,
                () -> new DeleteEventOutputData(null, false));
        assertEquals("Event cannot be null", exception.getMessage());
    }
}