/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.entities;

/**
 *
 * @author jet
 */
public class Advances {
    
    private Integer id;
    private Integer salaryId;
    private Double amount;
    private String advancesType;
    private String particulars;
    private String datePosted;
    private String employeeId;
    private String rowStatus;
    private String remarks;
    private String dateRemoved;
    
    public void setId(Integer id){ this.id = id; }
    public Integer getId(){ return id; }
    
    public void setSalaryId(Integer salaryId){ this.salaryId = salaryId; }
    public Integer getSalaryId(){ return salaryId; }
    
    public void setAmount(Double amount){ this.amount = amount; }
    public Double getAmount(){ return amount; }
    
    public void setAdvancesType(String advancesType){ this.advancesType = advancesType; }
    public String getAdvancesType(){ return advancesType; }
    
    public void setParticulars(String particulars){ this.particulars = particulars; }
    public String getParticulars(){ return particulars; }
    
    public void setDatePosted(String datePosted){ this.datePosted = datePosted; }
    public String getDatePosted(){ return datePosted; }
    
    public void setEmployeeId(String employeeId){ this.employeeId = employeeId; }
    public String getEmployeeId(){ return employeeId; }
    
    public void setRowStatus(String rowStatus){ this.rowStatus = rowStatus; }
    public String getRowStatus(){ return rowStatus; }
    
    public void setRemarks(String remarks){ this.remarks = remarks; }
    public String getRemarks(){ return remarks; }
    
    public void setDateRemoved(String dateRemoved){ this.dateRemoved = dateRemoved; }
    public String getDateRemoved(){ return dateRemoved; } 
    
}
