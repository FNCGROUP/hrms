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
import com.openhris.model.EmploymentInformation;
import com.openhris.model.PostEmploymentInformationBean;
import com.openhris.service.EmployeeService;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Window;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class AddNewEmployeeWindow extends Window {
    
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

    public AddNewEmployeeWindow(String employeeId) {
        this.employeeId = employeeId;
        
        setCaption("New Employee");
        setWidth("535px");
        
        errorLabel.setVisible(false);
        
        GridLayout grid = new GridLayout(3, 11);
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
        
        TextField employeeIdField = new HRISTextField("Company ID:");
        employeeIdField.setValue(employeeId);
        employeeIdField.setEnabled(false);
        grid.addComponent(employeeIdField, 2, 1);
        
        trade = new ComboBox("Trade: ");
        trade.setWidth("100%");
        corporation.addListener(corporateListener);
        grid.addComponent(trade, 0, 2, 1, 2);
        
        final ComboBox dependent = dropDown.populateTotalDependent(new ComboBox());
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
        
        final PopupDateField entryDate = new HRISPopupDateField("Entry Date:");
        grid.addComponent(entryDate, 1, 4);
        
        final TextField hdmfNo = new HRISTextField("HDMF #: ");
        grid.addComponent(hdmfNo, 2, 4);
        
        final TextField sssNo = new HRISTextField("SSS #:");
        grid.addComponent(sssNo, 0, 5);
        
        final TextField tinNo = new HRISTextField("Tin #:");
        grid.addComponent(tinNo, 1, 5);
        
        final TextField phicNo = new HRISTextField("Philhealth #");
        grid.addComponent(phicNo, 2, 5);
        
        final ComboBox employmentStatus = dropDown.populateEmploymentStatus(new ComboBox());
        grid.addComponent(employmentStatus, 0, 6);
        
        final ComboBox employmentWageStatus = dropDown.populateEmploymentWageStatus(new ComboBox());
        grid.addComponent(employmentWageStatus, 1, 6);
        
        final ComboBox employmentWageEntry = dropDown.populateEmploymentWageEntry(new ComboBox());
        grid.addComponent(employmentWageEntry, 2, 6);
        
        final TextField employmentWage = new HRISTextField("Employment Wage: ");
        employmentWage.setValue("0.0");
        employmentWage.addListener(checkEntryIfDoubleListener);
        grid.addComponent(employmentWage, 0, 7);    
        
        final TextField employmentAllowance = new HRISTextField("Allowance");
        employmentAllowance.setValue("0.0");
        employmentAllowance.addListener(checkEntryIfDoubleListener);
        grid.addComponent(employmentAllowance, 1, 7);
        
        final ComboBox employmentAllowanceEntry = dropDown.populateEmploymentAllowanceEntry(new ComboBox());
        grid.addComponent(employmentAllowanceEntry, 2, 7);    
        
        final TextField bankAccountNo = new HRISTextField("Bank Account #: ");
        grid.addComponent(bankAccountNo, 0, 8, 1, 8);
        
        if(getEmployeeId() != null){
            for(EmploymentInformation information : employeeService.getEmployeeEmploymentInformation(getEmployeeId())){
                firstname.setValue(information.getFirstname());
                middlename.setValue(information.getMiddlename());
                lastname.setValue(information.getLastname());

                Object dependentStatusId = dependent.addItem();
                dependent.setItemCaption(dependentStatusId, information.getTotalDependent());
                dependent.setValue(dependentStatusId);

                entryDate.setValue(information.getEntryDate());
                hdmfNo.setValue(information.gethdmfNo());
                sssNo.setValue(information.getSssNo());
                tinNo.setValue(information.getTinNo());
                phicNo.setValue(information.getPhicNo());

                Object EmploymentStatusId = employmentStatus.addItem();
                employmentStatus.setItemCaption(EmploymentStatusId, information.getEmploymentStatus());
                employmentStatus.setValue(EmploymentStatusId);

                Object EmploymentWageStatusId = employmentWageStatus.addItem();
                employmentWageStatus.setItemCaption(EmploymentWageStatusId, information.getEmploymentWageStatus());
                employmentWageStatus.setValue(EmploymentWageStatusId);

                Object EmploymentWageEntryStatusId = employmentWageEntry.addItem();
                employmentWageEntry.setItemCaption(EmploymentWageEntryStatusId, information.getEmploymentWageEntry());
                employmentWageEntry.setValue(EmploymentWageEntryStatusId);

                employmentWage.setValue(information.getEmploymentWage());
                employmentAllowance.setValue(information.getAllowance());

                Object allowanceEntryStatusId = employmentAllowanceEntry.addItem();
                employmentAllowanceEntry.setItemCaption(allowanceEntryStatusId, information.getAllowanceEntry());
                employmentAllowanceEntry.setValue(allowanceEntryStatusId);

                bankAccountNo.setValue(information.getBankAccountNo());
            }
            
            for(PostEmploymentInformationBean history : employeeService.getEmployeePositionHistory(getEmployeeId())){
                position.setValue(history.getPosition());
                
                Object companyStatusId = corporation.addItem();
                corporation.setItemCaption(companyStatusId, history.getCompany());
                corporation.setValue(companyStatusId);

                Object tradeStatusId = trade.addItem();
                trade.setItemCaption(tradeStatusId, history.getTrade());
                trade.setValue(tradeStatusId);

                Object branchStatusId = branch.addItem();
                branch.setItemCaption(tradeStatusId, history.getBranch());
                branch.setValue(tradeStatusId);
                
                department.setValue(history.getDepartment());
            }
        } else {
            NativeButton saveButton = new NativeButton("SAVE NEW EMPLOYEE");
            saveButton.setWidth("100%");
            saveButton.setHeight("40px");            
            saveButton.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    boolean resultQueryInsert, result1, result2, checkId;
                    boolean checkResultForDuplicate;
                    result1 = utilities.checkInputIfDouble(employmentWage.getValue().toString().trim());
                    result2 = utilities.checkInputIfDouble(employmentAllowance.getValue().toString().trim());

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

                    if(employmentAllowance.getValue() == null || employmentAllowance.getValue().toString().trim().equals("")){
                        employmentAllowanceEntry.setValue(0.0);
                    }

                    if(employmentAllowanceEntry.getValue() == null){
                        getWindow().showNotification("Select N/A if no allowance!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }

                    if(department.getValue() == null){
                        getWindow().showNotification("Select Department!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }

                    if(result1 == false || result2 == false){
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
                    
                    List<PostEmploymentInformationBean> insertList = new ArrayList<PostEmploymentInformationBean>();
                    PostEmploymentInformationBean ph = new PostEmploymentInformationBean();
                    ph.setFirstname(firstname.getValue().toString().trim());
                    ph.setMiddlename(middlename.getValue().toString().trim());
                    ph.setLastname(lastname.getValue().toString().trim());
                    ph.setCompany(corporation.getValue().toString());
                    ph.setTrade(trade.getValue().toString());
                    ph.setBranch(branch.getValue().toString());
                    ph.setBranchId(branch_id);
                    ph.setDepartment(department.getValue().toString().trim());
                    ph.setTotalDependent(dependent.getValue().toString());
                    ph.setPosition(position.getValue().toString().trim());
                    ph.setEntryDate(utilities.parsingDate(utilities.convertDateFormat(entryDate.getValue().toString())));
                    ph.setSssNo(sssNo.getValue().toString().trim());
                    ph.setTinNo(tinNo.getValue().toString().trim());
                    ph.setPhicNo(phicNo.getValue().toString().trim());
                    ph.setHdmfNo(hdmfNo.getValue().toString().trim());
                    ph.setEmploymentStatus(employmentStatus.getValue().toString());
                    ph.setEmploymentWageStatus(employmentWageStatus.getValue().toString().trim());
                    ph.setEmploymentWageEntry(employmentWageEntry.getValue().toString());
                    ph.setEmploymentWage(Double.parseDouble(employmentWage.getValue().toString().trim()));
                    ph.setAllowance(Double.parseDouble(employmentAllowance.getValue().toString().trim()));
                    ph.setAllowanceEntry(employmentAllowanceEntry.getValue().toString().trim());
                    ph.setBankAccountNo(bankAccountNo.getValue().toString().trim());                    
                    insertList.add(ph);
                    
                    resultQueryInsert = employeeService.insertNewEmployee(insertList);
                    if(resultQueryInsert == true){
                        close();          
                    }else{
                        getWindow().showNotification("SQL ERROR!");
                    }
                }
            });
            grid.addComponent(saveButton, 1, 9, 2, 9);
            grid.addComponent(errorLabel, 1, 10, 2, 10);
        }
        
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
