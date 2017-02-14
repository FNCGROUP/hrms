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
public class BankDebitMemo {
    
    private int payrollId;
    private String employeeId;
    private String bankAccountNo;
    private String firstname;
    private String middlename;
    private String lastname;
    private double amount;
    private String branch;
    private Date payrollDate;
    private String corporateName;

    public int getPayrollId() {
        return payrollId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getLastname() {
        return lastname;
    }

    public double getAmount() {
        return amount;
    }

    public String getBranch() {
        return branch;
    }

    public Date getPayrollDate() {
        return payrollDate;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setPayrollId(int payrollId) {
        this.payrollId = payrollId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setPayrollDate(Date payrollDate) {
        this.payrollDate = payrollDate;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }
    
}
