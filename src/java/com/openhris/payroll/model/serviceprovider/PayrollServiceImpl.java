/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.model.serviceprovider;

import com.openhris.dao.ServiceGetDAO;
import com.openhris.dao.ServiceInsertDAO;
import com.openhris.dao.ServiceUpdateDAO;
import com.openhris.payroll.model.Advances;
import com.openhris.payroll.model.Payroll;
import com.openhris.payroll.model.PayrollRegister;
import com.openhris.service.PayrollService;
import java.util.List;

/**
 *
 * @author jet
 */
public class PayrollServiceImpl implements PayrollService {

    ServiceGetDAO serviceGet = new ServiceGetDAO();
    ServiceUpdateDAO serviceUpdate = new ServiceUpdateDAO();
    ServiceInsertDAO serviceInsert = new ServiceInsertDAO();
    
    @Override
    public List<Payroll> getPayrollByBranchAndEmployee(int branchId, String employeeId) {
        return serviceGet.getPayrollByBranchAndEmployee(branchId, employeeId);
    }

    @Override
    public boolean removeSelectedRow(int id) {
        return serviceUpdate.removeSelectedRow(id);
    }

    @Override
    public List<Advances> getAdvancesByPayroll(int payrollId) {
        return serviceGet.getAdvancesByPayroll(payrollId);
    }

    @Override
    public boolean removeAdvanceById(int advanceId, 
        int payrollId, 
        Double removedAmount, 
        Double amountToBeReceive, 
        Double amountReceivable, 
        String remarks) {
            return serviceUpdate.removeAdvancesById(advanceId, 
                    payrollId, removedAmount, 
                    amountToBeReceive, 
                    amountReceivable, 
                    remarks);
    }

    @Override
    public List<String> getAdvanceTypeList() {
        return serviceGet.getAdvanceTypeLists();
    }

    @Override
    public boolean insertAdvanceType(String advanceType) {
        return serviceInsert.insertAdvanceType(advanceType);
    }

    @Override
    public boolean updateSalaryByAdvances(List<Advances> advanceceList) {
        return serviceUpdate.updateSalaryByAdvances(advanceceList);
    }

    @Override
    public double getTotalAdvancesByPayroll(int payrollId) {
        return serviceGet.getTotalAdvancesByPayroll(payrollId);
    }

    @Override
    public List<PayrollRegister> getPayrollRegisterByBranch(int branchId, 
        String payrollDate, 
        boolean prev) {
            return serviceGet.getPayrollRegisterByBranch(branchId, 
                    payrollDate, 
                    prev);
    }

    @Override
    public boolean updatePhicContribution(int payrollId, 
        double phicAmount, 
        double netPay, 
        double amountToBeReceive, 
        double amountReceive) {
            return serviceUpdate.updatePhicContribution(payrollId, 
                    phicAmount, 
                    netPay, 
                    amountToBeReceive, 
                    amountReceive);
    }

    @Override
    public String getPayrollPeriodByPayrollId(int payrollId) {
        return serviceGet.getPayrollPeriodByPayrollId(payrollId);
    }

    @Override
    public String getPayrollDateByPayrollId(int payrollId) {
        return serviceGet.getPayrollDateByPayrollId(payrollId);
    }

    @Override
    public boolean updatePayrollDate(int payrollId, String date) {
        return serviceUpdate.updatePayrollDate(payrollId, date);
    }

    @Override
    public boolean updateTaxWitheldAmount(int payrollId, 
        double newTaxWitheldAmount, 
        double netPay, 
        double amountToBeReceive, 
        double amountReceive) {
            return serviceUpdate.updateTaxWitheldAmount(payrollId, 
                    newTaxWitheldAmount, 
                    netPay, 
                    amountToBeReceive, 
                    amountReceive);
    }

    @Override
    public boolean updateHdmfContribution(int payrollId, 
        double hdmfContribution, 
        double netPay, 
        double amountToBeReceive, 
        double amountReceive) {
            return serviceUpdate.updateHdmfContribution(payrollId, 
                    hdmfContribution, 
                    netPay, 
                    amountToBeReceive, 
                    amountReceive);
    }

    @Override
    public boolean updateSssContribution(int payrollId, 
        double sssContribution, 
        double netPay, 
        double amountToBeReceive, 
        double amountReceive) {
            return serviceUpdate.updateSssContribution(payrollId, 
                    sssContribution, 
                    netPay, 
                    amountToBeReceive, 
                    amountReceive);
    }

    @Override
    public boolean lockPayroll(int payrollId) {
        return serviceUpdate.lockPayroll(payrollId);
    }

    @Override
    public double getForAdjustmentFromPreviousPayroll(String employeeId) {
        return serviceGet.getForAdjustmentFromPreviousPayroll(employeeId);
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
        return serviceGet.getPreviousAmountReceived(payrollId);
    }
    
}
