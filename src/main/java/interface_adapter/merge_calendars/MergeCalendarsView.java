package interface_adapter.merge_calendars;

import entity.Calendar;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MergeCalendarsView extends JPanel {
    private final MergeCalendarsController mergeCalendarsController;
    private final JButton mergeButton;

    public MergeCalendarsView(MergeCalendarsController controller) {
        this.mergeCalendarsController = controller;
        this.mergeButton = new JButton("Merge Calendars");

        setLayout(new BorderLayout());
        add(mergeButton, BorderLayout.CENTER);

        mergeButton.addActionListener(e -> {
            List<Calendar> calendarsToMerge = getSelectedCalendars();
            if (!calendarsToMerge.isEmpty()) {
                mergeCalendarsController.execute(calendarsToMerge, getCurrentDate());
            }
        });
    }

    private List<Calendar> getSelectedCalendars() {
        // This method would get the currently selected calendars
        // Implementation would depend on your UI structure
        return new ArrayList<>();
    }

    private String getCurrentDate() {
        // This method would get the current selected date
        // Implementation would depend on your UI structure
        return LocalDate.now().toString();
    }
}