/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.company.events;

import com.hrms.calendarClasses.BasicEvent;
import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.utilities.ConvertionUtilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class BasicEventBean {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    ConvertionUtilities conUtil = new ConvertionUtilities();
    
    private String eventType;
    private String caption;
    private String description;
    private String startDate;
    private String endDate;
    private String location; 
    private String employeeId;
    private String styleName;
    List<BasicEvent> returnEventLists = new ArrayList<BasicEvent>();
    
    public void setEventType(String eventType){
        this.eventType = eventType;
    }
    
    public String getEventType(){
        return eventType;
    }
    
    public void setCaption(String caption){
        this.caption = caption;
    }
    
    public String getCaption(){
        return caption;
    }
    
    public void setDescription(String description){
        this.description = description;
    }
    
    public String getDescription(){
        return description;
    }
    
    public void setStartDate(String startDate){
        this.startDate = startDate;
    }
    
    public String getStartDate(){
        return startDate;
    }
    
    public void setEndDate(String endDate){
        this.endDate = endDate;
    }
    
    public String getEndDate(){
        return endDate;
    }
    
    public void setLocation(String location){
        this.location = location;
    }
    
    public String getLocation(){
        return location;
    }
    
    public void setEmployeeId(String employeeId){
        this.employeeId = employeeId;
    }
    
    public String getEmployeeId(){
        return employeeId;
    }
    
    public void setStyleName(String styleName){
        this.styleName = styleName;
    }
    
    public String getStyleName(){
        return styleName;
    }
    
    public Boolean saveNewEvent(){
        Connection conn = getConnection.connection();
        Boolean result = false;
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO calendar_events(startDate, endDate, caption, location, description, "
                    + "color, eventType, employeeId, datePosted) VALUES(?, ?, ?, ?, ?, ?, ?, ?, now())");
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            pstmt.setString(3, caption);
            pstmt.setString(4, location);
            pstmt.setString(5, description);
            pstmt.setString(6, styleName);
            pstmt.setString(7, eventType);
            pstmt.setString(8, employeeId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(BasicEventBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BasicEventBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean editEvent(String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        try {
            PreparedStatement pstmt = conn.prepareStatement("UPDATE calendar_events SET startDate = ?, endDate = ?, caption = ?, location = ?, "
                    + "description = ?, color = ?, eventType = ?, employeeId = ?, datePosted = now() WHERE id = '"+id+"' ");
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            pstmt.setString(3, caption);
            pstmt.setString(4, location);
            pstmt.setString(5, description);
            pstmt.setString(6, styleName);
            pstmt.setString(7, eventType);
            pstmt.setString(8, employeeId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(BasicEventBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BasicEventBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public List eventsList(ResultSet rs){
        try {
            while(rs.next()){
                BasicEvent event = new BasicEvent();
                event.caption = rs.getString("caption");
                event.description = rs.getString("description");
                event.styleName = rs.getString("color");
                try {
                    event.start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(rs.getString("startDate"));
                    event.end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(rs.getString("endDate"));
                } catch (ParseException ex) {
                    Logger.getLogger(BasicEventBean.class.getName()).log(Level.SEVERE, null, ex);
                }
                returnEventLists.add(event);
            }            
            
        } catch (SQLException ex) {
            Logger.getLogger(BasicEventBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnEventLists;
    }
}
