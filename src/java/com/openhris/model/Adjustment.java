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
public class Adjustment extends Payroll{
    
    private int adjustmentId;
    private double amount;
    private String remarks;
    private Date datePosted;

    public int getAdjustmentId() {
        return adjustmentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public Date getDatePosted(){
        return datePosted;
    }
    
    public void setAdjustmentId(int adjustmentId) {
        this.adjustmentId = adjustmentId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }    
    
    public void setDatePosted(Date datePosted){
        this.datePosted = datePosted;
    }
}
