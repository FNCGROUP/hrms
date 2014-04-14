/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.beans;

/**
 *
 * @author jet
 */
public class PhilhealthBean {
    
    private Double minSalary;
    private Double maxSalary;
    private Double baseSalary;
    private Double totalMonthlyPremium;
    private Double employeeShare;
    private Double employerShare;
    
    public void setMinSalary(Double minSalary){
        this.minSalary = minSalary;
    }
    
    public Double getMinSalary(){
        return minSalary;
    }
    
    public void setMaxSalary(Double maxSalary){
        this.maxSalary = maxSalary;
    }
    
    public Double getMaxSalary(){
        return maxSalary;
    }
    
    public void setBaseSalary(Double baseSalary){
        this.baseSalary = baseSalary;
    }
    
    public Double getBaseSalary(){
        return baseSalary;
    }
    
    public void setTotalMonthlyPremium(Double totalMonthlyPremium){
        this.totalMonthlyPremium = totalMonthlyPremium;
    }
    
    public Double getTotalMonthlyPremium(){
        return totalMonthlyPremium;
    }
    
    public void setEmployeeShare(Double employeeShare){
        this.employeeShare = employeeShare;
    }
    
    public Double getEmployeeShare(){
        return employeeShare;
    }
    
    public void setEmployerShare(Double employerShare){
        this.employerShare = employerShare;
    }
    
    public Double getEmployerShare(){
        return employerShare;
    }
}
