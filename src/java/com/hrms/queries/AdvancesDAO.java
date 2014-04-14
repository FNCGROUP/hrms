/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.queries;

import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.entities.Advances;
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
 * @author jet
 */
public class AdvancesDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    
    public List getAdvancesBySalaryId(Integer salaryId){
        List<Advances> advancesList = new ArrayList<Advances>();
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM advances WHERE salaryId = "+salaryId+" ");
            while(rs.next()){
                Advances advances = new Advances();
                advances.setAmount(Double.parseDouble(rs.getString("amount")));
                advances.setAdvancesType(rs.getString("advancesType"));
                advances.setParticulars(rs.getString("particulars"));
                advances.setDatePosted(rs.getString("datePosted"));
                advances.setEmployeeId(rs.getString("employeeId"));
                advances.setRowStatus(rs.getString("rowStatus"));
                advances.setRemarks(rs.getString("remarks"));
                advances.setDateRemoved(rs.getString("dateRemoved"));
                advancesList.add(advances);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdvancesDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdvancesDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return advancesList;
    }
    
    public Boolean checkIfAdvancesExistForSalary(Integer salaryId){
        Boolean result = false;
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) AS result FROM advances WHERE salaryId = "+salaryId+" ");
            while(rs.next()){
                if(rs.getString("result").equals("0")){
                    result = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdvancesDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdvancesDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
}
