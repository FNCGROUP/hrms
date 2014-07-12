/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee.model;

/**
 *
 * @author jet
 */
public class Employee {
    
        private String employeeId;
	private String firstname;
	private String middlename;
	private String lastname;
	private String sssNo;
	private String tinNo;
	private String phicNo;  
	private String hdmfNo;	
	
	public String getEmployeeId(){ return employeeId; }	
	public void setEmployeeId(String employeeId){ this.employeeId = employeeId; }
	        
	public String getFirstname(){ return firstname; }
	public void setFirstname(String firstname){ this.firstname = firstname; }
	
	public String getMiddlename(){ return middlename; }
	public void setMiddlename(String middlename){ this.middlename = middlename; }
	
	public String getLastname(){ return lastname; }
	public void setLastname(String lastname){ this.lastname = lastname; }
		
	public String getSssNo(){ return sssNo; }
	public void setSssNo(String sssNo){ this.sssNo = sssNo; }
	
	public String getTinNo(){ return tinNo; }
	public void setTinNo(String tinNo){ this.tinNo = tinNo; }
	
	public String getPhicNo(){ return phicNo; }
	public void setPhicNo(String phicNo){ this.phicNo = phicNo; }
	
	public String gethdmfNo(){ return hdmfNo; }
	public void setHdmfNo(String hdmfNo){ this.hdmfNo = hdmfNo; }
        
}
