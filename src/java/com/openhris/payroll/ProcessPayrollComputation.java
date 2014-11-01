/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.hrms.utilities.ContributionUtilities;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.dao.ServiceInsertDAO;
import com.openhris.employee.service.EmployeeService;
import com.openhris.employee.serviceprovider.EmployeeServiceImpl;
import com.openhris.payroll.model.Payroll;
import com.openhris.payroll.service.PayrollService;
import com.openhris.payroll.serviceprovider.PayrollServiceImpl;
import com.openhris.timekeeping.model.Timekeeping;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jet
 */
public class ProcessPayrollComputation {
    
    EmployeeService employeeService = new EmployeeServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    PayrollComputation sal = new PayrollComputation();    
    ContributionUtilities contributionUtil = new ContributionUtilities();
    ServiceInsertDAO serviceInsert = new ServiceInsertDAO();
    PayrollService payrollService = new PayrollServiceImpl();    
    DecimalFormat df = new DecimalFormat("0.00");
        
    String employeeId;
    int branchId;
    
    double employmentWage;
    String employmentWageStatus;
    String employmentWageEntry;
    double allowance;
    String allowanceEntry;
    double allowanceForLiquidation;
    String totalDependent;
    
    List<Payroll> payrollList = new ArrayList<Payroll>();
    List<Timekeeping> timekeepingList = new ArrayList<Timekeeping>();
    List<String> dateList = new ArrayList<String>();
    List<String> policyList = new ArrayList<String>();
    List<String> holidayList = new ArrayList<String>();
    
    double totalLates;
    double totalUndertime;
    double totalOvertime;
    double totalNightDifferential;
    
    double totalLatesDeduction;
    double totalUndertimeDeduction;
    double totalOvertimePay;
    double totalNightDifferetinalPay;
    double totalLegalHolidayPaid;
    double totalSpecialHolidayPaid;
    double totalWorkingDayOffPaid;
    double totalNonWorkingHolidayPaid;
    double totalAbsences;
        
    public ProcessPayrollComputation(String employeeId, int branchId){
        this.employeeId = employeeId;
        this.branchId = branchId;
    }
    
    public void initVariables(){
        employmentWage = employeeService.getEmploymentWage(employeeId);
        employmentWageStatus = employeeService.getEmploymentWageStatus(employeeId);
        employmentWageEntry = employeeService.getEmploymentWageEntry(employeeId);
        allowance = employeeService.getEmploymentAllowance(employeeId);
        allowanceEntry = employeeService.getEmploymentAllowanceEntry(employeeId);
        allowanceForLiquidation = employeeService.getEmploymentAllowanceForLiquidation(employeeId);
        totalDependent = employeeService.getEmployeeTotalDependent(employeeId);
    }
    
    public void initVariablesForComputation(List<Timekeeping> tkeep){ 
        this.timekeepingList = tkeep;
        for(Timekeeping t : tkeep){
            dateList.add(util.convertDateFormat(t.getAttendanceDate().toString()));
            policyList.add(t.getPolicy());
            holidayList.add(t.getHoliday());
            
            totalLates = totalLates + t.getLates();
            totalUndertime = totalUndertime + t.getUndertime();
            totalOvertime = totalOvertime = t.getOvertime();
            totalNightDifferential = totalNightDifferential + t.getNightDifferential();
            
            totalLatesDeduction = totalLatesDeduction + t.getLateDeduction();
            totalUndertimeDeduction = totalUndertimeDeduction + t.getUndertimeDeduction();
            totalOvertimePay = totalOvertimePay + t.getOvertimePaid();
            totalNightDifferetinalPay = totalNightDifferetinalPay + t.getNightDifferentialPaid();
            
            totalLegalHolidayPaid = totalLegalHolidayPaid + t.getLegalHolidayPaid();
            totalSpecialHolidayPaid = totalSpecialHolidayPaid + t.getSpecialHolidayPaid();
            totalWorkingDayOffPaid = totalWorkingDayOffPaid + t.getWorkingDayOffPaid();
            totalNonWorkingHolidayPaid = totalNonWorkingHolidayPaid + t.getNonWorkingHolidayPaid();
        }
    }
    
    public boolean processPayrollComputation(String payrollDate, 
            String payrollPeriod, 
            String attendancePeriodFrom, 
            String attendancePeriodTo, 
            boolean editPayroll, 
            int previousPayrollId){
        boolean result = false;
        try{
            Payroll payroll = new Payroll();
            payroll.setEmployeeId(employeeId);
            payroll.setBranchId(branchId);
            payroll.setPayrollDate(util.parsingDate(payrollDate));
            payroll.setPayrollPeriod(payrollPeriod);
            payroll.setAttendancePeriodFrom(util.parsingDate(attendancePeriodFrom));
            payroll.setAttendancePeriodTo(util.parsingDate(attendancePeriodTo));
            payroll.setTotalLatesDeduction(totalLatesDeduction);
            payroll.setTotalUndertimeDeduction(totalUndertimeDeduction);
            payroll.setTotalOvertimePaid(totalOvertimePay);
            payroll.setTotalNightDifferentialPaid(totalNightDifferetinalPay);
            payroll.setTotalLegalHolidayPaid(totalLegalHolidayPaid);
            payroll.setTotalSpecialHolidayPaid(totalSpecialHolidayPaid);
            payroll.setTotalWorkingDayOffPaid(totalWorkingDayOffPaid);
            payroll.setTotalNonWorkingHolidayPaid(getTotalNonWorkingHolidayPay());
            
            double basicSalary = sal.getBasicSalary(employmentWage, employmentWageEntry);
            payroll.setBasicSalary(basicSalary);
            
            double halfMonthSalary = sal.getHalfMonthSalary(employmentWageEntry, policyList, employmentWage, dateList, employeeId) + getTotalNonWorkingHolidayPay();
            payroll.setHalfMonthSalary(halfMonthSalary);
                      
            double taxableSalary = 0;
            if(employmentWageEntry.equals("daily")){
                taxableSalary = payroll.getGrossPay();
            } else {
                taxableSalary = sal.getTaxableSalary(employmentWage, employmentWageEntry, policyList, payroll.getGrossPay());
            }
            double sssContribution = 0;
            double phicContribution = 0;
            double hdmfContribution = 0;
            
            if(payrollPeriod.equals("15th of the month")){ 
                phicContribution = contributionUtil.getPhilhealth(basicSalary);
                hdmfContribution = contributionUtil.getHdmf(basicSalary);
                taxableSalary = taxableSalary - (phicContribution + hdmfContribution);
            }else{              
                sssContribution = contributionUtil.getSss(payroll.getGrossPay(), employeeId, payrollDate);
                taxableSalary = taxableSalary - (sssContribution);
            }
            
            payroll.setSss(sssContribution);
            payroll.setPhic(phicContribution);
            payroll.setHdmf(hdmfContribution);
            payroll.setTaxableSalary(taxableSalary);            
            payroll.setAbsences(sal.getTotalAbsences());            
            
            double tax = sal.getTax(totalDependent, taxableSalary); //get tax          
            if(employmentWageStatus.equals("minimum") || tax < 0){
                tax = 0;
            }        
            payroll.setTax(tax);

            double cashBond = 0;   
            payroll.setCashBond(cashBond);

            allowance = sal.getAllowance(policyList, allowanceEntry, allowance);
            payroll.setAllowance(allowance);

            int numberOfDays = sal.getNumberOfDays(dateList, policyList);
            payroll.setNumOfDays(numberOfDays);

            double afl = sal.getAllowanceForLiquidationDeduction(dateList, policyList, allowanceForLiquidation);
            payroll.setAllowanceForLiquidation(afl);
                                                
            double netSalary = taxableSalary - tax;
            payroll.setNetSalary(netSalary);

            double amountReceivable = new Double(df.format(netSalary + allowance + afl));  
            payroll.setAmountReceivable(amountReceivable);

            double amountToBeReceive = amountReceivable;
            payroll.setAmountToBeReceive(amountToBeReceive); 

            double adjustment = sal.getAdjustmentFromPreviousPayroll(employeeId); 
	    
            result = payrollService.insertPayrollAndAttendance(payroll, 
                    timekeepingList, 
                    editPayroll, 
                    adjustment, 
                    previousPayrollId);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        
        return result;
    }    
    
    private double getTotalNonWorkingHolidayPay(){
        return totalNonWorkingHolidayPaid;
    }
    
    private String getEmployeeId(){
        return employeeId;
    }
}
