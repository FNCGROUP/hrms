/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.beans;

import com.hrms.dbconnection.GetSQLConnection;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class EmployeeDependentBean {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    
    private String employeeId;
    private String name;
    private String dob;
    
    public void setEmployeeId(String employeeId){ this.employeeId = employeeId; }
    public void setName(String name){ this.name = name; }
    public void setDob(String dob){ this.dob = dob; }
    
    public Boolean insertNewDependent(){
        Connection conn = getConnection.connection();
        Boolean result = false;
        try{
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO employee_dependent(employeeId, name, dob) VALUES(?, ?, ?)");
            pstmt.setString(1, employeeId);
            pstmt.setString(2, name);
            pstmt.setString(3, dob);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDependentBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDependentBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean removeDependent(String id){
        Connection conn  = getConnection.connection();
        Boolean result = true;
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM employee_dependent WHERE id = '"+id+"' ");
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeDependentBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeDependentBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
}
