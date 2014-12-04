/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.calendar;

import com.openhris.calendar.service.BasicEvent;
import com.openhris.calendar.service.CalendarService;
import com.openhris.calendar.serviceImpl.CalendarServiceImpl;
import com.openhris.commons.OpenHrisUtilities;
import com.vaadin.addon.calendar.gwt.client.ui.VCalendar;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents;
import com.vaadin.addon.calendar.ui.handler.BasicDateClickHandler;
import com.vaadin.addon.calendar.ui.handler.BasicWeekClickHandler;
import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class CalendarMainUI extends VerticalLayout {
    
    private static final long serialVersionUID = -5436777475398410597L;

    private static final String DEFAULT_ITEMID = "DEFAULT";
    CalendarService calendarService = new CalendarServiceImpl();
    
    private enum Mode {
        MONTH, WEEK, DAY;
    }
    
    private Calendar cal;
    private GregorianCalendar calendar;
    Date currentMonthsFirstDate;
    private final Label captionLabel = new Label("");
    private Mode viewMode = Mode.MONTH;
    private boolean showWeeklyView;
    private Button monthButton;
    private Button weekButton;
    private Button nextButton;
    private Button prevButton;
    private Window scheduleEventPopup;
    private Button deleteEventButton;    
    private Button saveEventButton;
    private Button editEventButton;
    
    private Select eventType;        
    private DateField startDate;
    private DateField endDate;
    private TextField caption;
    private TextField location;
    private TextArea description;
    private Select color;
    
    private Date dateStart;
    private Date dateEnd;
    private boolean useSecondResolution = true;
    private String username;
    private String userRole;
    Label monthLabel;
    
    private String eventId;
    private String employeeId;
        
    public CalendarMainUI(){
        setMargin(true);
        cal = new Calendar();
        
        calendarEvents();
        cal.setWidth("100%");
        cal.setHeight("100%");
        cal.setImmediate(true);
                
        Date today = new Date();
        calendar = new GregorianCalendar();
        calendar.setTime(today);
        
        updateCaptionLabel();
        initNavigationButtons();
        
        if(!showWeeklyView){
            int rollAmount = calendar.get(GregorianCalendar.DAY_OF_MONTH) - 1;
            calendar.add(GregorianCalendar.DAY_OF_MONTH, -rollAmount);
            currentMonthsFirstDate = calendar.getTime();
            cal.setStartDate(currentMonthsFirstDate);
            calendar.add(GregorianCalendar.MONTH, 1);
            calendar.add(GregorianCalendar.DATE, -1);
            cal.setEndDate(calendar.getTime());        
        }
        
        cal.setHandler(new BasicDateClickHandler() {
            @Override
            public void dateClick(CalendarComponentEvents.DateClickEvent event) {
                Calendar cal = event.getComponent();
                long currentCalDateRange = cal.getEndDate().getTime() - cal.getStartDate().getTime();

                if (currentCalDateRange < VCalendar.DAYINMILLIS) {
                // Change the date range to the current week
                    cal.setStartDate(cal.getFirstDateForWeek(event.getDate()));
                    cal.setEndDate(cal.getLastDateForWeek(event.getDate()));

                } else {
                // Default behaviour, change date range to one day
                    super.dateClick(event);
                }
            }
        });
        
        addCalendarEventListeners();
        
        GridLayout grid = new GridLayout(5, 1);
        grid.setSizeFull();
        
        monthButton.setVisible(false);
        weekButton.setVisible(false);
        
        grid.addComponent(monthButton, 1, 0);
        grid.setComponentAlignment(monthButton, Alignment.MIDDLE_CENTER);
        
	monthLabel = new Label();
	monthLabel.setValue(OpenHrisUtilities.convertDateFormatForCalendar(currentMonthsFirstDate.toString()));
	monthLabel.setContentMode(Label.CONTENT_XHTML);
	monthLabel.addStyleName("month");
	grid.addComponent(monthLabel, 2, 0);
	grid.setComponentAlignment(monthLabel, Alignment.MIDDLE_CENTER);
	
        grid.addComponent(weekButton, 3, 0);
        grid.setComponentAlignment(weekButton, Alignment.MIDDLE_CENTER);
        
        grid.addComponent(nextButton, 4, 0);
        grid.setComponentAlignment(nextButton, Alignment.MIDDLE_RIGHT);
        
        grid.addComponent(prevButton, 0, 0);
        grid.setComponentAlignment(prevButton, Alignment.MIDDLE_LEFT);
        
        addComponent(grid);
        addComponent(cal);
        setExpandRatio(cal, 1);
    }
    
    private void initNavigationButtons() {
        monthButton = new Button("Month view", new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                switchToMonthView();
            }
        });

        weekButton = new Button("Week view", new Button.ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                // simulate week click
                CalendarComponentEvents.WeekClickHandler handler = (CalendarComponentEvents.WeekClickHandler) cal.getHandler(CalendarComponentEvents.WeekClick.EVENT_ID);
                handler.weekClick(new CalendarComponentEvents.WeekClick(cal, calendar
                        .get(GregorianCalendar.WEEK_OF_YEAR), calendar
                        .get(GregorianCalendar.YEAR)));
            }
        });

        nextButton = new Button("Next", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                handleNextButtonClick();
            }
        });

        prevButton = new Button("Prev", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                handlePreviousButtonClick();
            }
        });
    }
    
    @SuppressWarnings("serial")
    private void addCalendarEventListeners() {
        // Register week clicks by changing the schedules start and end dates.
        cal.setHandler(new BasicWeekClickHandler() {

            @Override
            public void weekClick(CalendarComponentEvents.WeekClick event) {
                // let BasicWeekClickHandler handle calendar dates, and update
                // only the other parts of UI here
                super.weekClick(event);
                updateCaptionLabel();
                switchToWeekView();
            }
        });

        cal.setHandler(new CalendarComponentEvents.EventClickHandler() {

            @Override
            public void eventClick(CalendarComponentEvents.EventClick event) { 
                nextButton.setVisible(true);
                prevButton.setVisible(true);
                
                if(event.getCalendarEvent() == null){
                    Window subWindow = scheduleEventWindow(null, null, null, null, null, null, null);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow); 
                    }                    
                    subWindow.setModal(true);
                    subWindow.center();
                }else{
                    System.out.println(event.getCalendarEvent().getCaption());
                }                
            }
        });
        
        cal.setHandler(new BasicDateClickHandler() {

            @Override
            public void dateClick(CalendarComponentEvents.DateClickEvent event) {
                // let BasicDateClickHandler handle calendar dates, and update
                // only the other parts of UI here
                super.dateClick(event);
                switchToDayView();
            }
        });
        
        cal.setHandler(new CalendarComponentEvents.RangeSelectHandler() {

            @Override
            public void rangeSelect(CalendarComponentEvents.RangeSelectEvent event) {                
                handleRangeSelect(event);
            }
        });
    }

    private void handleNextButtonClick() {
        switch (viewMode) {
        case MONTH:
            nextMonth();
            break;
        case WEEK:
            nextWeek();
            break;
        case DAY:
            nextDay();
            break;
        }
    }

    private void handlePreviousButtonClick() {
        switch (viewMode) {
        case MONTH:
            previousMonth();
            break;
        case WEEK:
            previousWeek();
            break;
        case DAY:
            previousDay();
            break;
        }
    }

    private void handleRangeSelect(CalendarComponentEvents.RangeSelectEvent event) {
        dateStart = event.getStart();
        dateEnd = event.getEnd();

        /*
         * If a range of dates is selected in monthly mode, we want it to end at
         * the end of the last day.
         */
        if (event.isMonthlyMode()) {
            dateEnd = Calendar.getEndOfDay(calendar, dateEnd);
        }
                
        Window subWindow = scheduleEventWindow(null, null, null, null, null, null, null);
        if(subWindow.getParent() == null){
            getWindow().addWindow(subWindow); 
        }                    
        subWindow.setModal(true);
        subWindow.center();
        
    }

    public final void calendarEvents(){        
        List<BasicEvent> eventList = calendarService.getAllEvents();
        for(BasicEvent event : eventList){
            cal.addEvent(event);
        } 
    }
    
    private Window scheduleEventWindow(String type, 
            String eventCaption, 
            String eventDescription, 
            String eventStartDate, 
            String eventEndDate, 
            String eventStyleName, 
            String eventLocation){
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        final Window subWindow = new Window("New Event", layout);
        subWindow.setWidth("250px");

        eventType = createEventTypelect();
        layout.addComponent(eventType);
        
        startDate = createDateField("Start Date: ");
        layout.addComponent(startDate);
        
        endDate = createDateField("End Date: ");
        layout.addComponent(endDate);
        
        caption = createTextField("Caption: ");
        layout.addComponent(caption);
        
        location = createTextField("Where: ");
        layout.addComponent(location);
        
        description = new TextArea("Description: ");
        description.setWidth("100%");
        description.setRows(3);
        layout.addComponent(description);
        
        color = createStyleNameSelect();
        layout.addComponent(color);

        saveEventButton = new Button("SAVE", saveEventBtnListener);
        saveEventButton.setWidth("100%");
//        editEventButton = new Button("Edit", editEventBtnListener);
        deleteEventButton = new Button("DELETE", deleteEventBtnListener);
        deleteEventButton.setWidth("100%");
        
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setWidth("100%");
        buttons.addComponent(deleteEventButton);
        buttons.addComponent(saveEventButton);
//        buttons.addComponent(editEventButton);
        layout.addComponent(buttons);
        layout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
                
        return subWindow;
    }
    
    private void nextMonth() {
        rollMonth(1);
    }

    private void previousMonth() {
        rollMonth(-1);
    }

    private void nextWeek() {
        rollWeek(1);
    }

    private void previousWeek() {
        rollWeek(-1);
    }

    private void nextDay() {
        rollDate(1);
    }

    private void previousDay() {
        rollDate(-1);
    }
    
    private void rollMonth(int direction) {
        calendar.setTime(currentMonthsFirstDate);
        calendar.add(GregorianCalendar.MONTH, direction);
        resetTime(false);
        currentMonthsFirstDate = calendar.getTime();
        cal.setStartDate(currentMonthsFirstDate);
        
        //add/subtract 1 month each time next button is press
        monthLabel.setValue(OpenHrisUtilities.convertDateFormatForCalendar(currentMonthsFirstDate.toString()));

        updateCaptionLabel();

        calendar.add(GregorianCalendar.MONTH, 1);
        calendar.add(GregorianCalendar.DATE, -1);
        resetCalendarTime(true);
    }

    private void rollWeek(int direction) {
        calendar.add(GregorianCalendar.WEEK_OF_YEAR, direction);
        calendar.set(GregorianCalendar.DAY_OF_WEEK,
                calendar.getFirstDayOfWeek());
        resetCalendarTime(false);
        resetTime(true);
        calendar.add(GregorianCalendar.DATE, 6);
        cal.setEndDate(calendar.getTime());
    }

    private void rollDate(int direction) {
        calendar.add(GregorianCalendar.DATE, direction);
        resetCalendarTime(false);
        resetCalendarTime(true);
    }
    
    private void resetCalendarTime(boolean resetEndTime) {
        resetTime(resetEndTime);
        if (resetEndTime) {
            cal.setEndDate(calendar.getTime());
        } else {
            cal.setStartDate(calendar.getTime());
            updateCaptionLabel();
        }
    }
    
    private void updateCaptionLabel() {
        DateFormatSymbols s = new DateFormatSymbols();
        String month = s.getShortMonths()[calendar.get(GregorianCalendar.MONTH)];
        captionLabel.setValue(month + " " + calendar.get(GregorianCalendar.YEAR));
    }
    
    private void resetTime(boolean max) {
        if (max) {
            calendar.set(GregorianCalendar.HOUR_OF_DAY,
                    calendar.getMaximum(GregorianCalendar.HOUR_OF_DAY));
            calendar.set(GregorianCalendar.MINUTE,
                    calendar.getMaximum(GregorianCalendar.MINUTE));
            calendar.set(GregorianCalendar.SECOND,
                    calendar.getMaximum(GregorianCalendar.SECOND));
            calendar.set(GregorianCalendar.MILLISECOND,
                    calendar.getMaximum(GregorianCalendar.MILLISECOND));
        } else {
            calendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
            calendar.set(GregorianCalendar.MINUTE, 0);
            calendar.set(GregorianCalendar.SECOND, 0);
            calendar.set(GregorianCalendar.MILLISECOND, 0);
        }
    }
    
    /*
     * Switch the view to week view.
     */
    public void switchToWeekView() {
        viewMode = Mode.WEEK;
        weekButton.setVisible(false);
        monthButton.setVisible(true);
    }

    /*
     * Switch the Calendar component's start and end date range to the target
     * month only. (sample range: 01.01.2010 00:00.000 - 31.01.2010 23:59.999)
     */
    public void switchToMonthView() {
        viewMode = Mode.MONTH;
        monthButton.setVisible(false);
        weekButton.setVisible(false);

        calendar.setTime(currentMonthsFirstDate);
        cal.setStartDate(currentMonthsFirstDate);

        updateCaptionLabel();

        calendar.add(GregorianCalendar.MONTH, 1);
        calendar.add(GregorianCalendar.DATE, -1);
        resetCalendarTime(true);
    }

    /*
     * Switch to day view (week view with a single day visible).
     */
    public void switchToDayView() {
        viewMode = Mode.DAY;
        monthButton.setVisible(true);
        weekButton.setVisible(true);
    }
    
    private DateField createDateField(String caption) {
        DateField f = new DateField(caption);
        f.setSizeFull();
        if (useSecondResolution) {
            f.setResolution(DateField.RESOLUTION_SEC);
        } else {
            f.setResolution(DateField.RESOLUTION_MIN);
        }
            return f;
    }
    
    private Select createEventTypelect() {
        Select s = new Select("Event Type: ");
        s.setWidth("100%");
        s.addItem("Legal Holiday");
        s.addItem("Special Holiday");
        s.addItem("Birthday");
        s.addItem("Meeting");
        s.setNullSelectionAllowed(false);
        
        return s;
    }
    
    private Select createStyleNameSelect() {
        Select s = new Select("Color: ");
        s.setWidth("100%");
        s.setNullSelectionAllowed(false);
        s.addContainerProperty("c", String.class, "");
        s.setItemCaptionPropertyId("c");
        Item i = s.addItem("color1");
        i.getItemProperty("c").setValue("Green");
        i = s.addItem("color2");
        i.getItemProperty("c").setValue("Blue");
        i = s.addItem("color3");
        i.getItemProperty("c").setValue("Red");
        i = s.addItem("color4");
        i.getItemProperty("c").setValue("Orange");
        s.setImmediate(true);
        return s;
    }
    
    private TextField createTextField(String caption) {
        TextField f = new TextField(caption);
        f.setWidth("100%");
        f.setNullRepresentation("");
        return f;
    }
    
    Button.ClickListener saveEventBtnListener = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            getWindow().showNotification("SAVE");
        }
    };
    
    Button.ClickListener editEventBtnListener = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            getWindow().showNotification("EDIT");
        }
    };
            
    Button.ClickListener deleteEventBtnListener = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            Window sub = getDeleteWindow();
            sub.setModal(true);
            if(sub.getParent() == null){
                getWindow().addWindow(sub);
            }
            sub.center();
        }
    };
    
    Window getDeleteWindow(){
        Window sub = new Window("DELETE EVENT");
        sub.setWidth("250px");
        
        Button deleteBtn = new Button("DELETE EVENT?");
        deleteBtn.setWidth("100%");
        
        sub.addComponent(deleteBtn);
        
        return sub;
    }
}
