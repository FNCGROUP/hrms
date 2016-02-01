/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.serviceprovider;

import com.hrms.classes.GlobalVariables;
import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.Constant;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.dao.SalaryInformationDAO;
import com.openhris.model.EmploymentInformation;
import com.openhris.service.SalaryInformationService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jetdario
 */
public class SalaryInformationServiceImpl implements SalaryInformationService{
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    SalaryInformationDAO siDAO = new SalaryInformationDAO();

    @Override
    public EmploymentInformation getEmployeeSalaryInformation(String employeeId) {
        return siDAO.getEmployeeSalaryInformation(employeeId);
    }

    @Override
    public boolean updateEmployeeSalaryInformation(String employeeId, EmploymentInformation ei) {
        return siDAO.updateEmployeeSalaryInformation(employeeId, ei);
    }

    @Override
    public boolean updateEmployeeContributionBranch(String employeeId, int branchId, String remarks) {
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

    @Override
    public boolean editEmploymentDateEntry(String employeeId, String entryDate) {
        return siDAO.editEmploymentDateEntry(employeeId, entryDate);
    }

    @Override
    public boolean updateBankAccountNo(String employeeId, String bankAccountNo) {
        return siDAO.updateBankAccountNo(employeeId, bankAccountNo);
    }

    @Override
    public boolean updateEmploymentWage(double amount, 
            String remarks, 
            String employeeId) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("UPDATE employee SET "
                    + "employmentWage = ? "
                    + "WHERE employeeId = ? ");
            pstmt.setDouble(1, amount);
            pstmt.setString(2, employeeId);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement("INSERT INTO employee_logs "
                    + "SET EmployeeID = ?, "
                    + "Remarks = ?, "
                    + "DateRemarked = now(), "
                    + "UserID = ?");
            pstmt.setString(1, employeeId);
            pstmt.setString(2, "WAGE: "+amount+", "+remarks);
            pstmt.setInt(3, GlobalVariables.getUserId());
            pstmt.executeUpdate();
            
            conn.commit();
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(AllowanceInformationServiceImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }

    @Override
    public boolean updateEmploymentWageDetails(String column,
            String colValue, 
            String employeeId, 
            String remarks) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("UPDATE employee SET "
                    + ""+column+" = ? "
                    + "WHERE EmployeeID = ? ");
            pstmt.setString(1, colValue);
            pstmt.setString(2, employeeId);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement("INSERT INTO employee_logs "
                    + "SET EmployeeID = ?, "
                    + "Remarks = ?, "
                    + "DateRemarked = now(), "
                    + "UserID = ?");
            pstmt.setString(1, employeeId);
            pstmt.setString(2, Constant.getEmploymentWageDetailsColumn(column)+": "+colValue+", "+remarks);
            pstmt.setInt(3, GlobalVariables.getUserId());
            pstmt.executeUpdate();
            
            conn.commit();
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(AllowanceInformationServiceImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
}
