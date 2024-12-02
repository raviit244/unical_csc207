package view;

import entity.Event;
import interface_adapter.edit_event.EditEventController;
import interface_adapter.edit_event.EditEventState;
import interface_adapter.edit_event.EditEventViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.format.DateTimeFormatter;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import java.util.function.Consumer;

import java.awt.event.KeyEvent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;

public class EditEventView extends JPanel implements ActionListener, PropertyChangeListener {
  private class DocumentChangeListener implements DocumentListener {
    private final Consumer<String> updateAction;

    public DocumentChangeListener(Consumer<String> updateAction) {
      this.updateAction = updateAction;
    }

    @Override
    public void insertUpdate(DocumentEvent e) { updateField(e); }

    @Override
    public void removeUpdate(DocumentEvent e) { updateField(e); }

    @Override
    public void changedUpdate(DocumentEvent e) { updateField(e); }

    private void updateField(DocumentEvent e) {
      try {
        String text = e.getDocument().getText(0, e.getDocument().getLength());
        updateAction.accept(text);
      } catch (BadLocationException ex) {
        ex.printStackTrace();
      }
    }
  }

  private final String viewName = "edit event";
  private final EditEventViewModel editEventViewModel;
  private final EditEventController editEventController;

  // UI Components
  private final JTextField eventNameField = new JTextField(15);
  private final JTextField dateField = new JTextField(15);
  private final JTextField startTimeField = new JTextField(15);
  private final JTextField endTimeField = new JTextField(15);
  private final JButton saveButton;
  private final JButton cancelButton;
  private final JLabel calendarLabel;
  private final JLabel eventNameErrorField = new JLabel();
  private final JLabel dateErrorField = new JLabel();
  private final JLabel timeErrorField = new JLabel();

  public EditEventView(EditEventViewModel viewModel, EditEventController controller) {
    this.editEventViewModel = viewModel;
    this.editEventController = controller;
    this.editEventViewModel.addPropertyChangeListener(this);

    InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap actionMap = getActionMap();
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK), "save");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "cancel");

    actionMap.put("save", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        EditEventState state = editEventViewModel.getState();
        if (state.getOriginalEvent() != null) {
          editEventController.execute(
                  eventNameField.getText(),
                  dateField.getText(),
                  startTimeField.getText(),
                  endTimeField.getText(),
                  state.getOriginalEvent().getCalendarApi(),
                  state.getOriginalEvent()
          );
        }
      }
    });

    // Initialize components
    saveButton = new JButton(EditEventViewModel.SAVE_BUTTON_LABEL);
    cancelButton = new JButton(EditEventViewModel.CANCEL_BUTTON_LABEL);
    calendarLabel = new JLabel();

    // Setup UI
    setupUI();
    setupListeners();

    saveButton.setToolTipText("Save Event (Ctrl+S)");
  }

  private void setupUI() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Title
    JLabel title = new JLabel(EditEventViewModel.TITLE_LABEL);
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    add(title);
    add(Box.createVerticalStrut(10));

    // Event name section
    add(createFieldPanel(EditEventViewModel.EVENT_NAME_LABEL, eventNameField, eventNameErrorField));
    add(Box.createVerticalStrut(5));

    // Date section
    add(createFieldPanel(EditEventViewModel.DATE_LABEL, dateField, dateErrorField));
    add(Box.createVerticalStrut(5));

    // Time section
    add(createFieldPanel(EditEventViewModel.START_TIME_LABEL, startTimeField, null));
    add(createFieldPanel(EditEventViewModel.END_TIME_LABEL, endTimeField, timeErrorField));
    add(Box.createVerticalStrut(5));

    // Calendar info
    JPanel calendarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    calendarPanel.add(new JLabel(EditEventViewModel.CALENDAR_LABEL + ":"));
    calendarPanel.add(calendarLabel);
    add(calendarPanel);
    add(Box.createVerticalStrut(10));

    // Buttons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);
    add(buttonPanel);
  }

  private JPanel createFieldPanel(String labelText, JTextField field, JLabel errorLabel) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

    JPanel fieldPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    fieldPanel.add(new JLabel(labelText));
    fieldPanel.add(field);
    panel.add(fieldPanel);

    if (errorLabel != null) {
      errorLabel.setForeground(Color.RED);
      JPanel errorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
      errorPanel.add(errorLabel);
      panel.add(errorPanel);
    }

    return panel;
  }

  private void setupListeners() {
    saveButton.addActionListener(this);
    cancelButton.addActionListener(e -> {
      resetFields();
      Window window = SwingUtilities.getWindowAncestor(this);
      if (window instanceof JDialog) {
        window.dispose();
      }
    });

    // Document listeners
    eventNameField.getDocument().addDocumentListener(
            new DocumentChangeListener(s -> updateState(state -> state.setEventName(s)))
    );
    dateField.getDocument().addDocumentListener(
            new DocumentChangeListener(s -> updateState(state -> state.setDate(s)))
    );
    startTimeField.getDocument().addDocumentListener(
            new DocumentChangeListener(s -> updateState(state -> state.setStartTime(s)))
    );
    endTimeField.getDocument().addDocumentListener(
            new DocumentChangeListener(s -> updateState(state -> state.setEndTime(s)))
    );
  }

  private void updateState(Consumer<EditEventState> updater) {
    EditEventState currentState = editEventViewModel.getState();
    EditEventState newState = new EditEventState(currentState);
    updater.accept(newState);
    editEventViewModel.setState(newState);
  }

  public void setEvent(Event event) {
    EditEventState state = new EditEventState();
    state.setOriginalEvent(event);
    state.setEventName(event.getEventName());
    state.setDate(event.getDate().toString());
    state.setStartTime(event.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
    state.setEndTime(event.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
    state.setSelectedCalendar(event.getCalendarApi());
    editEventViewModel.setState(state);

    eventNameField.setText(event.getEventName());
    dateField.setText(event.getDate().toString());
    startTimeField.setText(event.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
    endTimeField.setText(event.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
    calendarLabel.setText(event.getCalendarApi().getCalendarName());

    clearErrors();
  }

  private void clearErrors() {
    eventNameErrorField.setText("");
    dateErrorField.setText("");
    timeErrorField.setText("");
  }

  private void resetFields() {
    eventNameField.setText("");
    dateField.setText("");
    startTimeField.setText("");
    endTimeField.setText("");
    calendarLabel.setText("");
    clearErrors();
  }

  @Override
  public void actionPerformed(ActionEvent evt) {
    if (evt.getSource() == saveButton) {
      EditEventState state = editEventViewModel.getState();
      if (state.getOriginalEvent() != null) {
        editEventController.execute(
                eventNameField.getText(),
                dateField.getText(),
                startTimeField.getText(),
                endTimeField.getText(),
                state.getOriginalEvent().getCalendarApi(),
                state.getOriginalEvent()
        );
      }
    }
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("state")) {
      EditEventState state = (EditEventState) evt.getNewValue();
      if (!state.isUseCaseFailed()) {
        resetFields();
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JDialog) {
          window.dispose();
        }
      } else {
        eventNameErrorField.setText(state.getEventNameError());
        dateErrorField.setText(state.getDateError());
        timeErrorField.setText(state.getTimeError());
      }
    }
  }

  public String getViewName() {
    return viewName;
  }
}
