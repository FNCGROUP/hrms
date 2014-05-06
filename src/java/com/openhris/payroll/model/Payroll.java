/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.model;

import com.openhris.employee.model.Employee;
import com.openhris.payroll.serviceprovider.PayrollServiceImpl;
import com.openhris.service.PayrollService;
import java.util.Date;

/**
 *
 * @author jet
 */
public class Payroll extends Employee {
    
    int id;
    Date attendancePeriodFrom;
    Date attendancePeriodTo;
    double basicSalary;
    double halfMonthSalary;
    double phic;
    double sss;
    double hdmf;
    double grossPay;
    double absences;
    int numOfDays;
    double taxableSalary;
    double tax;
    double cashBond;
    double totalLatesDeduction;
    double totalUndertimeDeduction;
    double totalOvertimePaid;
    double totalNightDifferentialPaid;
    double totalLegalHolidayPaid;
    double totalSpecialHolidayPaid;
    double totalWorkingDayOffPaid;
    double totalNonWorkingHolidayPaid;
    double allowance;
    double allowanceForLiquidation;
    double totalAdvances;
    double netSalary;
    double adjustment;
    double amountToBeReceive;
    double amountReceivable;
    double forAdjustments;
    int branchId;
    String payrollPeriod;
    Date payrollDate;
    String rowStatus;
    String actionTaken;

    public int getId(){
        return id;
    }
    
    public Date getAttendancePeriodFrom() {
        return attendancePeriodFrom;
    }

    public Date getAttendancePeriodTo() {
        return attendancePeriodTo;
    }

    public double getBasicSalary() {
        return basicSalary;
    }

    public double getHalfMonthSalary() {
        return halfMonthSalary;
    }

    public double getPhic() {
        return phic;
    }

    public double getSss() {
        return sss;
    }

    public double getHdmf() {
        return hdmf;
    }

    public double getGrossPay(){
        return grossPay = (halfMonthSalary + totalOvertimePaid + totalLegalHolidayPaid + totalSpecialHolidayPaid + 
                totalNightDifferentialPaid + totalWorkingDayOffPaid) - (absences + totalLatesDeduction + totalUndertimeDeduction);
    }
    
    public double getAbsences() {
        return absences;
    }

    public int getNumOfDays() {
        return numOfDays;
    }

    public double getTaxableSalary() {
        return taxableSalary;
    }

    public double getTax() {
        return tax;
    }

    public double getCashBond() {
        return cashBond;
    }

    public double getTotalLatesDeduction() {
        return totalLatesDeduction;
    }

    public double getTotalUndertimeDeduction() {
        return totalUndertimeDeduction;
    }

    public double getTotalOvertimePaid() {
        return totalOvertimePaid;
    }

    public double getTotalNightDifferentialPaid() {
        return totalNightDifferentialPaid;
    }

    public double getTotalLegalHolidayPaid() {
        return totalLegalHolidayPaid;
    }

    public double getTotalSpecialHolidayPaid() {
        return totalSpecialHolidayPaid;
    }

    public double getTotalWorkingDayOffPaid() {
        return totalWorkingDayOffPaid;
    }

    public double getTotalNonWorkingHolidayPaid(){
        return totalNonWorkingHolidayPaid;
    }
    
    public double getAllowance() {
        return allowance;
    }

    public double getAllowanceForLiquidation() {
        return allowanceForLiquidation;
    }

    public double getTotalAdvances(){
        PayrollService payrollService = new PayrollServiceImpl();
        return payrollService.getTotalAdvancesByPayroll(getId());
    }
    
    public double getNetSalary() {
        return netSalary;
    }

    public double getAdjustment(){
        return adjustment;
    }
    
    public double getAmountToBeReceive() {
        return amountToBeReceive;
    }

    public double getAmountReceivable() {
        return amountReceivable;
    }

    public double getForAdjustments() {
        return forAdjustments;
    }

    public int getBranchId() {
        return branchId;
    }

    public String getPayrollPeriod() {
        return payrollPeriod;
    }

    public Date getPayrollDate() {
        return payrollDate;
    }

    public String getRowStatus() {
        return rowStatus;
    }

    public String getActionTaken() {
        return actionTaken;
    }

    public void setId(int id){
        this.id = id;
    }
    
    public void setAttendancePeriodFrom(Date attendancePeriodFrom) {
        this.attendancePeriodFrom = attendancePeriodFrom;
    }

    public void setAttendancePeriodTo(Date attendancePeriodTo) {
        this.attendancePeriodTo = attendancePeriodTo;
    }

    public void setBasicSalary(double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public void setHalfMonthSalary(double halfMonthSalary) {
        this.halfMonthSalary = halfMonthSalary;
    }

    public void setPhic(double phic) {
        this.phic = phic;
    }

    public void setSss(double sss) {
        this.sss = sss;
    }

    public void setHdmf(double hdmf) {
        this.hdmf = hdmf;
    }

    public void setAbsences(double absences) {
        this.absences = absences;
    }

    public void setNumOfDays(int numOfDays) {
        this.numOfDays = numOfDays;
    }

    public void setTaxableSalary(double taxableSalary) {
        this.taxableSalary = taxableSalary;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public void setCashBond(double cashBond) {
        this.cashBond = cashBond;
    }

    public void setTotalLatesDeduction(double totalLatesDeduction) {
        this.totalLatesDeduction = totalLatesDeduction;
    }

    public void setTotalUndertimeDeduction(double totalUndertimeDeduction) {
        this.totalUndertimeDeduction = totalUndertimeDeduction;
    }

    public void setTotalOvertimePaid(double totalOvertimePaid) {
        this.totalOvertimePaid = totalOvertimePaid;
    }

    public void setTotalNightDifferentialPaid(double totalNightDifferentialPaid) {
        this.totalNightDifferentialPaid = totalNightDifferentialPaid;
    }

    public void setTotalLegalHolidayPaid(double totalLegalHolidayPaid) {
        this.totalLegalHolidayPaid = totalLegalHolidayPaid;
    }

    public void setTotalSpecialHolidayPaid(double totalSpecialHolidayPaid) {
        this.totalSpecialHolidayPaid = totalSpecialHolidayPaid;
    }

    public void setTotalWorkingDayOffPaid(double totalWorkingDayOffPaid) {
        this.totalWorkingDayOffPaid = totalWorkingDayOffPaid;
    }

    public void setTotalNonWorkingHolidayPaid(double totalNonWorkingHolidayPaid){
        this.totalNonWorkingHolidayPaid = totalNonWorkingHolidayPaid;
    }
    
    public void setAllowance(double allowance) {
        this.allowance = allowance;
    }

    public void setAllowanceForLiquidation(double allowanceForLiquidation) {
        this.allowanceForLiquidation = allowanceForLiquidation;
    }

    public void setTotalAdvances(double totalAdvances){
        this.totalAdvances = totalAdvances;
    }
    
    public void setNetSalary(double netSalary) {
        this.netSalary = netSalary;
    }

    public void setAdjustment(double adjustment){
        this.adjustment = adjustment;
    }
    
    public void setAmountToBeReceive(double amountToBeReceive) {
        this.amountToBeReceive = amountToBeReceive;
    }

    public void setAmountReceivable(double amountReceivable) {
        this.amountReceivable = amountReceivable;
    }

    public void setForAdjustments(double forAdjustments) {
        this.forAdjustments = forAdjustments;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public void setPayrollPeriod(String payrollPeriod) {
        this.payrollPeriod = payrollPeriod;
    }

    public void setPayrollDate(Date payrollDate) {
        this.payrollDate = payrollDate;
    }

    public void setRowStatus(String rowStatus) {
        this.rowStatus = rowStatus;
    }

    public void setActionTaken(String actionTaken) {
        this.actionTaken = actionTaken;
    }    
    
}
