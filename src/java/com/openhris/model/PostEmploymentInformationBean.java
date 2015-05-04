/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.model;

import java.util.Date;

/**
 *
 * @author jet
 */
public class PostEmploymentInformationBean extends EmploymentInformation {
    
        private int positionId;
	private String company;
	private String trade;
	private String branch;
        
        public int getPositionId(){ return positionId; }
        public void setPositionId(int positionId){ this.positionId = positionId; }
	
	public String getCompany(){ return company; }
	public void setCompany(String company){ this.company = company; }
	
	public String getTrade(){ return trade; }
	public void setTrade(String trade){ this.trade = trade; }
	
	public String getBranch(){ return branch; }
	public void setBranch(String branch){ this.branch = branch; }    
}
