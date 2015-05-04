/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.dao;

import com.hrms.classes.GlobalVariables;
import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.calendar.service.BasicEvent;
import com.openhris.commons.OpenHrisUtilities;
import com.vaadin.addon.calendar.ui.CalendarComponentEvents;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jetdario
 */
public class CalendarDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities utilities = new OpenHrisUtilities();
    
    public List<BasicEvent> getAllEvents(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        List<BasicEvent> eventList = new ArrayList<BasicEvent>();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM calendar_events");
            while(rs.next()){
                BasicEvent basicEvent = new BasicEvent();
                basicEvent.setEventId(utilities.convertStringToInteger(rs.getString("id")));
                basicEvent.setCaption(rs.getString("caption"));
                basicEvent.setDescription(rs.getString("description"));
                basicEvent.setStyleName(rs.getString("styleName"));
                try {
                    basicEvent.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(rs.getString("startDate")));
                    basicEvent.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(rs.getString("endDate")));
                } catch (ParseException ex) {
                    Logger.getLogger(CalendarDAO.class.getName()).log(Level.SEVERE, null, ex);
                }
                basicEvent.setLocation(rs.getString("location"));
                basicEvent.setEventType(rs.getString("eventType"));
                eventList.add(basicEvent);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CalendarDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return eventList;
    }
    
    public boolean addNewEvent(BasicEvent basicEvent){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("INSERT INTO calendar_events SET "
                    + "startDate = ?, "
                    + "endDate = ?, "
                    + "caption = ?, "
                    + "location = ?, "
                    + "description = ?, "
                    + "styleName = ?, "
                    + "eventType = ?, "
                    + "username = ?, "
                    + "datePosted = now()");
            pstmt.setString(1, utilities.convertDateFormatWithTime(basicEvent.getStart().toString()));
            pstmt.setString(2, utilities.convertDateFormatWithTime(basicEvent.getEnd().toString()));
            pstmt.setString(3, basicEvent.getCaption());
            pstmt.setString(4, basicEvent.getLocation());
            pstmt.setString(5, basicEvent.getDescription());
            pstmt.setString(6, basicEvent.getStyleName());
            pstmt.setString(7, basicEvent.getEventType());
            pstmt.setString(8, GlobalVariables.getUsername());
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CalendarDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public BasicEvent getEvent(CalendarComponentEvents.EventClick events){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        BasicEvent basicEvent = new BasicEvent();
           
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM calendar_events WHERE "
                    + "startDate = '"+utilities.convertDateFormatWithTime(events.getCalendarEvent().getStart().toString())+"' AND "
                    + "endDate =  '"+utilities.convertDateFormatWithTime(events.getCalendarEvent().getEnd().toString())+"' AND "
                    + "caption = '"+utilities.replaceSingleQuote(events.getCalendarEvent().getCaption())+"' AND "
                    + "description = '"+utilities.replaceSingleQuote(events.getCalendarEvent().getDescription())+"' ");
            while(rs.next()){
                basicEvent.setEventId(utilities.convertStringToInteger(rs.getString("id")));
                basicEvent.setEventType(rs.getString("eventType"));
                basicEvent.setLocation(rs.getString("location"));    
                basicEvent.setStyleName(rs.getString("styleName"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                stmt.close();
                rs.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(CalendarDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return basicEvent;
    }

    public boolean editEvent(BasicEvent basicEvent){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("UPDATE calendar_events "
                    + "SET startDate = ?, "
                    + "endDate = ?, "
                    + "caption = ?, "
                    + "location = ?, "
                    + "description = ?, "
                    + "styleName = ?, "
                    + "eventType = ? "
                    + "WHERE id = ? ");
            pstmt.setString(1, utilities.convertDateFormatWithTime(basicEvent.getStart().toString()));
            pstmt.setString(2, utilities.convertDateFormatWithTime(basicEvent.getEnd().toString()));
            pstmt.setString(3, basicEvent.getCaption());
            pstmt.setString(4, basicEvent.getLocation());
            pstmt.setString(5, basicEvent.getDescription());
            pstmt.setString(6, basicEvent.getStyleName());
            pstmt.setString(7, basicEvent.getEventType());
            pstmt.setInt(8, basicEvent.getEventId());
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(CalendarDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean removeEvent(int eventDataId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("DELETE FROM calendar_events WHERE id = ? ");
            pstmt.setInt(1, eventDataId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(CalendarDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                pstmt.close();
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(CalendarDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
}
