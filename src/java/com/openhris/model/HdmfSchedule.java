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
public class HdmfSchedule {
    
    private String employeeId;
    private String employeeName;
    private String hdmfNo;
    private double eeHdmf;
    private double erHdmf;
    private Date payrollDate;
    private String branchName;
    private String currentStatus;

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getHdmfNo() {
        return hdmfNo;
    }

    public double getEeHdmf() {
        return eeHdmf;
    }

    public double getErHdmf() {
        return erHdmf;
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

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setHdmfNo(String hdmfNo) {
        this.hdmfNo = hdmfNo;
    }

    public void setEeHdmf(double eeHdmf) {
        this.eeHdmf = eeHdmf;
    }

    public void setErHdmf(double erHdmf) {
        this.erHdmf = erHdmf;
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
    
}
