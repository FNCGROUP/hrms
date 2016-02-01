/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee;

import com.openhris.commons.DropDownComponent;
import com.openhris.commons.HRISPopupDateField;
import com.openhris.commons.HRISTextField;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.service.CompanyService;
import com.openhris.serviceprovider.CompanyServiceImpl;
import com.openhris.model.PostEmploymentInformationBean;
import com.openhris.service.EmployeeService;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;

/**
 *
 * @author jetdario
 */
public class NewEmployeeWindow extends Window {
    
    EmployeeService employeeService = new EmployeeServiceImpl();
    CompanyService companyService = new CompanyServiceImpl();
    DropDownComponent dropDown = new DropDownComponent();
    OpenHrisUtilities utilities = new OpenHrisUtilities();
    
    private int corporateId;
    private int tradeId;
    private int branchId;
    private String employeeId;
    
    ComboBox corporation;
    ComboBox trade;
    ComboBox branch;
    
    Label errorLabel = new Label("<p style=\"color: red\">*Duplicate Entry!</p>", Label.CONTENT_RAW);

    public NewEmployeeWindow(String employeeId) {
        this.employeeId = employeeId;
        
        setCaption("New Employee");
        setWidth("535px");
        
        errorLabel.setVisible(false);
        
        GridLayout grid = new GridLayout(3, 9);
        grid.setSpacing(true);          
        grid.setSizeFull();
        
        final TextField firstname = new HRISTextField("Firstname:");
        grid.addComponent(firstname, 0, 0);
        
        final TextField middlename = new HRISTextField("Middlename:");
        grid.addComponent(middlename, 1, 0);
        
        final TextField lastname = new HRISTextField("Lastname:");
        grid.addComponent(lastname, 2, 0);
        
        corporation = dropDown.populateCorporateComboBox(new ComboBox());        
        grid.addComponent(corporation, 0, 1, 1, 1);
                
        final PopupDateField entryDate = new HRISPopupDateField("Entry Date:");
        grid.addComponent(entryDate, 2, 1);
        
        trade = new ComboBox("Trade: ");
        trade.setWidth("100%");
        corporation.addListener(corporateListener);
        grid.addComponent(trade, 0, 2, 1, 2);
        
        final ComboBox dependent = dropDown.populateTotalDependent("Employee's Dependent");
        grid.addComponent(dependent, 2, 2);
        
        branch = new ComboBox("Branch: ");
        branch.setWidth("100%");
        trade.addListener(tradeListener);
        branch.addListener(branchListener);
        grid.addComponent(branch, 0, 3, 1, 3);
        
        final TextField department = new HRISTextField("Department: ");
        grid.addComponent(department, 2, 3);
        
        final TextField position = new HRISTextField("Position: ");
        grid.addComponent(position, 0, 4);
                
        final TextField sssNo = new HRISTextField("SSS #:");
        grid.addComponent(sssNo, 1, 4);
        
        final TextField hdmfNo = new HRISTextField("HDMF #: ");
        grid.addComponent(hdmfNo, 2, 4);
                
        final ComboBox employmentStatus = dropDown.populateEmploymentStatus("Employment Status: ");
        grid.addComponent(employmentStatus, 0, 5);        
        
        final TextField tinNo = new HRISTextField("Tin #:");
        grid.addComponent(tinNo, 1, 5);
        
        final TextField peicNo = new HRISTextField("Philhealth #");
        grid.addComponent(peicNo, 2, 5);
        
        final TextField employmentWage = new HRISTextField("Employment Wage: ");
        employmentWage.setValue("0.0");
        employmentWage.addListener(checkEntryIfDoubleListener);
        grid.addComponent(employmentWage, 0, 6); 
        
        final ComboBox employmentWageStatus = dropDown.populateEmploymentWageStatus("Employment Wage Status: ");
        grid.addComponent(employmentWageStatus, 1, 6);
        
        final ComboBox employmentWageEntry = dropDown.populateEmploymentWageEntry("Employment Wage Entry: ");
        grid.addComponent(employmentWageEntry, 2, 6);
                        
        Button saveButton = new Button("SAVE NEW EMPLOYEE");
        saveButton.setWidth("100%");           
        saveButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean resultQueryInsert, result1, result2, checkId;
                boolean checkResultForDuplicate;
                result1 = utilities.checkInputIfDouble(employmentWage.getValue().toString().trim());

                if(corporation.getValue() == null){
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
                    
                if(dependent.getValue() == null){
                    getWindow().showNotification("Select # of dependent!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }

                if(employmentStatus.getValue() == null){
                    getWindow().showNotification("Select Employment Status!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }

                if(employmentWageStatus.getValue() == null){
                    getWindow().showNotification("Select Employment Wage Status!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }

                if(employmentWageEntry.getValue() == null){
                    getWindow().showNotification("Select Employment Wage Entry!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }

                if(department.getValue() == null){
                    getWindow().showNotification("Select Department!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }

                if(result1 == false){
                    getWindow().showNotification("Invalid Input for EmploymentWage/EmploymentAllowanceEntry", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }

                checkResultForDuplicate = employeeService.checkForDuplicateEmployee(
                        firstname.getValue().toString().trim().toLowerCase(), 
                        middlename.getValue().toString().trim().toLowerCase(), 
                        lastname.getValue().toString().trim().toLowerCase());
                
                if(checkResultForDuplicate){      
                    try{                           
                        errorLabel.setVisible(true);
                    } catch (Exception e){
                        e.printStackTrace(System.out);                            
                    }                        
                
                    return;
                }
                    
                int corporate_id = companyService.getCorporateId(corporation.getValue().toString());
                int trade_id = companyService.getTradeId(trade.getValue().toString(), getCorporateId());
                int branch_id = companyService.getBranchId(getTradeId(), branch.getValue().toString());
                    
                PostEmploymentInformationBean pe = new PostEmploymentInformationBean();
                pe.setFirstname(firstname.getValue().toString().trim());
                pe.setMiddlename(middlename.getValue().toString().trim());
                pe.setLastname(lastname.getValue().toString().trim());
                pe.setCompany(corporation.getValue().toString());
                pe.setTrade(trade.getValue().toString());
                pe.setBranch(branch.getValue().toString());
                pe.setBranchId(branch_id);
                pe.setDepartment(department.getValue().toString().trim());
                pe.setTotalDependent(dependent.getItem(dependent.getValue()).toString());
                pe.setPosition(position.getValue().toString().trim());
                pe.setEntryDate(utilities.parsingDate(utilities.convertDateFormat(entryDate.getValue().toString())));
                pe.setSssNo(sssNo.getValue().toString().trim());
                pe.setTinNo(tinNo.getValue().toString().trim());
                pe.setPhicNo(peicNo.getValue().toString().trim());
                pe.setHdmfNo(hdmfNo.getValue().toString().trim());
                pe.setEmploymentStatus(employmentStatus.getItem(employmentStatus.getValue()).toString());
                pe.setEmploymentWageStatus(employmentWageStatus.getItem(employmentWageStatus.getValue()).toString());
                pe.setEmploymentWageEntry(employmentWageEntry.getItem(employmentWageEntry.getValue()).toString());
                pe.setEmploymentWage(utilities.convertStringToDouble(employmentWage.getValue().toString().trim()));
                    
                resultQueryInsert = employeeService.insertNewEmployee(pe);
                if(resultQueryInsert == true){
                    close();          
                }else{
                    getWindow().showNotification("SQL ERROR!");
                }
            }
        });
        grid.addComponent(saveButton, 1, 7, 2, 7);
        grid.addComponent(errorLabel, 1, 8, 2, 8);
        
        addComponent(grid);
    }

    int getCorporateId() {
        return corporateId;
    }

    int getTradeId() {
        return tradeId;
    }

    int getBranchId() {
        return branchId;
    }
        
    String getEmployeeId(){
        return employeeId;
    }
    
    ComboBox.ValueChangeListener corporateListener = new ComboBox.ValueChangeListener() {

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            if(event.getProperty().getValue() == null){                
            } else {
                corporateId = companyService.getCorporateId(event.getProperty().getValue().toString());
                dropDown.populateTradeComboBox(trade, corporateId);
            }            
        }
    };
    
    ComboBox.ValueChangeListener tradeListener = new ComboBox.ValueChangeListener() {

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            if(event.getProperty().getValue() == null){                
            } else {
                tradeId = companyService.getTradeId(event.getProperty().getValue().toString(), getCorporateId());
                dropDown.populateBranchComboBox(branch, tradeId, getCorporateId());
            }
        }
    };
    
    ComboBox.ValueChangeListener branchListener = new ComboBox.ValueChangeListener() {

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            if(event.getProperty().getValue() == null){                    
            } else {
                branchId = companyService.getBranchId(getTradeId(), event.getProperty().getValue().toString());
            }
        }
    };

    FieldEvents.TextChangeListener checkEntryIfDoubleListener = new FieldEvents.TextChangeListener() {

        @Override
        public void textChange(FieldEvents.TextChangeEvent event) {
            boolean result = utilities.checkInputIfDouble(event.getText().trim());
            if(result != true){
                getApplication().getMainWindow().showNotification("Input value is invalid!", Window.Notification.TYPE_ERROR_MESSAGE);
                return;
            }
        }
    };
}
