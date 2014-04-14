/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.calendarClasses;

import com.hrms.dbconnection.GetSQLConnection;
import com.vaadin.addon.calendar.event.CalendarEvent;
import com.vaadin.addon.calendar.event.CalendarEventEditor;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jet
 */
public class BasicEvent implements CalendarEventEditor, CalendarEvent.EventChangeNotifier {

    GetSQLConnection getConnection = new GetSQLConnection();
    
    private Integer eventId;
    public String caption;
    public String description;
    public Date end;
    public Date start;
    private String startDate;
    private String endDate;
    private String location;
    public String styleName;
    private Boolean isAllDay = false;
    private String color;
    private String eventType;
    private String employeeId;
    
    private transient List<EventChangeListener> listeners = new ArrayList<EventChangeListener>();
    
    public BasicEvent(){
        
    }
    
    public BasicEvent(Date start, Date end){
        this.start = start;
        this.end = end;
    }
    
    public BasicEvent(String caption, String description, String styleName, Date start, Date end){
        this.caption = caption;
        this.description = description;
        this.start = start;
        this.end = end;
        this.styleName = styleName;
    }
    
    public void setEventId(Integer eventId){
        this.eventId = eventId;
    }
    
    @Override
    public void setCaption(String caption) {
        this.caption = caption;
        fireEventChange();
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
        fireEventChange();
    }

    @Override
    public void setEnd(Date end) {
        this.end = end;
        fireEventChange();
    }

    public void setEndDate(String endDate){
        this.endDate = endDate;
    }
    
    @Override
    public void setStart(Date start) {
        this.start = start;
        fireEventChange();
    }

    public void setStartDate(String startDate){
        this.startDate = startDate;
    }
    
    public void setLocation(String location){
        this.location = location;
    }
    
    @Override
    public void setStyleName(String styleName) {
        this.styleName = styleName;
        fireEventChange();
    }

    @Override
    public void setAllDay(boolean isAllDay) {
        this.isAllDay = isAllDay;
        fireEventChange();
    }
    
    public void setEventType(String eventType){
        this.eventType = eventType;
    }
    
    public void setEmployeeId(String employeeId){
        this.employeeId = employeeId;
    }
    
    public Integer getEventId(){
        return eventId;
    }
    
    @Override
    public Date getStart() {
        return start;
    }
    
    public String getStartDate(){
        return startDate;
    }
    
    @Override
    public Date getEnd() {
        return end;
    }

    public String getEndDate(){
        return endDate;
    }
    
    @Override
    public String getCaption() {
        return caption;        
    }

    public String getLocation(){
        return location;
    }
    
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getStyleName() {
        return styleName;
    }

    @Override
    public boolean isAllDay() {
        return isAllDay;
    }
    
    public String getEventType(){
        return eventType;
    }
    
    public String getEmployeeId(){
        return employeeId;
    }
    
    @Override
    public void addListener(EventChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(EventChangeListener listener) {
        listeners.remove(listener);
    }
    
    protected void fireEventChange() {
        EventChange event = new EventChange(this);

        for (EventChangeListener listener : listeners) {
            listener.eventChange(event);
        }
    }
     
}
