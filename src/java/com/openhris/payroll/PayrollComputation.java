/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.openhris.serviceprovider.PayrollServiceImpl;
import com.openhris.service.EmployeeService;
import com.openhris.service.PayrollService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class PayrollComputation {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    EmployeeService employeeService = new EmployeeServiceImpl();
    PayrollService payrollService = new PayrollServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    double totalAbsences;
    
    /**
     * 
     * @param wage employment wage
     * @param wageEntry daily/monthly
     * @return
     * basic salary of an employee.
     */
    public double getBasicSalary(double wage, String wageEntry){
        double basic_salary;    
        if(wageEntry.equals("daily")){            
            basic_salary = util.roundOffToTwoDecimalPlaces((wage * 314) / 12);
        }else{
            basic_salary = wage;
        }
        return basic_salary;    
    }
    
    /**
     * 
     * @param wageEntry daily/monthly
     * @param policyList sick-leave, holiday, day-off, etc..
     * @param wage employment wage
     * @param dateList attendance date list
     * @param employeeId company ID
     * @return 
     * half month salary of an employee. 
     * 
     */
    public double getHalfMonthSalary(String wageEntry, List policyList, double wage, List dateList, String employeeId){
        double halfMonthSalary = 0;
                
        if(wageEntry.equals("daily")){        
            for(int i = 0; i < policyList.size(); i++){
                if(policyList.get(i).equals("null") || policyList.get(i).equals("working-day-off") || 
                        policyList.get(i).equals("working-holiday") || policyList.get(i).equals("paid-vacation-leave") || 
                        policyList.get(i).equals("paid-sick-leave") || policyList.get(i).equals("paternity-leave") || 
                        policyList.get(i).equals("maternity-leave") || policyList.get(i).toString().isEmpty()){                    
                    halfMonthSalary = halfMonthSalary + wage;                   
                }
            }    
        }else{
            String dateEmployed = employeeService.getEmploymentEntryDate(employeeId);            
            boolean checkIfResigned = employeeService.getEmployeeCurrentStatus(employeeId) != null;
            int count = 0;
            
            for(int i = 0; i < policyList.size(); i++){
                if(policyList.get(i).equals("day-off") || policyList.get(i).equals("paternity-leave") || policyList.get(i).equals("maternity-leave")){
                    count++;
                }
            }
            
            if(checkIfResigned == false){
                Boolean checkResultNewEmployee = checkEntryDateIfBetweenDateLists(dateList, dateEmployed);
                if(checkResultNewEmployee == true){
                    int numberOfDays = getNumberOfDaysForMonthlyEmployee(dateList, policyList);
                    double salaryPerDay = util.roundOffToTwoDecimalPlaces((wage/314)*12);
                    halfMonthSalary = salaryPerDay * (numberOfDays - count);
                }else{
                    halfMonthSalary = wage / 2;
                }
            }else{  
                String dateResigned = util.convertDateFormat(employeeService.getEmploymentEndDate(employeeId));
                boolean checkResultResignedEmployee = checkEntryDateIfBetweenDateLists(dateList, dateResigned);
                if(checkResultResignedEmployee == true){
                    int numberOfDays = getNumberOfDaysForMonthlyEmployee(dateList, policyList);
                    double salaryPerDay = util.roundOffToTwoDecimalPlaces((wage/314)*12);
                    halfMonthSalary = salaryPerDay * (numberOfDays - count);
                }else{
                    halfMonthSalary = wage / 2;
                }
            }
            
        }
        return util.roundOffToTwoDecimalPlaces(halfMonthSalary);
    }
    
    public double getTaxableSalary(double wage, String wageEntry, List policyList, double halfMonthSalary){        
        double absences = 0;
        double taxableSalary = 0;
        
        if(wageEntry.equals("daily")){            
            for(int i = 0; i < policyList.size(); i++){
                if(policyList.get(i).equals("null") || policyList.get(i).equals("working-day-off") || 
                        policyList.get(i).equals("working-holiday") || policyList.get(i).equals("paid-vacation-leave") || 
                        policyList.get(i).equals("paid-sick-leave") || policyList.get(i).equals("paternity-leave") || 
			policyList.get(i).equals("maternity-leave") || policyList.get(i).toString().isEmpty()){
                    taxableSalary = taxableSalary + wage;
                }
            }             
        }else{
            wage = util.roundOffToTwoDecimalPlaces((wage * 12)/314);
            for(int i = 0; i < policyList.size(); i++){
                if(policyList.get(i).equals("absent") || policyList.get(i).equals("unpaid-vacation-leave") || 
                        policyList.get(i).equals("unpaid-sick-leave") || policyList.get(i).equals("suspended") || 
                        policyList.get(i).equals("paternity-leave") || policyList.get(i).equals("maternity-leave")){
                    absences = absences + wage;
                }
            }
            taxableSalary = halfMonthSalary - absences;
            totalAbsences = absences;
        }
        
        return util.roundOffToTwoDecimalPlaces(taxableSalary);
    }  
    
    private Integer getMonthDifference(Date date1, Date date2){   
        Calendar firstDate = new GregorianCalendar(date2.getYear(), date2.getMonth(), date2.getDay());
        Calendar secondDate = new GregorianCalendar(date1.getYear(), date1.getMonth(), date1.getDay());
        int months  = (firstDate.get(Calendar.YEAR) - secondDate.get(Calendar.YEAR)) * 12 + 
                (firstDate.get(Calendar.MONTH)- secondDate.get(Calendar.MONTH)) + 
                (firstDate.get(Calendar.DAY_OF_MONTH) >= secondDate.get(Calendar.DAY_OF_MONTH)? 0: -1); 
        return months;
    }
    
    public Double getTax(String status, Double salary){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        double tax = 0.0;
        double taxRateExemption = 0;
        double taxRate = 0;
        double taxableRate = 0;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT getExemption('"+status+"', "+salary+") AS exemption ");
            while(rs.next()){
                taxRateExemption = Double.parseDouble(rs.getString("exemption"));
            }
            
            rs = stmt.executeQuery(" SELECT getTaxRate('"+status+"', "+salary+") as taxRate ");
            while(rs.next()){
                taxRate = Double.parseDouble(rs.getString("taxRate"));
            }
            
            rs = stmt.executeQuery(" SELECT getTaxableRate('"+status+"', "+salary+") as taxableRate ");
            while(rs.next()){
                taxableRate = Double.parseDouble(rs.getString("taxableRate"));
            }
            
            tax = ((salary - taxableRate) * taxRate) + taxRateExemption;
        } catch (SQLException ex) {
            Logger.getLogger(com.openhris.payroll.PayrollComputation.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(com.openhris.payroll.PayrollComputation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return util.roundOffToTwoDecimalPlaces(tax);
    }
    
    public double getAllowance(List policyList, String allowanceEntry, Double allowance){
        double halfmonth_allowance = 0;
        if(allowanceEntry.equals("daily")){            
            for(int i = 0; i < policyList.size(); i++){
                if(policyList.get(i).equals("null") || policyList.get(i).equals("working-day-off") || 
                        policyList.get(i).equals("working-holiday") || policyList.get(i).toString().isEmpty()){
                    halfmonth_allowance = halfmonth_allowance + allowance;
                }
            }            
        }else{
            double allowance_entry_per_day = util.roundOffToTwoDecimalPlaces((allowance * 12)/314);
            Double halfAllowance = allowance / 2;
            for(int i = 0; i < policyList.size(); i++){
                if(policyList.get(i).equals("absent") || policyList.get(i).equals("paternity-leave") || 
                    policyList.get(i).equals("service-incentive-leave") || policyList.get(i).equals("holiday") || 
                    policyList.get(i).equals("paid-vacation-leave") || policyList.get(i).equals("paid-sick-leave") || 
                    policyList.get(i).equals("unpaid-vacation-leave") || policyList.get(i).equals("unpaid-sick-leave") || 
                    policyList.get(i).equals("suspended") || policyList.get(i).equals("resigned") || 
                    policyList.get(i).equals("maternity-leave") || policyList.get(i).equals("end-of-contract")){
                    halfAllowance =  halfAllowance - allowance_entry_per_day;
                }
            }
            halfmonth_allowance = halfAllowance;
        }
        return util.roundOffToTwoDecimalPlaces(halfmonth_allowance);
    }
    
    public double getAllowanceForLiquidationDeduction(List dateList, List policyList, Double allowanceForLiquidation){
        int numberOfDays = dateList.size();
        double allowance_for_liquidation = 0;
        double allowanceToBeDeductedPerDay = 0;
        double allowance_entry_per_day = util.roundOffToTwoDecimalPlaces((allowanceForLiquidation * 12)/314);
        for(int i = 0; i < policyList.size(); i++){
            if(policyList.get(i).equals("absent") || policyList.get(i).equals("unpaid-vacation-leave") || 
                    policyList.get(i).equals("unpaid-sick-leave") || policyList.get(i).equals("suspended") || 
                    policyList.get(i).equals("paternity-leave") || policyList.get(i).equals("maternity-leave")){
                allowanceToBeDeductedPerDay =  allowanceToBeDeductedPerDay + allowance_entry_per_day;
            }
        }
        allowance_for_liquidation = (allowanceForLiquidation/2) - allowanceToBeDeductedPerDay;
        return util.roundOffToTwoDecimalPlaces(allowance_for_liquidation);
    }
    
    public int getNumberOfDays(List dateList, List policyList){
        int numberOfDays = dateList.size();
        int count = 0;
        for(int i = 0; i < policyList.size(); i++){
            if(policyList.get(i).equals("absent") || policyList.get(i).equals("day-off") || policyList.get(i).equals("paternity-leave") || 
                    policyList.get(i).equals("service-incentive-leave") || policyList.get(i).equals("holiday") || 
                    policyList.get(i).equals("paid-vacation-leave") || policyList.get(i).equals("paid-sick-leave") || 
                    policyList.get(i).equals("unpaid-vacation-leave") || policyList.get(i).equals("unpaid-sick-leave") || 
                    policyList.get(i).equals("suspended") || policyList.get(i).equals("resigned") || 
                    policyList.get(i).equals("maternity-leave") || policyList.get(i).equals("end-of-contract")){
                count++;
            }
        }
        numberOfDays = numberOfDays - count;
        return numberOfDays;
    }
    
    private int getNumberOfDaysForMonthlyEmployee(List dateList, List policyList){
        int numberOfDays = dateList.size();
        int count = 0;
        for(int i = 0; i < policyList.size(); i++){
            if(policyList.get(i).equals("absent") || policyList.get(i).equals("paternity-leave") || 
                    policyList.get(i).equals("service-incentive-leave") || policyList.get(i).equals("paid-vacation-leave") || 
                    policyList.get(i).equals("paid-sick-leave") || policyList.get(i).equals("unpaid-vacation-leave") || 
                    policyList.get(i).equals("unpaid-sick-leave")|| policyList.get(i).equals("suspended") ||  
                    policyList.get(i).equals("resigned") || policyList.get(i).equals("end-of-contract") || 
                    policyList.get(i).equals("maternity-leave")){
                count++;
            }
        }
        numberOfDays = numberOfDays - count;
        return numberOfDays;
    }
    
    private boolean checkEntryDateIfBetweenDateLists(List dateList, String dateEmployed){
        boolean checkResult = false;
        for(int i = 0; i < dateList.size(); i++){
            if(dateList.get(i).toString().trim().equals(dateEmployed.trim())){
                checkResult = true;
            }
        }
        return checkResult;
    }
    
    public double getTotalAbsences(){
        return util.roundOffToTwoDecimalPlaces(totalAbsences);
    }
    
    public double getAdjustmentFromPreviousPayroll(String employeeId){
        return payrollService.getAdjustmentFromPreviousPayroll(employeeId);
    }
}
