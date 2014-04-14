/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.classes;

import java.text.DecimalFormat;

/**
 *
 * @author jet
 */
public class AttendanceProcess {
    
    private Double deduction;  
    private Double addition;
    private String policy;
    private String holiday;
    DecimalFormat df = new DecimalFormat("0.00");    
    
    public double processEmployeesLates(String policy, String holiday, Double lates, Double wage){
        double rate = getHolidayRate(holiday, wage);
        double lateDeduction = calculateDeductionPerMinute(lates, rate);
        if(policy == null || policy.equals("working-holiday")){
            deduction = new Double(df.format(lateDeduction));
        }else if(policy.equals("working-day-off")){
            double wdo = lateDeduction * .3;
            deduction = new Double(df.format(lateDeduction + wdo));
        }
        
        return deduction;
    }
    
    public double processEmployeesUndertime(String policy, String holiday, Double lates, Double wage){
        double rate = getHolidayRate(holiday, wage);
        double lateDeduction = calculateDeductionPerMinute(lates, rate);
        if(policy == null || policy.equals("working-holiday")){
            deduction = new Double(df.format(lateDeduction));
        }else if(policy.equals("working-day-off")){
            double wdo = lateDeduction * .3;
            deduction = new Double(df.format(lateDeduction + wdo));
        }
        
        return deduction;
    }
    
    public double processEmployeesOvertime(String policy, String holiday, Double overtime, Double wage){
        double rate = getHolidayRate(holiday, wage);
        double overtimeAddition = calculateOvertimePerMinute(overtime, rate);
        if(policy == null || policy.equals("working-holiday")){
            addition = new Double(df.format(overtimeAddition));
        }else if(policy.equals("working-day-off")){
            double wdo = overtimeAddition * .3;
            addition = new Double(df.format(overtimeAddition + wdo));
        }
        return addition;
    }
    
    public double processEmployeesNightDifferential(String policy, String holiday, Double nightDifferential, Double wage){
        double rate = getHolidayRate(holiday, wage);
        double premiumAddition = calculateNightDifferentialPerMinute(nightDifferential, rate) * .1;
        if(policy == null || policy.equals("working-holiday")){
            addition = new Double(df.format(premiumAddition));
        }else if(policy.equals("working-day-off")){
            double wdo = premiumAddition * .3;
            addition = new Double(df.format(premiumAddition + wdo));
        }
        return addition;
    }
    
    public double processAdditionalHolidayRate(String holiday, Double wage){
        Double rate;
        if(holiday.equals("legal-holiday")){
            rate = wage * 1;
        }else{
            rate = wage * .3;
        }
        return rate;
    }
    
    public double processAdditionalWorkingDayOff(Double wage, String employmentWageStatus){
        Double rate;
        if(employmentWageStatus.equals("daily")){
            rate = wage * .3;
        }else{
            rate = wage * 1.3;
        }
        
        return rate;
    }
    
    private double getHolidayRate(String holiday, Double wage){
        Double rate = 0.0;
        if(holiday == null){
            rate = wage + (wage * rate);
        }else if(holiday.equals("legal-holiday")){
            rate = wage + (wage * 1);
        }else{
            rate = wage + (wage * .3);
        }
        return rate;
    }
    
    private double calculateDeductionPerMinute(Double lates, Double rate){
        Double lateDeductionPerMinute;
        Double ratePerMinute = rate / 480;
        lateDeductionPerMinute = lates * ratePerMinute;
        return lateDeductionPerMinute;
    }
    
    private double calculateOvertimePerMinute(Double overtime, Double rate){
        Double overtimeAdditionPerMinute;
        Double ratePerMinute = rate / 480;
        overtimeAdditionPerMinute = ((overtime * ratePerMinute) + ((overtime * ratePerMinute) * .25));
        return overtimeAdditionPerMinute;
    }
    
    private double calculateNightDifferentialPerMinute(Double premium, Double rate){
        Double overtimePremiumPerMinute;
        Double ratePerMinute = rate / 480;
        overtimePremiumPerMinute = premium * ratePerMinute;
        return overtimePremiumPerMinute;
    }
}
