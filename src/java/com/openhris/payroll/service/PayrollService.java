/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.service;

import com.openhris.payroll.model.Adjustment;
import com.openhris.payroll.model.Advances;
import com.openhris.payroll.model.Payroll;
import com.openhris.payroll.model.PayrollRegister;
import com.openhris.timekeeping.model.Timekeeping;
import java.util.List;

/**
 *
 * @author jet
 */
public interface PayrollService {
    
    public List<Payroll> getPayrollByBranchAndEmployee(int branchId, String employeeId);
    
    public boolean removeSelectedRow(int id);
    
    public List<Advances> getAdvancesByPayroll(int payrollId);
    
    public double getTotalAdvancesByPayroll(int payrollId);
    
    public boolean removeAdvanceById(int advanceId, int payrollId, 
            Double removedAmount, 
            Double amountToBeReceive, 
            Double amountReceivable, 
            String remarks);
    
    public List<String> getAdvanceTypeList();
    
    public boolean insertAdvanceType(String advanceType);
    
    public boolean updateSalaryByAdvances(List<Advances> advanceceList);
    
    public List<PayrollRegister> getPayrollRegisterByBranch(int branchId, String payrollDate, boolean prev);
    
    public boolean updatePhicContribution(int payrollId, 
            double phicAmount, 
            double netPay, 
            double amountToBeReceive, 
            double amountReceive);
    
    public String getPayrollPeriodByPayrollId(int payrollId);
    
    public String getPayrollDateByPayrollId(int payrollId);
    
    public boolean updatePayrollDate(int payrollId, String date);
    
    public boolean updateTaxWitheldAmount(int payrollId, 
            double newTaxWitheldAmount, 
            double netPay, 
            double amountToBeReceive, 
            double amountReceive);
    
    public boolean updateHdmfContribution(int payrollId, 
            double phicAmount, 
            double netPay, 
            double amountToBeReceive, 
            double amountReceive);
    
    public boolean updateSssContribution(int payrollId, 
            double sssContribution, 
            double netPay, 
            double amountToBeReceive, 
            double amountReceive);
    
    public boolean lockPayroll(int payrollId);
    
    public double getAdjustmentFromPreviousPayroll(String employeeId);
    
    public boolean insertForAdjustmentAndUpdatePayroll(int payrollId, 
            double adjustedAmount, 
            double amountReceivable, 
            double amountToBeReceive);
    
    public double getPreviousAmountReceived(int payrollId); 
    
    public boolean insertPayrollAndAttendance(Payroll payroll, 
            List<Timekeeping> insertAttendanceList, 
            boolean EDIT_PAYROLL, 
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
    
    public boolean isPayrollAdjusted(int id);
}
