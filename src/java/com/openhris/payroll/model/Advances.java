/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.model;

import java.util.Date;

/**
 *
 * @author jet
 */
public class Advances extends Payroll {
    
    int advanceId;
    double amount;
    String advanceType;
    String particulars;
    Date datePosted;
    String advanceRowStatus;
    String remarks;
    Date dateRemoved;

    public int getAdvanceId(){
        return advanceId;
    }
    
    /**
     * 
     * @return advances amount
     */
    public double getAmount() {
        return amount;
    }

    public String getAdvanceType() {
        return advanceType;
    }

    public String getParticulars() {
        return particulars;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    public String getAdvanceRowStatus() {
        return advanceRowStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public Date getDateRemoved() {
        return dateRemoved;
    }

    public void setAdvanceId(int advanceId){
        this.advanceId = advanceId;
    }
    
    /**
     * advances
     * @param amount 
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setAdvanceType(String advanceType) {
        this.advanceType = advanceType;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public void setDatePosted(Date datePosted) {
        this.datePosted = datePosted;
    }

    public void setAdvanceRowStatus(String advanceRowStatus) {
        this.advanceRowStatus = advanceRowStatus;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public void setDateRemoved(Date dateRemoved) {
        this.dateRemoved = dateRemoved;
    }
    
}
