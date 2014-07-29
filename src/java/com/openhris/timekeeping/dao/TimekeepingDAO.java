/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.timekeeping.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jetdario
 */
public class TimekeepingDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public Date getPreviousPayrollDate(String employeeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Date date = new Date();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT payrollDate FROM payroll_table WHERE employeeId = '"+employeeId+"' ORDER BY payrollDate DESC LIMIT 1");
            while(rs.next()){
                date = util.parsingDate(rs.getString("payrollDate"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(TimekeepingDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return date;
    }
    
}
