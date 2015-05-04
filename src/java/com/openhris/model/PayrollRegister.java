/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.model;

/**
 *
 * @author jet
 */
public class PayrollRegister extends Advances {
    
    private String name;
    private double ratePerDay;
    private String employmentWageEntry;
    private String branchName;
    private String tradeName;
    private String corporateName;
    private String currentStatus;

    public String getName() {
        return name;
    }

    public String getBranchName() {
        return branchName;
    }

    public double getRatePerDay(){
        return ratePerDay;
    }
    
    public String getEmploymentWageEntry(){
        return employmentWageEntry;
    }
    
    public String getTradeName() {
        return tradeName;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public String currentStatus(){
        return currentStatus;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public void setRatePerDay(double ratePerDay){
        this.ratePerDay = ratePerDay;
    }
    
    public void setEmploymentWageEntry(String employmentWageEntry){
        this.employmentWageEntry = employmentWageEntry;
    }
    
    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }
    
    public void setCurrentStatus(String currentStatus){
        this.currentStatus = currentStatus;
    }
    
}
