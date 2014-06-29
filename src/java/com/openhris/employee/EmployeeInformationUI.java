/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee;

import com.vaadin.ui.Alignment;
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
    EmployeePositionHistory employeePositionHistory;
	
    String employeeId;	
    	
    public EmployeeInformationUI(){}
    
    public EmployeeInformationUI(String employeeId){
        this.employeeId = employeeId;
	
	init();	
    }
    
    public void init(){
//	setSpacing(true);
//	setMargin(true);	
	setSizeFull();
	setImmediate(true);
	
	employeePersonalInformation =  new EmployeePersonalInformation(getEmployeeId());
        employeePositionHistory = new EmployeePositionHistory(getEmployeeId());
	addComponent(employeeInformationWindow());
    }
    
    public ComponentContainer employeeInformationWindow(){	
	TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addStyleName("bar");
	
	VerticalLayout vlayout = new VerticalLayout();
	vlayout.setCaption("Personal Information");
	vlayout.addComponent(employeePersonalInformation);
	vlayout.setComponentAlignment(employeePersonalInformation, Alignment.MIDDLE_LEFT);
	ts.addComponent(vlayout);
	
	vlayout = new VerticalLayout();
	vlayout.setCaption("Address");
//	vlayout.addComponent(employeePersonalInformation);
	ts.addComponent(vlayout);
	
	vlayout = new VerticalLayout();
	vlayout.setCaption("Character Reference");
//	vlayout.addComponent(employeePersonalInformation);
	ts.addComponent(vlayout);
	
	vlayout = new VerticalLayout();
	vlayout.setCaption("Dependent(s)");
//	vlayout.addComponent(employeePersonalInformation);
	ts.addComponent(vlayout);
	
	vlayout = new VerticalLayout();
	vlayout.setCaption("Educational Background");
//	vlayout.addComponent(employeePersonalInformation);
	ts.addComponent(vlayout);
	
	vlayout = new VerticalLayout();
	vlayout.setCaption("Position History");
	vlayout.addComponent(employeePositionHistory);
	ts.addComponent(vlayout);
	
	vlayout = new VerticalLayout();
	vlayout.setCaption("Employment Record");
//	vlayout.addComponent(employeePersonalInformation);
	ts.addComponent(vlayout);
	
	vlayout = new VerticalLayout();
	vlayout.setCaption("Salary Information");
//	vlayout.addComponent(employeePersonalInformation);
	ts.addComponent(vlayout);
	
	return ts;    
    }
    
    public String getEmployeeId(){
	return employeeId;    
    }
	
}
