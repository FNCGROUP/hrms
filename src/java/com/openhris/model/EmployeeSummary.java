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
public class EmployeeSummary {
    
    private String employeeId;
    private String employeeName;
    private String position;
    private String branch;

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public String getPosition() {
        return position;
    }

    public String getBranch() {
        return branch;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
    
}
