/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.entities;

/**
 *
 * @author jet
 */
public class Adjustments {
    
    private Integer id;
    private Integer salaryId;
    private Double amount;
    private String remarks;
    private String datePosted;
    
    public void setId(Integer id){
        this.id = id;
    }
    
    public Integer getId(){
        return id;
    }
    
    public void setSalaryId(Integer salaryId){
        this.salaryId = salaryId;
    }
    
    public Integer getSalaryId(){
        return salaryId;
    }
    
    public void setAmount(Double amount){
        this.amount = amount;
    }
    
    public Double getAmount(){
        return amount;
    }
    
    public void setRemarks(String remarks){
        this.remarks = remarks;
    }
    
    public String getRemarks(){
        return remarks;
    }
    
    public void setDatePosted(String datePosted){
        this.datePosted = datePosted;
    }
    
    public String getDatePosted(){
        return datePosted;
    }
    
}
