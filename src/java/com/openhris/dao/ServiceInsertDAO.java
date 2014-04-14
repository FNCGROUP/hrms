/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.dao;

import com.hrms.beans.EmployeesInfoBean;
import com.hrms.classes.GenerateCompanyId;
import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.utilities.ConvertionUtilities;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.contributions.model.Phic;
import com.openhris.contributions.model.Sss;
import com.openhris.employee.model.PositionHistory;
import com.openhris.payroll.model.Payroll;
import com.openhris.payroll.model.serviceprovider.PayrollServiceImpl;
import com.openhris.service.PayrollService;
import com.openhris.timekeeping.model.Timekeeping;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
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
    
    public boolean insertNewEmployee(List<PositionHistory> insertList){
        Connection conn = getConnection.connection();
        boolean result = false;
        PreparedStatement pstmt = null;
        
        GenerateCompanyId companyId = new GenerateCompanyId();
        try {
            conn.setAutoCommit(false);
            for(PositionHistory ph : insertList){
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
        
    public boolean insertPayrollAndAttendance(List<Payroll> insertPayrollList, 
            List<Timekeeping> insertAttendanceList, 
            boolean EDIT_PAYROLL, 
            double adjustments, 
            int previousPayrollId){
        Connection conn = getConnection.connection();
        boolean result = false;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
            for(Payroll p : insertPayrollList){
                pstmt = conn.prepareStatement("INSERT INTO payroll_table(employeeId, attendancePeriodFrom, attendancePeriodTo, "
                        + "basicSalary, halfMonthSalary, phic, sss, hdmf, absences, numberOfDays, taxableSalary, tax, "
                        + "cashBond, totalLatesDeduction, totalUndertimeDeduction, totalOvertimePaid, totalNightDifferentialPaid, "
                        + "totalLegalHolidayPaid, totalSpecialHolidayPaid, totalWorkingDayOffPaid, allowance, "
                        + "allowanceForLiquidation, netSalary, amountToBeReceive, amountReceivable, branchId, payrollPeriod, payrollDate) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                pstmt.setString(1, p.getEmployeeId());
                pstmt.setString(2, util.convertDateFormat(p.getAttendancePeriodFrom().toString()));
                pstmt.setString(3, util.convertDateFormat(p.getAttendancePeriodTo().toString()));
                pstmt.setDouble(4, p.getBasicSalary());
                pstmt.setDouble(5, p.getHalfMonthSalary());
                pstmt.setDouble(6, p.getPhic());
                pstmt.setDouble(7, p.getSss());
                pstmt.setDouble(8, p.getHdmf());
                pstmt.setDouble(9, p.getAbsences());
                pstmt.setInt(10, p.getNumOfDays());
                pstmt.setDouble(11, p.getTaxableSalary());
                pstmt.setDouble(12, p.getTax());
                pstmt.setDouble(13, p.getCashBond());
                pstmt.setDouble(14, p.getTotalLatesDeduction());
                pstmt.setDouble(15, p.getTotalUndertimeDeduction());
                pstmt.setDouble(16, p.getTotalOvertimePaid());
                pstmt.setDouble(17, p.getTotalNightDifferentialPaid());
                pstmt.setDouble(18, p.getTotalLegalHolidayPaid());
                pstmt.setDouble(19, p.getTotalSpecialHolidayPaid());
                pstmt.setDouble(20, p.getTotalWorkingDayOffPaid());
                pstmt.setDouble(21, p.getAllowance());
                pstmt.setDouble(22, p.getAllowanceForLiquidation());
                pstmt.setDouble(23, p.getNetSalary());
                pstmt.setDouble(24, p.getAmountToBeReceive());
                pstmt.setDouble(25, p.getAmountReceivable());
                pstmt.setInt(26, p.getBranchId());
                pstmt.setString(27, p.getPayrollPeriod());
                pstmt.setString(28, util.convertDateFormat(p.getPayrollDate().toString()));                
                pstmt.executeUpdate();
                
                int payrollId = 0;            
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT last_insert_id() AS id FROM payroll_table ");
                while(rs.next()){
                    payrollId = Integer.parseInt(rs.getString("id"));
                }
                
                for(Timekeeping t : insertAttendanceList){
                    pstmt = conn.prepareStatement("INSERT INTO timekeeping_table(payrollId, attendanceDate, policy, "
                            + "holiday, premium, lates, undertime, overtime, nightDifferential, latesDeduction, "
                            + "undertimeDeduction, overtimePaid, nightDifferentialPaid, legalHolidayPaid, "
                            + "specialHolidayPaid, workingDayOffPaid, psHolidayPaid) "
                            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"); 
                    pstmt.setInt(1, payrollId);
                    pstmt.setString(2, util.convertDateFormat(t.getAttendanceDate().toString()));
                    pstmt.setString(3, t.getPolicy());
                    pstmt.setString(4, t.getHoliday());
                    pstmt.setString(5, String.valueOf(t.isPremium()).toString());
                    pstmt.setDouble(6, t.getLates());
                    pstmt.setDouble(7, t.getUndertime());
                    pstmt.setDouble(8, t.getOvertime());
                    pstmt.setDouble(9, t.getNightDifferential());
                    pstmt.setDouble(10, t.getLateDeduction());
                    pstmt.setDouble(11, t.getUndertimeDeduction());
                    pstmt.setDouble(12, t.getOvertimePaid());
                    pstmt.setDouble(13, t.getNightDifferentialPaid());
                    pstmt.setDouble(14, t.getLegalHolidayPaid());
                    pstmt.setDouble(15, t.getSpecialHolidayPaid());
                    pstmt.setDouble(16, t.getWorkingDayOffPaid());
                    pstmt.setDouble(17, t.getNonWorkingHolidayPaid());
                    pstmt.executeUpdate();
                }
                
                if(adjustments != 0){
                    double amountReceivable = Math.round((p.getAmountReceivable() + adjustments)*100.0)/100.0;
                    double amountToBeReceive = amountReceivable;

                    pstmt = conn.prepareStatement("UPDATE payroll_table SET amountToBeReceive = ?, "
			    + "amountReceivable = ?, "
			    + "adjustments = ? "
                            + "WHERE id = ? ");
                    pstmt.setDouble(1, amountToBeReceive);
                    pstmt.setDouble(2, amountReceivable);
		    pstmt.setDouble(3, adjustments);
                    pstmt.setInt(4, payrollId);
                    pstmt.executeUpdate();
                }           
				
		if(previousPayrollId != 0){
		    double previousAmountReceived = serviceGet.getPreviousAmountReceived(previousPayrollId);
		    double forAdjustment = Math.round((p.getAmountReceivable() - previousAmountReceived)*100.0)/100.0;
		    
		    pstmt = conn.prepareStatement("INSERT INTO adjustments(payrollId, amount, remarks, datePosted) VALUES(?, ?, ?, now())");
		    pstmt.setInt(1, payrollId);
		    pstmt.setDouble(2, forAdjustment);
		    pstmt.setString(3, "edit timekeeping table");
		    pstmt.executeUpdate();
		    
		    pstmt = conn.prepareStatement("UPDATE payroll_table SET forAdjustments = ?, "
			    + "amountToBeReceive = ?, "
			    + "amountReceivable = ? "
			    + "WHERE id = ?");
		    pstmt.setDouble(1, forAdjustment);
		    pstmt.setDouble(2, p.getAmountReceivable());
		    pstmt.setDouble(3, previousAmountReceived);
		    pstmt.setInt(4, payrollId);
		    pstmt.executeUpdate();
		    
		    pstmt = conn.prepareStatement("UPDATE payroll_table SET actionTaken = 'adjusted' WHERE id = '"+payrollId+"' ");
                    pstmt.executeUpdate();
                
                    pstmt = conn.prepareStatement("UPDATE payroll_table SET actionTaken = 'previous' WHERE id = '"+previousPayrollId+"' ");
                    pstmt.executeUpdate();
		}
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
    
    public boolean checkForDuplicateEntry(String firstname, String middlename, String lastname){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Boolean result = true;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) AS result FROM employee WHERE firstname = '"+firstname+"' AND middlename = '"+middlename+"' "
                    + "AND lastname = '"+lastname+"' ");
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
    
    public boolean insertAdvanceType(String advanceType){
        Connection conn = getConnection.connection();
        boolean result = false;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("INSERT INTO advance_type(advanceType) VALUES(?)");
            pstmt.setString(1, advanceType);
            pstmt.executeUpdate();
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

    public boolean insertNewUser(String username, 
            String password, 
            String role, 
            String employeeId){
        
        Boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        Statement stmt;
        ResultSet rs;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("INSERT INTO user_(username_, password_, userRole, employeeId) "
                    + "VALUE(?, ?, ?, ?)");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.setString(4, employeeId);
            pstmt.executeUpdate();
            
            int userId = 0;            
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT last_insert_id() AS id FROM user_ ");
            while(rs.next()){
                userId = Integer.parseInt(rs.getString("id"));
            }
            
            pstmt = conn.prepareStatement("INSERT INTO user_toolbar_menu_access(userId) VALUES(?)");
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement("INSERT INTO user_advance_access(userId) VALUES(?)");
            pstmt.setInt(1, userId);
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
    
    public boolean checkUsernameIfExist(String username){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Boolean result = true;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT COUNT(*) AS result FROM user_ WHERE username_ = '"+username+"' ");
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
    
    public boolean insertNewCorporateForAccessToUser(int userId, int corporateId){
        Boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("INSERT INTO user_corporate_access(userId, corporateId) VALUES(?, ?)");
            pstmt.setInt(1, userId);
            pstmt.setInt(2, corporateId);
            pstmt.executeUpdate();
            
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
    
    public boolean insertNewTradeForAccessToUser(int userId, 
            int corporateId, 
            int tradeId){
        
        Boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("INSERT INTO user_trade_access(userId, corporateId, tradeId) VALUES(?, ?, ?)");
            pstmt.setInt(1, userId);
            pstmt.setInt(2, corporateId);
            pstmt.setInt(3, tradeId);
            pstmt.executeUpdate();
            
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
    
    public boolean insertNewBranchForAccessToUser(int userId,
            int tradeId, 
            int branchId){
        
        Boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("INSERT INTO user_branch_access(userId, tradeId, branchId) VALUES(?, ?, ?)");
            pstmt.setInt(1, userId);
            pstmt.setInt(2, tradeId);
            pstmt.setInt(3, branchId);
            pstmt.executeUpdate();
            
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
