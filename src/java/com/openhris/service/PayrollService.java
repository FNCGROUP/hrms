/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.service;

import com.openhris.payroll.model.Advances;
import com.openhris.payroll.model.Payroll;
import com.openhris.payroll.model.PayrollRegister;
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
    
    public double getForAdjustmentFromPreviousPayroll(String employeeId);
    
    public boolean insertForAdjustmentAndUpdatePayroll(int payrollId, 
            double adjustedAmount, 
            double amountReceivable, 
            double amountToBeReceive);
    
    public double getPreviousAmountReceived(int payrollId);    
}
