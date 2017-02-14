/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.model;

import java.util.Date;

/**
 *
 * @author jetdario
 */
public class TaxSchedule {
    
    private String employeeId;
    private String employeeName;
    private String tinNo;
    private double taxAmount;
    private Date payrollDate;
    private String branchName;
    private String currentStatus;
    private String corporateName;

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getTinNo() {
        return tinNo;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public Date getPayrollDate() {
        return payrollDate;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setTinNo(String tinNo) {
        this.tinNo = tinNo;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public void setPayrollDate(Date payrollDate) {
        this.payrollDate = payrollDate;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }
    
}
