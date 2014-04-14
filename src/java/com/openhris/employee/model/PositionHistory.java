/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee.model;

import java.util.Date;

/**
 *
 * @author jet
 */
public class PositionHistory extends EmploymentInformation {
    
        private String position;
	private String company;
	private String trade;
	private String branch;
	private int branchId;
	private String department;
	private Date entryDate;

        @Override
	public String getPosition(){ return position; }
        @Override
	public void setPosition(String position){ this.position = position; }
	
	public String getCompany(){ return company; }
	public void setCompany(String company){ this.company = company; }
	
	public String getTrade(){ return trade; }
	public void setTrade(String trade){ this.trade = trade; }
	
	public String getBranch(){ return branch; }
	public void setBranch(String branch){ this.branch = branch; }
	
        @Override
	public int getBranchId(){ return branchId; }
        @Override
	public void setBranchId(int branchId){ this.branchId = branchId; }
	
        @Override
	public String getDepartment(){ return department; }
        @Override
	public void setDepartment(String department){ this.department = department; }
	
        @Override
	public Date getEntryDate(){ return entryDate; }
        @Override
	public void setEntryDate(Date entryDate){ this.entryDate = entryDate; }
    
}
