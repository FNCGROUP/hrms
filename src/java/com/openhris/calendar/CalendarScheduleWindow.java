/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.calendar;

import com.openhris.calendar.service.BasicEvent;
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

/**
 *
 * @author jetdario
 */
public class CalendarScheduleWindow extends Window {

    BasicEvent basicEvent = new BasicEvent();
    private int eventId;
    private boolean useSecondResolution;
    
    private Select eventType;        
    private DateField startDate;
    private DateField endDate;
    private TextField caption;
    private TextField location;
    private TextArea description;
    private Select color;
    private Button deleteEventButton;    
    private Button saveEventButton;
    private Button editEventButton;
    
    private Date dateStart;
    private Date dateEnd;

    public CalendarScheduleWindow() {
    }
        
    public CalendarScheduleWindow(int eventId, 
            boolean useSecondResolution) {
        this.eventId = eventId;
        this.useSecondResolution = useSecondResolution;
        
        setCaption("Schedule Event");
        setWidth("250px");
        setModal(true);
        center();
        
        addComponent(scheduleLayout());
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
    
    public BasicEvent getBasicEvent(){
        return basicEvent;
    }
}
