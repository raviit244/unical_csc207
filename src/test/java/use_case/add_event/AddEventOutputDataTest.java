package use_case.add_event;

import entity.Event;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class AddEventOutputDataTest {
    @Test
    void constructor_ValidInput_CreatesObject() {
        Event mockEvent = mock(Event.class);
        boolean useCaseFailed = false;

        AddEventOutputData outputData = new AddEventOutputData(mockEvent, useCaseFailed);

        assertEquals(mockEvent, outputData.getEvent());
        assertFalse(outputData.isUseCaseFailed());
    }

    @Test
    void constructor_WithFailedStatus_CreatesObject() {
        Event mockEvent = mock(Event.class);
        boolean useCaseFailed = true;

        AddEventOutputData outputData = new AddEventOutputData(mockEvent, useCaseFailed);

        assertEquals(mockEvent, outputData.getEvent());
        assertTrue(outputData.isUseCaseFailed());
    }
}