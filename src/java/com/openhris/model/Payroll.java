/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.model;

import com.openhris.serviceprovider.PayrollServiceImpl;
import com.openhris.service.PayrollService;
import com.openhris.service.TimekeepingService;
import com.openhris.serviceprovider.TimekeepingServiceImpl;
import java.util.Date;

/**
 *
 * @author jet
 */
public class Payroll extends Employee {
    
    private int id;
    private Date attendancePeriodFrom;
    private Date attendancePeriodTo;
    private double basicSalary;
    private double halfMonthSalary;
    private double phic;
    private double sss;
    private double hdmf;
    private double grossPay;
    private double absences;
    private int numOfDays;
    private double taxableSalary;
    private double tax;
    private double cashBond;
    private double totalLatesDeduction;
    private double totalUndertimeDeduction;
    private double totalOvertimePaid;
    private double totalNightDifferentialPaid;
    private double totalDutyManagerPaid;
    private double totalLegalHolidayPaid;
    private double totalSpecialHolidayPaid;
    private double totalWorkingDayOffPaid;
    private double totalNonWorkingHolidayPaid;
    private double communicationAllowance;
    private double perDiemAllowance;
    private double colaAllowance;
    private double mealAllowance;
    private double transportationAllowance;
    double otherAllowances;    
    private double allowanceForLiquidation;
    private double totalAdvances;
    private double netSalary;
    private double adjustment;
    private double amountToBeReceive;
    private double amountReceivable;
    private double forAdjustments;
    private int branchId;
    private String payrollPeriod;
    private Date payrollDate;
    private String rowStatus;
    private String actionTaken;
    private Branch branch;

    TimekeepingService tkService = new TimekeepingServiceImpl();
    
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
        return grossPay = (getHalfMonthSalary() + getTotalOvertimePaid() + getTotalLegalHolidayPaid() + getTotalSpecialHolidayPaid() + 
                getTotalNightDifferentialPaid() + getTotalDutyManagerPaid() + getTotalWorkingDayOffPaid()) 
                - (getAbsences() + getTotalLatesDeduction() + getTotalUndertimeDeduction());
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
    
    public double getTotalDutyManagerPaid(){
        return totalDutyManagerPaid;
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

    public double getCommunicationAllowance() {
        return communicationAllowance;
    }

    public double getPerDiemAllowance() {
        return perDiemAllowance;
    }

    public double getColaAllowance() {
        return colaAllowance;
    }

    public double getMealAllowance() {
        return mealAllowance;
    }

    public double getTransportationAllowance() {
        return transportationAllowance;
    }

    public double getOtherAllowances() {
        return otherAllowances;
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

    public Branch getBranch() {
        return branch;
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
    
    public void setTotalDutyManagerPaid(double totalDutyManagerPaid){
        this.totalDutyManagerPaid = totalDutyManagerPaid;
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

    public void setGrossPay(double grossPay) {
        this.grossPay = grossPay;
    }

    public void setCommunicationAllowance(double communicationAllowance) {
        this.communicationAllowance = communicationAllowance;
    }

    public void setPerDiemAllowance(double perDiemAllowance) {
        this.perDiemAllowance = perDiemAllowance;
    }

    public void setColaAllowance(double colaAllowance) {
        this.colaAllowance = colaAllowance;
    }

    public void setMealAllowance(double mealAllowance) {
        this.mealAllowance = mealAllowance;
    }

    public void setTransportationAllowance(double transportationAllowance) {
        this.transportationAllowance = transportationAllowance;
    }

    public void setOtherAllowances(double otherAllowances) {
        this.otherAllowances = otherAllowances;
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

    public void setBranch(Branch branch) {
        this.branch = branch;
    }
    
}
