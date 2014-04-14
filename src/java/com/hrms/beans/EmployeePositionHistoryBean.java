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
public class EmployeePositionHistoryBean {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    
    private String employeeId;
    private String position;
    private Integer branchId;
    private String department;
    private String entryDate;
    
    public void setEmployeeId(String employeeId){ this.employeeId = employeeId; }
    public void setPosition(String position){ this.position = position; }
    public void setBranchId(Integer branchName){ this.branchId = branchName; }
    public void setDepartment(String department){ this.department = department; }
    public void setEntryDate(String entryDate){ this.entryDate = entryDate; }
    
    public Boolean updatePosition(){
        Connection conn = getConnection.connection();
        Boolean result = false;
        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO employee_position_history(employeeId, position, department, entryDate, branchId) "
                    + "VALUES(?, ?, ?, ?, ?)");
            pstmt.setString(1, employeeId);
            pstmt.setString(2, position);
            pstmt.setString(3, department);
            pstmt.setString(4, entryDate);
            pstmt.setInt(5, branchId);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement("UPDATE employee SET branchId = ? WHERE employeeId = ?");
            pstmt.setInt(1, branchId);
            pstmt.setString(2, employeeId);
            pstmt.executeUpdate();
            
            conn.commit();
            System.out.println("Transaction commit...");
            result = true;
        } catch (SQLException ex) {
            if(conn != null){
                try {
                    conn.rollback();
                    System.out.println("Connection rollback...");
                } catch (SQLException ex1) {
                    Logger.getLogger(EmployeePositionHistoryBean.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
            Logger.getLogger(EmployeePositionHistoryBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeePositionHistoryBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public Boolean removePositionHistory(String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM employee_position_history WHERE id = '"+id+"' ");
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeePositionHistoryBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeePositionHistoryBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
}
