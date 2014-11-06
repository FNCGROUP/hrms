/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee.model;

import java.io.InputStream;
import java.util.Date;

/**
 *
 * @author jet
 */
public class EmploymentInformation extends Employee {
    
        private String employmentStatus;
	private String employmentWageStatus;
	private String employmentWageEntry;
	private double employmentWage;    
        private String totalDependent;
	private int branchId;
	private Date entryDate;
	private double allowance;
	private String allowanceEntry;
	private double afl; //Allowance for Liquidation
	private String position;
	private String department;
	private String bankAccountNo;
        private String currentStatus;
        private Date endDate;
	
	public String getEmploymentStatus(){ return employmentStatus; }
	public void setEmploymentStatus(String employmentStatus){ this.employmentStatus = employmentStatus; }
	
	public String getEmploymentWageStatus(){ return employmentWageStatus; }
	public void setEmploymentWageStatus(String employmentWageStatus){ this.employmentWageStatus = employmentWageStatus; }
	
	public String getEmploymentWageEntry(){ return employmentWageEntry; }
	public void setEmploymentWageEntry(String employmentWageEntry){ this.employmentWageEntry = employmentWageEntry; }
	
	public double getEmploymentWage(){ return employmentWage; }
	public void setEmploymentWage(double employmentWage){ this.employmentWage = employmentWage; }
	
        public String getTotalDependent(){ return totalDependent; }
        public void setTotalDependent(String totalDependent){ this.totalDependent = totalDependent; }
        
	public int getBranchId(){ return branchId; }
	public void setBranchId(int branchId){ this.branchId = branchId; }
	
	public Date getEntryDate(){ return entryDate; }
	public void setEntryDate(Date entryDate){ this.entryDate = entryDate; }
	
	public double getAllowance(){ return allowance; }
	public void setAllowance(double allowance){ this.allowance = allowance; }
	
	public String getAllowanceEntry(){ return allowanceEntry; }
	public void setAllowanceEntry(String allowanceEntry){ this.allowanceEntry = allowanceEntry; }
	
	public double getAfl(){ return afl; }
	public void setAfl(double afl){ this.afl = afl; }
	
	public String getPosition(){ return position; }
	public void setPosition(String position){ this.position = position; }
	
	public String getDepartment(){ return department; }
	public void setDepartment(String department){ this.department = department; }
	
	public String getBankAccountNo(){ return bankAccountNo; }
	public void setBankAccountNo(String bankAccountNo){ this.bankAccountNo = bankAccountNo; }
        
        public String getCurrentStatus(){ return currentStatus; }        
        public void setCurrentStatus(String currentStatus){ this.currentStatus = currentStatus; }
        
        public Date getEndDate(){ return endDate; }
        public void setEndDate(Date endDate) { this.endDate = endDate; }
}
