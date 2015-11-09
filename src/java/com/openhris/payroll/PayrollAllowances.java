/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.Allowances;
import com.openhris.service.AllowanceInformationService;
import com.openhris.serviceprovider.AllowanceInformationServiceImpl;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class PayrollAllowances {
 
    AllowanceInformationService ais = new AllowanceInformationServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public double getCommunicationAllowance(List policyList, String employeeId){
        Allowances a = ais.getAllowancesByEmployee(employeeId);
        double amount = 0;
        if(a.getComEntryType().equals("daily")){            
            for (Object policy : policyList) {
                if (policy.equals("null") || policy.equals("working-day-off") || 
                        policy.equals("working-holiday") || policy.toString().isEmpty()) {
                    amount = amount + a.getCommunication();
                }
            }            
        }else{
            double allowance_entry_per_day = util.roundOffToTwoDecimalPlaces((a.getCommunication() * 12)/314);
            Double halfAllowance = a.getCommunication() / 2;
            for (Object policy : policyList) {
                if (policy.equals("absent") || policy.equals("paternity-leave") || 
                        policy.equals("service-incentive-leave") || policy.equals("holiday") || 
                        policy.equals("paid-vacation-leave") || policy.equals("paid-sick-leave") || 
                        policy.equals("unpaid-vacation-leave") || policy.equals("unpaid-sick-leave") || 
                        policy.equals("suspended") || policy.equals("resigned") || 
                        policy.equals("maternity-leave") || policy.equals("awol") || 
                        policy.equals("end-of-contract") || policy.equals("resigned")) {
                    halfAllowance =  halfAllowance - allowance_entry_per_day;
                }
            }
            amount = halfAllowance;
        }
        
//        return util.roundOffToTwoDecimalPlaces(amount);
        return amount;
    }
    
    public double getPerDiemAllowance(List policyList, String employeeId){
        Allowances a = ais.getAllowancesByEmployee(employeeId);
        double amount = 0;
        if(a.getPerDiemEntryType().equals("daily")){            
            for (Object policy : policyList) {
                if (policy.equals("null") || policy.equals("working-day-off") || 
                        policy.equals("working-holiday") || policy.toString().isEmpty()) {
                    amount = amount + a.getPerDiem();
                }
            }            
        }else{
            double allowance_entry_per_day = util.roundOffToTwoDecimalPlaces((a.getPerDiem()* 12)/314);
            Double halfAllowance = a.getPerDiem()/ 2;
            for (Object policy : policyList) {
                if (policy.equals("absent") || policy.equals("paternity-leave") || 
                        policy.equals("service-incentive-leave") || policy.equals("holiday") || 
                        policy.equals("paid-vacation-leave") || policy.equals("paid-sick-leave") || 
                        policy.equals("unpaid-vacation-leave") || policy.equals("unpaid-sick-leave") || 
                        policy.equals("suspended") || policy.equals("resigned") || 
                        policy.equals("maternity-leave") || policy.equals("awol") || 
                        policy.equals("end-of-contract") || policy.equals("resigned")) {
                    halfAllowance =  halfAllowance - allowance_entry_per_day;
                }
            }
            amount = halfAllowance;
        }
        
//        return util.roundOffToTwoDecimalPlaces(amount);        
        return amount;
    }
        
    public double getColaAllowance(List policyList, String employeeId){
        Allowances a = ais.getAllowancesByEmployee(employeeId);
        double amount = 0;
        if(a.getColaEntryType().equals("daily")){            
            for (Object policy : policyList) {
                if (policy.equals("null") || policy.equals("working-day-off") || 
                        policy.equals("working-holiday") || policy.toString().isEmpty()) {
                    amount = amount + a.getCola();
                }
            }            
        }else{
            double allowance_entry_per_day = util.roundOffToTwoDecimalPlaces((a.getCola() * 12)/314);
            Double halfAllowance = a.getCola()/ 2;
            for (Object policy : policyList) {
                if (policy.equals("absent") || policy.equals("paternity-leave") || 
                        policy.equals("service-incentive-leave") || policy.equals("holiday") || 
                        policy.equals("paid-vacation-leave") || policy.equals("paid-sick-leave") || 
                        policy.equals("unpaid-vacation-leave") || policy.equals("unpaid-sick-leave") || 
                        policy.equals("suspended") || policy.equals("resigned") || 
                        policy.equals("maternity-leave") || policy.equals("awol") || 
                        policy.equals("end-of-contract") || policy.equals("resigned")) {
                    halfAllowance =  halfAllowance - allowance_entry_per_day;
                }
            }
            amount = halfAllowance;
        }
        
//        return util.roundOffToTwoDecimalPlaces(amount);
        return amount;
    }
    
    public double getMealAllowance(List policyList, String employeeId){
        Allowances a = ais.getAllowancesByEmployee(employeeId);
        double amount = 0;
        if(a.getMealEntryType().equals("daily")){            
            for (Object policy : policyList) {
                if (policy.equals("null") || policy.equals("working-day-off") || 
                        policy.equals("working-holiday") || policy.toString().isEmpty()) {
                    amount = amount + a.getMeal();
                }
            }            
        }else{
            double allowance_entry_per_day = util.roundOffToTwoDecimalPlaces((a.getMeal()* 12)/314);
            Double halfAllowance = a.getMeal()/ 2;
            for (Object policy : policyList) {
                if (policy.equals("absent") || policy.equals("paternity-leave") || 
                        policy.equals("service-incentive-leave") || policy.equals("holiday") || 
                        policy.equals("paid-vacation-leave") || policy.equals("paid-sick-leave") || 
                        policy.equals("unpaid-vacation-leave") || policy.equals("unpaid-sick-leave") || 
                        policy.equals("suspended") || policy.equals("resigned") || 
                        policy.equals("maternity-leave") || policy.equals("awol") || 
                        policy.equals("end-of-contract") || policy.equals("resigned")) {
                    halfAllowance =  halfAllowance - allowance_entry_per_day;
                }
            }
            amount = halfAllowance;
        }
        
//        return util.roundOffToTwoDecimalPlaces(amount);
        return amount;
    }
    
    public double getTransportationAllowance(List policyList, String employeeId){
        Allowances a = ais.getAllowancesByEmployee(employeeId);
        double amount = 0;
        if(a.getTransEntryType().equals("daily")){            
            for (Object policy : policyList) {
                if (policy.equals("null") || policy.equals("working-day-off") || 
                        policy.equals("working-holiday") || policy.toString().isEmpty()) {
                    amount = amount + a.getTransportation();
                }
            }            
        }else{
            double allowance_entry_per_day = util.roundOffToTwoDecimalPlaces((a.getTransportation()* 12)/314);
            Double halfAllowance = a.getTransportation()/ 2;
            for (Object policy : policyList) {
                if (policy.equals("absent") || policy.equals("paternity-leave") || 
                        policy.equals("service-incentive-leave") || policy.equals("holiday") || 
                        policy.equals("paid-vacation-leave") || policy.equals("paid-sick-leave") || 
                        policy.equals("unpaid-vacation-leave") || policy.equals("unpaid-sick-leave") || 
                        policy.equals("suspended") || policy.equals("resigned") || 
                        policy.equals("maternity-leave") || policy.equals("awol") || 
                        policy.equals("end-of-contract") || policy.equals("resigned")) {
                    halfAllowance =  halfAllowance - allowance_entry_per_day;
                }
            }
            amount = halfAllowance;
        }
        
//        return util.roundOffToTwoDecimalPlaces(amount);
        return amount;
    }
    
    public double getOtherAllowance(List policyList, String employeeId){
        Allowances a = ais.getAllowancesByEmployee(employeeId);
        double amount = 0;
        if(a.getOthersEntryType().equals("daily")){            
            for (Object policy : policyList) {
                if (policy.equals("null") || policy.equals("working-day-off") || 
                        policy.equals("working-holiday") || policy.toString().isEmpty()) {
                    amount = amount + a.getOthers();
                }
            }            
        }else{
            double allowance_entry_per_day = util.roundOffToTwoDecimalPlaces((a.getOthers()* 12)/314);
            Double halfAllowance = a.getOthers()/ 2;
            for (Object policy : policyList) {
                if (policy.equals("absent") || policy.equals("paternity-leave") || 
                        policy.equals("service-incentive-leave") || policy.equals("holiday") || 
                        policy.equals("paid-vacation-leave") || policy.equals("paid-sick-leave") || 
                        policy.equals("unpaid-vacation-leave") || policy.equals("unpaid-sick-leave") || 
                        policy.equals("suspended") || policy.equals("resigned") || 
                        policy.equals("maternity-leave") || policy.equals("awol") || 
                        policy.equals("end-of-contract") || policy.equals("resigned")) {
                    halfAllowance =  halfAllowance - allowance_entry_per_day;
                }
            }
            amount = halfAllowance;
        }
        
//        return util.roundOffToTwoDecimalPlaces(amount);
        return amount;
    }
}
