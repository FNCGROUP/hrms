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
    
    EmployeePersonalInformation employeePersonalInformation;	
	
    String employeeId;	
    	
    public EmployeeInformationUI(){}
    
    public EmployeeInformationUI(String employeeId){
        this.employeeId = employeeId;
	
	init();	
    }
    
    public void init(){
	setSpacing(true);
	setMargin(true);	
	setSizeFull();
	setImmediate(true);
	
	employeePersonalInformation =  new EmployeePersonalInformation(getEmployeeId());
    }
    
    public Window employeeInformationWindow(){
	Window window = new Window("Employee Information"); 
	window.setWidth("90%");
	window.setHeight("100%");
	
	TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addStyleName("bar");
	
	VerticalLayout vlayout = new VerticalLayout();
	vlayout.setCaption("Personal Information");
	vlayout.addComponent(employeePersonalInformation);
	ts.addComponent(vlayout);
		
	window.addComponent(ts);
	
	return window;    
    }
    
    public String getEmployeeId(){
	return employeeId;    
    }
	
}
