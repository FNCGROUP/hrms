/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.service;

import com.openhris.model.Adjustment;
import com.openhris.model.Advances;
import com.openhris.model.Payroll;
import com.openhris.model.PayrollRegister;
import com.openhris.model.Timekeeping;
import java.util.List;

/**
 *
 * @author jet
 */
public interface PayrollService {
    
    public List<Payroll> findPayrollByBranchAndEmployee(int branchId, String employeeId);
    
    public boolean removeSelectedRow(int id);
    
    public List<Advances> getAdvancesByPayroll(int payrollId);
    
    public double getTotalAdvancesByPayroll(int payrollId);
    
    public boolean removeAdvanceById(int advanceId, int payrollId, 
            double removedAmount, 
            double amountToBeReceive, 
            double amountReceivable, 
            String remarks);
    
    public List<String> getAdvanceTypeList();
    
    public boolean insertAdvanceType(String advanceType);
    
    public boolean updateSalaryByAdvances(List<Advances> advanceceList);
    
    public List<PayrollRegister> getPayrollRegisterByBranch(int branchId, String payrollDate, boolean prev);
    
    public boolean updatePhicContribution(int payrollId, 
            double phicAmount, 
            double netPay, 
            double amountToBeReceive, 
            double amountReceive, 
            boolean isPayrollAdjusted);
    
    public String getPayrollPeriodByPayrollId(int payrollId);
    
    public String getPayrollDateByPayrollId(int payrollId);
    
    public boolean updatePayrollDate(int payrollId, String date);
    
    public boolean updateTaxWitheldAmount(int payrollId, 
            double newTaxWitheldAmount, 
            double netPay, 
            double amountToBeReceive, 
            double amountReceive, 
            boolean isPayrollAdjusted);
    
    public boolean updateHdmfContribution(int payrollId, 
            double phicAmount, 
            double netPay, 
            double amountToBeReceive, 
            double amountReceive, 
            boolean isPayrollAdjusted);
    
    public boolean updateSssContribution(int payrollId, 
            double sssContribution, 
            double netPay, 
            double amountToBeReceive, 
            double amountReceive, 
            boolean isPayrollAdjusted);
    
    public boolean lockPayroll(int payrollId);
    
    public double getAdjustmentFromPreviousPayroll(String employeeId);
    
    public boolean insertForAdjustmentAndUpdatePayroll(int payrollId, 
            double adjustedAmount, 
            double amountReceivable, 
            double amountToBeReceive);
    
    public double getPreviousAmountReceived(int payrollId); 
    
    public boolean insertPayrollAndAttendance(Payroll payroll, 
            List<Timekeeping> insertAttendanceList,
            double adjustments, 
            int previousPayrollId);
    
    public List<Adjustment> getListOfAdjustmentFromPayrollId(int payrollId);
    
    public boolean removeAdjustmentById(int adjustmentId, 
            double amountToBeReceive, 
            double amountReceived, 
            double adjustments, 
            int payrollId);
    
    public double getForAdjustmentFromPreviousPayroll(int previousPayrollId);
    
    public boolean insertAdjustmentToPayroll(int payrollId, 
            double amountToBeReceive, 
            double amountReceived, 
            double adjustment, 
            String remarks);
    
    public boolean isPayrollAdjusted(int payrollId);
    
    public boolean unlockedPayroll(int payrollId);
    
    public boolean addPerDiem(int payrollId, 
            double newAmount, 
            double oldAmount, 
            double amountToBeReceive, 
            double amountReceived);
    
    public int findPreviousPayrollId(String employeeId);
    
    public boolean update(String column, String value, int payrollId);
}
