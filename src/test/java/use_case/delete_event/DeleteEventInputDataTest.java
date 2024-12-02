package use_case.delete_event;

import entity.Event;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class DeleteEventInputDataTest {
    @Test
    void constructor_ValidInput_CreatesObject() {
        Event mockEvent = mock(Event.class);
        DeleteEventInputData inputData = new DeleteEventInputData(mockEvent);

        assertEquals(mockEvent, inputData.getEvent());
    }

    @Test
    void constructor_NullEvent_ThrowsException() {
        assertThrows(NullPointerException.class, () -> new DeleteEventInputData(null));
    }
}