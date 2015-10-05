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
    
    TextField communication;
    TextField perDiem;
    TextField cola;
    TextField meal;
    TextField transportation;
    TextField other;
    
    ComboBox communicationEntry;
    ComboBox perDiemEntry;
    ComboBox colaEntry;
    ComboBox mealEntry;
    ComboBox transportationEntry;
    ComboBox otherEntry;
    
    ComponentContainer container(){
        GridLayout glayout = new GridLayout(3, 12);
        glayout.setSpacing(true);          
        glayout.setWidth("600px");
	glayout.setHeight("100%");
                
        communication = new TextField("Communication Allowance");
        communication.setWidth("170px");
        communication.addStyleName("numerical");
        glayout.addComponent(communication, 0, 0);
        
        communicationEntry = dropDown.populateEmploymentAllowanceEntry(new ComboBox(), "Entry Type: ");
        communicationEntry.setWidth("150px");
        glayout.addComponent(communicationEntry, 1, 0);
        
        Button communicationBtn = new Button("UPDATE COMMUNICATION ALLOWANCE");
        communicationBtn.setWidth("250px");
        glayout.addComponent(communicationBtn, 2, 0);
        glayout.setComponentAlignment(communicationBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 1, 2, 1);
        
        perDiem = new TextField("Per Diem");
        perDiem.setWidth("170px");
        perDiem.addStyleName("numerical");
        glayout.addComponent(perDiem, 0, 2);
        
        perDiemEntry = dropDown.populateEmploymentAllowanceEntry(new ComboBox(), "Entry Type: ");
        perDiemEntry.setWidth("150px");
        glayout.addComponent(perDiemEntry, 1, 2);
        
        Button perDiemBtn = new Button("UPDATE PER DIEM");
        perDiemBtn.setWidth("250px");
        glayout.addComponent(perDiemBtn, 2, 2);
        glayout.setComponentAlignment(perDiemBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 3, 2, 3);
        
        cola = new TextField("Cola");
        cola.setWidth("170px");
        cola.addStyleName("numerical");
        glayout.addComponent(cola, 0, 4);
        
        colaEntry = dropDown.populateEmploymentAllowanceEntry(new ComboBox(), "Entry Type: ");
        colaEntry.setWidth("150px");
        glayout.addComponent(colaEntry, 1, 4);
        
        Button colaBtn = new Button("UPDATE COLA");
        colaBtn.setWidth("250px");
        glayout.addComponent(colaBtn, 2, 4);
        glayout.setComponentAlignment(colaBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 5, 2, 5);
        
        meal = new TextField("Meal Allowance");
        meal.setWidth("170px");
        meal.addStyleName("numerical");
        glayout.addComponent(meal, 0, 6);
        
        mealEntry = dropDown.populateEmploymentAllowanceEntry(new ComboBox(), "Entry Type: ");
        mealEntry.setWidth("150px");
        glayout.addComponent(mealEntry, 1, 6);
        
        Button mealBtn = new Button("UPDATE MEAL ALLOWANCE");
        mealBtn.setWidth("250px");
        glayout.addComponent(mealBtn, 2, 6);
        glayout.setComponentAlignment(mealBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 7, 2, 7);
        
        transportation = new TextField("Transportation Allowance");
        transportation.setWidth("170px");
        transportation.addStyleName("numerical");
        glayout.addComponent(transportation, 0, 8);
        
        transportationEntry = dropDown.populateEmploymentAllowanceEntry(new ComboBox(), "Entry Type: ");
        transportationEntry.setWidth("150px");
        glayout.addComponent(transportationEntry, 1, 8);
        
        Button transportationBtn = new Button("UPDATE TRANSPORTATION ALLOWANCE");
        transportationBtn.setWidth("250px");
        glayout.addComponent(transportationBtn, 2, 8);
        glayout.setComponentAlignment(transportationBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 9, 2, 9);
        
        other = new TextField("Other Allowances");
        other.setWidth("170px");
        other.addStyleName("numerical");
        glayout.addComponent(other, 0, 10);
        
        otherEntry = dropDown.populateEmploymentAllowanceEntry(new ComboBox(), "Entry Type: ");
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
    
    Button.ClickListener buttonClickListner = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            switch(event.getButton().getCaption()){
                case "UPDATE COMMUNICATION ALLOWANCE" : {
                    break;
                }
                
                case "UPDATE PER DIEM" : {
                    break;
                }
                
                case "UPDATE COLA" : {
                    break;
                }
                
                case "UPDATE MEAL ALLOWANCE" : {
                    break;
                }
                
                case "UPDATE TRANSPORTATION ALLOWANCE" : {
                    break;
                }
                
                default : {
                    break;
                }
            }
        }
    };
}
