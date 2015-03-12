/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.dao;

import com.hrms.classes.GenerateCompanyId;
import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.contributions.model.Phic;
import com.openhris.contributions.model.Sss;
import com.openhris.employee.model.PostEmploymentInformationBean;
import com.openhris.payroll.dao.PayrollDAO;
import com.openhris.payroll.model.Payroll;
import com.openhris.timekeeping.model.Timekeeping;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class ServiceInsertDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    ServiceGetDAO serviceGet = new ServiceGetDAO();
    PayrollDAO payrollDAO = new PayrollDAO();
    
    public boolean insertNewEmployee(List<PostEmploymentInformationBean> insertList){
        Connection conn = getConnection.connection();
        boolean result = false;
        PreparedStatement pstmt = null;
        
        GenerateCompanyId companyId = new GenerateCompanyId();
        try {
            conn.setAutoCommit(false);
            for(PostEmploymentInformationBean ph : insertList){
                String employeeId = companyId.generateId(ph.getCompany(), ph.getTrade(), 
                        util.convertDateFormat(ph.getEntryDate().toString()));
                
                pstmt = conn.prepareStatement(" INSERT INTO employee(employeeId, firstname, middlename, "
                        + "lastname, sssNo, tinNo, phicNo, hdmfNo, employmentStatus, employmentWageStatus, "
                        + "employmentWageEntry, employmentWage, allowance, allowanceEntry, branchId, entryDate, "
                        + "totalDependent, bankAccountNo) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
                pstmt.setString(1, employeeId);
                pstmt.setString(2, ph.getFirstname().toLowerCase());
                pstmt.setString(3, ph.getMiddlename().toLowerCase());
                pstmt.setString(4, ph.getLastname().toLowerCase());
                pstmt.setString(5, ph.getSssNo());
                pstmt.setString(6, ph.getTinNo());
                pstmt.setString(7, ph.getPhicNo());
                pstmt.setString(8, ph.gethdmfNo());
                pstmt.setString(9, ph.getEmploymentStatus());
                pstmt.setString(10, ph.getEmploymentWageStatus());
                pstmt.setString(11, ph.getEmploymentWageEntry());
                pstmt.setDouble(12, ph.getEmploymentWage());
                pstmt.setDouble(13, ph.getAllowance());
                pstmt.setString(14, ph.getAllowanceEntry());
                pstmt.setInt(15, ph.getBranchId());
                pstmt.setString(16, util.convertDateFormat(ph.getEntryDate().toString()));
                pstmt.setString(17, ph.getTotalDependent());
                pstmt.setString(18, ph.getBankAccountNo());
                pstmt.executeUpdate();
                
                pstmt = conn.prepareStatement("INSERT INTO employee_position_history(employeeId, position, "
                        + "corporate, trade, branch, department, entryDate, branchId) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
                pstmt.setString(1, employeeId);
                pstmt.setString(2, ph.getPosition().toLowerCase());
                pstmt.setString(3, ph.getCompany());
                pstmt.setString(4, ph.getTrade());
                pstmt.setString(5, ph.getBranch());
                pstmt.setString(6, ph.getDepartment());
                pstmt.setString(7, util.convertDateFormat(ph.getEntryDate().toString()));
                pstmt.setInt(8, ph.getBranchId());
                pstmt.executeUpdate();
                
                pstmt = conn.prepareStatement("INSERT INTO employment_wage_history(employeeId, "
                        + "employmentWage, datePosted) VALUES(?, ?, ?)");
                pstmt.setString(1, employeeId);
                pstmt.setDouble(2, ph.getEmploymentWage());
                pstmt.setString(3, util.convertDateFormat(ph.getEntryDate().toString()));
                pstmt.executeUpdate();
                
                pstmt = conn.prepareStatement("INSERT INTO employee_contribution_main(employeeId, branchId) VALUES(?, ?)");
                pstmt.setString(1, employeeId);
                pstmt.setInt(2, ph.getBranchId());
                pstmt.executeUpdate();
            }
            conn.commit();
            System.out.println("Transaction commit...");
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
                System.out.println("Connection rollback...");
            } catch (SQLException ex1) {
                Logger.getLogger(ServiceInsertDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }                
            Logger.getLogger(ServiceInsertDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceInsertDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public boolean checkForDuplicateEntry(String firstname, 
            String middlename, 
            String lastname){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Boolean result = true;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) AS result FROM employee WHERE firstname = '"+firstname+"' AND middlename = '"+middlename+"' "
                    + "AND lastname = '"+lastname+"' AND currentStatus IS NULL");
            while(rs.next()){
                if(rs.getString("result").equals("0")){
                    result = false;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceInsertDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceInsertDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean insertNewSssData(List<Sss> sssList){
        Boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            for(Sss sss : sssList){
                pstmt = conn.prepareStatement("INSERT INTO sss_table(minSalary, maxSalary, monthlySalaryCredit, "
                        + "employeeContribution, employerContribution, totalContribution) VALUES(?, ?, ?, ?, ?, ?)");
                pstmt.setDouble(1, sss.getMinSalary());
                pstmt.setDouble(2, sss.getMaxSalary());
                pstmt.setDouble(3, sss.getMonthlySalaryCredit());
                pstmt.setDouble(4, sss.getEmployeeContribution());
                pstmt.setDouble(5, sss.getEmployerContribution());
                pstmt.setDouble(6, sss.getTotalMontlyContribution());
                pstmt.executeUpdate();
            }
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceInsertDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceInsertDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }

    public boolean insertNewPhicData(List<Phic> phicList){
        Boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            for(Phic p : phicList){
                pstmt = conn.prepareStatement("INSERT INTO phic_table(minSalary, maxSalary, baseSalary, "
                        + "totalMonthlyPremium, employeeShare, employerShare) VALUES(?, ?, ?, ?, ?, ?)");
                pstmt.setDouble(1, p.getMinSalary());
                pstmt.setDouble(2, p.getMaxSalary());
                pstmt.setDouble(3, p.getBaseSalary());
                pstmt.setDouble(6, p.getTotalMonthlyPremium());
                pstmt.setDouble(4, p.getEmployeeShare());
                pstmt.setDouble(5, p.getEmployerShare());                
                pstmt.executeUpdate();
            }
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(ServiceInsertDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceInsertDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
             
    public boolean insertForAdjustmentAndUpdatePayroll(int payrollId, 
            double adjustedAmount, 
            double amountReceivable, 
            double amountToBeReceive){
        Boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("INSERT INTO adjustments(salaryId, amount, remarks, datePosted) VALUES(?, ?, ?, now())");
            pstmt.setInt(1, payrollId);
            pstmt.setDouble(2, adjustedAmount);
            pstmt.setString(3, "for adjustment");
            pstmt.executeUpdate();

            amountReceivable = Math.round((amountReceivable + adjustedAmount)*100.0)/100.0;
            amountToBeReceive = amountReceivable;

            pstmt = conn.prepareStatement("UPDATE payroll_table SET amountToBeReceive = ?, amountReceivable = ? WHERE id = ? ");
            pstmt.setDouble(1, amountToBeReceive);
            pstmt.setDouble(2, amountReceivable);
            pstmt.setInt(3, payrollId);
            pstmt.executeUpdate();
            
            conn.commit();
            System.out.println("Transaction commit...");
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
                System.out.println("Connection rollback...");
            } catch (SQLException ex1) {
                Logger.getLogger(ServiceInsertDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(ServiceInsertDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceInsertDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
        
        return result;
    }
}
