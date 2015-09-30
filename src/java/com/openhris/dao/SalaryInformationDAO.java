/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.EmploymentInformation;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class SalaryInformationDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public EmploymentInformation getEmployeeSalaryInformation(String employeeId){
        EmploymentInformation employeeInformation = new EmploymentInformation();
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM employee WHERE employeeId = '"+employeeId+"' "
                    + "AND (currentStatus IS NULL OR currentStatus = 'resigned') ");
            while(rs.next()){
                employeeInformation.setEmploymentStatus(rs.getString("employmentStatus"));
                employeeInformation.setEmploymentWageStatus(rs.getString("employmentWageStatus"));
                employeeInformation.setEmploymentWageEntry(rs.getString("employmentWageEntry"));
                employeeInformation.setEmploymentWage(util.convertStringToDouble(rs.getString("employmentWage")));
                employeeInformation.setAllowance(util.convertStringToDouble(rs.getString("allowance")));
                employeeInformation.setAllowanceEntry(rs.getString("allowanceEntry"));
                employeeInformation.setAfl(util.convertStringToDouble(rs.getString("allowanceForLiquidation")));
                employeeInformation.setSssNo(rs.getString("sssNo"));
                employeeInformation.setTinNo(rs.getString("tinNo"));
                employeeInformation.setPhicNo(rs.getString("phicNo"));
                employeeInformation.setHdmfNo(rs.getString("hdmfNo"));
                employeeInformation.setTotalDependent(rs.getString("totalDependent"));
                employeeInformation.setEntryDate(util.parsingDate(rs.getString("entryDate")));
                employeeInformation.setBankAccountNo(rs.getString("bankAccountNo"));
                employeeInformation.setCurrentStatus((rs.getString("currentStatus") == null ? "employed" : rs.getString("currentStatus")));
                employeeInformation.setEndDate((util.parsingDate(rs.getString("endDate")) == null) ? new Date() : util.parsingDate(rs.getString("endDate")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(SalaryInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalaryInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return employeeInformation;
    }
    
    public boolean updateEmployeeSalaryInformation(String employeeId, EmploymentInformation ei){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("UPDATE employee SET "
                    + "employmentStatus = ?, "
                    + "employmentWageStatus = ?, "
                    + "employmentWageEntry = ?, "
                    + "employmentWage = ?, "
                    + "allowanceForLiquidation = ?, "
                    + "sssNo = ?, "
                    + "tinNo = ?, "
                    + "phicNo = ?, "
                    + "hdmfNo = ?, "
                    + "totalDependent = ? "
                    + "WHERE employeeId = ? ");
            pstmt.setString(1, ei.getEmploymentStatus());
            pstmt.setString(2, ei.getEmploymentWageStatus());
            pstmt.setString(3, ei.getEmploymentWageEntry());
            pstmt.setDouble(4, ei.getEmploymentWage());
            pstmt.setDouble(5, ei.getAfl());
            pstmt.setString(6, ei.getSssNo());
            pstmt.setString(7, ei.getTinNo());
            pstmt.setString(8, ei.getPhicNo());
            pstmt.setString(9, ei.gethdmfNo());
            pstmt.setString(10, ei.getTotalDependent());
            pstmt.setString(11, employeeId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(SalaryInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalaryInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean updateEmployeeContributionBranch(String employeeId, int branchId, String remarks){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("INSERT INTO employee_contribution_main(employeeId, branchId, remarks, dateTransferred) "
                    + "VALUES(?, ?, ?, now())");
            pstmt.setString(1, employeeId);
            pstmt.setInt(2, branchId);
            pstmt.setString(3, remarks.toLowerCase());
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(SalaryInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalaryInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean editEmploymentDateEntry(String employeeId, String entryDate){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("UPDATE employee SET entryDate = ? WHERE employeeId = ? ");
            pstmt.setString(1, entryDate);
            pstmt.setString(2, employeeId);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement("UPDATE employee_position_history "
                    + "SET entryDate = ? "
                    + "WHERE employeeId = ? "
                    + "ORDER BY id DESC "
                    + "LIMIT 1");
            pstmt.setString(1, entryDate);
            pstmt.setString(2, employeeId);
            pstmt.executeUpdate();
            
            conn.commit();
            System.out.println("Transaction commit...");
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
                System.out.println("Connection rollback...");
            } catch (SQLException ex1) {
                Logger.getLogger(SalaryInformationDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(SalaryInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalaryInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }    
    
    public boolean updateBankAccountNo(String employeeId, String bankAccountNo){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("UPDATE employee SET bankAccountNo = ? WHERE employeeId = ? ");
            pstmt.setString(1, bankAccountNo);
            pstmt.setString(2, employeeId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(SalaryInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(SalaryInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
}
