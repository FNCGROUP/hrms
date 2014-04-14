/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.queries;

import com.hrms.dbconnection.GetSQLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class SalaryDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    
    public SalaryDAO(){        
    }
    
    public String getAttendanceFirsDay(String salaryId){
        String attendanceFrom = null;
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT attendancePeriodFrom FROM salary WHERE id = '"+salaryId+"' ");
            while(rs.next()){
                attendanceFrom = rs.getString("attendancePeriodFrom");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalaryDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return attendanceFrom;
    }
    
    public String getAttendanceLastDay(String salaryId){
        String attendanceTo = null;
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT attendancePeriodTo FROM salary WHERE id = '"+salaryId+"' ");
            while(rs.next()){
                attendanceTo = rs.getString("attendancePeriodTo");
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalaryDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return attendanceTo;
    }
    
    public Double getTotalAdvancesPerSalaryId(Integer salaryId){
        Double totalAdvances = 0.0;
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT ifnull(advances, 0) AS advances FROM salary WHERE id = "+salaryId+" ");
            while(rs.next()){
                totalAdvances = Double.parseDouble(rs.getString("advances"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalaryDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return totalAdvances;
    }
    
    public Double getTotalAdjustmentsPerSalaryId(Integer salaryId){
        Double adjustments = 0.0;
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT ifnull(sum(amount), 0) AS amount FROM adjustments WHERE salaryId = "+salaryId+" ");
            while(rs.next()){
                adjustments = Double.parseDouble(rs.getString("amount"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalaryDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return adjustments;
    }
    
    public Double getAmountReceivable(Integer salaryId){
        Double amountReceivable = 0.0;
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT amountReceivable FROM salary WHERE id = '"+salaryId+"' ");
            while(rs.next()){
                amountReceivable = Double.parseDouble(rs.getString("amountReceivable"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalaryDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalaryDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return amountReceivable;
    }
}
