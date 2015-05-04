/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.model;

import java.util.Date;

/**
 *
 * @author jet
 */
public class Dependent extends Employee {
    
        private String name;
	private Date dob;

	public String getName(){ return name; }
	public void setName(String name){ this.name = name; }
	
	public Date getDob(){ return dob; }
	public void setDob(Date dob){ this.dob = dob; }
    
}
