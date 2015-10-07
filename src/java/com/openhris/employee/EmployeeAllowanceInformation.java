/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee;

import com.hrms.classes.GlobalVariables;
import com.openhris.commons.Constant;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.Allowances;
import com.openhris.service.AllowanceInformationService;
import com.openhris.service.SalaryInformationService;
import com.openhris.serviceprovider.AllowanceInformationServiceImpl;
import com.openhris.serviceprovider.SalaryInformationServiceImpl;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author jetdario
 */
public class EmployeeAllowanceInformation extends VerticalLayout {

//    SalaryInformationService salaryInformationService = new SalaryInformationServiceImpl();
    AllowanceInformationService ais = new AllowanceInformationServiceImpl();
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
    TextField others;
    
    ComboBox communicationEntry;
    ComboBox perDiemEntry;
    ComboBox colaEntry;
    ComboBox mealEntry;
    ComboBox transportationEntry;
    ComboBox othersEntry;
    
    ComponentContainer container(){
        GridLayout glayout = new GridLayout(3, 12);
        glayout.setSpacing(true);          
        glayout.setWidth("600px");
	glayout.setHeight("100%");
                
        communication = new TextField("Communication Allowance");
        communication.setWidth("170px");
        communication.addStyleName("numerical");
        glayout.addComponent(communication, 0, 0);
        
        communicationEntry = dropDown.populateEmploymentAllowanceEntry("Entry Type: ");
        communicationEntry.setWidth("150px");
        glayout.addComponent(communicationEntry, 1, 0);
        
        Button communicationBtn = new Button("UPDATE COMMUNICATION ALLOWANCE");
        communicationBtn.setWidth("250px");
        communicationBtn.addListener(buttonClickListner);
        glayout.addComponent(communicationBtn, 2, 0);
        glayout.setComponentAlignment(communicationBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 1, 2, 1);
        
        perDiem = new TextField("Per Diem");
        perDiem.setWidth("170px");
        perDiem.addStyleName("numerical");
        glayout.addComponent(perDiem, 0, 2);
        
        perDiemEntry = dropDown.populateEmploymentAllowanceEntry("Entry Type: ");
        perDiemEntry.setWidth("150px");
        glayout.addComponent(perDiemEntry, 1, 2);
        
        Button perDiemBtn = new Button("UPDATE PER DIEM");
        perDiemBtn.setWidth("250px");
        perDiemBtn.addListener(buttonClickListner);
        glayout.addComponent(perDiemBtn, 2, 2);
        glayout.setComponentAlignment(perDiemBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 3, 2, 3);
        
        cola = new TextField("Cola");
        cola.setWidth("170px");
        cola.addStyleName("numerical");
        glayout.addComponent(cola, 0, 4);
        
        colaEntry = dropDown.populateEmploymentAllowanceEntry("Entry Type: ");
        colaEntry.setWidth("150px");
        glayout.addComponent(colaEntry, 1, 4);
        
        Button colaBtn = new Button("UPDATE COLA");
        colaBtn.setWidth("250px");
        colaBtn.addListener(buttonClickListner);
        glayout.addComponent(colaBtn, 2, 4);
        glayout.setComponentAlignment(colaBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 5, 2, 5);
        
        meal = new TextField("Meal Allowance");
        meal.setWidth("170px");
        meal.addStyleName("numerical");
        glayout.addComponent(meal, 0, 6);
        
        mealEntry = dropDown.populateEmploymentAllowanceEntry("Entry Type: ");
        mealEntry.setWidth("150px");
        glayout.addComponent(mealEntry, 1, 6);
        
        Button mealBtn = new Button("UPDATE MEAL ALLOWANCE");
        mealBtn.setWidth("250px");
        mealBtn.addListener(buttonClickListner);
        glayout.addComponent(mealBtn, 2, 6);
        glayout.setComponentAlignment(mealBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 7, 2, 7);
        
        transportation = new TextField("Transportation Allowance");
        transportation.setWidth("170px");
        transportation.addStyleName("numerical");
        glayout.addComponent(transportation, 0, 8);
        
        transportationEntry = dropDown.populateEmploymentAllowanceEntry("Entry Type: ");
        transportationEntry.setWidth("150px");
        glayout.addComponent(transportationEntry, 1, 8);
        
        Button transportationBtn = new Button("UPDATE TRANSPORTATION ALLOWANCE");
        transportationBtn.setWidth("250px");
        transportationBtn.addListener(buttonClickListner);
        glayout.addComponent(transportationBtn, 2, 8);
        glayout.setComponentAlignment(transportationBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 9, 2, 9);
        
        others = new TextField("Other Allowances");
        others.setWidth("170px");
        others.addStyleName("numerical");
        glayout.addComponent(others, 0, 10);
        
        othersEntry = dropDown.populateEmploymentAllowanceEntry("Entry Type: ");
        othersEntry.setWidth("150px");
        glayout.addComponent(othersEntry, 1, 10);
        
        Button othersBtn = new Button("UPDATE OTHER ALLOWANCES");
        othersBtn.setWidth("250px");
        othersBtn.addListener(buttonClickListner);
        glayout.addComponent(othersBtn, 2, 10);
        glayout.setComponentAlignment(othersBtn, Alignment.BOTTOM_LEFT);
        
        if(getEmployeeId() != null){
            Allowances a = ais.getAllowancesByEmployee(getEmployeeId());
            
            communication.setValue(a.getCommunication());
            communicationEntry.setValue(Constant.getKeyByValue(Constant.MAP_CONSTANT_EMPLOYMENT_ALLOWANCE_ENTRY, a.getComEntryType()));
            
            perDiem.setValue(a.getPerDiem());
            perDiemEntry.setValue(Constant.getKeyByValue(Constant.MAP_CONSTANT_EMPLOYMENT_ALLOWANCE_ENTRY, a.getPerDiemEntryType()));
            
            cola.setValue(a.getCola());
            colaEntry.setValue(Constant.getKeyByValue(Constant.MAP_CONSTANT_EMPLOYMENT_ALLOWANCE_ENTRY, a.getColaEntryType()));
            
            meal.setValue(a.getMeal());
            mealEntry.setValue(Constant.getKeyByValue(Constant.MAP_CONSTANT_EMPLOYMENT_ALLOWANCE_ENTRY, a.getMealEntryType()));
            
            transportation.setValue(a.getTransportation());
            transportationEntry.setValue(Constant.getKeyByValue(Constant.MAP_CONSTANT_EMPLOYMENT_ALLOWANCE_ENTRY, a.getTransEntryType()));
            
            others.setValue(a.getOthers());
            System.out.println("others: "+a.getOthers());
            othersEntry.setValue(Constant.getKeyByValue(Constant.MAP_CONSTANT_EMPLOYMENT_ALLOWANCE_ENTRY, a.getOthersEntryType()));
        }        
        
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
                    if(communication.getValue() == null || communication.getValue().toString().trim().isEmpty()){
                        getWindow().showNotification("Enter Amount for Communication Allowance!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    } else {
                        if(!util.checkInputIfInteger(communication.getValue().toString().trim())){
                            getWindow().showNotification("Enter a Numeric Amount!", Window.Notification.TYPE_ERROR_MESSAGE);
                            return;
                        } else {
                            if(util.convertStringToDouble(communication.getValue().toString()) > 0){
                                if(communicationEntry.getItem(communicationEntry.getValue()).toString().equals("N/A")){
                                    getWindow().showNotification("Select an Entry Type!", Window.Notification.TYPE_WARNING_MESSAGE);
                                    return;
                                }
                            }
                        }
                    }
                    
                    boolean result = ais.updateAllowance("Communication", 
                            util.convertStringToDouble(communication.getValue().toString().trim()), 
                            "CommunicationEntryType", 
                            communicationEntry.getItem(communicationEntry.getValue()).toString(), 
                            getEmployeeId());
                    if(result){
                        getWindow().showNotification("Update Communication Allowance!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    }
                    
                    break;
                }
                
                case "UPDATE PER DIEM" : {
                    if(perDiem.getValue() == null || perDiem.getValue().toString().trim().isEmpty()){
                        getWindow().showNotification("Enter Amount for Per Diem Allowance!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    } else {
                        if(!util.checkInputIfInteger(perDiem.getValue().toString().trim())){
                            getWindow().showNotification("Enter a Numeric Amount!", Window.Notification.TYPE_ERROR_MESSAGE);
                            return;
                        } else {
                            if(util.convertStringToDouble(perDiem.getValue().toString()) > 0){
                                if(perDiemEntry.getItem(perDiemEntry.getValue()).toString().equals("N/A")){
                                    getWindow().showNotification("Select an Entry Type!", Window.Notification.TYPE_WARNING_MESSAGE);
                                    return;
                                }
                            }
                        }
                    }
                    
                    boolean result = ais.updateAllowance("PerDiem", 
                            util.convertStringToDouble(perDiem.getValue().toString().trim()), 
                            "PerDiemEntryType", 
                            perDiemEntry.getItem(perDiemEntry.getValue()).toString(), 
                            getEmployeeId());
                    if(result){
                        getWindow().showNotification("Update Per Diem Allowance!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    }
                    
                    break;
                }
                
                case "UPDATE COLA" : {
                    if(cola.getValue() == null || cola.getValue().toString().trim().isEmpty()){
                        getWindow().showNotification("Enter Amount for COLA!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    } else {
                        if(!util.checkInputIfInteger(cola.getValue().toString().trim())){
                            getWindow().showNotification("Enter a Numeric Amount!", Window.Notification.TYPE_ERROR_MESSAGE);
                            return;
                        } else {
                            if(util.convertStringToDouble(cola.getValue().toString()) > 0){
                                if(colaEntry.getItem(colaEntry.getValue()).toString().equals("N/A")){
                                    getWindow().showNotification("Select an Entry Type!", Window.Notification.TYPE_WARNING_MESSAGE);
                                    return;
                                }
                            }
                        }
                    }
                    
                    boolean result = ais.updateAllowance("Cola", 
                            util.convertStringToDouble(cola.getValue().toString().trim()), 
                            "ColaEntryType", 
                            colaEntry.getItem(colaEntry.getValue()).toString(), 
                            getEmployeeId());
                    if(result){
                        getWindow().showNotification("Update COLA!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    }
                    
                    break;
                }
                
                case "UPDATE MEAL ALLOWANCE" : {
                    if(meal.getValue() == null || meal.getValue().toString().trim().isEmpty()){
                        getWindow().showNotification("Enter Amount for Meal Allowance!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    } else {
                        if(!util.checkInputIfInteger(meal.getValue().toString().trim())){
                            getWindow().showNotification("Enter a Numeric Amount!", Window.Notification.TYPE_ERROR_MESSAGE);
                            return;
                        } else {
                            if(util.convertStringToDouble(meal.getValue().toString()) > 0){
                                if(mealEntry.getItem(mealEntry.getValue()).toString().equals("N/A")){
                                    getWindow().showNotification("Select an Entry Type!", Window.Notification.TYPE_WARNING_MESSAGE);
                                    return;
                                }
                            }
                        }
                    }
                    
                    boolean result = ais.updateAllowance("Meal", 
                            util.convertStringToDouble(meal.getValue().toString().trim()), 
                            "MealEntryType", 
                            mealEntry.getItem(mealEntry.getValue()).toString(), 
                            getEmployeeId());
                    if(result){
                        getWindow().showNotification("Update Meal Allowance!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    }
                    
                    break;
                }
                
                case "UPDATE TRANSPORTATION ALLOWANCE" : {
                    if(transportation.getValue() == null || transportation.getValue().toString().trim().isEmpty()){
                        getWindow().showNotification("Enter Amount for Transportation Allowance!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    } else {
                        if(!util.checkInputIfInteger(transportation.getValue().toString().trim())){
                            getWindow().showNotification("Enter a Numeric Amount!", Window.Notification.TYPE_ERROR_MESSAGE);
                            return;
                        } else {
                            if(util.convertStringToDouble(transportation.getValue().toString()) > 0){
                                if(transportationEntry.getItem(transportationEntry.getValue()).toString().equals("N/A")){
                                    getWindow().showNotification("Select an Entry Type!", Window.Notification.TYPE_WARNING_MESSAGE);
                                    return;
                                }
                            }
                        }
                    }
                    
                    boolean result = ais.updateAllowance("Transportation", 
                            util.convertStringToDouble(transportation.getValue().toString().trim()), 
                            "TransportationEntryType", 
                            transportationEntry.getItem(transportationEntry.getValue()).toString(), 
                            getEmployeeId());
                    if(result){
                        getWindow().showNotification("Update Transportation Allowance!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    }
                    
                    break;
                }
                
                default : {
                    if(others.getValue() == null || others.getValue().toString().trim().isEmpty()){
                        getWindow().showNotification("Enter Amount for Other Allowances!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    } else {
                        if(!util.checkInputIfInteger(others.getValue().toString().trim())){
                            getWindow().showNotification("Enter a Numeric Amount!", Window.Notification.TYPE_ERROR_MESSAGE);
                            return;
                        } else {
                            if(util.convertStringToDouble(others.getValue().toString()) > 0){
                                if(othersEntry.getItem(othersEntry.getValue()).toString().equals("N/A")){
                                    getWindow().showNotification("Select an Entry Type!", Window.Notification.TYPE_WARNING_MESSAGE);
                                    return;
                                }
                            }
                        }
                    }
                    
                    boolean result = ais.updateAllowance("Others", 
                            util.convertStringToDouble(others.getValue().toString().trim()), 
                            "OthersEntryType", 
                            othersEntry.getItem(othersEntry.getValue()).toString(), 
                            getEmployeeId());
                    if(result){
                        getWindow().showNotification("Update Other Allowances!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    }
                    
                    break;
                }
            }
        }
    };
}
