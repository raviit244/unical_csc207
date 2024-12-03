package use_case.delete_event;

/**
 * Output Boundary interface for the Delete Event use-case.
 */
public interface DeleteEventOutputBoundary {
    /**
     * Prepares the success view for the Delete Event Use Case.
     *
     * @param deleteEventOutputData the output data
     */
    void prepareSuccessView(DeleteEventOutputData deleteEventOutputData);

    /**
     * Prepares the failure view for the Delete Event Use Case.
     *
     * @param error the error message
     */
    void prepareFailView(String error);
}
