/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee.others;

import com.hrms.classes.GlobalVariables;
import com.openhris.commons.DropDownComponent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author jetdario
 */
public class OtherInformation extends VerticalLayout {

    DropDownComponent dropDown = new DropDownComponent();
    
    private String employeeId;
    private boolean userRoleResult = false;
    
    public OtherInformation(String employeeId) {
        this.employeeId = employeeId;
        
        if(GlobalVariables.getUserRole().equals("hr")){
            userRoleResult = true;
        }
        
        setSpacing(true);
	setMargin(true);
	setSizeFull();
	setImmediate(true);
        
        addComponent(component());
    }
    
    Component component(){
        GridLayout glayout = new GridLayout(2, 8);
        glayout.setSpacing(true);          
        glayout.setWidth("600px");
	glayout.setHeight("100%");
        
        final ComboBox employmentStatus = dropDown.populateEmploymentStatus("Employment Status: ");
        employmentStatus.setWidth("210px");
        glayout.addComponent(employmentStatus, 0, 0);
        
        final ComboBox employmentWageStatus = dropDown.populateEmploymentWageStatus("Employment Wage Status: ");
        employmentWageStatus.setWidth("210px");
        glayout.addComponent(employmentWageStatus, 0, 1);
        
        final ComboBox employmentWageEntry = dropDown.populateEmploymentWageEntry("Employment Wage Entry: ");
        employmentWageEntry.setWidth("210px");
        glayout.addComponent(employmentWageEntry, 0, 2);
        
        final TextField employmentWage = new TextField("Employment Wage");
        employmentWage.setWidth("100%");
        employmentWage.addStyleName("numerical");
        glayout.addComponent(employmentWage, 0, 3);
        
        return glayout;
    }
    
    String getEmployeeId(){
        return employeeId;
    }
}
