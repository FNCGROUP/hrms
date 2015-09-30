/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee;

import com.hrms.classes.GlobalVariables;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.service.SalaryInformationService;
import com.openhris.serviceprovider.SalaryInformationServiceImpl;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author jetdario
 */
public class EmployeeAllowanceInformation extends VerticalLayout {

    SalaryInformationService salaryInformationService = new SalaryInformationServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    DropDownComponent dropDown = new DropDownComponent();
    
    private String employeeId;
    private boolean userRoleResult = false;
    
    public EmployeeAllowanceInformation(String employeeId) {
        this.employeeId = employeeId;
        
        if(GlobalVariables.getUserRole().equals("hr")){
            userRoleResult = true;
        }
        
        setSpacing(true);
	setMargin(true);
	setSizeFull();
	setImmediate(true);
        
        addComponent(container());
    }
    
    ComponentContainer container(){
        GridLayout glayout = new GridLayout(3, 12);
        glayout.setSpacing(true);          
        glayout.setWidth("600px");
	glayout.setHeight("100%");
                
        TextField communication = new TextField("Communication Allowance");
        communication.setWidth("170px");
        communication.addStyleName("numerical");
        glayout.addComponent(communication, 0, 0);
        
        ComboBox communicationEntry = dropDown.populateEmploymentAllowanceEntry(new ComboBox());
        communicationEntry.setWidth("150px");
        glayout.addComponent(communicationEntry, 1, 0);
        
        Button communicationBtn = new Button("UPDATE COMMUNICATION ALLOWANCE");
        communicationBtn.setWidth("250px");
        glayout.addComponent(communicationBtn, 2, 0);
        glayout.setComponentAlignment(communicationBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 1, 2, 1);
        
        TextField perDiem = new TextField("Per Diem");
        perDiem.setWidth("170px");
        perDiem.addStyleName("numerical");
        glayout.addComponent(perDiem, 0, 2);
        
        ComboBox perDiemEntry = dropDown.populateEmploymentAllowanceEntry(new ComboBox());
        perDiemEntry.setWidth("150px");
        glayout.addComponent(perDiemEntry, 1, 2);
        
        Button perDiemBtn = new Button("UPDATE PER DIEM");
        perDiemBtn.setWidth("250px");
        glayout.addComponent(perDiemBtn, 2, 2);
        glayout.setComponentAlignment(perDiemBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 3, 2, 3);
        
        TextField cola = new TextField("Cola");
        cola.setWidth("170px");
        cola.addStyleName("numerical");
        glayout.addComponent(cola, 0, 4);
        
        ComboBox colaEntry = dropDown.populateEmploymentAllowanceEntry(new ComboBox());
        colaEntry.setWidth("150px");
        glayout.addComponent(colaEntry, 1, 4);
        
        Button colaBtn = new Button("UPDATE COLA");
        colaBtn.setWidth("250px");
        glayout.addComponent(colaBtn, 2, 4);
        glayout.setComponentAlignment(colaBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 5, 2, 5);
        
        TextField meal = new TextField("Meal Allowance");
        meal.setWidth("170px");
        meal.addStyleName("numerical");
        glayout.addComponent(meal, 0, 6);
        
        ComboBox mealEntry = dropDown.populateEmploymentAllowanceEntry(new ComboBox());
        mealEntry.setWidth("150px");
        glayout.addComponent(mealEntry, 1, 6);
        
        Button mealBtn = new Button("UPDATE MEAL ALLOWANCE");
        mealBtn.setWidth("250px");
        glayout.addComponent(mealBtn, 2, 6);
        glayout.setComponentAlignment(mealBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 7, 2, 7);
        
        TextField transportation = new TextField("Transportation Allowance");
        transportation.setWidth("170px");
        transportation.addStyleName("numerical");
        glayout.addComponent(transportation, 0, 8);
        
        ComboBox transportationEntry = dropDown.populateEmploymentAllowanceEntry(new ComboBox());
        transportationEntry.setWidth("150px");
        glayout.addComponent(transportationEntry, 1, 8);
        
        Button transportationBtn = new Button("UPDATE TRANSPORTATION ALLOWANCE");
        transportationBtn.setWidth("250px");
        glayout.addComponent(transportationBtn, 2, 8);
        glayout.setComponentAlignment(transportationBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 9, 2, 9);
        
        TextField other = new TextField("Other Allowances");
        other.setWidth("170px");
        other.addStyleName("numerical");
        glayout.addComponent(other, 0, 10);
        
        ComboBox otherEntry = dropDown.populateEmploymentAllowanceEntry(new ComboBox());
        otherEntry.setWidth("150px");
        glayout.addComponent(otherEntry, 1, 10);
        
        Button otherBtn = new Button("UPDATE OTHER ALLOWANCES");
        otherBtn.setWidth("250px");
        glayout.addComponent(otherBtn, 2, 10);
        glayout.setComponentAlignment(otherBtn, Alignment.BOTTOM_LEFT);
        
        return glayout;
    }
    
    String getEmployeeId(){
        return employeeId;
    }
    
    boolean getUserRoleResult(){
        return userRoleResult;
    }
}
