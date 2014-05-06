/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author jet
 */
public class EmployeeInformationUI extends VerticalLayout {
    
    String employeeId;	
    	
    public EmployeeInformationUI(){}
    
    public EmployeeInformationUI(String employeeId){
        this.employeeId = employeeId;
	
	employeeInformationInit();
    }
    
    public void employeeInformationInit(){
	setSpacing(true);
	setMargin(true);	
	setSizeFull();
	setImmediate(true);
    }
    
    public Window employeeInformationWindow(){
	Window window = new Window("Employee Information"); 
	window.setWidth("90%");
	window.setHeight("100%");
	
	TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addStyleName("bar");
	
	GridLayout glayout = new GridLayout();
	glayout.setSizeFull();
	glayout.setCaption("Personal Information");
	ts.addComponent(glayout);
	
	glayout = new GridLayout();
	glayout.setSizeFull();
	glayout.setCaption("Educational Background");
	ts.addComponent(glayout);
	
	window.addComponent(ts);
	
	return window;    
    }
    
    public String getEmployeeId(){
	return employeeId;    
    }
	
}
