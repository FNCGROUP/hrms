/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.beans;

/**
 *
 * @author jet
 */
public class SSSBean {
    private Double minSalary;
    private Double maxSalary;
    private Double monthlySalaryCredit;
    private Double employeeContribution;
    private Double employerContribution;
    private Double totalMontlyContribution;
    
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
    
    public void setMonthlySalaryCredit(Double monthlySalaryCredit){
        this.monthlySalaryCredit = monthlySalaryCredit;
    }
    
    public Double getMonthlySalaryCredit(){
        return monthlySalaryCredit;
    }
    
    public void setEmployeeContribution(Double employeeContribution){
        this.employeeContribution = employeeContribution;
    }
    
    public Double getEmployeeContribution(){
        return employeeContribution;
    }
    
    public void setEmployerContribution(Double employerContribution){
        this.employerContribution = employerContribution;
    }
    
    public Double getEmployerContribution(){
        return employerContribution;
    }
    
    public void setTotalMonthlyContribution(Double totalMonthlyContribution){
        this.totalMontlyContribution = totalMonthlyContribution;
    }
    
    public Double getTotalMonthlyContribution(){
        return totalMontlyContribution;
    }
}
