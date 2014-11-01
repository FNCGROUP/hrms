/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.payroll.model;

/**
 *
 * @author jetdario
 */
public class Adjustment extends Payroll{
    
    private int adjustmentId;
    private double amount;
    private String remarks;

    public int getAdjustmentId() {
        return adjustmentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getRemarks() {
        return remarks;
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
    
}
