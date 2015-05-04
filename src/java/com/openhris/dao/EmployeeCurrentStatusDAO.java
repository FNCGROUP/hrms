/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jetdario
 */
public class EmployeeCurrentStatusDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public boolean removeEmployee(String employeeId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("UPDATE employee SET currentStatus = ? WHERE employeeId = ? ");
            pstmt.setString(1, "removed");
            pstmt.setString(2, employeeId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeCurrentStatusDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeCurrentStatusDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }   
    
}
