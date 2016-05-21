/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.model;

/**
 *
 * @author jetdario
 */
public class AFLSchedule {
 
    private String employeeId;
    private String employeeName;
    private double amount;
    private String branchName;

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public double getAmount() {
        return amount;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }
    
}
