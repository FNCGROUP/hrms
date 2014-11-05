/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee;

import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.company.service.CompanyService;
import com.openhris.company.serviceprovider.CompanyServiceImpl;
import com.openhris.employee.model.EmploymentInformation;
import com.openhris.employee.service.SalaryInformationService;
import com.openhris.employee.serviceprovider.SalaryInformationServiceImpl;
import com.vaadin.data.Property;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
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
    CompanyService companyService = new CompanyServiceImpl();
    
    ComboBox corporate = new ComboBox("Corporate: ");
    ComboBox trade = new ComboBox("Trade: ");
    ComboBox branch = new ComboBox("Branch: ");
      
    String employeeId;
    int corporateId;
    int tradeId;
    int branchId;
    
    public EmployeeSalaryInformation(){        
    }
    
    public EmployeeSalaryInformation(String employeeId){
        this.employeeId = employeeId;
        
        init();
        addComponent(layout());
        addComponent(new Label("<br />", Label.CONTENT_XHTML));
        addComponent(layout2());
    }
    
    public void init(){
        setSpacing(true);
	setMargin(true);
	setSizeFull();
	setImmediate(true);
    }
    
    public ComponentContainer layout(){
        
        GridLayout glayout = new GridLayout(3, 5);
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
        
        final ComboBox employeeDependent = dropDown.populateTotalDependent(new ComboBox());
        employeeDependent.setWidth("200px");
        glayout.addComponent(employeeDependent, 0, 4);
        
        Button updateBtn = new Button("UPDATE SALARY INFORMATION");
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
                
                if(util.checkInputIfInteger(employeeDependent.getValue().toString())){
                    employmentInformation.setTotalDependent(employeeDependent.getItemCaption(employeeDependent.getValue()));
                } else {
                    employmentInformation.setTotalDependent(employeeDependent.getValue().toString());
                }
                
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
                
        Button setContributionBtn = new Button("SET EMPLOYEE'S CONTRIBUTION MAIN BRANCH");
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
        glayout.addComponent(setContributionBtn, 1, 4, 2, 4);
        glayout.setComponentAlignment(setContributionBtn, Alignment.BOTTOM_CENTER);
        
        GridLayout glayout2 = new GridLayout(2, 2);
        
        if(getEmployeeId() != null){
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
            
            Object employeeTotalDependentId = employeeDependent.addItem();
            employeeDependent.setItemCaption(employeeTotalDependentId, employmentInformation.getTotalDependent());
            employeeDependent.setValue(employeeTotalDependentId);
        }
        
        return glayout;
    }
    
    public ComponentContainer layout2(){
        GridLayout glayout = new GridLayout(3, 4);
        glayout.setSpacing(true);          
        glayout.setSizeFull();
        
        TextField employmentStatusField = new TextField("Employment Status: ");
        employmentStatusField.setWidth("200px");        
        glayout.addComponent(employmentStatusField, 0, 0);
        
        PopupDateField entryDate = new PopupDateField("Employment Entry: ");
        entryDate.addStyleName("mydate");          
        entryDate.setDateFormat("yyyy-MM-dd");
        entryDate.setWidth("200px");
        entryDate.setResolution(DateField.RESOLUTION_DAY);                
        glayout.addComponent(entryDate, 1, 0);
        glayout.setComponentAlignment(entryDate, Alignment.MIDDLE_LEFT);                
        
        Button changeDateEntry = new Button("UPDATE DATE ENTRY");
        changeDateEntry.setWidth("200px");
        changeDateEntry.setImmediate(true);
        changeDateEntry.addListener(updateDateEntryBtn);
        glayout.addComponent(changeDateEntry, 2, 0);
        glayout.setComponentAlignment(changeDateEntry, Alignment.BOTTOM_LEFT);
        
        final TextField bankAccountNo = new TextField("Bank Account No: ");
        bankAccountNo.setWidth("200px");        
        bankAccountNo.addListener(new Field.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(event.getProperty().getValue() == null || event.getProperty().getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Enter Bank Account No.", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                } else {
                    getWindow().showNotification("Test Verified!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }
            }
        });
        bankAccountNo.setImmediate(true);
        glayout.addComponent(bankAccountNo, 0, 1);
                
        PopupDateField endDate = new PopupDateField("Exit Date: ");
        endDate.addStyleName("mydate");          
        endDate.setDateFormat("yyyy-MM-dd");
        endDate.setWidth("200px");
        endDate.setResolution(DateField.RESOLUTION_DAY);                
        glayout.addComponent(endDate, 1, 1);
        glayout.setComponentAlignment(endDate, Alignment.MIDDLE_LEFT);
        
        Button endDateBtn = new Button("SUBMIT");
        endDateBtn.setWidth("200px");
        endDateBtn.addListener(exitDateBtn);
        glayout.addComponent(endDateBtn, 2, 1);
        glayout.setComponentAlignment(endDateBtn, Alignment.BOTTOM_LEFT);
        
        if(getEmployeeId() != null){
            EmploymentInformation employmentInformation = siService.getEmployeeSalaryInformation(getEmployeeId());
            
            employmentStatusField.setValue(employmentInformation.getCurrentStatus().toUpperCase());
            employmentStatusField.setReadOnly(true);
            entryDate.setValue((employmentInformation.getEntryDate() == null) ? "" : employmentInformation.getEntryDate());
            bankAccountNo.setValue(employmentInformation.getBankAccountNo());    
            bankAccountNo.setReadOnly(true);
        }
        
        CheckBox editBankAccountNo = new CheckBox("Edit Bank Account No.");
        editBankAccountNo.setImmediate(true);
        editBankAccountNo.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                bankAccountNo.setReadOnly(!event.getButton().booleanValue());
            }
        });
        glayout.addComponent(editBankAccountNo, 0, 2);
        glayout.setComponentAlignment(editBankAccountNo, Alignment.BOTTOM_LEFT);
        
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
                    
                    boolean result = siService.updateEmployeeContributionBranch(getEmployeeId(), branchId, remarks.getValue().toString());
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
    
    ClickListener updateDateEntryBtn = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            getWindow().showNotification("Button TEST!");
        }
    };
    
    ClickListener exitDateBtn = new Button.ClickListener() {

        @Override
        public void buttonClick(Button.ClickEvent event) {
            getWindow().showNotification("Exit Date Button!");
        }
    };
}
