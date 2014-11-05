/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.employee.model.Employee;
import com.openhris.employee.model.EmploymentInformation;
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
                employeeInformation.setSssNo(rs.getString("sssNo"));
                employeeInformation.setTinNo(rs.getString("tinNo"));
                employeeInformation.setPhicNo(rs.getString("phicNo"));
                employeeInformation.setHdmfNo(rs.getString("hdmfNo"));
                employeeInformation.setTotalDependent(rs.getString("totalDependent"));
                employeeInformation.setEntryDate(util.parsingDate(rs.getString("entryDate")));
                employeeInformation.setBankAccountNo(rs.getString("bankAccountNo"));
                employeeInformation.setCurrentStatus((rs.getString("currentStatus") == null ? "employed" : rs.getString("currentStatus")));
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
    
    public boolean updateEmployeeSalaryInformation(String employeeId, EmploymentInformation employeeInformation){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("UPDATE employee SET employmentStatus = ?, employmentWageStatus = ?, employmentWageEntry = ?, "
                    + "employmentWage = ?, allowance = ?, allowanceEntry = ?, sssNo = ?, tinNo = ?, phicNo = ?, hdmfNo = ?, totalDependent = ? "
                    + "WHERE employeeId = ? ");
            pstmt.setString(1, employeeInformation.getEmploymentStatus());
            pstmt.setString(2, employeeInformation.getEmploymentWageStatus());
            pstmt.setString(3, employeeInformation.getEmploymentWageEntry());
            pstmt.setDouble(4, employeeInformation.getEmploymentWage());
            pstmt.setDouble(5, employeeInformation.getAllowance());
            pstmt.setString(6, employeeInformation.getAllowanceEntry());
            pstmt.setString(7, employeeInformation.getSssNo());
            pstmt.setString(8, employeeInformation.getTinNo());
            pstmt.setString(9, employeeInformation.getPhicNo());
            pstmt.setString(10, employeeInformation.gethdmfNo());
            pstmt.setString(11, employeeInformation.getTotalDependent());
            pstmt.setString(12, employeeId);
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
}
