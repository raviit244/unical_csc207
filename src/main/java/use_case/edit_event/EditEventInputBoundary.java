package use_case.edit_event;

/**
 * Input Boundary interface for the Edit Event use-case.
 */
public interface EditEventInputBoundary {
    /**
     * Execute method for the Edit Event use-case.
      * @param editEventInputData input data for the Edit Event use-case.
     */
    void execute(EditEventInputData editEventInputData);
}
