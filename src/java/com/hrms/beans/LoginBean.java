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
public class LoginBean implements Serializable {
    
    private String username;
    private String password;
    private Boolean result;
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public String getUsername(){
        return username;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setResult(boolean result){
        this.result = result;
    }
    
    public Boolean getResult(){
        return result;
    }
    
}
