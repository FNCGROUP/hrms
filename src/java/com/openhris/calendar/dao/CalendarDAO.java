/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.calendar.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.calendar.service.BasicEvent;
import com.openhris.commons.OpenHrisUtilities;
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
    OpenHrisUtilities util = new OpenHrisUtilities();
    
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
                basicEvent.setCaption(rs.getString("caption"));
                basicEvent.setDescription(rs.getString("description"));
                basicEvent.setStyleName(rs.getString("styleName"));
                try {
                    basicEvent.setStart(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(rs.getString("start")));
                    basicEvent.setEnd(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(rs.getString("end")));
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
    
    public boolean insertNewEvent(BasicEvent basicEvent){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("INSERT INTO SET "
                    + "start = ?, "
                    + "end = ?, "
                    + "caption = ?, "
                    + "location = ?, "
                    + "description = ?, "
                    + "styleName = ?, "
                    + "eventType = ?, "
                    + "employeeId = ?, "
                    + "datePosted = now()");
            pstmt.setString(1, util.convertDateFormat(basicEvent.getStart().toString()));
            pstmt.setString(2, util.convertDateFormat(basicEvent.getEnd().toString()));
            pstmt.setString(3, basicEvent.getCaption());
            pstmt.setString(4, basicEvent.getLocation());
            pstmt.setString(5, basicEvent.getDescription());
            pstmt.setString(6, basicEvent.getStyleName());
            pstmt.setString(7, basicEvent.getEventType());
            pstmt.setString(8, basicEvent.getEmployeeId());
            pstmt.executeUpdate();
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
    
}
