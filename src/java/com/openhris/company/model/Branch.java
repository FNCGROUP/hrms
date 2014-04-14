/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.company.model;

/**
 *
 * @author jet
 */
public class Branch extends Trade {
    
        private int branchId;
        private String branchName;
	private String address;

        public int getBranchId(){ return branchId; }
        public void setBranchId(int branchId){ this.branchId = branchId; }
        
	public String getBranchName(){return branchName; }
	public void setBranchName(String branchName){ this.branchName = branchName; }

	public String getBranchAdress(){ return address; }
	public void setBranchAddress(String address){ this.address = address; }
        
}
