/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.administrator.model;

import com.openhris.employee.model.Employee;
import com.vaadin.Application;
import com.vaadin.service.ApplicationContext.TransactionListener;

/**
 *
 * @author jet
 */
public class User{
    
    private int id;
    private String name;
    private String trade;
    private String branch;
    private String username;
    private String role;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTrade() {
        return trade;
    }

    public String getBranch() {
        return branch;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTrade(String trade) {
        this.trade = trade;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}
