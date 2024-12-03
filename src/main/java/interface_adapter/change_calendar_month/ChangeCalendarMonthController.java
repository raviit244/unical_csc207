package interface_adapter.change_calendar_month;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.Calendar;
import use_case.change_calendar_month.ChangeCalendarMonthInputBoundary;
import use_case.change_calendar_month.ChangeCalendarMonthInputData;

/**
 * The controller for Change Month Calendar Use Case.
 */
public class ChangeCalendarMonthController {
    private final ChangeCalendarMonthInputBoundary changeCalendarMonthUseCaseInteractor;
    private final Map<String, String> monthNumeric;

    public ChangeCalendarMonthController(ChangeCalendarMonthInputBoundary changeCalendarMonthUseCaseInteractor) {
        this.changeCalendarMonthUseCaseInteractor = changeCalendarMonthUseCaseInteractor;
        this.monthNumeric = initializeMonthMap();
    }

    /**
     * Executes the change calendar day use case with the given calendars and date.
     *
     * @param calendarList list of calendars to fetch events from
     * @param month the Month
     * @param year The Year
     */
    public void execute(List<Calendar> calendarList, String month, Integer year) {
        final String monthNum = monthNumeric.get(month);
        final String date = String.format("%d-%s-01", year, monthNum);

        final ChangeCalendarMonthInputData inputData = new ChangeCalendarMonthInputData(
                calendarList,
                date
        );

        changeCalendarMonthUseCaseInteractor.execute(inputData);
    }

    /**
     * Month String to Number.
     * @return Returns a Mapping between Month and the Number
     */
    private Map<String, String> initializeMonthMap() {
        final Map<String, String> map = new HashMap<>();
        map.put("January", "01");
        map.put("February", "02");
        map.put("March", "03");
        map.put("April", "04");
        map.put("May", "05");
        map.put("June", "06");
        map.put("July", "07");
        map.put("August", "08");
        map.put("September", "09");
        map.put("October", "10");
        map.put("November", "11");
        map.put("December", "12");
        return map;
    }
}
