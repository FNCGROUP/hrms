/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.beans;

import com.hrms.dbconnection.GetSQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class EmploymentRecordsBean {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    
    private String employeeId;
    private String dateStart;
    private String dateEnd;
    private String position;
    private String company;
    
    public void setEmployeeId(String employeeId){ this.employeeId = employeeId; }
    public void setDateStart(String dateStart){ this.dateStart = dateStart; }
    public void setDateEnd(String dateEnd){ this.dateEnd = dateEnd; }
    public void setPosition(String position){ this.position = position; }
    public void setCompany(String company){ this.company = company; }
    
    public Boolean addEmploymentRecords(){
        Connection conn = getConnection.connection();
        Boolean result = false;
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO employment_record(employeeId, startDate, endDate, position, company) "
                    + "VALUES(?, ?, ?, ?, ?)");
            pstmt.setString(1, employeeId);
            pstmt.setString(2, dateStart);
            pstmt.setString(3, dateEnd);
            pstmt.setString(4, position);
            pstmt.setString(5, company);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmploymentRecordsBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmploymentRecordsBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean removeEmploymentRecord(String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM employment_record WHERE id = '"+id+"' ");
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmploymentRecordsBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmploymentRecordsBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
}
