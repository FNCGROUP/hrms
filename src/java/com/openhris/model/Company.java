/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.model;

import java.util.List;

/**
 *
 * @author jet
 */
public class Company {
    
        private String companyName;
	private int companyId;
	private List<String> companyList;
    
	public String getCompanyName(){ return companyName; }
	public void setCompanyName(String companyName){ this.companyName = companyName; }	

	public int getCompanyId(){ return companyId; }
	public void setCompanyId(int companyId){ this.companyId = companyId; }
	
	public List getCompanyList(){ return companyList; }
	public void setCompanyList(List companyList){ this.companyList = companyList; }
    
}
