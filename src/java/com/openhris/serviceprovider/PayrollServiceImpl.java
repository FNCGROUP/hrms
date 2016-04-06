/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.serviceprovider;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.dao.PayrollDAO;
import com.openhris.model.Adjustment;
import com.openhris.model.Advances;
import com.openhris.model.Payroll;
import com.openhris.model.PayrollRegister;
import com.openhris.service.PayrollService;
import com.openhris.model.Timekeeping;
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
public class PayrollServiceImpl implements PayrollService {

    PayrollDAO payrollDAO = new PayrollDAO();
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    @Override
    public List<Payroll> getPayrollByBranchAndEmployee(int branchId, String employeeId) {
        return payrollDAO.getPayrollByBranchAndEmployee(branchId, employeeId);
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
        return payrollDAO.insertPayrollAndAttendance(payroll, 
                insertAttendanceList, 
                adjustments, 
                previousPayrollId);
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
    
}
