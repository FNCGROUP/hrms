/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.employee.serviceprovider.EmployeeServiceImpl;
import com.openhris.payroll.serviceprovider.PayrollServiceImpl;
import com.openhris.employee.service.EmployeeService;
import com.openhris.payroll.service.PayrollService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
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
    
    public double getBasicSalary(double salary, String wageEntry){
        double basic_salary;    
        if(wageEntry.equals("daily")){            
//            basic_salary = Math.round((((salary * 314) / 12)*100.0)/100.0);
            basic_salary = util.roundOffToTwoDecimalPlaces((salary * 314) / 12);
        }else{
            basic_salary = salary;
        }
        return basic_salary;    
    }
    
    public double getHalfMonthSalary(String wageEntry, List policyList, double wage, List dateList, String employeeId){
        double halfMonthSalary = 0;
        
        String holiday = null;        
        
        if(wageEntry.equals("daily")){            
            for(int i = 0; i < policyList.size(); i++){
                if(policyList.get(i).equals("null") || policyList.get(i).equals("working-day-off") || 
                        policyList.get(i).equals("working-holiday") || policyList.get(i).equals("paid-vacation-leave") || 
                        policyList.get(i).equals("paid-sick-leave") || policyList.get(i).toString().isEmpty()){                    
                    halfMonthSalary = halfMonthSalary + wage;                   
                }
            }            
        }else{
            String dateEmployed = employeeService.getEmploymentEntryDate(employeeId);            
            boolean checkIfResigned = employeeService.getEmployeeCurrentStatus(employeeId) != null;
            
            if(checkIfResigned == false){
                Boolean checkResultNewEmployee = checkEntryDateIfBetweenDateLists(dateList, dateEmployed);
                if(checkResultNewEmployee == true){
                    int numberOfDays = getNumberOfDaysForMonthlyEmployee(dateList, policyList);
//                    double salaryPerDay = new Double(df.format((wage/314) * 12);
                    double salaryPerDay = util.roundOffToTwoDecimalPlaces((wage/314)*12);
                    halfMonthSalary = salaryPerDay * numberOfDays;
                }else{
                    halfMonthSalary = wage / 2;
                }
            }else{  
                String dateResigned = util.convertDateFormat(employeeService.getEmploymentEndDate(employeeId));
                boolean checkResultResignedEmployee = checkEntryDateIfBetweenDateLists(dateList, dateResigned);
                if(checkResultResignedEmployee == true){
                    int numberOfDays = getNumberOfDaysForMonthlyEmployee(dateList, policyList);
//                    double salaryPerDay = new Double(df.format((wage/314) * 12));
                    double salaryPerDay = util.roundOffToTwoDecimalPlaces((wage/314)*12);
                    halfMonthSalary = salaryPerDay * numberOfDays;
                }else{
                    halfMonthSalary = wage / 2;
                }
            }
            
        }
//        return new Double(df.format(halfMonthSalary));
        return util.roundOffToTwoDecimalPlaces(halfMonthSalary);
    }
    
    public double getTaxableSalary(double wage, String wageEntry, List policyList, double halfMonthSalary){        
        double absences = 0;
//        double totalAbsences = 0.0;
        double taxableSalary = 0;
        
        if(wageEntry.equals("daily")){            
            for(int i = 0; i < policyList.size(); i++){
                if(policyList.get(i).equals("null") || policyList.get(i).equals("working-day-off") || 
                        policyList.get(i).equals("working-holiday") || policyList.get(i).equals("paid-vacation-leave") || 
                        policyList.get(i).equals("paid-sick-leave") || policyList.get(i).equals("paternity-leave") || 
			policyList.get(i).toString().isEmpty()){
                    taxableSalary = taxableSalary + wage;
                }
            }             
        }else{
//            wage = new Double(df.format((wage * 12)/314));
            wage = util.roundOffToTwoDecimalPlaces((wage * 12)/314);
            for(int i = 0; i < policyList.size(); i++){
                if(policyList.get(i).equals("absent") || policyList.get(i).equals("unpaid-vacation-leave") || 
                        policyList.get(i).equals("unpaid-sick-leave") || policyList.get(i).equals("suspended")){
                    absences = absences + wage;
                }
            }
            taxableSalary = halfMonthSalary - absences;
            totalAbsences = absences;
        }
        
//        return new Double(df.format(taxableSalary));
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
//        return new Double(df.format(tax));
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
//            double allowance_entry_per_day = new Double(df.format((allowance * 12)/314));
            double allowance_entry_per_day = util.roundOffToTwoDecimalPlaces((allowance * 12)/314);
            Double halfAllowance = allowance / 2;
            for(int i = 0; i < policyList.size(); i++){
                if(policyList.get(i).equals("absent") || policyList.get(i).equals("paternity-leave") || 
                    policyList.get(i).equals("service-incentive-leave") || policyList.get(i).equals("holiday") || 
                    policyList.get(i).equals("paid-vacation-leave") || policyList.get(i).equals("paid-sick-leave") || 
                    policyList.get(i).equals("unpaid-vacation-leave") || policyList.get(i).equals("unpaid-sick-leave") || 
                        policyList.get(i).equals("suspended")){
                    halfAllowance =  halfAllowance - allowance_entry_per_day;
                }
            }
            halfmonth_allowance = halfAllowance;
        }
//        return new Double(df.format(halfmonth_allowance));
        return util.roundOffToTwoDecimalPlaces(halfmonth_allowance);
    }
    
    public double getAllowanceForLiquidationDeduction(List policyList, Double allowanceForLiquidation){
        double allowanceToBeDeductedPerDay = 0;
//        double allowance_entry_per_day = new Double(df.format((allowanceForLiquidation * 12)/314));
        double allowance_entry_per_day = util.roundOffToTwoDecimalPlaces((allowanceForLiquidation * 12)/314);
        for(int i = 0; i < policyList.size(); i++){
            if(policyList.get(i).equals("absent") || policyList.get(i).equals("unpaid-vacation-leave") || 
                        policyList.get(i).equals("unpaid-sick-leave") || policyList.get(i).equals("suspended")){
                allowanceToBeDeductedPerDay =  allowanceToBeDeductedPerDay + allowance_entry_per_day;
            }
        }
        
//        return new Double(df.format(allowanceToBeDeductedPerDay));
        return util.roundOffToTwoDecimalPlaces(allowanceToBeDeductedPerDay);
    }
    
    public Integer getNumberOfDays(List dateList, List policyList){
        int numberOfDays = dateList.size();
        int count = 0;
        for(int i = 0; i < policyList.size(); i++){
            if(policyList.get(i).equals("absent") || policyList.get(i).equals("day-off") || policyList.get(i).equals("paternity-leave") || 
                    policyList.get(i).equals("service-incentive-leave") || policyList.get(i).equals("holiday") || 
                    policyList.get(i).equals("paid-vacation-leave") || policyList.get(i).equals("paid-sick-leave") || 
                    policyList.get(i).equals("unpaid-vacation-leave") || policyList.get(i).equals("unpaid-sick-leave")){
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
                    policyList.get(i).equals("unpaid-sick-leave")|| policyList.get(i).equals("suspended")){
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
//        return new Double(df.format(totalAbsences));
        return util.roundOffToTwoDecimalPlaces(totalAbsences);
    }
    
    public double getAdjustmentFromPreviousPayroll(String employeeId){
        return payrollService.getAdjustmentFromPreviousPayroll(employeeId);
    }
}
