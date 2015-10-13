/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.salary;

import com.hrms.classes.GlobalVariables;
import com.openhris.commons.Constant;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.HRISTextField;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.service.CompanyService;
import com.openhris.serviceprovider.CompanyServiceImpl;
import com.openhris.model.EmploymentInformation;
import com.openhris.service.SalaryInformationService;
import com.openhris.serviceprovider.SalaryInformationServiceImpl;
import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author jetdario
 */
public class EmployeeSalaryInformation extends VerticalLayout{
    
    SalaryInformationService salaryInformationService = new SalaryInformationServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    DropDownComponent dropDown = new DropDownComponent();
    CompanyService companyService = new CompanyServiceImpl();
    
    ComboBox corporate = new ComboBox("Corporate: ");
    ComboBox trade = new ComboBox("Trade: ");
    ComboBox branch = new ComboBox("Branch: ");
      
    String employeeId;
    int corporateId;
    int tradeId;
    int branchId;
    boolean userRoleResult = false;
    
    public EmployeeSalaryInformation(){        
    }
    
    public EmployeeSalaryInformation(String employeeId){
        this.employeeId = employeeId;
        
        if(GlobalVariables.getUserRole().equals("hr")){
            userRoleResult = true;
        }
        
        init();
        addComponent(layout());
        addComponent(new Label("<br />", Label.CONTENT_XHTML));
        addComponent(new Label("<HR>", Label.CONTENT_XHTML));
        addComponent(new Label("<br />", Label.CONTENT_XHTML));
        addComponent(layout2());
    }
    
    public void init(){
        setSpacing(true);
	setMargin(true);
	setSizeFull();
	setImmediate(true);
    }
    
    ComboBox employmentStatus;
    ComboBox employmentWageStatus;
    ComboBox employmentWageEntry;
    TextField employmentWage;
    
    public ComponentContainer layout(){        
        GridLayout glayout = new GridLayout(2, 8);
        glayout.setSpacing(true);          
        glayout.setWidth("600px");
	glayout.setHeight("100%");
        
        employmentStatus = dropDown.populateEmploymentStatus("Employment Status: ");
        employmentStatus.setWidth("200px");
        glayout.addComponent(employmentStatus, 0, 0);
        
        Button esBtn = new Button("UPDATE EMPLOYMENT STATUS");
        esBtn.setWidth("100%");
        glayout.addComponent(esBtn, 1, 0);
        glayout.setColumnExpandRatio(1, 1);
        glayout.setComponentAlignment(esBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 2, 1, 2);
        
        employmentWageStatus = dropDown.populateEmploymentWageStatus("Employment Wage Status: ");
        employmentWageStatus.setWidth("200px");
        glayout.addComponent(employmentWageStatus, 0, 3);
        
        Button ewsBtn = new Button("UPDATE EMPLOYMENT WAGE STATUS");
        ewsBtn.setWidth("100%");
        glayout.addComponent(ewsBtn, 1, 3);
        glayout.setComponentAlignment(ewsBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 4, 1, 4);
        
        employmentWageEntry = dropDown.populateEmploymentWageEntry("Employment Wage Entry: ");
        employmentWageEntry.setWidth("200px");
        glayout.addComponent(employmentWageEntry, 0, 5);
        
        Button eweBtn = new Button("UPDATE EMPLOYMENT WAGE ENTRY");
        eweBtn.setWidth("100%");
        glayout.addComponent(eweBtn, 1, 5);
        glayout.setComponentAlignment(eweBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 6, 1, 6);
        
        employmentWage = new TextField("Employment Wage");
        employmentWage.setWidth("200px");
        employmentWage.addStyleName("numerical");
        glayout.addComponent(employmentWage, 0, 7);
                
        Button ewBtn = new Button("UPDATE EMPLOYMENT WAGE");
        ewBtn.setWidth("100%");
        glayout.addComponent(ewBtn, 1, 7);
        glayout.setComponentAlignment(ewBtn, Alignment.BOTTOM_LEFT);
                                        
        if(getEmployeeId() != null){
            EmploymentInformation ei = salaryInformationService.getEmployeeSalaryInformation(getEmployeeId());
            
            employmentStatus.setValue(Constant.getKeyByValue(Constant.MAP_CONSTANT_EMPLOYMENT_STATUS, ei.getEmploymentStatus()));
            employmentWageStatus.setValue(Constant.getKeyByValue(Constant.MAP_CONSTANT_EMPLOYMENT_WAGE_STATUS, ei.getEmploymentWageStatus()));
            employmentWageEntry.setValue(Constant.getKeyByValue(Constant.MAP_CONSTANT_EMPLOYMENT_WAGE_ENTRY, ei.getEmploymentWageEntry()));            
            employmentWage.setValue(ei.getEmploymentWage());                        
        }
        
        
        return glayout;
    }
    
    public ComponentContainer layout2(){
        GridLayout glayout = new GridLayout(3, 4);
        glayout.setSpacing(true);          
        glayout.setSizeFull();
        
        TextField employmentStatusField = new HRISTextField("Employment Status: ");
        glayout.addComponent(employmentStatusField, 0, 0);
                
        Button setContributionBtn = new Button("SET EMPLOYEE'S CONTRIBUTIONS MAIN BRANCH");
        setContributionBtn.setWidth("100%");
        setContributionBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                Window subWindow = setContributionMainBranch();
                if(subWindow.getParent() == null){
                    getWindow().addWindow(subWindow);
                }
                subWindow.setModal(true);
                subWindow.center();
            }
        });
        glayout.addComponent(setContributionBtn, 1, 0, 2, 0);
        glayout.setComponentAlignment(setContributionBtn, Alignment.BOTTOM_CENTER);
                                                    
        if(getEmployeeId() != null){
            EmploymentInformation employmentInformation = salaryInformationService.getEmployeeSalaryInformation(getEmployeeId());            
            employmentStatusField.setValue(employmentInformation.getCurrentStatus().toUpperCase());                       
        }
        employmentStatusField.setReadOnly(true);
                               
        return glayout;
    }
    
    private String getEmployeeId(){
        return employeeId;
    }
    
    private Window setContributionMainBranch(){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        
        final Window subWindow = new Window("Set Branch", vlayout);
        subWindow.setWidth("300px");
        
        corporate = dropDown.populateCorporateComboBox(new ComboBox());
        corporate.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(corporate.getValue() == null){                    
                } else {
                    corporateId = companyService.getCorporateId(corporate.getValue().toString());
                    trade = dropDown.populateTradeComboBox(trade, corporateId);
                }
            }
        });
        corporate.setWidth("100%");
        subWindow.addComponent(corporate);
                
        trade.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(trade.getValue() == null){                    
                } else {
                    tradeId = companyService.getTradeId(trade.getValue().toString(), corporateId);
                    branch = dropDown.populateBranchComboBox(branch, tradeId, corporateId);
                }                
            }
        });
        trade.setWidth("100%");
        subWindow.addComponent(trade);
        
        branch.setWidth("100%");
        branch.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(branch.getValue() == null){                    
                } else {
                    branchId = companyService.getBranchId(tradeId, branch.getValue().toString());
                }
            }
        });
        subWindow.addComponent(branch);
            
        final ComboBox remarks = new ComboBox("Remarks");
        remarks.setWidth("100%");
        remarks.setNullSelectionItemId(false);
        remarks.addItem("Transfer to new Branch.");
        remarks.addItem("Wrong Entry");
        subWindow.addComponent(remarks);
        
        Button updateBtn = new Button("SET BRANCH for CONTRIBUTION");
        updateBtn.setWidth("100%");
        updateBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                    if(corporate.getValue() == null){
                        getWindow().showNotification("Select Corporation!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                
                    if(trade.getValue() == null){
                        getWindow().showNotification("Select Trade!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                    
                    if(branch.getValue() == null){
                        getWindow().showNotification("Select Branch!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                    
                    if(remarks.getValue() == null){
                        getWindow().showNotification("Remarks required!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                    
                    boolean result = salaryInformationService.updateEmployeeContributionBranch(getEmployeeId(), branchId, remarks.getValue().toString());
                    if(result){
                        getWindow().showNotification("Successfully transferred to new Branch!");
                        (subWindow.getParent()).removeWindow(subWindow);
                    } else {
                        getWindow().showNotification("SQL Error, Contact your DBA!");
                        (subWindow.getParent()).removeWindow(subWindow);
                    }                           
            }
        });
        subWindow.addComponent(updateBtn);
        
        return subWindow;
    }     
    
    private Window editDateEntryWindow(final String entryDate){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setWidth("100%");
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        
        final Window window = new Window("EDIT DATE ENTRY", vlayout);
        window.setResizable(false);
        
        Button editBtn = new Button("EDIT DATE ENTRY?");
        editBtn.setWidth("100%");
        editBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean result = salaryInformationService.editEmploymentDateEntry(getEmployeeId(), entryDate);
                if(result){
                    getWindow().showNotification("Update Entry Date.", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    (window.getParent()).removeWindow(window);
                }
            }
        });
        window.addComponent(editBtn);
        
        return window;
    }    
}
