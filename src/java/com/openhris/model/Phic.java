/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.model;

/**
 *
 * @author jet
 */
public class Phic {
    
    public int id;
    public double minSalary;
    public double maxSalary;
    public double baseSalary;
    public double totalMonthlyPremium;
    public double employeeShare;
    public double employerShare;

    public int getId() {
        return id;
    }

    public double getMinSalary() {
        return minSalary;
    }

    public double getMaxSalary() {
        return maxSalary;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public double getTotalMonthlyPremium() {
        return totalMonthlyPremium;
    }

    public double getEmployeeShare() {
        return employeeShare;
    }

    public double getEmployerShare() {
        return employerShare;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMinSalary(double minSalary) {
        this.minSalary = minSalary;
    }

    public void setMaxSalary(double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public void setTotalMonthlyPremium(double totalMonthlyPremium) {
        this.totalMonthlyPremium = totalMonthlyPremium;
    }

    public void setEmployeeShare(double employeeShare) {
        this.employeeShare = employeeShare;
    }

    public void setEmployerShare(double employerShare) {
        this.employerShare = employerShare;
    }
    
}
