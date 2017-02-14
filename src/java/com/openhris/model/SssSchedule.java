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
    private double eeShare;
    private double erShare;
    private double ec;
    private String branch;
    private String corporate;

    public String getEmployeeId() {
        return employeeId;
    }

    public String getName() {
        return name;
    }

    public String getSssNo() {
        return sssNo;
    }

    public double getEeShare() {
        return eeShare;
    }

    public double getErShare() {
        return erShare;
    }

    public double getEc() {
        return ec;
    }

    public String getBranch() {
        return branch;
    }

    public String getCorporate() {
        return corporate;
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

    public void setEeShare(double eeShare) {
        this.eeShare = eeShare;
    }

    public void setErShare(double erShare) {
        this.erShare = erShare;
    }

    public void setEc(double ec) {
        this.ec = ec;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setCorporate(String corporate) {
        this.corporate = corporate;
    }
    
}
