/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee;

import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.employee.model.EmploymentInformation;
import com.openhris.employee.service.SalaryInformationService;
import com.openhris.employee.serviceprovider.SalaryInformationServiceImpl;
import com.vaadin.data.Container;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author jetdario
 */
public class EmployeeSalaryInformation extends VerticalLayout{
    
    SalaryInformationService siService = new SalaryInformationServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    DropDownComponent dropDown = new DropDownComponent();
    
    GridLayout glayout;    
    String employeeId;
    
    public EmployeeSalaryInformation(){        
    }
    
    public EmployeeSalaryInformation(String employeeId){
        this.employeeId = employeeId;
        
        init();
        addComponent(layout());
    }
    
    public void init(){
        setSpacing(true);
	setMargin(true);
	setSizeFull();
	setImmediate(true);
    }
    
    public ComponentContainer layout(){
        glayout = new GridLayout(3, 4);
        glayout.setSpacing(true);          
        glayout.setWidth("100%");
	glayout.setHeight("100%");
        
        final ComboBox employmentStatus = dropDown.populateEmploymentStatus(new ComboBox());
        employmentStatus.setWidth("200px");
        glayout.addComponent(employmentStatus, 0, 0);
        
        final ComboBox employmentWageStatus = dropDown.populateEmploymentWageStatus(new ComboBox());
        employmentWageStatus.setWidth("200px");
        glayout.addComponent(employmentWageStatus, 1, 0);
        
        final ComboBox employmentWageEntry = dropDown.populateEmploymentWageEntry(new ComboBox());
        employmentWageEntry.setWidth("200px");
        glayout.addComponent(employmentWageEntry, 2, 0);
        
        final TextField employmentWage = new TextField("Employment Wage");
        employmentWage.setWidth("200px");
        glayout.addComponent(employmentWage, 0, 1);
        
        final TextField employmentAllowance = new TextField("Allowance");
        employmentAllowance.setWidth("200px");
        glayout.addComponent(employmentAllowance, 1, 1);
        
        final ComboBox employmentAllowanceEntry = dropDown.populateEmploymentAllowanceEntry(new ComboBox());
        employmentAllowanceEntry.setWidth("200px");
        glayout.addComponent(employmentAllowanceEntry, 2, 1);
        
        final TextField sssField = new TextField("SSS: ");
        sssField.setWidth("200px");
        glayout.addComponent(sssField, 0, 2);
        
        final TextField phicField = new TextField("Philhealth");
        phicField.setWidth("200px");
        glayout.addComponent(phicField, 1, 2);
        
        final TextField hdmfField = new TextField("HDMF: ");
        hdmfField.setWidth("200px");
        glayout.addComponent(hdmfField, 2, 2);
        
        final TextField tinField = new TextField("TIN: ");
        tinField.setWidth("200px");
        glayout.addComponent(tinField, 0, 3);
        
        Button updateBtn = new Button("UPDATE SALARY");
        updateBtn.setWidth("100%");
        updateBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(employmentWage.getValue() == null || employmentWage.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("NULL/Empty for Employment Wage is not Allowed!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                } else {
                    boolean checkValueIfDouble = util.checkInputIfDouble(employmentWage.getValue().toString().trim());
                    if(!checkValueIfDouble){
                        getWindow().showNotification("Enter a numeric format for Employment Wage!");
                        return;
                    }
                }
                
                if(employmentAllowance.getValue() == null || employmentAllowance.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("NULL/Empty for Employment Allowance is not Allowed!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                } else {
                    boolean checkValueIfDouble = util.checkInputIfDouble(employmentAllowance.getValue().toString().trim());
                    if(!checkValueIfDouble){
                        getWindow().showNotification("Enter a numeric format for Employment Allowance!");
                        return;
                    }
                }
                
                EmploymentInformation employmentInformation = new EmploymentInformation();
                
                if(util.checkInputIfInteger(employmentStatus.getValue().toString())){
                    employmentInformation.setEmploymentStatus(employmentStatus.getItemCaption(employmentStatus.getValue()));
                } else {
                    employmentInformation.setEmploymentStatus(employmentStatus.getValue().toString());
                }
                
                if(util.checkInputIfInteger(employmentWageStatus.getValue().toString())){
                    employmentInformation.setEmploymentWageStatus(employmentWageStatus.getItemCaption(employmentWageStatus.getValue()));
                } else {
                    employmentInformation.setEmploymentWageStatus(employmentWageStatus.getValue().toString());
                }
                
                if(util.checkInputIfInteger(employmentWageEntry.getValue().toString())){
                    employmentInformation.setEmploymentWageEntry(employmentWageEntry.getItemCaption(employmentWageEntry.getValue()));
                } else {
                    employmentInformation.setEmploymentWageEntry(employmentWageEntry.getValue().toString());
                }
                
                employmentInformation.setEmploymentWage(util.convertStringToDouble(employmentWage.getValue().toString().trim()));
                employmentInformation.setAllowance(util.convertStringToDouble(employmentAllowance.getValue().toString().trim()));
                
                if(util.checkInputIfInteger(employmentAllowanceEntry.getValue().toString())){
                    employmentInformation.setAllowanceEntry(employmentAllowanceEntry.getItemCaption(employmentAllowanceEntry.getValue()));
                } else {
                    employmentInformation.setAllowanceEntry(employmentAllowanceEntry.getValue().toString());
                }
                
                employmentInformation.setSssNo((sssField.getValue() == null) ? "N/A" : sssField.getValue().toString().trim());
                employmentInformation.setPhicNo((phicField.getValue() == null) ? "N/A" : phicField.getValue().toString().trim());
                employmentInformation.setHdmfNo((hdmfField.getValue() == null) ? "N/A" : hdmfField.getValue().toString().trim());
                employmentInformation.setTinNo((tinField.getValue() == null) ? "N/A" : tinField.getValue().toString().trim());
                
                boolean result = siService.updateEmployeeSalaryInformation(getEmployeeId(), employmentInformation);
                if(result){
                    getWindow().showNotification("Update Employment Salary Information!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                } else {
                    getWindow().showNotification("Cannot Update Employment Salary Information, Contact your DBA!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
        glayout.addComponent(updateBtn, 1, 3, 2, 3);
        glayout.setComponentAlignment(updateBtn, Alignment.BOTTOM_CENTER);
        
        if(employeeId != null){
            EmploymentInformation employmentInformation = siService.getEmployeeSalaryInformation(getEmployeeId());
            
            Object employmentStatusId = employmentStatus.addItem();
            employmentStatus.setItemCaption(employmentStatusId, employmentInformation.getEmploymentStatus());
            employmentStatus.setValue(employmentStatusId);
            
            Object employmentWageStatusId = employmentWageStatus.addItem();
            employmentWageStatus.setItemCaption(employmentWageStatusId, employmentInformation.getEmploymentWageStatus());
            employmentWageStatus.setValue(employmentWageStatusId);
            
            Object employmentWageEntryId = employmentWageEntry.addItem();
            employmentWageEntry.setItemCaption(employmentWageEntryId, employmentInformation.getEmploymentWageEntry());
            employmentWageEntry.setValue(employmentWageEntryId);
            
            employmentWage.setValue(employmentInformation.getEmploymentWage());
            employmentAllowance.setValue(employmentInformation.getAllowance());
            
            Object employmentAllowanceEntryId = employmentAllowanceEntry.addItem();
            employmentAllowanceEntry.setItemCaption(employmentAllowanceEntryId, employmentInformation.getAllowanceEntry());
            employmentAllowanceEntry.setValue(employmentAllowanceEntryId);
            
            sssField.setValue(employmentInformation.getSssNo());
            phicField.setValue(employmentInformation.getPhicNo());
            hdmfField.setValue(employmentInformation.gethdmfNo());
            tinField.setValue(employmentInformation.getTinNo());
        }
        
        return glayout;
    }
    
    private String getEmployeeId(){
        return employeeId;
    }
    
}
