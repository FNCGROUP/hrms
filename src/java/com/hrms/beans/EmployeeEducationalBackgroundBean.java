/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.beans;

import com.hrms.dbconnection.GetSQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class EmployeeEducationalBackgroundBean {
    
    GetSQLConnection getConnection = new GetSQLConnection();
        
    private String employeeId;
    private String course;
    private String school;
    private String yearGraduated;
    
    public void setEmployeeId(String employeeId){ this.employeeId = employeeId; }
    public void setCourse(String course){ this.course = course; }
    public void setSchool(String school){ this.school = school; }
    public void setYearGraduated(String yearGraduated){ this.yearGraduated = yearGraduated; }
    
    public Boolean insertNewEducationalBackground(){
        Connection conn = getConnection.connection();
        Boolean result = false;
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO employee_educational_background(employeeId, course, school, yearGraduated) "
                    + "VALUES(?, ?, ?, ?)");
            pstmt.setString(1, employeeId);
            pstmt.setString(2, course);
            pstmt.setString(3, school);
            pstmt.setString(4, yearGraduated);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeEducationalBackgroundBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeEducationalBackgroundBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean removeEducationalBackground(String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM employee_educational_background WHERE id = '"+id+"' ");
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeEducationalBackgroundBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeEducationalBackgroundBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
}
