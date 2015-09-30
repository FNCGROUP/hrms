/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee;

import com.vaadin.Application;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author jet
 */
public class EmployeeInformationUI extends VerticalLayout {
    
    EmployeePersonalInformation employeePersonalInformation;	
    PostEmploymentInfomation postEmploymentInfomation;
    EmployeeSalaryInformation employeeSalaryInformation;
    EmployeeAddress employeeAddress;
    EmployeeCharacterReference characterReference;
    EmployeeAllowanceInformation employeeAllowanceInformation;
    Application application;
	
    private String employeeId;	
    	
    public EmployeeInformationUI(){}
    
    public EmployeeInformationUI(String employeeId, Application application){
        this.employeeId = employeeId;
        this.application = application;
        
	init();	
    }
    
    public void init(){
//	setSpacing(true);
//	setMargin(true);	
	setSizeFull();
	setImmediate(true);
	       
	employeePersonalInformation =  new EmployeePersonalInformation(getEmployeeId(), getThisApplication());
        postEmploymentInfomation = new PostEmploymentInfomation(getEmployeeId());
        employeeSalaryInformation = new EmployeeSalaryInformation(getEmployeeId());
        employeeAddress = new EmployeeAddress(getEmployeeId());
        characterReference = new EmployeeCharacterReference(getEmployeeId());
        employeeAllowanceInformation = new EmployeeAllowanceInformation(getEmployeeId());
        
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
	vlayout.addComponent(employeeAddress);
	ts.addComponent(vlayout);
	
	vlayout = new VerticalLayout();
	vlayout.setCaption("Character Reference");
	vlayout.addComponent(characterReference);
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
	vlayout.setCaption("Post Employment Info");
	vlayout.addComponent(postEmploymentInfomation);
	ts.addComponent(vlayout);
	
	vlayout = new VerticalLayout();
	vlayout.setCaption("Work History");
//	vlayout.addComponent(employeePersonalInformation);
	ts.addComponent(vlayout);
	
	vlayout = new VerticalLayout();
	vlayout.setCaption("Salary Information");
	vlayout.addComponent(employeeSalaryInformation);
	ts.addComponent(vlayout);
        
        vlayout = new VerticalLayout();
	vlayout.setCaption("Allowance Information");
	vlayout.addComponent(employeeAllowanceInformation);
	ts.addComponent(vlayout);
	
	return ts;    
    }
    
    public String getEmployeeId(){
	return employeeId;    
    }    
    
    public Application getThisApplication(){
	 return application;
    }
}
