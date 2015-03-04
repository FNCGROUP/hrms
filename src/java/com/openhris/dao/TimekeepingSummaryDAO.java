/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.timekeeping.model.Timekeeping;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jetdario
 */
public class TimekeepingSummaryDAO {
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public List<Timekeeping> getAttendanceByBranchAndEmployee(int branchId, String employeeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        List<Timekeeping> timekeepingSummaryList = new ArrayList<Timekeeping>();
        List<Integer> timekeepingIdList = new ArrayList<Integer>();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT DISTINCT payrollId FROM timekeeping_table "
                    + "WHERE branchId = "+branchId+" AND employeeId = '"+employeeId+"'");
            while(rs.next()){
                timekeepingIdList.add(util.convertStringToInteger(rs.getString("payrollId")));
            }
            
            for(Object id : timekeepingIdList){
                Timekeeping t = new Timekeeping();
                t.setId(util.convertStringToInteger(id.toString()));
                
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT lates FROM fnc_hris_db.timekeeping_summary "
                        + "WHERE payrollId = "+id+" ");
                while(rs.next()){
                    t.setLates(util.convertStringToDouble(rs.getString("lates")));
                }
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(TimekeepingSummaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return timekeepingSummaryList;
    }
}
