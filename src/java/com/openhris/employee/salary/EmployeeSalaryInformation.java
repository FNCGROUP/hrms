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
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
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
public class EmployeeSalaryInformation extends VerticalLayout{
    
    SalaryInformationService si = new SalaryInformationServiceImpl();
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
    TextArea remarks;
    Window remarksSubWindow;
    
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
        esBtn.addListener(buttonClickListener);
        glayout.setColumnExpandRatio(1, 1);
        glayout.setComponentAlignment(esBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 2, 1, 2);
        
        employmentWageStatus = dropDown.populateEmploymentWageStatus("Employment Wage Status: ");
        employmentWageStatus.setWidth("200px");
        glayout.addComponent(employmentWageStatus, 0, 3);
        
        Button ewsBtn = new Button("UPDATE EMPLOYMENT WAGE STATUS");
        ewsBtn.setWidth("100%");
        ewsBtn.addListener(buttonClickListener);
        glayout.addComponent(ewsBtn, 1, 3);
        glayout.setComponentAlignment(ewsBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 4, 1, 4);
        
        employmentWageEntry = dropDown.populateEmploymentWageEntry("Employment Wage Entry: ");
        employmentWageEntry.setWidth("200px");
        glayout.addComponent(employmentWageEntry, 0, 5);
        
        Button eweBtn = new Button("UPDATE EMPLOYMENT WAGE ENTRY");
        eweBtn.setWidth("100%");
        glayout.addComponent(eweBtn, 1, 5);
        eweBtn.addListener(buttonClickListener);
        glayout.setComponentAlignment(eweBtn, Alignment.BOTTOM_LEFT);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 6, 1, 6);
        
        employmentWage = new TextField("Employment Wage");
        employmentWage.setWidth("200px");
        employmentWage.addStyleName("numerical");
        glayout.addComponent(employmentWage, 0, 7);
                
        Button ewBtn = new Button("UPDATE EMPLOYMENT WAGE");
        ewBtn.setWidth("100%");
        glayout.addComponent(ewBtn, 1, 7);
        ewBtn.addListener(buttonClickListener);
        glayout.setComponentAlignment(ewBtn, Alignment.BOTTOM_LEFT);
                                        
        if(getEmployeeId() != null){
            EmploymentInformation ei = si.getEmployeeSalaryInformation(getEmployeeId());
            
            employmentStatus.setValue(Constant.getKeyByValue(Constant.MAP_CONSTANT_EMPLOYMENT_STATUS, ei.getEmploymentStatus()));
            employmentWageStatus.setValue(Constant.getKeyByValue(Constant.MAP_CONSTANT_EMPLOYMENT_WAGE_STATUS, ei.getEmploymentWageStatus()));
            employmentWageEntry.setValue(Constant.getKeyByValue(Constant.MAP_CONSTANT_EMPLOYMENT_WAGE_ENTRY, ei.getEmploymentWageEntry()));            
            employmentWage.setValue(ei.getEmploymentWage());                        
        }
        
        
        return glayout;
    }
    
    Button.ClickListener updateClickListener = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            if(remarks.getValue() == null || remarks.getValue().toString().trim().isEmpty()){
                getWindow().showNotification("Add Remarks!", Window.Notification.TYPE_ERROR_MESSAGE);
                return;
            }
            
            switch(event.getButton().getCaption()){
                case "UPDATE EMPLOYMENT STATUS" : {
                    boolean result = si.updateEmploymentWageDetails("employmentStatus", 
                            employmentStatus.getItem(employmentStatus.getValue()).toString(), 
                            getEmployeeId(), 
                            remarks.getValue().toString());
                    
                    if(result){
                        getWindow().showNotification("Update Employment Status!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        (remarksSubWindow.getParent()).removeWindow(remarksSubWindow);
                    }
                    
                    break;
                }
                
                case "UPDATE EMPLOYMENT WAGE STATUS" : {
                    boolean result = si.updateEmploymentWageDetails("employmentWageStatus", 
                            employmentWageStatus.getItem(employmentWageStatus.getValue()).toString(), 
                            getEmployeeId(), 
                            remarks.getValue().toString());
                    
                    if(result){
                        getWindow().showNotification("Update Employment Wage Status!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        (remarksSubWindow.getParent()).removeWindow(remarksSubWindow);
                    }
                    
                    break;
                }
                
                case "UPDATE EMPLOYMENT WAGE ENTRY" : {
                    boolean result = si.updateEmploymentWageDetails("employmentWageEntry", 
                            employmentWageEntry.getItem(employmentWageEntry.getValue()).toString(), 
                            getEmployeeId(), 
                            remarks.getValue().toString());
                    
                    if(result){
                        getWindow().showNotification("Update Employment Wage Entry!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        (remarksSubWindow.getParent()).removeWindow(remarksSubWindow);
                    }
                    
                    break;
                }
                
                default : {
                    if(employmentWage.getValue() == null || employmentWage.getValue().toString().trim().isEmpty()){
                        getWindow().showNotification("Enter Employment Wage!", Window.Notification.TYPE_ERROR_MESSAGE);
                    } else {
                        if(!util.checkInputIfDouble(employmentWage.getValue().toString().trim())){
                            getWindow().showNotification("Enter Numeric Format!", Window.Notification.TYPE_ERROR_MESSAGE);
                        }
                    }
                    
                    boolean result = si.updateEmploymentWage(util.convertStringToDouble(employmentWage.getValue().toString().trim()), 
                            remarks.getValue().toString().trim(), getEmployeeId());
                    
                    if(result){
                        getWindow().showNotification("Update Employment Wage!", Window.Notification.TYPE_TRAY_NOTIFICATION);
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
    
    public ComponentContainer layout2(){
        GridLayout glayout = new GridLayout(2, 1);
        glayout.setSpacing(true);          
        glayout.setWidth("600px");
        
        TextField employmentStatusField = new HRISTextField("Employment Status: ");
        employmentStatusField.setWidth("200px");
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
        glayout.addComponent(setContributionBtn, 1, 0);
        glayout.setColumnExpandRatio(1, 1);
        glayout.setComponentAlignment(setContributionBtn, Alignment.BOTTOM_CENTER);
                                                    
        if(getEmployeeId() != null){
            EmploymentInformation employmentInformation = si.getEmployeeSalaryInformation(getEmployeeId());            
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
                    
                    boolean result = si.updateEmployeeContributionBranch(getEmployeeId(), branchId, remarks.getValue().toString());
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
                boolean result = si.editEmploymentDateEntry(getEmployeeId(), entryDate);
                if(result){
                    getWindow().showNotification("Update Entry Date.", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    (window.getParent()).removeWindow(window);
                }
            }
        });
        window.addComponent(editBtn);
        
        return window;
    }    

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
