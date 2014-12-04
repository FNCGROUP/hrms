/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.calendar.serviceImpl;

import com.openhris.calendar.dao.CalendarDAO;
import com.openhris.calendar.service.BasicEvent;
import com.openhris.calendar.service.CalendarService;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class CalendarServiceImpl implements CalendarService{

    CalendarDAO calendarDAO = new CalendarDAO();
    
    @Override
    public List<BasicEvent> getAllEvents() {
        return calendarDAO.getAllEvents();
    }
    
}
