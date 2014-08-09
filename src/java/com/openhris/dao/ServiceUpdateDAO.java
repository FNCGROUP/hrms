/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.queries.GetSQLQueryUpdate;
import com.hrms.utilities.ConvertionUtilities;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.contributions.model.Phic;
import com.openhris.contributions.model.Sss;
import com.openhris.contributions.model.Tax;
import com.openhris.employee.model.PositionHistory;
import com.openhris.payroll.model.Advances;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class ServiceUpdateDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    ConvertionUtilities conUtil = new ConvertionUtilities();
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public boolean updateEmployeeEmploymentInformation(String employeeId, List<PositionHistory> updateList){
        Connection conn = getConnection.connection();
        boolean result = false;
        PreparedStatement pstmt = null;
        try {     
            conn.setAutoCommit(false);
            for(PositionHistory ph : updateList){                
                pstmt = conn.prepareStatement("UPDATE employee SET "
                        + "firstname = ?, middlename = ?, lastname = ?, sssNo = ?, tinNo = ?, phicNo = ?, "
                        + "hdmfNo = ?, employmentStatus = ?, employmentWageStatus = ?, employmentWageEntry = ?, "
                        + "employmentWage = ?, allowance = ?, allowanceEntry = ?, branchId = ?, "
                        + "entryDate = ?, totalDependent = ?, bankAccountNo = ? WHERE employeeId = ? ");
                pstmt.setString(1, ph.getFirstname());
                pstmt.setString(2, ph.getMiddlename());
                pstmt.setString(3, ph.getLastname());
                pstmt.setString(4, ph.getSssNo());
                pstmt.setString(5, ph.getTinNo());
                pstmt.setString(6, ph.getPhicNo());
                pstmt.setString(7, ph.gethdmfNo());
                pstmt.setString(8, ph.getEmploymentStatus());
                pstmt.setString(9, ph.getEmploymentWageStatus());
                pstmt.setString(10, ph.getEmploymentWageEntry());
                pstmt.setDouble(11, ph.getEmploymentWage());
                pstmt.setDouble(12, ph.getAllowance());
                pstmt.setString(13, ph.getAllowanceEntry());
                pstmt.setInt(14, ph.getBranchId());
                pstmt.setString(15, conUtil.convertDateFormat(ph.getEntryDate().toString()));
                pstmt.setString(16, ph.getTotalDependent());
                pstmt.setString(17, ph.getBankAccountNo());
                pstmt.setString(18, employeeId);
                pstmt.executeUpdate();
                
                pstmt = conn.prepareStatement("UPDATE employee_position_history SET "
                        + "position = ?, corporate = ?, trade = ?, branch = ?, department = ?, "
                        + "entryDate = ?, branchId = ? WHERE employeeId = ? ");
                pstmt.setString(1, ph.getPosition());
                pstmt.setString(2, ph.getCompany());
                pstmt.setString(3, ph.getTrade());
                pstmt.setString(4, ph.getBranch());
                pstmt.setString(5, ph.getDepartment());
                pstmt.setString(6, conUtil.convertDateFormat(ph.getEntryDate().toString()));
                pstmt.setInt(7, ph.getBranchId());
                pstmt.setString(8, employeeId);
                pstmt.executeUpdate(); 
                
                pstmt = conn.prepareStatement("UPDATE employment_wage_history SET "
                        + "employmentWage = "+ph.getEmploymentWage()+" "
                        + "WHERE employeeId = '"+employeeId+"' ORDER BY id DESC LIMIT 1 ");
            }            
            conn.commit();
            System.out.println("Transaction commit...");
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
                System.out.println("Connection rollback...");
            } catch (SQLException ex1) {
                Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }                    
            Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                 }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
        
    public boolean updateEmploymentAllowanceForLiquidation(double afl, String employeeId){
        Connection conn = getConnection.connection();
        boolean result = false;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("UPDATE employee SET allowanceForLiquidation = ? WHERE employeeId = ?");
            pstmt.setDouble(1, afl);
            pstmt.setString(2, employeeId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                 }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
        
    public boolean updateSssTableData(List<Sss> sssList){
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            for(Sss s : sssList){
                pstmt = conn.prepareStatement("UPDATE sss_table SET minSalary = ?, "
                        + "maxSalary = ?, monthlySalaryCredit = ?, "
                        + "employeeContribution = ?, "
                        + "employerContribution = ?, "
                        + "totalContribution = ? "
                        + "WHERE id = ?");
                pstmt.setDouble(1, s.getMinSalary());
                pstmt.setDouble(2, s.getMaxSalary());
                pstmt.setDouble(3, s.getMonthlySalaryCredit());
                pstmt.setDouble(4, s.getEmployeeContribution());
                pstmt.setDouble(5, s.getEmployerContribution());
                pstmt.setDouble(6, s.getTotalMontlyContribution());
                pstmt.setInt(7, s.getId());
                pstmt.executeUpdate();                
            }
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean updatePhicTableData(List<Phic> phicList){
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            for(Phic p : phicList){
                pstmt = conn.prepareStatement("UPDATE phic_table SET minSalary = ?, "
                        + "maxSalary = ?, "
                        + "baseSalary = ?, "
                        + "employeeShare = ?, "
                        + "employerShare = ?, "
                        + "totalMonthlyPremium = ? "
                        + "WHERE id = ?");
                pstmt.setDouble(1, p.getMinSalary());
                pstmt.setDouble(2, p.getMaxSalary());
                pstmt.setDouble(3, p.getBaseSalary());
                pstmt.setDouble(4, p.getEmployeeShare());
                pstmt.setDouble(5, p.getEmployerShare());
                pstmt.setDouble(6, p.getTotalMonthlyPremium());
                pstmt.setInt(7, p.getId());
                pstmt.executeUpdate();                
            }
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean updateTaxTableData(List<Tax> taxList){
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            for(Tax t : taxList){
                pstmt = conn.prepareStatement("UPDATE tax_table SET taxRate1 = ?, taxRate2 = ?, taxRate3 = ?, taxRate4 = ?, "
                        + "taxRate5 = ?, taxRate6 = ?, taxRate7 = ? WHERE id = ? ");
                pstmt.setDouble(1, t.getTaxRate1());
                pstmt.setDouble(2, t.getTaxRate2());
                pstmt.setDouble(3, t.getTaxRate3());
                pstmt.setDouble(4, t.getTaxRate4());
                pstmt.setDouble(5, t.getTaxRate5());
                pstmt.setDouble(6, t.getTaxRate6());
                pstmt.setDouble(7, t.getTaxRate7());
                pstmt.setInt(8, t.getId());
                pstmt.executeUpdate();
                
                result = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
          
    public boolean updateUserRole(int id, String role){
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("UPDATE user_ SET userRole = ? WHERE id = ?");
            pstmt.setString(1, role);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean updateUserPassword(int id, String password){
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("UPDATE user_ SET password_ = ? WHERE id = ?");
            pstmt.setString(1, password);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean removeUser(int id){
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("DELETE FROM user_ WHERE id = ?");
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }

    public boolean removeCorporateAssignedForUser(int corporateId, int userId){
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("DELETE FROM user_corporate_access WHERE userId = ? AND corporateId = ?");
            pstmt.setInt(1, userId);
            pstmt.setInt(2, corporateId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean removeTradeAssignedForUser(int tradeId, int userId){
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("DELETE FROM user_trade_access WHERE userId = ? AND tradeId = ?");
            pstmt.setInt(1, userId);
            pstmt.setInt(2, tradeId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean removeBranchAssignedForUser(int branchId, int userId){
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("DELETE FROM user_branch_access WHERE userId = ? AND branchId = ?");
            pstmt.setInt(1, userId);
            pstmt.setInt(2, branchId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }

    public boolean allowAccessOfUserToolbarMenu(int userId, String menu, boolean isAllowed){
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("UPDATE user_toolbar_menu_access SET "+menu+" = ? WHERE userId = ?");
            pstmt.setString(1, String.valueOf(isAllowed).toString());
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean allowAccessOfUserAdvanceAccess(int userId, String menu, boolean isAllowed){
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("UPDATE user_advance_access SET "+menu+" = ? WHERE userId = ?");
            pstmt.setString(1, String.valueOf(isAllowed).toString());
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceUpdateDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
}
