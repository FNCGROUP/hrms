/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.serviceprovider;

import com.openhris.payroll.dao.PayrollDAO;
import com.openhris.payroll.model.Adjustment;
import com.openhris.payroll.model.Advances;
import com.openhris.payroll.model.Payroll;
import com.openhris.payroll.model.PayrollRegister;
import com.openhris.payroll.service.PayrollService;
import com.openhris.timekeeping.model.Timekeeping;
import java.util.List;

/**
 *
 * @author jet
 */
public class PayrollServiceImpl implements PayrollService {

    PayrollDAO payrollDAO = new PayrollDAO();
    
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
        Double removedAmount, 
        Double amountToBeReceive, 
        Double amountReceivable, 
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
            return payrollDAO.getPayrollRegisterByBranch(branchId, 
                    payrollDate, 
                    prev);
    }

    @Override
    public boolean updatePhicContribution(int payrollId, 
        double phicAmount, 
        double netPay, 
        double amountToBeReceive, 
        double amountReceive) {
            return payrollDAO.updatePhicContribution(payrollId, 
                    phicAmount, 
                    netPay, 
                    amountToBeReceive, 
                    amountReceive);
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
        double amountReceive) {
            return payrollDAO.updateTaxWitheldAmount(payrollId, 
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
            return payrollDAO.updateHdmfContribution(payrollId, 
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
            return payrollDAO.updateSssContribution(payrollId, 
                    sssContribution, 
                    netPay, 
                    amountToBeReceive, 
                    amountReceive);
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
            boolean EDIT_PAYROLL, 
            double adjustments, 
            int previousPayrollId) {
        return payrollDAO.insertPayrollAndAttendance(payroll, 
                insertAttendanceList, 
                EDIT_PAYROLL, 
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
    
}
