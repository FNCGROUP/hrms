/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.serviceprovider;

import com.hrms.classes.GlobalVariables;
import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.dao.PayrollDAO;
import com.openhris.global.SystemConstants;
import com.openhris.model.Adjustment;
import com.openhris.model.Advances;
import com.openhris.model.Branch;
import com.openhris.model.Payroll;
import com.openhris.model.PayrollRegister;
import com.openhris.service.PayrollService;
import com.openhris.model.Timekeeping;
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
 * @author jet
 */
public class PayrollServiceImpl implements PayrollService {

    PayrollDAO payrollDAO = new PayrollDAO();
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    @Override
    public List<Payroll> findPayrollByBranchAndEmployee(int branchId, String employeeId) {
//        return payrollDAO.getPayrollByBranchAndEmployee(branchId, employeeId);
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        String queryPayrollList;
        
        if(employeeId == null){
            queryPayrollList = "SELECT * FROM payroll_view WHERE branchId = "+branchId+" "
                    + "AND (currentStatus != 'removed' OR currentStatus IS NULL) "
                    + "ORDER BY id DESC";
        } else {
            queryPayrollList = "SELECT * FROM payroll_view WHERE employeeId = '"+employeeId+"' "
                    + "AND (currentStatus != 'removed' OR currentStatus IS NULL) "
                    + "ORDER BY id DESC";
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
                p.setTotalLatesHolidayDeduction(util.convertStringToDouble(rs.getString("totalLatesHolidayDeduction")));
                p.setTotalUndertimeDeduction(util.convertStringToDouble(rs.getString("totalUndertimeDeduction")));
                p.setTotalUndertimeHolidayDeduction(util.convertStringToDouble(rs.getString("totalUndertimeHolidayDeduction")));
                p.setTotalOvertimePaid(util.convertStringToDouble(rs.getString("totalOvertimePaid")));
                p.setTotalNightDifferentialPaid(util.convertStringToDouble(rs.getString("totalNightDifferentialPaid")));
                p.setTotalDutyManagerPaid(util.convertStringToDouble(rs.getString("totalDutyManagerPaid")));
                p.setTotalLegalHolidayPaid(util.convertStringToDouble(rs.getString("totalLegalHolidayPaid")));
                p.setTotalSpecialHolidayPaid(util.convertStringToDouble(rs.getString("totalSpecialHolidayPaid")));
                p.setTotalWorkingDayOffPaid(util.convertStringToDouble(rs.getString("totalWorkingDayOffPaid")));
                p.setCommunicationAllowance(util.convertStringToDouble(rs.getString("communicationAllowance")));
                p.setPerDiemAllowance(util.convertStringToDouble(rs.getString("perDiemAllowance")));
                p.setColaAllowance(util.convertStringToDouble(rs.getString("colaAllowance")));
                p.setMealAllowance(util.convertStringToDouble(rs.getString("mealAllowance")));
                p.setTransportationAllowance(util.convertStringToDouble(rs.getString("transportationAllowance")));
                p.setOtherAllowances(util.convertStringToDouble(rs.getString("otherAllowances")));
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
                p.setRate(util.convertStringToDouble(rs.getString("rate")));
                p.setWageEntry(rs.getString("wageEntry").charAt(0));
                
                Branch b = new Branch();
                b.setTradeName(rs.getString("tradeName"));
                b.setBranchName(rs.getString("branchName"));
                p.setBranch(b);
                
                payrollList.add(p);
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return payrollList;
    }

    @Override
    public boolean removeSelectedRow(int id) {
        return payrollDAO.removeSelectedRow(id);
    }

    @Override
    public List<Advances> getAdvancesByPayroll(int payrollId) {
        return payrollDAO.getAdvancesByPayroll(payrollId);
    }

    @Override
    public boolean removeAdvanceById(int advanceId, 
        int payrollId, 
        double removedAmount, 
        double amountToBeReceive, 
        double amountReceivable, 
        String remarks) {
            return payrollDAO.removeAdvancesById(advanceId, 
                    payrollId, removedAmount, 
                    amountToBeReceive, 
                    amountReceivable, 
                    remarks);
    }

    @Override
    public List<String> getAdvanceTypeList() {
        return payrollDAO.getAdvanceTypeLists();
    }

    @Override
    public boolean insertAdvanceType(String advanceType) {
        return payrollDAO.insertAdvanceType(advanceType);
    }

    @Override
    public boolean updateSalaryByAdvances(List<Advances> advanceceList) {
        return payrollDAO.updateSalaryByAdvances(advanceceList);
    }

    @Override
    public double getTotalAdvancesByPayroll(int payrollId) {
        return payrollDAO.getTotalAdvancesByPayroll(payrollId);
    }

    @Override
    public List<PayrollRegister> getPayrollRegisterByBranch(int branchId, 
        String payrollDate, 
        boolean prev) {
        
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
                pr.setTotalLatesHolidayDeduction(util.convertStringToDouble(rs.getString("totalLatesHolidayDeduction")));
                pr.setTotalUndertimeDeduction(util.convertStringToDouble(rs.getString("totalUndertimeDeduction")));
                pr.setTotalUndertimeHolidayDeduction(util.convertStringToDouble(rs.getString("totalUndertimeHolidayDeduction")));
                pr.setSss(util.convertStringToDouble(rs.getString("sss")));
                pr.setPhic(util.convertStringToDouble(rs.getString("phic")));
                pr.setHdmf(util.convertStringToDouble(rs.getString("hdmf")));
                pr.setTax(util.convertStringToDouble(rs.getString("tax")));
                pr.setNetSalary(util.convertStringToDouble(rs.getString("netSalary")));
                pr.setCommunicationAllowance(util.convertStringToDouble(rs.getString("communicationAllowance")));
                pr.setPerDiemAllowance(util.convertStringToDouble(rs.getString("perDiemAllowance")));
                pr.setColaAllowance(util.convertStringToDouble(rs.getString("colaAllowance")));
                pr.setMealAllowance(util.convertStringToDouble(rs.getString("mealAllowance")));
                pr.setTransportationAllowance(util.convertStringToDouble(rs.getString("transportationAllowance")));
                pr.setOtherAllowances(util.convertStringToDouble(rs.getString("otherAllowances")));
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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return payrollRegisterList;
        
//            return payrollDAO.getPayrollRegisterByBranch(branchId, 
//                    payrollDate, 
//                    prev);
    }

    @Override
    public boolean updatePhicContribution(int payrollId, 
        double phicAmount, 
        double netPay, 
        double amountToBeReceive, 
        double amountReceive, 
        boolean isPayrollAdjusted) {
            return payrollDAO.updatePhicContribution(payrollId, 
                    phicAmount, 
                    netPay, 
                    amountToBeReceive, 
                    amountReceive, 
                    isPayrollAdjusted);
    }

    @Override
    public String getPayrollPeriodByPayrollId(int payrollId) {
        return payrollDAO.getPayrollPeriodByPayrollId(payrollId);
    }

    @Override
    public String getPayrollDateByPayrollId(int payrollId) {
        return payrollDAO.getPayrollDateByPayrollId(payrollId);
    }

    @Override
    public boolean updatePayrollDate(int payrollId, String date) {
        return payrollDAO.updatePayrollDate(payrollId, date);
    }

    @Override
    public boolean updateTaxWitheldAmount(int payrollId, 
        double newTaxWitheldAmount, 
        double netPay, 
        double amountToBeReceive, 
        double amountReceive, 
        boolean isPayrollAdjusted) {
            return payrollDAO.updateTaxWitheldAmount(payrollId, 
                    newTaxWitheldAmount, 
                    netPay, 
                    amountToBeReceive, 
                    amountReceive, 
                    isPayrollAdjusted);
    }

    @Override
    public boolean updateHdmfContribution(int payrollId, 
        double hdmfContribution, 
        double netPay, 
        double amountToBeReceive, 
        double amountReceive, 
        boolean isPayrollAdjusted) {
            return payrollDAO.updateHdmfContribution(payrollId, 
                    hdmfContribution, 
                    netPay, 
                    amountToBeReceive, 
                    amountReceive, 
                    isPayrollAdjusted);
    }

    @Override
    public boolean updateSssContribution(int payrollId, 
        double sssContribution, 
        double netPay, 
        double amountToBeReceive, 
        double amountReceive, 
        boolean isPayrollAdjusted) {
            return payrollDAO.updateSssContribution(payrollId, 
                    sssContribution, 
                    netPay, 
                    amountToBeReceive, 
                    amountReceive, 
                    isPayrollAdjusted);
    }

    @Override
    public boolean lockPayroll(int payrollId) {
        return payrollDAO.lockPayroll(payrollId);
    }

    @Override
    public double getAdjustmentFromPreviousPayroll(String employeeId) {
        return payrollDAO.getAdjustmentFromPreviousPayroll(employeeId);
    }

    @Override
    public boolean insertForAdjustmentAndUpdatePayroll(int payrollId, 
        double adjustedAmount, 
        double amountReceivable, 
        double amountToBeReceive) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getPreviousAmountReceived(int payrollId) {
        return payrollDAO.getPreviousAmountReceived(payrollId);
    }

    @Override
    public boolean insertPayrollAndAttendance(Payroll payroll, 
            List<Timekeeping> insertAttendanceList,
            double adjustments, 
            int previousPayrollId) {
//        return payrollDAO.insertPayrollAndAttendance(payroll, 
//                insertAttendanceList, 
//                adjustments, 
//                previousPayrollId);
        Connection conn = getConnection.connection();
        boolean result = false;
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("INSERT INTO payroll_table "
                    + "SET employeeId = ?, "
                    + "attendancePeriodFrom = ?, "
                    + "attendancePeriodTo = ?, "
                    + "basicSalary = ?, "
                    + "halfMonthSalary = ?, "
                    + "phic = ?, "
                    + "sss = ?, "
                    + "hdmf = ?, "
                    + "absences = ?, "
                    + "numberOfDays = ?, "
                    + "taxableSalary = ?, "
                    + "tax = ?, "
                    + "cashBond = ?, "
                    + "totalLatesDeduction = ?, "
                    + "totalLatesHolidayDeduction = ?, "
                    + "totalUndertimeDeduction = ?, "
                    + "totalUndertimeHolidayDeduction = ?, "
                    + "totalOvertimePaid = ?, "
                    + "totalNightDifferentialPaid = ?, "
                    + "totalDutyManagerPaid = ?, "
                    + "totalLegalHolidayPaid = ?, "
                    + "totalSpecialHolidayPaid = ?, "
                    + "totalWorkingDayOffPaid = ?, "
                    + "totalNonWorkingHolidayPaid = ?, "
                    + "communicationAllowance = ?, "
                    + "perDiemAllowance = ?, "
                    + "colaAllowance = ?, "
                    + "mealAllowance = ?, "
                    + "transportationAllowance = ?, "
                    + "otherAllowances = ?, "
                    + "allowanceForLiquidation = ?, "
                    + "netSalary = ?, "
                    + "adjustments = ?, "
                    + "amountToBeReceive = ?, "
                    + "amountReceivable = ?, "
                    + "branchId = ?, "
                    + "payrollPeriod = ?, "
                    + "payrollDate = ?, "
                    + "rate = ?, "
                    + "wageEntry = ? ");
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
            pstmt.setDouble(15, payroll.getTotalLatesHolidayDeduction());
            pstmt.setDouble(16, payroll.getTotalUndertimeDeduction());
            pstmt.setDouble(17, payroll.getTotalUndertimeHolidayDeduction());
            pstmt.setDouble(18, payroll.getTotalOvertimePaid());
            pstmt.setDouble(19, payroll.getTotalNightDifferentialPaid());
            pstmt.setDouble(20, payroll.getTotalDutyManagerPaid());
            pstmt.setDouble(21, payroll.getTotalLegalHolidayPaid());
            pstmt.setDouble(22, payroll.getTotalSpecialHolidayPaid());
            pstmt.setDouble(23, payroll.getTotalWorkingDayOffPaid());
            pstmt.setDouble(24, payroll.getTotalNonWorkingHolidayPaid());
            pstmt.setDouble(25, payroll.getCommunicationAllowance());
            pstmt.setDouble(26, payroll.getPerDiemAllowance());
            pstmt.setDouble(27, payroll.getColaAllowance());
            pstmt.setDouble(28, payroll.getMealAllowance());
            pstmt.setDouble(29, payroll.getTransportationAllowance());
            pstmt.setDouble(30, payroll.getOtherAllowances());
            pstmt.setDouble(31, payroll.getAllowanceForLiquidation());
            pstmt.setDouble(32, payroll.getNetSalary());
            pstmt.setDouble(33, adjustments);
            pstmt.setDouble(34, payroll.getAmountToBeReceive() + adjustments);
            pstmt.setDouble(35, payroll.getAmountReceivable() + adjustments);
            pstmt.setInt(36, payroll.getBranchId());
            pstmt.setString(37, payroll.getPayrollPeriod());
            pstmt.setString(38, util.convertDateFormat(payroll.getPayrollDate().toString())); 
            pstmt.setDouble(39, payroll.getRate());
            pstmt.setString(40, String.valueOf(payroll.getWageEntry()));
            pstmt.executeUpdate();
                
            int payrollId = 0;            
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT last_insert_id() AS id FROM payroll_table ");
            while(rs.next()){
                payrollId = Integer.parseInt(rs.getString("id"));
            }
                
            for(Timekeeping t : insertAttendanceList){
                pstmt = conn.prepareStatement("INSERT INTO timekeeping_table "
                        + "SET payrollId = ?, "
                        + "attendanceDate = ?, "
                        + "policy = ?, "
                        + "holiday = ?, "
                        + "premium = ?, "
                        + "lates = ?, "
                        + "undertime = ?, "
                        + "overtime = ?, "
                        + "nightDifferential = ?, "
                        + "dutyManager = ?, "
                        + "latesDeduction = ?, "
                        + "undertimeDeduction = ?, "
                        + "overtimePaid = ?, "
                        + "nightDifferentialPaid = ?, "
                        + "dutyManagerPaid = ?, "
                        + "legalHolidayPaid = ?, "
                        + "specialHolidayPaid = ?, "
                        + "workingDayOffPaid = ?, "
                        + "psHolidayPaid = ?, "
                        + "latesLegalHolidayDeduction = ?, "
                        + "latesSpecialHolidayDeduction = ?, "
                        + "latesWorkingDayOffDeduction = ?, "
                        + "undertimeLegalHolidayDeduction = ?, "
                        + "undertimeSpecialHolidayDeduction = ?, "
                        + "undertimeWorkingDayOffDeduction = ? "); 
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
                pstmt.setDouble(20, t.getLatesLegalHolidayDeduction());
                pstmt.setDouble(21, t.getLatesSpecialHolidayDeduction());
                pstmt.setDouble(22, t.getLatesWorkingDayOffDeduction());
                pstmt.setDouble(23, t.getUndertimeLegalHolidayDeduction());
                pstmt.setDouble(24, t.getUndertimeSpecialHolidayDeduction());
                pstmt.setDouble(25, t.getUndertimeWorkingDayOffDeduction());
                pstmt.executeUpdate();
            }                          
			                
            if(getForAdjustmentFromPreviousPayroll(findPreviousPayrollId(payroll.getEmployeeId())) != 0){                    
                pstmt = conn.prepareStatement("INSERT INTO adjustments(payrollId, amount, remarks, datePosted) VALUES(?, ?, ?, now())");
                pstmt.setInt(1, payrollId);
                pstmt.setDouble(2, getForAdjustmentFromPreviousPayroll(findPreviousPayrollId(payroll.getEmployeeId())));
                pstmt.setString(3, "edit timekeeping table");
                pstmt.executeUpdate();
            }
                
            if(previousPayrollId != 0){
                double grossPay = 0;
                double sss = 0;
                double phic = 0;
                double hdmf = 0;
                double tax = 0;
                double allowance = 0;
                double afl = payroll.getAllowanceForLiquidation();
                double forAdjustment = 0;
                double previousAdjustments = 0;
                double previousAdvances = 0;
		double previousAmountReceived = 0;
                    
                double communication = payroll.getCommunicationAllowance();
                double perDiem = payroll.getPerDiemAllowance();
                double cola = payroll.getColaAllowance();
                double meal = payroll.getMealAllowance();
                double transportation = payroll.getTransportationAllowance();
                double others = payroll.getOtherAllowances();
                                        
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT * FROM payroll_register WHERE salaryId = "+previousPayrollId+" ");
                while(rs.next()){
                    sss = util.convertStringToDouble(rs.getString("sss"));
                    phic = util.convertStringToDouble(rs.getString("phic"));
                    hdmf = util.convertStringToDouble(rs.getString("hdmf"));
                    tax = util.convertStringToDouble(rs.getString("tax"));
                    previousAdvances = util.convertStringToDouble(rs.getString("advances"));
                    previousAdjustments = util.convertStringToDouble(rs.getString("adjustments"));
                    previousAmountReceived = util.convertStringToDouble(rs.getString("amountReceivable"));
                }
                    
                allowance = communication + perDiem + cola + meal + transportation + others;
                double netSalary = payroll.getGrossPay() - sss - phic - hdmf - tax; 
                double amountToBeReceive = (netSalary + allowance + afl + previousAdjustments + forAdjustment) - previousAdvances;
                forAdjustment = amountToBeReceive - previousAmountReceived;
                    
		pstmt = conn.prepareStatement("UPDATE payroll_table SET "
                        + "sss = ?, "
                        + "phic = ?, "
                        + "hdmf = ?, "
                        + "tax = ?, "
                        + "netSalary = ?, "
                        + "allowanceForLiquidation = ?, " 
                        + "adjustments = ?, "
			+ "amountToBeReceive = ?, "
			+ "amountReceivable = ?, "
                        + "forAdjustments = ? "
			+ "WHERE id = ?");
		pstmt.setDouble(1, sss);
		pstmt.setDouble(2, phic);
		pstmt.setDouble(3, hdmf);
                pstmt.setDouble(4, tax);
		pstmt.setDouble(5, netSalary);
                pstmt.setDouble(6, afl);
		pstmt.setDouble(7, previousAdjustments);
		pstmt.setDouble(8, amountToBeReceive);
                pstmt.setDouble(9, previousAmountReceived);
		pstmt.setDouble(10, forAdjustment);
                pstmt.setInt(11, payrollId);
		pstmt.executeUpdate();
		    
		pstmt = conn.prepareStatement("UPDATE payroll_table SET actionTaken = 'adjusted' WHERE id = "+payrollId+" ");
                pstmt.executeUpdate();
                
                pstmt = conn.prepareStatement("UPDATE payroll_table SET actionTaken = 'previous' WHERE id = "+previousPayrollId+" ");
                pstmt.executeUpdate();
                             
                if(previousAdvances != 0){
                    double advancesAmount = 0;
                    String advanceType = null;
                    String particulars = null;
                    String date = null;
                        
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery("SELECT * FROM advance_table "
                            + "WHERE payrollId = "+previousPayrollId+" "
                            + "AND rowStatus IS NULL");
                    while(rs.next()){
                        pstmt = conn.prepareStatement("INSERT INTO advance_table SET "
                                + "payrollId = ?, "
                                + "amount = ?, "
                                + "advanceType = ?, "
                                + "particulars = ?, "
                                + "datePosted = ?, "
                                + "remarks = ? ");
                        pstmt.setInt(1, payrollId);
                        pstmt.setDouble(2, util.convertStringToDouble(rs.getString("amount")));
                        pstmt.setString(3, rs.getString("advanceType"));
                        pstmt.setString(4, rs.getString("particulars"));
                        pstmt.setString(5, rs.getString("datePosted"));
                        pstmt.setString(6, "from adjustment of previous payroll with payrollId: "+previousPayrollId);
                        pstmt.executeUpdate();
                    }                        
                }
            }
 
            pstmt = conn.prepareStatement("INSERT INTO payroll_logs "
                    + "SET PayrollID = ?, "
                    + "Remarks = ?, "
                    + "DateRemarked = now(), "
                    + "UserID = ?");
            pstmt.setInt(1, payrollId);
            pstmt.setString(2, SystemConstants.NEW_PAYROLL);
            pstmt.setInt(3, GlobalVariables.getUserId());
            pstmt.executeUpdate();
                
            conn.commit();
            System.out.println("Transaction commit...");
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
                System.out.println("Connection rollback...");
            } catch (SQLException ex1) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex1);
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
    public List<Adjustment> getListOfAdjustmentFromPayrollId(int payrollId) {
        return payrollDAO.getListOfAdjustmentFromPayrollId(payrollId);
    }

    @Override
    public boolean removeAdjustmentById(int adjustmentId, 
            double amountToBeReceive, 
            double amountReceived, 
            double adjustments, 
            int payrollId) {
        return payrollDAO.removeAdjustmentById(adjustmentId, 
                amountToBeReceive, 
                amountReceived, 
                adjustments, 
                payrollId);
    }

    @Override
    public double getForAdjustmentFromPreviousPayroll(int previousPayrollId) {
        return payrollDAO.getForAdjustmentFromPreviousPayroll(previousPayrollId);
    }

    @Override
    public boolean insertAdjustmentToPayroll(int payrollId, 
            double amountToBeReceive, 
            double amountReceived, 
            double adjustment, 
            String remarks) {
        return payrollDAO.insertAdjustmentToPayroll(payrollId, 
                amountToBeReceive, 
                amountReceived, 
                adjustment, 
                remarks);
    }

    @Override
    public boolean isPayrollAdjusted(int payrollId) {
        return payrollDAO.isPayrollAdjusted(payrollId);
    }

    @Override
    public boolean unlockedPayroll(int payrollId) {
        return payrollDAO.unlockedPayroll(payrollId);
    }

    @Override
    public boolean addPerDiem(int payrollId, 
            double newAmount, 
            double oldAmount, 
            double amountToBeReceive, 
            double amountReceived) {
        return payrollDAO.addPerDiem(payrollId, 
                newAmount, 
                oldAmount, 
                amountToBeReceive, 
                amountReceived);
    }

    @Override
    public int findPreviousPayrollId(String employeeId) {
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
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return previousPayrollId;
    }

    @Override
    public boolean update(String column, String value, int payrollId) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("UPDATE payroll_table "
                    + "SET "+column+ "= ? "
                    + "WHERE id = ? ");
            pstmt.setString(1, value);
            pstmt.setInt(2, payrollId);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement("INSERT INTO payroll_logs "
                    + "SET PayrollID = ?, "
                    + "Remarks = ?, "
                    + "DateRemarked = now(), "
                    + "UserID = ?");
            pstmt.setInt(1, payrollId);
            pstmt.setString(2, SystemConstants.UPDATE_PAYROLL+": change "+column);
            pstmt.setInt(3, GlobalVariables.getUserId());
            pstmt.executeUpdate();
            
            result = true;
            conn.commit();
        } catch (SQLException ex) {
            try {
                conn.rollback();
                System.out.println("Connection rollback...");
            } catch (SQLException ex1) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex1);
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
