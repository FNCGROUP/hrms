/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.contributions.model;

/**
 *
 * @author jet
 */
public class Sss {
    
    private int id;
    private Double minSalary;
    private Double maxSalary;
    private Double monthlySalaryCredit;
    private Double employeeContribution;
    private Double employerContribution;
    private Double totalMontlyContribution;

    public int getId(){
        return id;
    }
    
    public Double getMinSalary() {
        return minSalary;
    }

    public Double getMaxSalary() {
        return maxSalary;
    }

    public Double getMonthlySalaryCredit() {
        return monthlySalaryCredit;
    }

    public Double getEmployeeContribution() {
        return employeeContribution;
    }

    public Double getEmployerContribution() {
        return employerContribution;
    }

    public Double getTotalMontlyContribution() {
        return totalMontlyContribution;
    }

    public void setId(int id){
        this.id = id;
    }
    
    public void setMinSalary(Double minSalary) {
        this.minSalary = minSalary;
    }

    public void setMaxSalary(Double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public void setMonthlySalaryCredit(Double monthlySalaryCredit) {
        this.monthlySalaryCredit = monthlySalaryCredit;
    }

    public void setEmployeeContribution(Double employeeContribution) {
        this.employeeContribution = employeeContribution;
    }

    public void setEmployerContribution(Double employerContribution) {
        this.employerContribution = employerContribution;
    }

    public void setTotalMontlyContribution(Double totalMontlyContribution) {
        this.totalMontlyContribution = totalMontlyContribution;
    }
    
}
