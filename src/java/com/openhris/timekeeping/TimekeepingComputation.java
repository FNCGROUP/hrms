/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.timekeeping;

import com.openhris.commons.OpenHrisUtilities;

/**
 *
 * @author jet
 */
public class TimekeepingComputation {
    
//    private Double deduction;  
//    private Double addition;
    private String policy;
    private String holiday;
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public double processEmployeesLates(String policy, String holiday, int lates, Double wage){
        double rate = getHolidayRate(holiday, wage);
        double lateDeductionPerMinute = calculateDeductionPerMinute(lates, rate);
        double lateDeduction = 0.0;
        if(policy == null || policy.equals("working-holiday") || policy.isEmpty()){
            lateDeduction = lateDeductionPerMinute;
        }else if(policy.equals("working-day-off")){
            double wdo = lateDeductionPerMinute * .3;
            lateDeduction = lateDeductionPerMinute + wdo;
        }
        
//        return new Double(df.format(lateDeduction));
        return util.roundOffToTwoDecimalPlaces(lateDeduction);
    }
    
    public double processEmployeesUndertime(String policy, String holiday, int undertime, Double wage){
        double rate = getHolidayRate(holiday, wage);
        double undertimeDeductionPerMinute = calculateDeductionPerMinute(undertime, rate);
        double undertimeDeduction = 0.0;
        if(policy == null || policy.equals("working-holiday") || policy.isEmpty()){
            undertimeDeduction = undertimeDeductionPerMinute;
        }else if(policy.equals("working-day-off")){
            double wdo = undertimeDeductionPerMinute * .3;
            undertimeDeduction = undertimeDeductionPerMinute + wdo;
        }
        
//        return new Double(df.format(undertimeDeduction));
        return util.roundOffToTwoDecimalPlaces(undertimeDeduction);
    }
    
    public double processEmployeesOvertime(String policy, String holiday, int overtime, Double wage){
        double rate = getHolidayRate(holiday, wage);
        double overtimeAdditionPerMinute = calculateOvertimePerMinute(overtime, rate);
        double overtimeAddition = 0.0;
        if(policy == null || policy.equals("working-holiday") || policy.isEmpty()){
            overtimeAddition = overtimeAdditionPerMinute;
        }else if(policy.equals("working-day-off")){
            double wdo = wage * 1.3;                                                //Compute Working Day-off Pay
            double wdoPerMinute = wdo/480;                                          //Compute Working Day-off Per Minute
            double wdoOvertimePerMinute = wdoPerMinute * 1.25;                      //Additional 25% overtime for wdo per minute 
            overtimeAddition = overtime * wdoOvertimePerMinute;                     //overtime per minute multiplied by wdo overtime per minute
        }
        
//        return new Double(df.format(overtimeAddition));
        return util.roundOffToTwoDecimalPlaces(overtimeAddition);
    }
    
    public double processEmployeesNightDifferential(String policy, String holiday, int nightDifferential, Double wage){
        double rate = getHolidayRate(holiday, wage);
        double premiumAdditionPerMinute = calculateNightDifferentialPerMinute(nightDifferential, rate) * .1;
        double premiumAddition = 0.0;
        if(policy == null || policy.equals("working-holiday") || policy.isEmpty()){
            premiumAddition = premiumAdditionPerMinute;
        }else if(policy.equals("working-day-off")){
            double wdo = premiumAdditionPerMinute * .3;
            premiumAddition = premiumAdditionPerMinute + wdo;
        }
        
//        return new Double(df.format(premiumAddition));
        return util.roundOffToTwoDecimalPlaces(premiumAddition);
    }
    
    public double processEmployeeDutyManager(String policy, 
            String holiday, 
            int dutyManager, Double wage){
        double rate = getHolidayRate(holiday, wage);
        double dutyManagerPayPerMinute = calculateDutyManagerPerMinute(dutyManager, rate);
        double totalDutyManagerPay = 0.0;
        
        if(policy == null || policy.isEmpty()){
            totalDutyManagerPay = dutyManagerPayPerMinute;
        } else if(policy.equals("working-holiday") ){ 
            totalDutyManagerPay = dutyManagerPayPerMinute * .1;
        }else if(policy.equals("working-day-off")){
            totalDutyManagerPay = dutyManagerPayPerMinute *.3;
        }
        
        return util.roundOffToTwoDecimalPlaces(totalDutyManagerPay);
    }
    
    public double processAdditionalHolidayPay(String holiday, Double wage){
        Double rate;
        if(holiday.equals("legal-holiday")){
            rate = wage * 1;
        }else{
            rate = wage * .3;
        }
        
//        return new Double(df.format(rate));
        return util.roundOffToTwoDecimalPlaces(rate);
    }
    
    public double processAdditionalWorkingDayOff(Double wage, String wageEntry){
        double rate = 0;
        if(wageEntry.equals("daily")){
            rate = wage * .3; 
        } else {
            rate = wage * 1.3; 
        }     
        return util.roundOffToTwoDecimalPlaces(rate);
    }
    
    public double processMultiplePremiumPay(String holiday, double wage){
        double rate = 0;
        double wdo = 0;
        double premium;
        if(holiday.equals("legal-holiday")){
            wdo = wage * 1.3;
            premium = wdo * 2;
            rate = premium - wdo;
        }else{
            wdo = wage * 1.3;
            premium = wdo * 1.3;
            rate = premium - wdo;
        }
        
        return util.roundOffToTwoDecimalPlaces(rate);
    }
    
    private double getHolidayRate(String holiday, Double wage){
        Double rate = 0.0;
        if(holiday == null || holiday.isEmpty()){
            rate = wage + (wage * rate);
        }else if(holiday.equals("legal-holiday")){
            rate = wage + (wage * 1);
        }else{
            rate = wage + (wage * .3);
        }
        
//        return new Double(df.format(rate));
        return util.roundOffToTwoDecimalPlaces(rate);
    }
    
    private double calculateDeductionPerMinute(int lates, Double rate){
        double lateDeductionPerMinute;
        double ratePerMinute = rate / 480;
        lateDeductionPerMinute = lates * ratePerMinute;
//        return new Double(df.format(lateDeductionPerMinute));
        return util.roundOffToTwoDecimalPlaces(lateDeductionPerMinute);
    }
    
    private double calculateOvertimePerMinute(int overtime, Double rate){
        double overtimeAdditionPerMinute;
        double ratePerMinute = rate / 480;
        overtimeAdditionPerMinute = ((overtime * ratePerMinute) + ((overtime * ratePerMinute) * .25));
//        return new Double(df.format(overtimeAdditionPerMinute));
        return util.roundOffToTwoDecimalPlaces(overtimeAdditionPerMinute);
    }
    
    private double calculateNightDifferentialPerMinute(int nightDifferential, Double rate){
        double overtimePremiumPerMinute;
        double ratePerMinute = rate / 480;
        overtimePremiumPerMinute = nightDifferential * ratePerMinute;
//        return new Double(df.format(overtimePremiumPerMinute));
        return util.roundOffToTwoDecimalPlaces(overtimePremiumPerMinute);
    }
    
    private double calculateDutyManagerPerMinute(int dutyManager, double rate){
        double dutyManagerPerMinute;
        double ratePerMinute = rate /480;
        dutyManagerPerMinute = dutyManager * ratePerMinute;
        
        return util.roundOffToTwoDecimalPlaces(dutyManagerPerMinute);
    }
}
