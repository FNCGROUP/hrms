/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.modules;

import com.openhris.administrator.model.UserAccessControl;
import com.hrms.calendarClasses.BasicEvent;
import com.hrms.company.events.BasicEventBean;
import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.queries.GetSQLQuery;
import com.hrms.utilities.ConvertionUtilities;
import com.vaadin.addon.calendar.gwt.client.ui.VCalendar;
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.DateClickEvent;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.EventClick;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.EventClickHandler;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.RangeSelectEvent;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.RangeSelectHandler;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.WeekClick;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents.WeekClickHandler;
import com.vaadin.addon.calendar.ui.handler.BasicDateClickHandler;
import com.vaadin.addon.calendar.ui.handler.BasicWeekClickHandler;
import com.vaadin.data.Item;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class CalendarModule extends VerticalLayout {
    
    private static final long serialVersionUID = -5436777475398410597L;

    private static final String DEFAULT_ITEMID = "DEFAULT";

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
    
    private Date dateStart;
    private Date dateEnd;
    private boolean useSecondResolution = true;
    private String username;
    private String userRole;
    
    BasicEventBean basicEventBean = new BasicEventBean();
    ConvertionUtilities conUtil = new ConvertionUtilities();
    GetSQLConnection getConnection = new GetSQLConnection();
    GetSQLQuery query = new GetSQLQuery();
    
    public CalendarModule(String username, String userRole){
        this.username = username;
        this.userRole = userRole;
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
            public void dateClick(DateClickEvent event) {
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
        
        GridLayout grid = new GridLayout(4, 1);
        grid.setSizeFull();
        
        monthButton.setVisible(false);
        weekButton.setVisible(false);
        
        grid.addComponent(monthButton, 1, 0);
        grid.setComponentAlignment(monthButton, Alignment.MIDDLE_CENTER);
        
        grid.addComponent(weekButton, 2, 0);
        grid.setComponentAlignment(weekButton, Alignment.MIDDLE_CENTER);
        
        grid.addComponent(nextButton, 3, 0);
        grid.setComponentAlignment(nextButton, Alignment.MIDDLE_RIGHT);
        
        grid.addComponent(prevButton, 0, 0);
        grid.setComponentAlignment(prevButton, Alignment.MIDDLE_LEFT);
        
        addComponent(grid);
        addComponent(cal);
        setExpandRatio(cal, 1);
    }       
    
    private void initNavigationButtons() {
        monthButton = new Button("Month view", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                switchToMonthView();
            }
        });

        weekButton = new Button("Week view", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                // simulate week click
                WeekClickHandler handler = (WeekClickHandler) cal.getHandler(WeekClick.EVENT_ID);
                handler.weekClick(new WeekClick(cal, calendar
                        .get(GregorianCalendar.WEEK_OF_YEAR), calendar
                        .get(GregorianCalendar.YEAR)));
            }
        });

        nextButton = new Button("Next", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                handleNextButtonClick();
            }
        });

        prevButton = new Button("Prev", new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                handlePreviousButtonClick();
            }
        });
    }
    
    @SuppressWarnings("serial")
    private void addCalendarEventListeners() {
        // Register week clicks by changing the schedules start and end dates.
        cal.setHandler(new BasicWeekClickHandler() {

            @Override
            public void weekClick(WeekClick event) {
                // let BasicWeekClickHandler handle calendar dates, and update
                // only the other parts of UI here
                super.weekClick(event);
                updateCaptionLabel();
                switchToWeekView();
            }
        });

        cal.setHandler(new EventClickHandler() {

            @Override
            public void eventClick(EventClick event) {                   
                if(event.getCalendarEvent() == null){
                    Window subWindow = scheduleEventWindow();
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow); 
                    }                    
                    subWindow.setModal(true);
                    subWindow.center();
                }else{
                    System.out.println(event.getCalendarEvent().getDescription());
                }                
            }
        });
        
        cal.setHandler(new BasicDateClickHandler() {

            @Override
            public void dateClick(DateClickEvent event) {
                // let BasicDateClickHandler handle calendar dates, and update
                // only the other parts of UI here
                super.dateClick(event);
                switchToDayView();
            }
        });
        
        cal.setHandler(new RangeSelectHandler() {

            @Override
            public void rangeSelect(RangeSelectEvent event) {                
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
    
    private void handleRangeSelect(RangeSelectEvent event) {
        dateStart = event.getStart();
        dateEnd = event.getEnd();

        /*
         * If a range of dates is selected in monthly mode, we want it to end at
         * the end of the last day.
         */
        if (event.isMonthlyMode()) {
            dateEnd = Calendar.getEndOfDay(calendar, dateEnd);
        }
        
        if(userRole.equals("administrator")){
            getWindow().showNotification("You are not allowed to add events", Window.Notification.TYPE_WARNING_MESSAGE);
            return;
        }
        
        if(UserAccessControl.isAddEvents()== true){
            Window subWindow = scheduleEventWindow();
            if(subWindow.getParent() == null){
                getWindow().addWindow(subWindow); 
            }                    
            subWindow.setModal(true);
            subWindow.center();
        }else{
            getWindow().showNotification("You are not allowed to add events", Window.Notification.TYPE_WARNING_MESSAGE);
        }
        
    }
    
    public final void calendarEvents(){
        Connection conn = getConnection.connection();
        try {
            //Integer i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM calendar_events");
            /*while(rs.next()){
                GregorianCalendar start = new GregorianCalendar();
                GregorianCalendar end = new GregorianCalendar();
                Date startEvent = null;                
                Date endEvent = null;
                try {
                    startEvent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(rs.getString("startDate"));
                    endEvent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(rs.getString("endDate"));
                } catch (ParseException ex) {
                    Logger.getLogger(CalendarModule.class.getName()).log(Level.SEVERE, null, ex);
                }
                start.setTime(startEvent);                
                end.setTime(endEvent);
                cal.addEvent(new BasicEvent(
                        rs.getString("caption"), 
                        rs.getString("description"),
                        rs.getString("color"),
                        start.getTime(), end.getTime()));                 
            }*/            
            BasicEventBean eventList = new BasicEventBean();
            List<BasicEvent> newEventList = eventList.eventsList(rs);
            for(BasicEvent bean : newEventList){
                cal.addEvent(bean);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CalendarModule.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    private Window scheduleEventWindow(){
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        final Window subWindow = new Window("New Event", layout);
        subWindow.setWidth("250px");

        final Select eventType = createEventTypelect();
        layout.addComponent(eventType);
        
        final DateField startDate = createDateField("Start Date: ");
        startDate.setValue(dateStart);
        layout.addComponent(startDate);
        
        final DateField endDate = createDateField("End Date: ");
        endDate.setValue(dateEnd);
        layout.addComponent(endDate);
        
        final TextField caption = createTextField("Caption: ");
        layout.addComponent(caption);
        
        final TextField location = createTextField("Where: ");
        layout.addComponent(location);
        
        final TextArea description = new TextArea("Description: ");
        description.setWidth("100%");
        description.setRows(3);
        layout.addComponent(description);
        
        final Select color = createStyleNameSelect();
        layout.addComponent(color);

        saveEventButton = new Button("Apply", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                 
                String employeeId = query.getEmployeesIdByUsername(username);
                                
                if(startDate.getValue() == null || endDate.getValue() == null){
                    subWindow.getWindow().showNotification("ADD Dates!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(caption.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("ADD Caption!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(location.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("ADD Location!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(description.getValue().toString().trim().isEmpty()){
                    subWindow.getWindow().showNotification("ADD Description!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(eventType.getValue() == null){
                    subWindow.getWindow().showNotification("ADD Event Type!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                basicEventBean.setEventType(eventType.getValue().toString());
                basicEventBean.setCaption(caption.getValue().toString().trim());
                basicEventBean.setLocation(location.getValue().toString().trim());
                basicEventBean.setDescription(description.getValue().toString().trim());
                basicEventBean.setStartDate(conUtil.convertDateFormatWithTime(startDate.getValue().toString().trim()));
                basicEventBean.setEndDate(conUtil.convertDateFormatWithTime(endDate.getValue().toString().trim()));
                basicEventBean.setEmployeeId(employeeId);
                basicEventBean.setStyleName(color.getValue().toString());                
                
                Boolean result = basicEventBean.saveNewEvent();
                if(result == true){ 
                    updateCaptionLabel();
                    resetCalendarTime(true);
                    GregorianCalendar start = new GregorianCalendar();
                    GregorianCalendar end = new GregorianCalendar();
                    Date startEvent = conUtil.parsingDateWithTime(conUtil.convertDateFormatWithTime(startDate.getValue().toString()));               
                    Date endEvent = conUtil.parsingDateWithTime(conUtil.convertDateFormatWithTime(endDate.getValue().toString()));
                    start.setTime(startEvent);
                    end.setTime(endEvent);
                    cal.addEvent(new BasicEvent(
                            caption.getValue().toString(), description.getValue().toString(), color.getValue().toString(), 
                            start.getTime(), end.getTime()                                                        
                    ));                    
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    subWindow.getWindow().showNotification("Cannot ADD new EVENT!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
        }); 
        
        editEventButton = new Button("Edit", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                //discardCalendarEvent();
            }
        });
        
        deleteEventButton = new Button("Delete", new ClickListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(ClickEvent event) {
                //deleteCalendarEvent();
            }
        });
        /*scheduleEventPopup.addListener(new CloseListener() {

            private static final long serialVersionUID = 1L;

            @Override
            public void windowClose(CloseEvent e) {
                //discardCalendarEvent();
            }
        }); */

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        //buttons.addComponent(deleteEventButton);
        buttons.addComponent(saveEventButton);
        //buttons.addComponent(editEventButton);
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
}
