/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.beans;

import java.io.Serializable;

/**
 *
 * @author jet
 */
public class AttendanceBean implements Serializable {
    
    //private String employeeId;
    private String aDate;
    private Double lates;
    private Double overtime;
    private String shift;
    private String holidays;
    private Double deduction;
    private Double addition;
    
    public AttendanceBean(){}
    public AttendanceBean(String date){
        this.aDate = date;
    }
    
    //public void setEmployeeId(String employeeId){ this.employeeId = employeeId; }
    //public String getEmployeeId(){ return employeeId; }
    
    public String getADate(){ return aDate; }
    public void setADate(String aDate){ this.aDate = aDate; }    
    
    public void setLates(Double lates){ this.lates = lates; }
    public Double getLates(){ return lates; }
    
    public void setOvertime(Double overtime){ this.overtime = overtime; }
    public Double getOvertime(){ return overtime; }
    
    public void setShift(String shift){ this.shift = shift; }
    public String getShift(){ return shift; }
    
    public void setHolidays(String holidays){ this.holidays = holidays; }
    public String getHolidays(){ return holidays; }
    
    public void setDeduction(Double deduction){ this.deduction = deduction; }
    public Double getDeduction(){ return deduction; }
    
    public void setAddition(Double addition){ this.addition = addition; }
    public Double getAddition(){ return addition; }
}
