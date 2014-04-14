/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.hrms.utilities.ContributionUtilities;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.dao.ServiceInsertDAO;
import com.openhris.employee.serviceprovider.EmployeeServiceImpl;
import com.openhris.payroll.model.Payroll;
import com.openhris.payroll.model.serviceprovider.PayrollServiceImpl;
import com.openhris.service.EmployeeService;
import com.openhris.service.PayrollService;
import com.openhris.timekeeping.model.Timekeeping;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
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
            payroll.setTotalNonWorkingHolidayPaid(totalNonWorkingHolidayPaid);

            double basicSalary = sal.getBasicSalary(employmentWage, employmentWageEntry);
            payroll.setBasicSalary(basicSalary);

            double halfMonthSalary = sal.getHalfMonthSalary(employmentWageEntry, policyList, employmentWage, dateList, employeeId);
            payroll.setHalfMonthSalary(halfMonthSalary);
	    
            double taxableSalary = sal.getTaxableSalary(employmentWage, employmentWageEntry, policyList, payroll.getGrossPay());
            payroll.setTaxableSalary(payroll.getGrossPay());
            payroll.setAbsences(sal.getTotalAbsences());
            
            double tax = sal.getTax(totalDependent, payroll.getGrossPay()); //get tax          
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

            double afl = (allowanceForLiquidation/2) - sal.getAllowanceForLiquidationDeduction(policyList, allowanceForLiquidation);
            payroll.setAllowanceForLiquidation(afl);

            double netSalary;
            double sssContribution = 0;
            double phicContribution = 0;
            double hdmfContribution = 0;
            
            if(payrollPeriod.equals("15th of the month")){ 
                phicContribution = contributionUtil.getPhilhealth(basicSalary);
                hdmfContribution = contributionUtil.getHdmf(basicSalary);
                netSalary = payroll.getGrossPay() - (phicContribution + hdmfContribution + tax);
            }else{              
                sssContribution = contributionUtil.getSss(payroll.getGrossPay(), employeeId, payrollDate.toString());
                netSalary = payroll.getGrossPay() - (sssContribution + tax);
            } 
            
            payroll.setSss(sssContribution);
            payroll.setPhic(phicContribution);
            payroll.setHdmf(hdmfContribution);
            payroll.setNetSalary(netSalary);

            double amountReceivable = new Double(df.format(netSalary + allowance + afl));  
            payroll.setAmountReceivable(amountReceivable);

            double amountToBeReceive = amountReceivable;
            payroll.setAmountToBeReceive(amountToBeReceive);  
            payrollList.add(payroll);

            double forAdjustments = sal.getForAdjustmentFromPreviousPayroll(employeeId);            
            System.out.println("for adjustments: " +forAdjustments);
	    System.out.println("employee ID: " +employeeId);
	    
            result = serviceInsert.insertPayrollAndAttendance(payrollList, 
                    timekeepingList, 
                    editPayroll, 
                    forAdjustments, 
                    previousPayrollId);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        
        return result;
    }    
}
