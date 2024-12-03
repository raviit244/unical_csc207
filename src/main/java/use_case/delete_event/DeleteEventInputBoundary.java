package use_case.delete_event;

/**
 * Input Boundary interface for the Delete Event use-case.
 */
public interface DeleteEventInputBoundary {
    /**
     * Executes the delete event use case.
     *
     * @param deleteEventInputData the input data
     */
    void execute(DeleteEventInputData deleteEventInputData);
}
