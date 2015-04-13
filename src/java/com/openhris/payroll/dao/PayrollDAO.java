/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.payroll.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.payroll.model.Adjustment;
import com.openhris.payroll.model.Advances;
import com.openhris.payroll.model.Payroll;
import com.openhris.payroll.model.PayrollRegister;
import com.openhris.timekeeping.model.Timekeeping;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
public class PayrollDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public List<Payroll> getPayrollByBranchAndEmployee(int branchId, String employeeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        String queryPayrollList;
        
        if(employeeId == null){
            queryPayrollList = "SELECT p.id AS id, p.employeeId AS employeeId, p.attendancePeriodFrom AS attendancePeriodFrom, "
                    + "p.attendancePeriodTo AS attendancePeriodTo, p.basicSalary AS basicSalary, p.halfMonthSalary AS halfMonthSalary, "
                    + "p.phic AS phic, p.sss AS sss, p.hdmf AS hdmf, p.absences AS absences, p.numberOfDays AS numberOfDays, "
                    + "p.taxableSalary AS taxableSalary, p.tax AS tax, p.cashBond AS cashBond, p.totalLatesDeduction AS totalLatesDeduction, "
                    + "p.totalUndertimeDeduction AS totalUndertimeDeduction, p.totalOvertimePaid AS totalOvertimePaid, "
                    + "p.totalNightDifferentialPaid AS totalNightDifferentialPaid, p.totalDutyManagerPaid AS totalDutyManagerPaid, "
                    + "p.totalLegalHolidayPaid AS totalLegalHolidayPaid, p.totalSpecialHolidayPaid AS totalSpecialHolidayPaid, "
                    + "p.totalWorkingDayOffPaid AS totalWorkingDayOffPaid, p.allowance AS allowance, "
                    + "p.allowanceForLiquidation AS allowanceForLiquidation, p.netSalary AS netSalary, "
                    + "p.amountToBeReceive AS amountToBeReceive, p.amountReceivable AS amountReceivable, e.branchId AS branchId, "
                    + "p.payrollPeriod AS payrollPeriod, p.payrollDate AS payrollDate, p.rowStatus AS rowStatus, "
		    + "p.forAdjustments AS forAdjustments, p.adjustments AS adjustments "
                    + "FROM payroll_table p INNER JOIN employee e ON p.employeeId = e.employeeId "
                    + "WHERE e.branchId = "+branchId+" AND (e.currentStatus != 'removed' OR e.currentStatus IS NULL) ORDER BY id DESC";
        } else {
            queryPayrollList = "SELECT p.id AS id, p.employeeId AS employeeId, p.attendancePeriodFrom AS attendancePeriodFrom, "
                    + "p.attendancePeriodTo AS attendancePeriodTo, p.basicSalary AS basicSalary, p.halfMonthSalary AS halfMonthSalary, "
                    + "p.phic AS phic, p.sss AS sss, p.hdmf AS hdmf, p.absences AS absences, p.numberOfDays AS numberOfDays, "
                    + "p.taxableSalary AS taxableSalary, p.tax AS tax, p.cashBond AS cashBond, p.totalLatesDeduction AS totalLatesDeduction, "
                    + "p.totalUndertimeDeduction AS totalUndertimeDeduction, p.totalOvertimePaid AS totalOvertimePaid, "
                    + "p.totalNightDifferentialPaid AS totalNightDifferentialPaid, p.totalDutyManagerPaid AS totalDutyManagerPaid, "
                    + "p.totalLegalHolidayPaid AS totalLegalHolidayPaid, p.totalSpecialHolidayPaid AS totalSpecialHolidayPaid, "
                    + "p.totalWorkingDayOffPaid AS totalWorkingDayOffPaid, p.allowance AS allowance, "
                    + "p.allowanceForLiquidation AS allowanceForLiquidation, p.netSalary AS netSalary, "
                    + "p.amountToBeReceive AS amountToBeReceive, p.amountReceivable AS amountReceivable, e.branchId AS branchId, "
                    + "p.payrollPeriod AS payrollPeriod, p.payrollDate AS payrollDate, p.rowStatus AS rowStatus, "
		    + "p.forAdjustments AS forAdjustments, p.adjustments AS adjustments "
                    + "FROM payroll_table p INNER JOIN employee e ON p.employeeId = e.employeeId "
                    + "WHERE e.employeeId = '"+employeeId+"' AND (e.currentStatus != 'removed' OR e.currentStatus IS NULL) ORDER BY id DESC" ;
        }
        
        List<Payroll> payrollList = new ArrayList<Payroll>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(queryPayrollList);
            while(rs.next()){
                Payroll p = new Payroll();
                p.setId(util.convertStringToInteger(rs.getString("id")));
                p.setEmployeeId(rs.getString("employeeId"));
                p.setAttendancePeriodFrom(util.parsingDate(rs.getString("attendancePeriodFrom")));
                p.setAttendancePeriodTo(util.parsingDate(rs.getString("attendancePeriodTo")));
                p.setBasicSalary(util.convertStringToDouble(rs.getString("basicSalary")));
                p.setHalfMonthSalary(util.convertStringToDouble(rs.getString("halfMonthSalary")));
                p.setPhic(util.convertStringToDouble(rs.getString("phic")));
                p.setSss(util.convertStringToDouble(rs.getString("sss")));
                p.setHdmf(util.convertStringToDouble(rs.getString("hdmf")));
                p.setAbsences(util.convertStringToDouble(rs.getString("absences")));
                p.setNumOfDays(util.convertStringToInteger(rs.getString("numberOfDays")));
                p.setTaxableSalary(util.convertStringToDouble(rs.getString("taxableSalary")));
                p.setTax(util.convertStringToDouble(rs.getString("tax")));
                p.setCashBond(util.convertStringToDouble(rs.getString("cashBond")));
                p.setTotalLatesDeduction(util.convertStringToDouble(rs.getString("totalLatesDeduction")));
                p.setTotalUndertimeDeduction(util.convertStringToDouble(rs.getString("totalUndertimeDeduction")));
                p.setTotalOvertimePaid(util.convertStringToDouble(rs.getString("totalOvertimePaid")));
                p.setTotalNightDifferentialPaid(util.convertStringToDouble(rs.getString("totalNightDifferentialPaid")));
                p.setTotalDutyManagerPaid(util.convertStringToDouble(rs.getString("totalDutyManagerPaid")));
                p.setTotalLegalHolidayPaid(util.convertStringToDouble(rs.getString("totalLegalHolidayPaid")));
                p.setTotalSpecialHolidayPaid(util.convertStringToDouble(rs.getString("totalSpecialHolidayPaid")));
                p.setTotalWorkingDayOffPaid(util.convertStringToDouble(rs.getString("totalWorkingDayOffPaid")));
                p.setAllowance(util.convertStringToDouble(rs.getString("allowance")));
                p.setAllowanceForLiquidation(util.convertStringToDouble(rs.getString("allowanceForLiquidation")));
                p.setNetSalary(util.convertStringToDouble(rs.getString("netSalary")));
                p.setAmountToBeReceive(util.convertStringToDouble(rs.getString("amountToBeReceive")));
                p.setAmountReceivable(util.convertStringToDouble(rs.getString("amountReceivable")));
                p.setBranchId(util.convertStringToInteger(rs.getString("branchId")));
                p.setPayrollPeriod(rs.getString("payrollPeriod"));
                p.setPayrollDate(util.parsingDate(rs.getString("payrollDate")));
		p.setForAdjustments(util.convertStringToDouble(rs.getString("forAdjustments")));
		p.setAdjustment(util.convertStringToDouble(rs.getString("adjustments")));
                p.setRowStatus(rs.getString("rowStatus"));
                payrollList.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return payrollList;
    }
    
    public boolean removeSelectedRow(int id){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        String queryRemove = "DELETE FROM payroll_table WHERE id = ?";
        Boolean result = false;        
        try {
            pstmt = conn.prepareStatement(queryRemove);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public double getAdvancesByPayrollId(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double advances = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT SUM(amount) AS amount FROM advance_table WHERE rowStatus IS NULL AND payrollId = "+payrollId+" ");
            while(rs.next()){
                advances = util.convertStringToDouble(rs.getString("amount"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return advances;
    }
    
    public List<Advances> getAdvancesByPayroll(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Advances> advancesList = new ArrayList<Advances>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM advance_table WHERE payrollId = "+payrollId+" AND "
                    + "rowStatus IS NULL ");
            while(rs.next()){
                Advances a = new Advances();
                a.setAdvanceId(util.convertStringToInteger(rs.getString("id")));
                a.setAmount(util.convertStringToDouble(rs.getString("amount")));
                a.setAdvanceType(rs.getString("advanceType"));
                a.setParticulars(rs.getString("particulars"));
                a.setDatePosted(util.parsingDate(rs.getString("datePosted")));
                a.setAdvanceRowStatus(rs.getString("rowStatus"));
                a.setRemarks(rs.getString("remarks"));
                a.setDateRemoved(util.parsingDate(rs.getString("dateRemoved")));
                advancesList.add(a);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return advancesList;
    }
    
    public boolean removeAdvancesById(int advanceId, int payrollId, 
            Double removedAmount, 
            Double amountToBeReceive, 
            Double amountReceivable, 
            String remarks){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        String queryUpdate = "UPDATE payroll_table SET amountToBeReceive = ?, amountReceivable = ? WHERE id = ? ";
        String queryRemove = "UPDATE advance_table SET rowStatus = 'removed', remarks = ?, dateRemoved = now() WHERE id = ?";
        Boolean result = false;        
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(queryRemove);
            pstmt.setString(1, remarks);
            pstmt.setInt(2, advanceId);
            pstmt.executeUpdate();
            
            amountReceivable = amountReceivable + removedAmount;
            amountToBeReceive = amountToBeReceive + removedAmount;
            
            pstmt = conn.prepareStatement(queryUpdate);
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
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex1);
            } 
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public List getAdvanceTypeLists(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<String> advancesTypeLists = new ArrayList<String>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT advanceType FROM advance_type ");
            while(rs.next()){
                advancesTypeLists.add(rs.getString("advanceType").toLowerCase());
            }
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return advancesTypeLists;
    }
    
    public boolean insertAdvanceType(String advanceType){
        Connection conn = getConnection.connection();
        boolean result = false;
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("INSERT INTO advance_type(advanceType) VALUES(?)");
            pstmt.setString(1, advanceType.trim().toLowerCase());
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean updateSalaryByAdvances(List<Advances> advanceList){
        Connection conn = getConnection.connection();
        boolean result = false;
        PreparedStatement pstmt = null;
        String queryInsert = "INSERT INTO advance_table(payrollId, amount, advanceType, particulars, datePosted) VALUES(?, ?, ?, ?, ?)";
        String queryUpdate = "UPDATE payroll_table SET amountToBeReceive = ?, amountReceivable = ? WHERE id = ? ";
        try {
            conn.setAutoCommit(false);
            for(Advances a : advanceList){
                pstmt = conn.prepareStatement(queryInsert);
                pstmt.setInt(1, a.getId());
                pstmt.setDouble(2, a.getAmount());
                pstmt.setString(3, a.getAdvanceType());
                pstmt.setString(4, a.getParticulars());
                pstmt.setString(5, util.convertDateFormat(a.getDatePosted().toString()));
                pstmt.executeUpdate();
                
                double amountReceivable = a.getAmountReceivable() - a.getAmount();
                double amountToBeReceive = Math.round((a.getAmountToBeReceive() - a.getAmount())*100.0)/100.0;
                
                pstmt = conn.prepareStatement(queryUpdate);
                pstmt.setDouble(1, amountToBeReceive);
                pstmt.setDouble(2, amountReceivable);
                pstmt.setInt(3, a.getId());
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
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex1);
            } 
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public double getTotalAdvancesByPayroll(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        double totalAmount = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT ROUND(SUM(amount), 2) AS amount FROM advance_table "
                    + "WHERE payrollId = "+payrollId+" AND rowStatus IS NULL ");
            while(rs.next()){
                totalAmount = util.convertStringToDouble(rs.getString("amount"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return totalAmount;
    }
    
    public List<PayrollRegister> getPayrollRegisterByBranch(int branchId, 
            String payrollDate, 
            boolean prev){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        String queryPayrollRegisterList;
        
        if(prev){
            queryPayrollRegisterList = "SELECT * FROM payroll_register Where "
                    + "branchId = "+branchId+" AND payrollDate = '"+payrollDate+"' "
                    + "AND (currentStatus != 'removed' OR currentStatus IS NULL) AND "
                    + "(actionTaken = 'previous' OR actionTaken IS NULL ) ORDER BY name ASC";            
        } else {
            queryPayrollRegisterList = "SELECT * FROM payroll_register Where "
                    + "branchId = "+branchId+" AND payrollDate = '"+payrollDate+"' "
                    + "AND (currentStatus != 'removed' OR currentStatus IS NULL) AND "
                    + "(actionTaken = 'adjusted' OR actionTaken IS NULL ) ORDER BY name ASC";
        }
        
        List<PayrollRegister> payrollRegisterList = new ArrayList<PayrollRegister>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(queryPayrollRegisterList);
            while(rs.next()){
                PayrollRegister pr = new PayrollRegister();
                pr.setId(util.convertStringToInteger(rs.getString("salaryId")));
                pr.setName(rs.getString("name"));
                pr.setNumOfDays(util.convertStringToInteger(rs.getString("numberOfDays")));                
                pr.setBasicSalary(util.convertStringToDouble(rs.getString("basicSalary")));
                pr.setRatePerDay(util.convertStringToDouble(rs.getString("ratePerDay")));
                pr.setHalfMonthSalary(util.convertStringToDouble(rs.getString("halfMonthSalary")));
                pr.setTotalOvertimePaid(util.convertStringToDouble(rs.getString("overtimePay")));
                pr.setTotalLegalHolidayPaid(util.convertStringToDouble(rs.getString("legalHolidayPay")));
                pr.setTotalSpecialHolidayPaid(util.convertStringToDouble(rs.getString("specialHolidayPay")));
                pr.setTotalNightDifferentialPaid(util.convertStringToDouble(rs.getString("nightDifferentialPay")));
                pr.setTotalDutyManagerPaid(util.convertStringToDouble(rs.getString("dutyManagerPay")));
                pr.setTotalWorkingDayOffPaid(util.convertStringToDouble(rs.getString("workingDayOffPay")));
                pr.setAbsences(util.convertStringToDouble(rs.getString("absences")));
                pr.setTotalLatesDeduction(util.convertStringToDouble(rs.getString("totalLatesDeduction")));
                pr.setTotalUndertimeDeduction(util.convertStringToDouble(rs.getString("totalUndertimeDeduction")));
                pr.setSss(util.convertStringToDouble(rs.getString("sss")));
                pr.setPhic(util.convertStringToDouble(rs.getString("phic")));
                pr.setHdmf(util.convertStringToDouble(rs.getString("hdmf")));
                pr.setTax(util.convertStringToDouble(rs.getString("tax")));
                pr.setNetSalary(util.convertStringToDouble(rs.getString("netSalary")));
                pr.setAllowance(util.convertStringToDouble(rs.getString("allowance")));
                pr.setAllowanceForLiquidation(util.convertStringToDouble(rs.getString("allowanceForLiquidation")));
                pr.setAmount(util.convertStringToDouble(rs.getString("advances")));
                pr.setAdjustment(util.convertStringToDouble(rs.getString("adjustments")));
                pr.setAmountReceivable(util.convertStringToDouble(rs.getString("amountReceivable")));
                pr.setAmountToBeReceive(util.convertStringToDouble(rs.getString("amountToBeReceive")));
                pr.setForAdjustments(util.convertStringToDouble(rs.getString("forAdjustments")));
                pr.setActionTaken(rs.getString("actionTaken"));
                pr.setPayrollDate(util.parsingDate(rs.getString("payrollDate")));
                pr.setAttendancePeriodFrom(util.parsingDate(rs.getString("attendancePeriodFrom")));
                pr.setAttendancePeriodTo(util.parsingDate(rs.getString("attendancePeriodTo")));
                pr.setBranchId(util.convertStringToInteger(rs.getString("branchId")));
                pr.setEmploymentWageEntry(rs.getString("employmentWageEntry"));
                pr.setBranchName(rs.getString("branchName"));
                pr.setTradeName(rs.getString("tradeName"));
                pr.setCorporateName(rs.getString("corporateName"));
                pr.setCurrentStatus(rs.getString("currentStatus"));
                payrollRegisterList.add(pr);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return payrollRegisterList;
    }
    
    public boolean updatePhicContribution(int payrollId, 
            double phicAmount, 
            double netPay, 
            double amountToBeReceive, 
            double amountReceive, 
            boolean isPayrollAdjusted){        
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("UPDATE payroll_table SET phic = ?, netSalary = ?, "
                    + "amountToBeReceive = ?, amountReceivable = ? WHERE id = ?");
            pstmt.setDouble(1, phicAmount);
            pstmt.setDouble(2, netPay);
            pstmt.setDouble(3, amountToBeReceive);
            pstmt.setDouble(4, amountReceive);
            pstmt.setInt(5, payrollId);
            pstmt.executeUpdate();
            
            if(isPayrollAdjusted){
                double forAdjustment = amountToBeReceive - amountReceive;
                pstmt = conn.prepareStatement("UPDATE payroll_table SET "
                    + "forAdjustments = ? WHERE id = ?");
                pstmt.setDouble(1, forAdjustment);
                pstmt.setInt(2, payrollId);
                pstmt.executeUpdate();
            }
            
            conn.commit();
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
                System.out.println("Transaction Rollback");
            } catch (SQLException ex1) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;        
    }
    
    public String getPayrollPeriodByPayrollId(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        String payrollPeriod = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT payrollPeriod FROM payroll_table WHERE id = "+payrollId+" ");
            while(rs.next()){
                payrollPeriod = rs.getString("payrollPeriod");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return payrollPeriod;
    }
    
    public String getPayrollDateByPayrollId(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        String payrollDate = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT payrollDate FROM payroll_table WHERE id = "+payrollId+" ");
            while(rs.next()){
                payrollDate = rs.getString("payrollDate");
            }
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return payrollDate;
    }
    
    public Boolean updatePayrollDate(int payrollId, String date){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt;
        String queryUpdate = "UPDATE payroll_table SET payrollDate = ? WHERE id = ?";
        Boolean result = false;        
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, date);
            pstmt.setInt(2, payrollId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public boolean updateTaxWitheldAmount(int payrollId, 
            double newTaxWitheldAmount, 
            double netPay, 
            double amountToBeReceive, 
            double amountReceive, 
            boolean isPayrollAdjusted){        
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("UPDATE payroll_table SET tax = ?, netSalary = ?, "
                    + "amountToBeReceive = ?, amountReceivable = ? WHERE id = ?");
            pstmt.setDouble(1, newTaxWitheldAmount);
            pstmt.setDouble(2, netPay);
            pstmt.setDouble(3, amountToBeReceive);
            pstmt.setDouble(4, amountReceive);
            pstmt.setInt(5, payrollId);
            pstmt.executeUpdate();
            
            if(isPayrollAdjusted){
                double forAdjustment = amountToBeReceive - amountReceive;
                pstmt = conn.prepareStatement("UPDATE payroll_table SET "
                    + "forAdjustments = ? WHERE id = ?");
                pstmt.setDouble(1, forAdjustment);
                pstmt.setInt(2, payrollId);
                pstmt.executeUpdate();
            }
            
            conn.commit();
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
                System.out.println("Transaction Rollback");
            } catch (SQLException ex1) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;        
    }
    
    public boolean updateHdmfContribution(int payrollId, 
            double newHdmfContribution, 
            double netPay, 
            double amountToBeReceive, 
            double amountReceive, 
            boolean isPayrollAdjusted){        
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("UPDATE payroll_table SET hdmf = ?, netSalary = ?, "
                    + "amountToBeReceive = ?, amountReceivable = ? WHERE id = ?");
            pstmt.setDouble(1, newHdmfContribution);
            pstmt.setDouble(2, netPay);
            pstmt.setDouble(3, amountToBeReceive);
            pstmt.setDouble(4, amountReceive);
            pstmt.setInt(5, payrollId);
            pstmt.executeUpdate();
            
            if(isPayrollAdjusted){
                double forAdjustment = amountToBeReceive - amountReceive;
                pstmt = conn.prepareStatement("UPDATE payroll_table SET "
                    + "forAdjustments = ? WHERE id = ?");
                pstmt.setDouble(1, forAdjustment);
                pstmt.setInt(2, payrollId);
                pstmt.executeUpdate();
            }
            
            conn.commit();
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
                System.out.println("Transaction Rollback");
            } catch (SQLException ex1) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;        
    }
    
    public boolean updateSssContribution(int payrollId, 
            double sssContribution, 
            double netPay, 
            double amountToBeReceive, 
            double amountReceive, 
            boolean isPayrollAdjusted){        
        boolean result = false;
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("UPDATE payroll_table SET sss = ?, netSalary = ?, "
                    + "amountToBeReceive = ?, amountReceivable = ? WHERE id = ?");
            pstmt.setDouble(1, sssContribution);
            pstmt.setDouble(2, netPay);
            pstmt.setDouble(3, amountToBeReceive);
            pstmt.setDouble(4, amountReceive);
            pstmt.setInt(5, payrollId);
            pstmt.executeUpdate();
            
            if(isPayrollAdjusted){
                double forAdjustment = amountToBeReceive - amountReceive;
                pstmt = conn.prepareStatement("UPDATE payroll_table SET "
                    + "forAdjustments = ? WHERE id = ?");
                pstmt.setDouble(1, forAdjustment);
                pstmt.setInt(2, payrollId);
                pstmt.executeUpdate();
            }
            
            conn.commit();
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
                System.out.println("Transaction Rollback");
            } catch (SQLException ex1) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;        
    }
    
    public boolean lockPayroll(int payrollId){
        Connection conn = getConnection.connection();
        Boolean result = false;     
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement("UPDATE payroll_table SET rowStatus = 'locked' WHERE id = ?");
            pstmt.setInt(1, payrollId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public double getAdjustmentFromPreviousPayroll(String employeeId){
        double adjustment = 0;
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
	    rs = stmt.executeQuery("SELECT ifnull(forAdjustments, 0) AS forAdjustments "
		    + "FROM payroll_table WHERE employeeId = '"+employeeId+"' "
                    + "ORDER BY payrollDate DESC, id DESC LIMIT 1");
            while(rs.next()){
                adjustment = util.convertStringToDouble(rs.getString("forAdjustments"));                
            }
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return adjustment;
    }
    
    public List<Adjustment> getListOfAdjustmentFromPayrollId(int payrollId){
        List<Adjustment> adjustmentList = new ArrayList<Adjustment>();
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM adjustments WHERE payrollId = "+payrollId+" ");
            while(rs.next()){
                Adjustment adjustment = new Adjustment();
                adjustment.setAdjustmentId(util.convertStringToInteger(rs.getString("id")));
                adjustment.setAmount(util.convertStringToDouble(rs.getString("amount")));
                adjustment.setRemarks(rs.getString("remarks"));
                adjustment.setDatePosted(util.parsingDate(rs.getString("datePosted")));
                adjustmentList.add(adjustment);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return adjustmentList;
    }
    
    public double getTotalAdjustmentByPayrollId(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double totalAdjustment = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT SUM(amount) AS totalAdjustment FROM adjustments WHERE payrollId = "+payrollId+" ");
            while(rs.next()){
                totalAdjustment = util.roundOffToTwoDecimalPlaces(util.convertStringToDouble(rs.getString("totalAdjustment")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return totalAdjustment;
    }
    
    public boolean removeAdjustmentById(int adjustmentId, 
            double amountToBeReceive,
            double amountReceived, 
            double adjustment, 
            int payrollId){
        Connection conn = getConnection.connection();
        boolean result = false;     
        PreparedStatement pstmt = null;        
        amountToBeReceive = amountToBeReceive - adjustment;
        amountReceived = amountReceived - adjustment;
        double totalAdjustment = getTotalAdjustmentByPayrollId(payrollId) - adjustment;
        try {
            pstmt = conn.prepareStatement("UPDATE payroll_table SET amountToBeReceive = ?, amountReceivable = ?, adjustments = ? "
                    + "WHERE id = ? ");
            pstmt.setDouble(1, amountToBeReceive);
            pstmt.setDouble(2, amountReceived);
            pstmt.setDouble(3, totalAdjustment);
            pstmt.setInt(4, payrollId);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement("DELETE FROM adjustments WHERE id = "+adjustmentId+" ");
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean insertAdjustmentToPayroll(int payrollId, 
            double amountToBeReceive, 
            double amountReceived, 
            double adjustment,
            String remarks){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        amountToBeReceive = amountToBeReceive + adjustment;
        amountReceived = amountReceived + adjustment;
        double new_adjustment = getTotalAdjustmentByPayrollId(payrollId) + adjustment;
        
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("INSERT INTO adjustments(payrollId, amount, remarks, datePosted) VALUES(?, ?, ?, now()) ");
            pstmt.setInt(1, payrollId);
            pstmt.setDouble(2, adjustment);
            pstmt.setString(3, remarks);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement("UPDATE payroll_table SET amountToBeReceive = ? , amountReceivable = ?, "
                    + "adjustments = ? WHERE id = ? ");
            pstmt.setDouble(1, amountToBeReceive);
            pstmt.setDouble(2, amountReceived);
            pstmt.setDouble(3, new_adjustment);
            pstmt.setInt(4, payrollId);
            pstmt.executeUpdate();
            
            conn.commit();
            System.out.println("Transaction commit...");
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
                System.out.println("Connection rollback...");
            } catch (SQLException ex1) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public double getForAdjustmentFromPreviousPayroll(int previousPayrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double forAdjustment = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT forAdjustments FROM payroll_table WHERE id = "+previousPayrollId+" ");
            while(rs.next()){
                forAdjustment = util.roundOffToTwoDecimalPlaces(util.convertStringToDouble(rs.getString("forAdjustments")));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return forAdjustment;
    }
    
    public double getPreviousAmountReceived(int payrollId){
	Double amountReceivable = 0.0;
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT amountReceivable FROM payroll_table WHERE id = "+payrollId+" ");
            while(rs.next()){
                amountReceivable = util.convertStringToDouble(rs.getString("amountReceivable"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return amountReceivable;
    }
    
    public boolean insertPayrollAndAttendance(Payroll payroll, 
            List<Timekeeping> insertAttendanceList,  
            double adjustments, 
            int previousPayrollId){
        Connection conn = getConnection.connection();
        boolean result = false;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn.setAutoCommit(false);
                pstmt = conn.prepareStatement("INSERT INTO payroll_table(employeeId, attendancePeriodFrom, attendancePeriodTo, "
                        + "basicSalary, halfMonthSalary, phic, sss, hdmf, absences, numberOfDays, taxableSalary, tax, "
                        + "cashBond, totalLatesDeduction, totalUndertimeDeduction, totalOvertimePaid, totalNightDifferentialPaid, "
                        + "totalDutyManagerPaid, totalLegalHolidayPaid, totalSpecialHolidayPaid, totalWorkingDayOffPaid, "
                        + "totalNonWorkingHolidayPaid, allowance, allowanceForLiquidation, netSalary, adjustments, amountToBeReceive, "
                        + "amountReceivable, branchId, payrollPeriod, payrollDate) "
                        + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                pstmt.setString(1, payroll.getEmployeeId());
                pstmt.setString(2, util.convertDateFormat(payroll.getAttendancePeriodFrom().toString()));
                pstmt.setString(3, util.convertDateFormat(payroll.getAttendancePeriodTo().toString()));
                pstmt.setDouble(4, payroll.getBasicSalary());
                pstmt.setDouble(5, payroll.getHalfMonthSalary());
                pstmt.setDouble(6, payroll.getPhic());
                pstmt.setDouble(7, payroll.getSss());
                pstmt.setDouble(8, payroll.getHdmf());
                pstmt.setDouble(9, payroll.getAbsences());
                pstmt.setInt(10, payroll.getNumOfDays());
                pstmt.setDouble(11, payroll.getTaxableSalary());
                pstmt.setDouble(12, payroll.getTax());
                pstmt.setDouble(13, payroll.getCashBond());
                pstmt.setDouble(14, payroll.getTotalLatesDeduction());
                pstmt.setDouble(15, payroll.getTotalUndertimeDeduction());
                pstmt.setDouble(16, payroll.getTotalOvertimePaid());
                pstmt.setDouble(17, payroll.getTotalNightDifferentialPaid());
                pstmt.setDouble(18, payroll.getTotalDutyManagerPaid());
                pstmt.setDouble(19, payroll.getTotalLegalHolidayPaid());
                pstmt.setDouble(20, payroll.getTotalSpecialHolidayPaid());
                pstmt.setDouble(21, payroll.getTotalWorkingDayOffPaid());
                pstmt.setDouble(22, payroll.getTotalNonWorkingHolidayPaid());
                pstmt.setDouble(23, payroll.getAllowance());
                pstmt.setDouble(24, payroll.getAllowanceForLiquidation());
                pstmt.setDouble(25, payroll.getNetSalary());
		pstmt.setDouble(26, adjustments);
                pstmt.setDouble(27, payroll.getAmountToBeReceive() + adjustments);
                pstmt.setDouble(28, payroll.getAmountReceivable() + adjustments);
                pstmt.setInt(29, payroll.getBranchId());
                pstmt.setString(30, payroll.getPayrollPeriod());
                pstmt.setString(31, util.convertDateFormat(payroll.getPayrollDate().toString()));                
                pstmt.executeUpdate();
                
                int payrollId = 0;            
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT last_insert_id() AS id FROM payroll_table ");
                while(rs.next()){
                    payrollId = Integer.parseInt(rs.getString("id"));
                }
                
                for(Timekeeping t : insertAttendanceList){
                    pstmt = conn.prepareStatement("INSERT INTO timekeeping_table(payrollId, attendanceDate, policy, "
                            + "holiday, premium, lates, undertime, overtime, nightDifferential, dutyManager, latesDeduction, "
                            + "undertimeDeduction, overtimePaid, nightDifferentialPaid, dutyManagerPaid, legalHolidayPaid, "
                            + "specialHolidayPaid, workingDayOffPaid, psHolidayPaid) "
                            + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"); 
                    pstmt.setInt(1, payrollId);
                    pstmt.setString(2, util.convertDateFormat(t.getAttendanceDate().toString()));
                    pstmt.setString(3, t.getPolicy());
                    pstmt.setString(4, t.getHoliday());
                    pstmt.setString(5, String.valueOf(t.isPremium()));
                    pstmt.setDouble(6, t.getLates());
                    pstmt.setDouble(7, t.getUndertime());
                    pstmt.setDouble(8, t.getOvertime());
                    pstmt.setDouble(9, t.getNightDifferential());
                    pstmt.setDouble(10, t.getDutyManager());
                    pstmt.setDouble(11, t.getLateDeduction());
                    pstmt.setDouble(12, t.getUndertimeDeduction());
                    pstmt.setDouble(13, t.getOvertimePaid());
                    pstmt.setDouble(14, t.getNightDifferentialPaid());
                    pstmt.setDouble(15, t.getDutyManagerPaid());
                    pstmt.setDouble(16, t.getLegalHolidayPaid());
                    pstmt.setDouble(17, t.getSpecialHolidayPaid());
                    pstmt.setDouble(18, t.getWorkingDayOffPaid());
                    pstmt.setDouble(19, t.getNonWorkingHolidayPaid());
                    pstmt.executeUpdate();
                }                          
			                
                if(getForAdjustmentFromPreviousPayroll(getPreviousPayrollId(payroll.getEmployeeId())) != 0){                    
                    pstmt = conn.prepareStatement("INSERT INTO adjustments(payrollId, amount, remarks, datePosted) VALUES(?, ?, ?, now())");
                    pstmt.setInt(1, payrollId);
                    pstmt.setDouble(2, getForAdjustmentFromPreviousPayroll(getPreviousPayrollId(payroll.getEmployeeId())));
                    pstmt.setString(3, "edit timekeeping table");
                    pstmt.executeUpdate();
                }
                
		if(previousPayrollId != 0){
		    double previousAmountReceived = getPreviousAmountReceived(previousPayrollId);
                    double previousAmountOfAdvances = getAdvancesByPayrollId(previousPayrollId);
		    double forAdjustment = (payroll.getAmountReceivable() - previousAmountOfAdvances) - previousAmountReceived;
                    double amountToBeReceive = previousAmountReceived + forAdjustment;
		    		    
		    pstmt = conn.prepareStatement("UPDATE payroll_table SET forAdjustments = ?, "
			    + "amountToBeReceive = ?, "
			    + "amountReceivable = ? "
			    + "WHERE id = ?");
		    pstmt.setDouble(1, forAdjustment);
		    pstmt.setDouble(2, amountToBeReceive);
		    pstmt.setDouble(3, previousAmountReceived);
		    pstmt.setInt(4, payrollId);
		    pstmt.executeUpdate();
		    
		    pstmt = conn.prepareStatement("UPDATE payroll_table SET actionTaken = 'adjusted' WHERE id = "+payrollId+" ");
                    pstmt.executeUpdate();
                
                    pstmt = conn.prepareStatement("UPDATE payroll_table SET actionTaken = 'previous' WHERE id = "+previousPayrollId+" ");
                    pstmt.executeUpdate();
                             
                    if(previousAmountOfAdvances != 0){
                        String advanceType = null;
                        String particulars = null;
                        String date = null;
                        
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery("SELECT * FROM advance_table WHERE payrollId = "+previousPayrollId+"");
                        while(rs.next()){
                            advanceType = rs.getString("advanceType");
                            particulars = rs.getString("particulars");
                            date = rs.getString("datePosted");
                        }

                        pstmt = conn.prepareStatement("INSERT INTO advance_table(payrollId, amount, advanceType, particulars, datePosted, remarks) "
                                + "VALUES(?, ?, ?, ?, ?, ?)");
                        pstmt.setInt(1, payrollId);
                        pstmt.setDouble(2, previousAmountOfAdvances);
                        pstmt.setString(3, advanceType);
                        pstmt.setString(4, particulars);
                        pstmt.setString(5, date);
                        pstmt.setString(6, "from adjustment of previous payroll with payrollId:"+previousPayrollId);
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
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex1);
            } 
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public int getPreviousPayrollId(String employeeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        int previousPayrollId = 0;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id FROM payroll_table WHERE employeeId = '"+employeeId+"' ORDER BY id DESC LIMIT 1 ");
            while(rs.next()){
                previousPayrollId = util.convertStringToInteger(rs.getString("id"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return previousPayrollId;
    }
    
    public boolean isPayrollAdjusted(int payrollId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT actionTaken FROM payroll_table WHERE id = "+payrollId+" ");
            while(rs.next()){
                if(rs.getString("actionTaken") == null){
                    result = false;
                } else if(rs.getString("actionTaken").equals("adjusted")) {
                    result = true;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean unlockedPayroll(int payrollId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("UPDATE payroll_table SET rowStatus = ? "
                    + "WHERE id = ?");
            pstmt.setString(1, "unlocked");
            pstmt.setInt(2, payrollId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PayrollDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
}
