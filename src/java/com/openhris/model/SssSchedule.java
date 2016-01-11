/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.model;

/**
 *
 * @author jetdario
 */
public class SssSchedule {
    
    private String employeeId;
    private String name;
    private String sssNo;
    private double erShare;
    private double ec;

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getSssNo() {
        return sssNo;
    }

    public double getErShare() {
        return erShare;
    }

    public double getEc() {
        return ec;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSssNo(String sssNo) {
        this.sssNo = sssNo;
    }

    public void setErShare(double erShare) {
        this.erShare = erShare;
    }

    public void setEc(double ec) {
        this.ec = ec;
    }
    
}
