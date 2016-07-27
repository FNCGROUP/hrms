/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.hrms.utilities.ContributionUtilities;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.dao.ServiceInsertDAO;
import com.openhris.service.EmployeeService;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.openhris.model.Payroll;
import com.openhris.service.PayrollService;
import com.openhris.serviceprovider.PayrollServiceImpl;
import com.openhris.model.Timekeeping;
import com.openhris.service.CompanyService;
import com.openhris.serviceprovider.CompanyServiceImpl;
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
    PayrollComputation payrollComputation = new PayrollComputation();
    PayrollAllowances pa = new PayrollAllowances();
    ContributionUtilities contributionUtil = new ContributionUtilities();
    ServiceInsertDAO serviceInsert = new ServiceInsertDAO();
    PayrollService payrollService = new PayrollServiceImpl();  
    CompanyService cs = new CompanyServiceImpl();
    DecimalFormat df = new DecimalFormat("0.00");
        
    String employeeId;
    int branchId;
    
    double employmentWage;
    String employmentWageStatus;
    String employmentWageEntry;
//    double allowance;
//    String allowanceEntry;
    double allowanceForLiquidation;
    String totalDependent;
    
    List<Payroll> payrollList = new ArrayList<>();
    List<Timekeeping> timekeepingList = new ArrayList<>();
    List<String> dateList = new ArrayList<>();
    List<String> policyList = new ArrayList<>();
    List<String> holidayList = new ArrayList<>();
    
    double totalLates;
    double totalUndertime;
    double totalOvertime;
    double totalNightDifferential;
    double totalDutyManager;
    
    double totalLatesDeduction;
    double totalUndertimeDeduction;
    double totalOvertimePay;
    double totalNightDifferetinalPay;
    double totalDutyManagerPay;
    double totalLegalHolidayPaid;
    double totalSpecialHolidayPaid;
    double totalWorkingDayOffPaid;
    double totalNonWorkingHolidayPaid;
    double totalLatesWHLHDeduction;
    double totalLatesWHSHDeduction;
    double totalLatesWDODeduction;
    double totalUndertimeWHLHDeduction;
    double totalUndertimeWHSHDeduction;
    double totalUndertimeWDODeduction;
    double totalAbsences;
        
    private String branch;
    
    public ProcessPayrollComputation(String employeeId, int branchId){
        this.employeeId = employeeId;
        this.branchId = branchId;
    }
    
    public void initVariables(){
        employmentWage = employeeService.getEmploymentWage(getEmployeeId());
        employmentWageStatus = employeeService.getEmploymentWageStatus(getEmployeeId());
        employmentWageEntry = employeeService.getEmploymentWageEntry(getEmployeeId());
//        allowance = employeeService.getEmploymentAllowance(getEmployeeId());
//        allowanceEntry = employeeService.getEmploymentAllowanceEntry(getEmployeeId());
        allowanceForLiquidation = employeeService.getEmploymentAllowanceForLiquidation(getEmployeeId());
        totalDependent = employeeService.getEmployeeTotalDependent(getEmployeeId());
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
            totalDutyManager = totalDutyManager + t.getDutyManager();
            
            totalLatesDeduction = totalLatesDeduction + t.getLateDeduction();
            totalUndertimeDeduction = totalUndertimeDeduction + t.getUndertimeDeduction();
            totalOvertimePay = totalOvertimePay + t.getOvertimePaid();
            totalNightDifferetinalPay = totalNightDifferetinalPay + t.getNightDifferentialPaid();
            totalDutyManagerPay = totalDutyManagerPay + t.getDutyManagerPaid();
            
            totalLegalHolidayPaid = totalLegalHolidayPaid + t.getLegalHolidayPaid();
            totalSpecialHolidayPaid = totalSpecialHolidayPaid + t.getSpecialHolidayPaid();
            totalWorkingDayOffPaid = totalWorkingDayOffPaid + t.getWorkingDayOffPaid();
            totalNonWorkingHolidayPaid = totalNonWorkingHolidayPaid + t.getNonWorkingHolidayPaid();
            totalLatesWHLHDeduction = totalLatesWHLHDeduction + t.getLatesLegalHolidayDeduction();
            totalLatesWHSHDeduction = totalLatesWHSHDeduction + t.getLatesSpecialHolidayDeduction();
            totalLatesWDODeduction = totalLatesWDODeduction + t.getLatesWorkingDayOffDeduction();
            totalUndertimeWHLHDeduction = totalUndertimeWHLHDeduction + t.getUndertimeLegalHolidayDeduction();
            totalUndertimeWHSHDeduction = totalUndertimeWHSHDeduction + t.getUndertimeSpecialHolidayDeduction();
            totalUndertimeWDODeduction = totalUndertimeWDODeduction + t.getUndertimeWorkingDayOffDeduction();
        }
    }
    
    public boolean processPayrollComputation(String payrollDate, 
            String payrollPeriod, 
            String attendancePeriodFrom, 
            String attendancePeriodTo, 
            int previousPayrollId){
        boolean result = false;
        try{
            Payroll payroll = new Payroll();
            payroll.setEmployeeId(getEmployeeId());
            payroll.setBranchId(getBranchId());
            payroll.setPayrollDate(util.parsingDate(payrollDate));
            payroll.setPayrollPeriod(payrollPeriod);
            payroll.setAttendancePeriodFrom(util.parsingDate(attendancePeriodFrom));
            payroll.setAttendancePeriodTo(util.parsingDate(attendancePeriodTo));
            payroll.setTotalLatesDeduction(totalLatesDeduction);
            payroll.setTotalUndertimeDeduction(totalUndertimeDeduction);
            payroll.setTotalOvertimePaid(totalOvertimePay);
            payroll.setTotalNightDifferentialPaid(totalNightDifferetinalPay);
            payroll.setTotalDutyManagerPaid(totalDutyManagerPay);
            payroll.setTotalLegalHolidayPaid(totalLegalHolidayPaid);
            payroll.setTotalSpecialHolidayPaid(totalSpecialHolidayPaid);
            payroll.setTotalWorkingDayOffPaid(totalWorkingDayOffPaid);
            payroll.setTotalLatesHolidayDeduction(totalLatesWHLHDeduction + totalLatesWHSHDeduction + totalLatesWDODeduction);
            payroll.setTotalUndertimeHolidayDeduction(totalUndertimeWHLHDeduction + totalUndertimeWHSHDeduction + totalUndertimeWDODeduction);
            payroll.setTotalNonWorkingHolidayPaid(getTotalNonWorkingHolidayPay());
            
            double basicSalary = payrollComputation.getBasicSalary(employmentWage, employmentWageEntry);
            payroll.setBasicSalary(basicSalary);
            
            double halfMonthSalary = payrollComputation.getHalfMonthSalary(employmentWageEntry, policyList, employmentWage, dateList, getEmployeeId()) + getTotalNonWorkingHolidayPay();
            payroll.setHalfMonthSalary(halfMonthSalary);
                      
            double taxableSalary = 0;
            if(employmentWageEntry.equals("daily")){
                taxableSalary = payroll.getGrossPay() - (payroll.getTotalOvertimePaid() + payroll.getTotalSpecialHolidayPaid() + payroll.getTotalLegalHolidayPaid());
            } else {
                double newTaxableSalary = payroll.getGrossPay() - (payroll.getTotalOvertimePaid() + payroll.getTotalSpecialHolidayPaid() + payroll.getTotalLegalHolidayPaid());
                taxableSalary = payrollComputation.getTaxableSalary(employmentWage, employmentWageEntry, policyList, newTaxableSalary);
            }
            
            branch = cs.getBranchById(getBranchId()).replaceAll("\\(.*?\\)", "");
            if(branch.trim().equals("on-call and trainees")){
                taxableSalary = payroll.getGrossPay();
            }
            
            double sssContribution = 0;
            double phicContribution = 0;
            double hdmfContribution = 0;
                              
            if(branch.trim().equals("on-call and trainees")){
                payroll.setSss(0);
                payroll.setPhic(0);
                payroll.setHdmf(0);
                payroll.setTaxableSalary(taxableSalary);
            } else {
                if(employmentWageStatus.equals("senior citizen")){
                    payroll.setSss(0);
                    payroll.setPhic(0);
                    payroll.setHdmf(0);
                    payroll.setTaxableSalary(taxableSalary);
                } else {
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
                }
            }
                        
            payroll.setAbsences(payrollComputation.getTotalAbsences());            
            
            double tax = payrollComputation.getTax(totalDependent, taxableSalary); //get tax          
            if(employmentWageStatus.equals("minimum") || tax < 0 || employmentWageStatus.equals("senior citizen")){
                tax = 0;
            }        
            
            if(branch.trim().equals("on-call and trainees")){
                tax = 0;
            }
            
            payroll.setTax(tax);

            double cashBond = 0;   
            payroll.setCashBond(cashBond);

            double communication = pa.getCommunicationAllowance(policyList, getEmployeeId());
            double perDiem = pa.getPerDiemAllowance(policyList, getEmployeeId());
            double cola = pa.getColaAllowance(policyList, getEmployeeId());
            double meal = pa.getMealAllowance(policyList, getEmployeeId());
            double transportation = pa.getTransportationAllowance(policyList, getEmployeeId());
            double others = pa.getOtherAllowance(policyList, getEmployeeId());
            
            payroll.setCommunicationAllowance(communication);            
            payroll.setPerDiemAllowance(perDiem);           
            payroll.setColaAllowance(cola);         
            payroll.setMealAllowance(meal);         
            payroll.setTransportationAllowance(transportation);
            payroll.setOtherAllowances(others);            

            double allowances = communication + perDiem + cola + meal + transportation + others;
                        
            int numberOfDays = payrollComputation.getNumberOfDays(dateList, policyList);
            payroll.setNumOfDays(numberOfDays);

            double afl = payrollComputation.getAllowanceForLiquidationDeduction(dateList, policyList, allowanceForLiquidation);
            payroll.setAllowanceForLiquidation(afl);
                          
            double netSalary;
            if(branch.trim().equals("on-call and trainees")){
                netSalary = taxableSalary;
                System.out.println("sss"+payroll.getSss());
            } else {
                if(employmentWageStatus.equals("senior citizen")){
                    netSalary = taxableSalary + payroll.getTotalOvertimePaid() + payroll.getTotalSpecialHolidayPaid() + payroll.getTotalLegalHolidayPaid();
                } else {
                    netSalary = taxableSalary - tax + payroll.getTotalOvertimePaid() + payroll.getTotalSpecialHolidayPaid() + payroll.getTotalLegalHolidayPaid();
                }
            }
            
            
            payroll.setNetSalary(netSalary);

            double amountReceivable = new Double(df.format(netSalary + allowances + afl));  
            payroll.setAmountReceivable(amountReceivable);

            double amountToBeReceive = amountReceivable;
            payroll.setAmountToBeReceive(amountToBeReceive); 

            double adjustment = payrollComputation.getAdjustmentFromPreviousPayroll(employeeId); 
	                
            result = payrollService.insertPayrollAndAttendance(payroll, 
                    timekeepingList, 
                    adjustment, 
                    previousPayrollId);
        } catch (NumberFormatException e) {
            e.printStackTrace(System.out);
        }
        
        return result;
    }    
    
    double getTotalNonWorkingHolidayPay(){
        return totalNonWorkingHolidayPaid;
    }
    
    String getEmployeeId(){
        return employeeId;
    }
    
    int getBranchId(){
        return branchId;
    }
}
