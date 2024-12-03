package use_case.edit_event;

/**
 * Output Boundary for the Edit Event use-case.
 */
public interface EditEventOutputBoundary {
    /**
     * Prepares the success view for the Edit Event use-case.
     *
     * @param outputData output data from the Edit Event use-case
     */
    void prepareSuccessView(EditEventOutputData outputData);

    /**
     * Prepares the fail view for the Edit Event use-case.
     *
     * @param error the error message
     */
    void prepareFailView(String error);
}
