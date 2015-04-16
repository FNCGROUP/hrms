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
import com.vaadin.addon.calendar.ui.Calendar;
import com.vaadin.addon.calendar.ui.CalendarComponentEvent;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents;
import com.vaadin.data.Item;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author jetdario
 */
public class CalendarScheduleWindow extends Window {

    BasicEvent basicEvent = new BasicEvent();
    OpenHrisUtilities utilities = new OpenHrisUtilities();
    CalendarService calendarService = new CalendarServiceImpl();
    private int eventId;
    private boolean useSecondResolution;
    
    private Select eventType;        
    private DateField startDate;
    private DateField endDate;
    private TextField caption;
    private TextField location;
    private TextArea description;
    private Select color;
    private TextField eventDataId;
    private Button deleteEventButton;    
    private Button saveEventButton;
    private Button editEventButton;
    
    CalendarComponentEvents.EventClick event;
    Calendar cal;

    public CalendarScheduleWindow() {
        addComponent(scheduleLayout());
    }
        
    public CalendarScheduleWindow(int eventId, 
            boolean useSecondResolution, 
            Calendar cal) {
        this.eventId = eventId;
        this.useSecondResolution = useSecondResolution;
        this.cal = cal;
        
        setCaption("Schedule Event");
        setWidth("250px");
        setModal(true);
        center();
        
        addComponent(scheduleLayout());
    }
    
    public CalendarScheduleWindow(CalendarComponentEvents.EventClick event, 
            BasicEvent basicEvent, 
            Calendar cal){
        this.event = event;
        this.basicEvent = basicEvent;
        this.cal = cal;
        
        setCaption("Scheduled Event");
        setWidth("250px");
        setModal(true);
        center();
        
        addComponent(populateSchedule(event));
    }
    
    VerticalLayout scheduleLayout(){
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

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
                
        return layout;
    }
    
    VerticalLayout populateSchedule(CalendarComponentEvents.EventClick event){
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setSpacing(true);

        eventType = createEventTypelect();
        eventType.setValue(basicEvent.getEventType());
        layout.addComponent(eventType);
        
        startDate = createDateField("Start Date: ");
        startDate.setValue(event.getCalendarEvent().getStart());
        layout.addComponent(startDate);
        
        endDate = createDateField("End Date: ");
        endDate.setValue(event.getCalendarEvent().getEnd());
        layout.addComponent(endDate);
        
        caption = createTextField("Caption: ");
        caption.setValue(event.getCalendarEvent().getCaption());
        layout.addComponent(caption);
        
        location = createTextField("Where: ");
        location.setValue(getBasicEvent().getLocation());
        layout.addComponent(location);
        
        description = new TextArea("Description: ");
        description.setWidth("100%");
        description.setRows(3);
        description.setValue(event.getCalendarEvent().getDescription());
        layout.addComponent(description);
        
        color = createStyleNameSelect();
        color.setValue(basicEvent.getStyleName());
        layout.addComponent(color);
        
        eventDataId = createTextField("id: ");
        eventDataId.setValue(getBasicEvent().getEventId());
        eventDataId.setVisible(false);
        layout.addComponent(eventDataId);
                
        editEventButton = new Button("EDIT", editEventBtnListener);
        editEventButton.setWidth("100%");
                
        deleteEventButton = new Button("DELETE", deleteEventBtnListener);
        deleteEventButton.setWidth("100%");
        
        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setSpacing(true);
        buttons.setWidth("100%");
        buttons.addComponent(deleteEventButton);
        buttons.addComponent(editEventButton);
        layout.addComponent(buttons);
        layout.setComponentAlignment(buttons, Alignment.BOTTOM_RIGHT);
                
        return layout;
    }
    
    int getEventId(){
        return eventId;
    }
    
    boolean isUseSecondResolution(){
        return useSecondResolution;
    }
    
    private DateField createDateField(String caption) {
        DateField f = new DateField(caption);
        f.setSizeFull();
        if (isUseSecondResolution()) {
            f.setResolution(DateField.RESOLUTION_SEC);
        } else {
            f.setResolution(DateField.RESOLUTION_MIN);
        }
            return f;
    }
    
    private Select createEventTypelect() {
        Select s = new Select("Event Type: ");
        s.setWidth("100%");
        s.setNullSelectionAllowed(false);
        s.addContainerProperty("c", String.class, "");
        s.setItemCaptionPropertyId("c");
        Item i = s.addItem("event1");
        i.getItemProperty("c").setValue("Legal Holiday");
        i = s.addItem("event2");
        i.getItemProperty("c").setValue("Special Holiday");
        i = s.addItem("event3");
        i.getItemProperty("c").setValue("Birthday");
        i = s.addItem("event4");
        i.getItemProperty("c").setValue("Meeting");
        s.setImmediate(true);
        
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
            if(eventType.getValue() == null){ getNotifications(); return; }
            if(startDate.getValue() == null){ getNotifications(); return; }
            if(endDate.getValue() == null){ getNotifications(); return; }
            if(caption.getValue() == null || caption.getValue().toString().trim().isEmpty()){ getNotifications(); return; }
            if(location.getValue() == null || location.getValue().toString().trim().isEmpty()){ getNotifications(); return; }
            if(description.getValue() == null || description.getValue().toString().trim().isEmpty()){ getNotifications(); return; }
            if(color.getValue() == null){ getNotifications(); return; }
            
            basicEvent = new BasicEvent();
            basicEvent.setEventType(eventType.getValue().toString());
            basicEvent.setStart(utilities.parsingDateWithTime(utilities.convertDateFormatWithTime(startDate.getValue().toString())));
            basicEvent.setEnd(utilities.parsingDateWithTime(utilities.convertDateFormatWithTime(endDate.getValue().toString())));
            basicEvent.setCaption(caption.getValue().toString().trim().toLowerCase());
            basicEvent.setLocation(location.getValue().toString().trim().toLowerCase());
            basicEvent.setDescription(description.getValue().toString().trim().toLowerCase());
            basicEvent.setStyleName(color.getValue().toString().trim().toLowerCase());
            
            GregorianCalendar start = new GregorianCalendar();
            GregorianCalendar end = new GregorianCalendar();
            start.setTime(basicEvent.getStart());
            end.setTime(basicEvent.getEnd());
            
            boolean result = calendarService.addNewEvent(basicEvent);
            if(result){
                close();
                getCalendar().addEvent(new BasicEvent(
                        basicEvent.getCaption(), 
                        basicEvent.getDescription(), 
                        basicEvent.getStyleName(), 
                        start.getTime(), 
                        end.getTime()
                ));
                getWindow().showNotification("Event Added!", Window.Notification.TYPE_TRAY_NOTIFICATION);
            }         
        }
    };
    
    Button.ClickListener editEventBtnListener = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            getWindow().showNotification("event data ID: "+eventDataId.getValue());
        }
    };
            
    Button.ClickListener deleteEventBtnListener = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
//            getWindow().showNotification("event data ID: "+eventDataId.getValue());
            Window sub = getDeleteWindow();
            sub.setModal(true);
            if(sub.getParent() == null){
                getApplication().getMainWindow().addWindow(sub);
            }
            sub.center();
        }
    };
    
    Window getDeleteWindow(){
        final Window sub = new Window("DELETE EVENT");
        sub.setWidth("250px");
        
        Button deleteBtn = new Button("DELETE EVENT?");
        deleteBtn.setWidth("100%");
        deleteBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean result = calendarService.removeEvent(utilities.convertStringToInteger(eventDataId.getValue().toString()));
                if(result){
                    getApplication().getMainWindow().removeWindow(sub);
                    close();
                    getCalendar().removeEvent(getEvent().getCalendarEvent());
                }
            }
        });
        
        sub.addComponent(deleteBtn);
        
        return sub;
    }
    
    CalendarComponentEvents.EventClick getEvent(){
        return event;
    }
    
    public BasicEvent getBasicEvent(){
        return basicEvent;
    }
    
    private void getNotifications(){
        getWindow().showNotification("Complete all Fields!", Window.Notification.TYPE_ERROR_MESSAGE);
    }    
    
    Calendar getCalendar(){
        return cal;
    }
}
