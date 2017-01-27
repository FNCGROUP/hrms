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
    
    EmployeeService es = new EmployeeServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    PayrollComputation pComputation = new PayrollComputation();
    PayrollAllowances pa = new PayrollAllowances();
    ContributionUtilities contributionUtil = new ContributionUtilities();
    ServiceInsertDAO serviceInsert = new ServiceInsertDAO();
    PayrollService pService = new PayrollServiceImpl();  
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
    
    List<Payroll> pList = new ArrayList<>();
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
        employmentWage = es.getEmploymentWage(getEmployeeId());
        employmentWageStatus = es.getEmploymentWageStatus(getEmployeeId());
        employmentWageEntry = es.getEmploymentWageEntry(getEmployeeId());
        allowanceForLiquidation = es.getEmploymentAllowanceForLiquidation(getEmployeeId());
        totalDependent = es.getEmployeeTotalDependent(getEmployeeId());
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
    
    public boolean processPayrollComputation(String pDate, 
            String pPeriod, 
            String attendancePeriodFrom, 
            String attendancePeriodTo, 
            int previousPayrollId){
        boolean result = false;
        try{
            Payroll p = new Payroll();
            p.setEmployeeId(getEmployeeId());
            p.setBranchId(getBranchId());
            p.setPayrollDate(util.parsingDate(pDate));
            p.setPayrollPeriod(pPeriod);
            p.setAttendancePeriodFrom(util.parsingDate(attendancePeriodFrom));
            p.setAttendancePeriodTo(util.parsingDate(attendancePeriodTo));
            p.setTotalLatesDeduction(totalLatesDeduction);
            p.setTotalUndertimeDeduction(totalUndertimeDeduction);
            p.setTotalOvertimePaid(totalOvertimePay);
            p.setTotalNightDifferentialPaid(totalNightDifferetinalPay);
            p.setTotalDutyManagerPaid(totalDutyManagerPay);
            p.setTotalLegalHolidayPaid(totalLegalHolidayPaid);
            p.setTotalSpecialHolidayPaid(totalSpecialHolidayPaid);
            p.setTotalWorkingDayOffPaid(totalWorkingDayOffPaid);
            p.setTotalLatesHolidayDeduction(totalLatesWHLHDeduction + totalLatesWHSHDeduction + totalLatesWDODeduction);
            p.setTotalUndertimeHolidayDeduction(totalUndertimeWHLHDeduction + totalUndertimeWHSHDeduction + totalUndertimeWDODeduction);
            p.setTotalNonWorkingHolidayPaid(getTotalNonWorkingHolidayPay());
            p.setRate(employmentWage);
            p.setWageEntry(employmentWageEntry.charAt(0));
            p.setTag(es.findEmployeeRemittanceTag(getEmployeeId()));
            
            double basicSalary = pComputation.getBasicSalary(employmentWage, employmentWageEntry);
            p.setBasicSalary(basicSalary);
            
            double halfMonthSalary = pComputation.getHalfMonthSalary(employmentWageEntry, policyList, employmentWage, dateList, getEmployeeId()) + getTotalNonWorkingHolidayPay();
            p.setHalfMonthSalary(halfMonthSalary);
                      
            double taxableSalary = 0;
            if(employmentWageEntry.equals("daily")){
                taxableSalary = p.getGrossPay() - (p.getTotalOvertimePaid() + p.getTotalSpecialHolidayPaid() + p.getTotalLegalHolidayPaid());
            } else {
                double newTaxableSalary = p.getGrossPay() - (p.getTotalOvertimePaid() + p.getTotalSpecialHolidayPaid() + p.getTotalLegalHolidayPaid());
                taxableSalary = pComputation.getTaxableSalary(employmentWage, employmentWageEntry, policyList, newTaxableSalary);
            }
            
            branch = cs.getBranchById(getBranchId()).replaceAll("\\(.*?\\)", "");
            if(branch.trim().equals("on-call and trainees")){
                taxableSalary = p.getGrossPay();
            }
            
            double sssContribution = 0;
            double phicContribution = 0;
            double hdmfContribution = 0;
                              
            if(branch.trim().equals("on-call and trainees")){
                p.setSss(0);
                p.setPhic(0);
                p.setHdmf(0);
                p.setTaxableSalary(taxableSalary);
            } else {
                if(employmentWageStatus.equals("senior citizen")){
                    p.setSss(0);
                    p.setPhic(0);
                    p.setHdmf(0);
                    p.setTaxableSalary(taxableSalary);
                } else {
                    if(pPeriod.equals("15th of the month")){ 
                        phicContribution = contributionUtil.getPhilhealth(basicSalary);
                        hdmfContribution = contributionUtil.getHdmf(basicSalary);
                        taxableSalary = taxableSalary - (phicContribution + hdmfContribution);
                    }else{              
                        sssContribution = contributionUtil.getSss(p.getGrossPay(), employeeId, pDate);
                        taxableSalary = taxableSalary - (sssContribution);
                    }

                    p.setSss(sssContribution);
                    p.setPhic(phicContribution);
                    p.setHdmf(hdmfContribution);
                    p.setTaxableSalary(taxableSalary);
                }
            }
                        
            p.setAbsences(pComputation.getTotalAbsences());            
            
            double tax = pComputation.getTax(totalDependent, taxableSalary); //get tax          
            if(employmentWageStatus.equals("minimum") || tax < 0 || employmentWageStatus.equals("senior citizen")){
                tax = 0;
            }        
            
            if(branch.trim().equals("on-call and trainees")){
                tax = 0;
            }
            
            p.setTax(tax);

            double cashBond = 0;   
            p.setCashBond(cashBond);

            double communication = pa.getCommunicationAllowance(policyList, getEmployeeId());
            double perDiem = pa.getPerDiemAllowance(policyList, getEmployeeId());
            double cola = pa.getColaAllowance(policyList, getEmployeeId());
            double meal = pa.getMealAllowance(policyList, getEmployeeId());
            double transportation = pa.getTransportationAllowance(policyList, getEmployeeId());
            double others = pa.getOtherAllowance(policyList, getEmployeeId());
            
            p.setCommunicationAllowance(communication);            
            p.setPerDiemAllowance(perDiem);           
            p.setColaAllowance(cola);         
            p.setMealAllowance(meal);         
            p.setTransportationAllowance(transportation);
            p.setOtherAllowances(others);            

            double allowances = communication + perDiem + cola + meal + transportation + others;
                        
            int numberOfDays = pComputation.getNumberOfDays(dateList, policyList);
            p.setNumOfDays(numberOfDays);

            double afl = pComputation.getAllowanceForLiquidationDeduction(dateList, policyList, allowanceForLiquidation);
            p.setAllowanceForLiquidation(afl);
                          
            double netSalary;
            if(branch.trim().equals("on-call and trainees")){
                netSalary = taxableSalary;
            } else {
                if(employmentWageStatus.equals("senior citizen")){
                    netSalary = taxableSalary + p.getTotalOvertimePaid() + p.getTotalSpecialHolidayPaid() + p.getTotalLegalHolidayPaid();
                } else {
                    netSalary = taxableSalary - tax + p.getTotalOvertimePaid() + p.getTotalSpecialHolidayPaid() + p.getTotalLegalHolidayPaid();
                }
            }
                        
            p.setNetSalary(netSalary);

            double amountReceivable = new Double(df.format(netSalary + allowances + afl));  
            p.setAmountReceivable(amountReceivable);

            double amountToBeReceive = amountReceivable;
            p.setAmountToBeReceive(amountToBeReceive); 

            double adjustment = pComputation.getAdjustmentFromPreviousPayroll(employeeId); 
	                
            result = pService.insertPayrollAndAttendance(p, 
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
