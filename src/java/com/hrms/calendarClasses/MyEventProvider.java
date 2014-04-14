/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.calendarClasses;

import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.utilities.ConvertionUtilities;
import com.vaadin.addon.calendar.event.CalendarEvent;
import com.vaadin.addon.calendar.event.CalendarEventProvider;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author jet
 */
public class MyEventProvider implements CalendarEventProvider {

    private List<CalendarEvent> events = new ArrayList<CalendarEvent>();
    //BasicEvent event = new BasicEvent();
    GetSQLConnection getConnection = new GetSQLConnection();
    ConvertionUtilities conUtil = new ConvertionUtilities();
        
    public MyEventProvider() {       
        events = new ArrayList<CalendarEvent>();
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());

        /*Date start = cal.getTime();
        cal.add(GregorianCalendar.HOUR, 1);
        Date end = cal.getTime();
        
        event.setStyleName("color1");
        event.setCaption("My Event");
        event.setDescription("My Event Description");
        event.setStart(start);
        event.setEnd(end);
        events.add(event); */        
        
    }
        
    public void addEvent(CalendarEvent BasicEvent) {
        events.add(BasicEvent);
    }
    
    @Override
    public List<CalendarEvent> getEvents(Date startDate, Date endDate) {        
        return events;
    }
    
}
