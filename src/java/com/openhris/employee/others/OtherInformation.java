/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee.others;

import com.hrms.classes.GlobalVariables;
import com.openhris.commons.Constant;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.HRISTextField;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.EmploymentInformation;
import com.openhris.service.SalaryInformationService;
import com.openhris.serviceprovider.SalaryInformationServiceImpl;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author jetdario
 */
public class OtherInformation extends VerticalLayout {

    SalaryInformationService si = new SalaryInformationServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
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
    
    TextField tinField;
    TextField sssField;
    TextField phicField;
    TextField hdmfField;
    TextField bankAccountNo;  
    ComboBox employeeDependent;
    TextArea remarks;
    Window remarksSubWindow;
    
    Component component(){
        GridLayout glayout = new GridLayout(2, 13);
        glayout.setSpacing(true);          
        glayout.setWidth("600px");
	glayout.setHeight("100%");
        
        tinField = new HRISTextField("TIN: ");
        glayout.addComponent(tinField, 0, 0);        
        
        Button tinBtn = new Button("UPDATE TIN NO.");
        tinBtn.setWidth("100%");
        tinBtn.addListener(buttonClickListener);
        glayout.addComponent(tinBtn, 1, 0);
        glayout.setComponentAlignment(tinBtn, Alignment.BOTTOM_CENTER);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 1, 1, 1);
        
        employeeDependent = dropDown.populateTotalDependent("Employee's Dependent");
        employeeDependent.setWidth("100%");
        glayout.addComponent(employeeDependent, 0, 2);
        
        Button edBtn = new Button("UPDATE EMPLOYEE's DEPENDENT");
        edBtn.setWidth("100%");
        edBtn.addListener(buttonClickListener);
        glayout.addComponent(edBtn, 1, 2);
        glayout.setComponentAlignment(edBtn, Alignment.BOTTOM_CENTER);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 3, 1, 3);
        
        sssField = new HRISTextField("SSS: ");
        glayout.addComponent(sssField, 0, 4);
        
        Button sssbtn = new Button("UPDATE SSS NO.");
        sssbtn.setWidth("100%");
        sssbtn.addListener(buttonClickListener);
        glayout.addComponent(sssbtn, 1, 4);
        glayout.setComponentAlignment(sssbtn, Alignment.BOTTOM_CENTER);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 5, 1, 5);
        
        phicField = new HRISTextField("Philhealth");
        glayout.addComponent(phicField, 0, 6);
        
        Button phicBtn = new Button("UPDATE PHIC NO.");
        phicBtn.setWidth("100%");
        phicBtn.addListener(buttonClickListener);
        glayout.addComponent(phicBtn, 1, 6);
        glayout.setComponentAlignment(phicBtn, Alignment.BOTTOM_CENTER);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 7, 1, 7);
        
        hdmfField = new HRISTextField("HDMF: ");
        glayout.addComponent(hdmfField, 0, 8);
        
        Button hdmfBtn = new Button("UPDATE HDMF NO.");
        hdmfBtn.setWidth("100%");
        hdmfBtn.addListener(buttonClickListener);
        glayout.addComponent(hdmfBtn, 1, 8);
        glayout.setComponentAlignment(hdmfBtn, Alignment.BOTTOM_CENTER);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 9, 1, 9);
                
        bankAccountNo = new HRISTextField("Bank Account No: ");
        bankAccountNo.setImmediate(true);
        glayout.addComponent(bankAccountNo, 0, 10);
        
        Button bankAccountBtn = new Button("UPDATE BANK NO.");
        bankAccountBtn.setWidth("100%");
        bankAccountBtn.addListener(buttonClickListener);
        glayout.addComponent(bankAccountBtn, 1, 10);
        glayout.setComponentAlignment(bankAccountBtn, Alignment.BOTTOM_CENTER);

        if(getEmployeeId() != null){
            EmploymentInformation ei = si.getEmployeeSalaryInformation(getEmployeeId());
            
            tinField.setValue(ei.getTinNo());
            sssField.setValue(ei.getSssNo());
            phicField.setValue(ei.getPhicNo());
            hdmfField.setValue(ei.gethdmfNo());
            bankAccountNo.setValue(ei.getBankAccountNo());
//            employmentStatusField.setValue(ei.getEmploymentStatus());
            employeeDependent.setValue(Constant.getKeyByValue(Constant.MAP_CONSTANT_DEPENDENT, ei.getTotalDependent()));
        }
        
        return glayout;
    }
        
    String getEmployeeId(){
        return employeeId;
    }
    
    Button.ClickListener updateClickListener = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            if(remarks.getValue() == null || remarks.getValue().toString().trim().isEmpty()){
                getWindow().showNotification("Add Remarks!", Window.Notification.TYPE_ERROR_MESSAGE);
                return;
            }
            
            switch(event.getButton().getCaption()){
                case "UPDATE TIN NO." : {
                    if(tinField.getValue() == null || tinField.getValue().toString().trim().isEmpty()){
                        (remarksSubWindow.getParent()).removeWindow(remarksSubWindow);
                        return;
                    }
                    
                    boolean result = si.updateEmploymentWageDetails("tinNo", 
                            tinField.getValue().toString().trim(), 
                            getEmployeeId(), 
                            remarks.getValue().toString());
                    
                    if(result){
                        getWindow().showNotification("Update TIN No!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        (remarksSubWindow.getParent()).removeWindow(remarksSubWindow);
                    }
                    
                    break;
                }
                
                case "UPDATE EMPLOYEE's DEPENDENT" : {
                    boolean result = si.updateEmploymentWageDetails("totalDependent", 
                            employeeDependent.getItem(employeeDependent.getValue()).toString(), 
                            getEmployeeId(), 
                            remarks.getValue().toString());
                    
                    if(result){
                        getWindow().showNotification("Update Employee's Dependent!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        (remarksSubWindow.getParent()).removeWindow(remarksSubWindow);
                    }
                    
                    break;
                }
                
                case "UPDATE SSS NO." : {
                    if(sssField.getValue() == null || sssField.getValue().toString().trim().isEmpty()){
                        (remarksSubWindow.getParent()).removeWindow(remarksSubWindow);
                        return;
                    }
                    
                    boolean result = si.updateEmploymentWageDetails("sssNo", 
                            sssField.getValue().toString().trim(), 
                            getEmployeeId(), 
                            remarks.getValue().toString());
                    
                    if(result){
                        getWindow().showNotification("Update SSS No.!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        (remarksSubWindow.getParent()).removeWindow(remarksSubWindow);
                    }
                    
                    break;
                }
                
                case "UPDATE PHIC NO." : {
                    if(phicField.getValue() == null || phicField.getValue().toString().trim().isEmpty()){
                        (remarksSubWindow.getParent()).removeWindow(remarksSubWindow);
                        return;
                    }
                    
                    boolean result = si.updateEmploymentWageDetails("phicNo", 
                            phicField.getValue().toString().trim(), 
                            getEmployeeId(), 
                            remarks.getValue().toString());
                    
                    if(result){
                        getWindow().showNotification("Update PHIC No.!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        (remarksSubWindow.getParent()).removeWindow(remarksSubWindow);
                    }
                    
                    break;
                }
                
                case "UPDATE HDMF NO." : {
                    if(hdmfField.getValue() == null || hdmfField.getValue().toString().trim().isEmpty()){
                        (remarksSubWindow.getParent()).removeWindow(remarksSubWindow);
                        return;
                    }
                    
                    boolean result = si.updateEmploymentWageDetails("hdmfNo", 
                            hdmfField.getValue().toString().trim(), 
                            getEmployeeId(), 
                            remarks.getValue().toString());
                    
                    if(result){
                        getWindow().showNotification("Update HDMF No.!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        (remarksSubWindow.getParent()).removeWindow(remarksSubWindow);
                    }
                    
                    break;
                }
                
                default : {
                    if(bankAccountNo.getValue() == null || bankAccountNo.getValue().toString().trim().isEmpty()){
                        (remarksSubWindow.getParent()).removeWindow(remarksSubWindow);
                        return;
                    }
                    
                    boolean result = si.updateEmploymentWageDetails("bankAccountNo", 
                            bankAccountNo.getValue().toString().trim(), 
                            getEmployeeId(), 
                            remarks.getValue().toString());
                    
                    if(result){
                        getWindow().showNotification("Update BANK Account No.!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        (remarksSubWindow.getParent()).removeWindow(remarksSubWindow);
                    }
                    
                    break;
                }
            }
        }
    };
    
    Button.ClickListener buttonClickListener = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            Window sub = remarks(event.getButton().getCaption());
            if(sub.getParent() == null){
                getWindow().addWindow(sub);
            }
        }
    };
    
    Window remarks(final String buttonCaption){
        VerticalLayout v = new VerticalLayout();
        v.setWidth("100%");
        v.setMargin(true);
        v.setSpacing(true);        
        
        remarksSubWindow = new Window("REMARKS", v);
        remarksSubWindow.setWidth("400px");
        remarksSubWindow.setModal(true);
        remarksSubWindow.center();
        
        remarks = new TextArea("Add Remarks: ");
        remarks.setWidth("100%");
        remarks.setRows(3);
        remarksSubWindow.addComponent(remarks);
        
        Button b = new Button(buttonCaption);
        b.setWidth("100%");
        b.addListener(updateClickListener);
        remarksSubWindow.addComponent(b);
        
        return remarksSubWindow;
    }
}
