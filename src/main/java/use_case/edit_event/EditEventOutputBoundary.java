package use_case.edit_event;

public interface EditEventOutputBoundary {
  void prepareSuccessView(EditEventOutputData outputData);
  void prepareFailView(String error);
}
