/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.calendar.service;

import com.vaadin.addon.calendar.ui.CalendarComponentEvent;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents;
import java.util.List;

/**
 *
 * @author jetdario
 */
public interface CalendarService {
    
    public List<BasicEvent> getAllEvents();
    
    public BasicEvent getEvent(CalendarComponentEvents.EventClick events);
    
    public boolean addNewEvent(BasicEvent basicEvent);
    
    public boolean editEvent(BasicEvent basicEvent);
    
    public boolean removeEvent(int eventDataId);
}
