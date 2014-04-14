/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.beans;

/**
 *
 * @author jet
 */
public class BranchBean {
    
    private String branchName;
    private Integer tradeId;
    
    public void setBranchName(String branchName){
        this.branchName = branchName;
    }
    
    public String getBranchName(){
        return branchName;
    }
    
    public void setTradeId(int tradeId){
        this.tradeId = tradeId;
    }
    
    public Integer getTradeId(){
        return tradeId;
    }
    
}
