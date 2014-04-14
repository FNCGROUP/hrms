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
public class EmployeeCharacterReferencesBean {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    
    private String employeeId;
    private String name;
    private String occupation;
    private String address;
    private String contactNo;
    
    public void setEmployeeId(String employeeId){ this.employeeId = employeeId; }
    public void setName(String name){ this.name = name; }
    public void setOccupation(String occupation){ this.occupation = occupation; }
    public void setAddress(String address){ this.address = address; }
    public void setContactNo(String contactNo){ this.contactNo = contactNo; }
    
    public Boolean insertNewCharacterReference(){
        Connection conn = getConnection.connection();
        Boolean result = false;
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO employee_character_references(employeeId, name, occupation, address, contactNo) "
                    + "VALUES(?, ?, ?, ?, ?)");
            pstmt.setString(1, employeeId);
            pstmt.setString(2, name);
            pstmt.setString(3, occupation);
            pstmt.setString(4, address);
            pstmt.setString(5, contactNo);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeCharacterReferencesBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeCharacterReferencesBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
                
        return result;
    }
    
    public Boolean removeCharacterReference(String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM employee_character_references WHERE id = '"+id+"' ");
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeCharacterReferencesBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeCharacterReferencesBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
}
