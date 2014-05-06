/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee;

import com.hrms.classes.BranchName;
import com.hrms.classes.CorporateName;
import com.hrms.classes.TradeName;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.company.serviceprovider.CompanyServiceImpl;
import com.openhris.employee.model.EmploymentInformation;
import com.openhris.employee.model.PositionHistory;
import com.openhris.employee.serviceprovider.EmployeeServiceImpl;
import com.openhris.service.CompanyService;
import com.openhris.service.EmployeeService;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jet
 */
public class EmployeeMainUI extends VerticalLayout {
    
    Table employeesTbl = new Table();
        
    final CorporateName corporateNames = new CorporateName();
    final TradeName tradeNames = new TradeName();
    final BranchName branchNames = new BranchName();
    
    EmployeeService employeeService = new EmployeeServiceImpl();
    CompanyService companyService = new CompanyServiceImpl();
    DropDownComponent dropDown = new DropDownComponent();    
    int corporateId;
    int tradeId;
    int branchId;
    
    ComboBox corporation;
    ComboBox trade;
    ComboBox branch;
    
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    private String userRole;    
    
    public EmployeeMainUI(final String userRole, int branchId){
        
        this.userRole = userRole;
        this.branchId = branchId;
                
        setSpacing(true);
        
        employeesTable(branchId);
        
        addComponent(employeesTbl);
        setExpandRatio(employeesTbl, 1.0f);
                        
        corporateNames.setUserRole(userRole);
        tradeNames.setUserRole(userRole);
        branchNames.setUserRole(userRole);
        
    }
    
    public void employeesTable(int branchId){
        employeesTbl.removeAllItems();
        employeesTbl.setSizeFull();
        employeesTbl.setSelectable(true);
        
        employeesTbl.addContainerProperty("id", String.class, null);
        employeesTbl.addContainerProperty("name", String.class, null);
        employeesTbl.addContainerProperty("corporate name", String.class, null);
        employeesTbl.addContainerProperty("trade name", String.class, null);
        employeesTbl.addContainerProperty("branch", String.class, null);
        
        List<PositionHistory> employeeList = employeeService.getEmployeePerBranch(branchId);
        int i = 0; 
        for(PositionHistory p : employeeList){
            String name = p.getLastname()+", "+p.getFirstname()+" "+p.getMiddlename();
            employeesTbl.addItem(new Object[]{
                p.getEmployeeId(), 
                name.toUpperCase(), 
                p.getCompany().toUpperCase(), 
                p.getTrade().toUpperCase(), 
                p.getBranch().toUpperCase()
            }, new Integer(i));
            i++;
        }
        employeesTbl.setPageLength(25);
        
        for(Object listener : employeesTbl.getListeners(ItemClickEvent.class)){
            employeesTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        employeesTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = employeesTbl.getItem(itemId);
                
                if(event.getPropertyId().equals("id")){
                    Window subWindow = openNewEmployeeWindow(item.getItemProperty("id").getValue().toString());
                    subWindow.setModal(true);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.center();
                }
                
                if(event.getPropertyId().equals("name")){          
                    String employeeId = employeeService.getEmployeeId(item.getItemProperty("name").toString());
                    Window subWindow = addAllowanceForLiquidation(item.getItemProperty("id").getValue().toString());
                    subWindow.setModal(true);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.center();
                }
            }
        });
        
        employeesTbl.setImmediate(true);
    }
    
    public Window openNewEmployeeWindow(final String employeeId){
        final Window subWindow = new Window("New Employee");
        subWindow.setWidth("535px");
        
        GridLayout grid = new GridLayout(3, 10);
        grid.setSpacing(true);          
        grid.setSizeFull();
        
        final TextField firstname = new TextField("Firstname:");
        firstname.setWidth("100%");
        firstname.setNullSettingAllowed(false);
        grid.addComponent(firstname, 0, 0);
        
        final TextField middlename = new TextField("Middlename:");
        middlename.setWidth("100%");
        middlename.setNullSettingAllowed(false);
        grid.addComponent(middlename, 1, 0);
        
        final TextField lastname = new TextField("Lastname:");
        lastname.setWidth("100%");
        lastname.setNullSettingAllowed(false);
        grid.addComponent(lastname, 2, 0);   
        
        corporation = dropDown.populateCorporateComboBox(new ComboBox());        
        grid.addComponent(corporation, 0, 1, 1, 1);
        
        final TextField employee_id = new TextField("Company ID:");
        employee_id.setWidth("100%");
        employee_id.setNullSettingAllowed(false);
        employee_id.setValue(employeeId);
        employee_id.setEnabled(false);
        grid.addComponent(employee_id, 2, 1);
        
        trade = new ComboBox("Trade: ");
        trade.setWidth("100%");
        corporation.addListener(new ComboBox.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(corporation.getValue() == null){                    
                } else {
                    corporateId = companyService.getCorporateId(corporation.getValue().toString());
                    dropDown.populateTradeComboBox(trade, corporateId);
                }
                
            }
        });        
        grid.addComponent(trade, 0, 2, 1, 2);
        
        final ComboBox dependent = dropDown.populateTotalDependent(new ComboBox());
        grid.addComponent(dependent, 2, 2);
        
        branch = new ComboBox("Branch: ");
        branch.setWidth("100%");
        trade.addListener(new ComboBox.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(trade.getValue() == null){                    
                } else {
                    tradeId = companyService.getTradeId(trade.getValue().toString(), corporateId);
                    dropDown.populateBranchComboBox(branch, tradeId, corporateId);
                }
                
            }
        });
        branch.addListener(new ComboBox.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(branch.getValue() == null){                    
                } else {
                    branchId = companyService.getBranchId(tradeId, branch.getValue().toString());
                }                
            }
        });
        grid.addComponent(branch, 0, 3, 1, 3);
        
        final TextField department = new TextField("Department: ");
        department.setWidth("100%");
        department.setNullSettingAllowed(false);
        grid.addComponent(department, 2, 3);        
        
        final TextField position = new TextField("Position: ");
        position.setWidth("100%");
        position.setNullSettingAllowed(false);
        grid.addComponent(position, 0, 4);
        
        final PopupDateField entryDate = new PopupDateField("Entry Date:");
        entryDate.addStyleName("mydate");
        entryDate.setValue(new Date());
        entryDate.setDateFormat("yyyy-MM-dd");
        entryDate.setWidth("100%");
        entryDate.setResolution(DateField.RESOLUTION_DAY);
        grid.addComponent(entryDate, 1, 4);
        
        final TextField hdmfNo = new TextField("HDMF #: ");
        hdmfNo.setWidth("100%");
        hdmfNo.setNullSettingAllowed(true);
        grid.addComponent(hdmfNo, 2, 4);
        
        final TextField sssNo = new TextField("SSS #:");
        sssNo.setWidth("100%");
        sssNo.setNullSettingAllowed(true);
        grid.addComponent(sssNo, 0, 5);
        
        final TextField tinNo = new TextField("Tin #:");
        tinNo.setWidth("100%");
        tinNo.setNullSettingAllowed(true);
        grid.addComponent(tinNo, 1, 5);
        
        final TextField phicNo = new TextField("Philhealth #");
        phicNo.setWidth("100%");
        phicNo.setNullSettingAllowed(true);
        grid.addComponent(phicNo, 2, 5);
        
        final ComboBox employmentStatus = dropDown.populateEmploymentStatus(new ComboBox());
        grid.addComponent(employmentStatus, 0, 6);
        
        final ComboBox employmentWageStatus = dropDown.populateEmploymentWageStatus(new ComboBox());
        grid.addComponent(employmentWageStatus, 1, 6);
        
        final ComboBox employmentWageEntry = dropDown.populateEmploymentWageEntry(new ComboBox());
        grid.addComponent(employmentWageEntry, 2, 6);
        
        final TextField employmentWage = new TextField("Salary");
        employmentWage.setWidth("100%");
        employmentWage.setNullSettingAllowed(false);
        employmentWage.addListener(new FieldEvents.TextChangeListener() {

            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                boolean result = util.checkInputIfDouble(event.getText().trim());
                if(result != true){
                    (subWindow.getParent()).showNotification("Input value is invalid!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
        grid.addComponent(employmentWage, 0, 7);                
        
        final TextField employmentAllowance = new TextField("Allowance");
        employmentAllowance.setWidth("100%");
        employmentAllowance.setNullSettingAllowed(false);
        employmentAllowance.setValue("0.0");
        employmentAllowance.addListener(new FieldEvents.TextChangeListener() {

            @Override
            public void textChange(FieldEvents.TextChangeEvent event) {
                boolean result = util.checkInputIfDouble(event.getText().trim());
                if(result != true){
                    (subWindow.getParent()).showNotification("Allowance value is invalid!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
        grid.addComponent(employmentAllowance, 1, 7);
        
        final ComboBox employmentAllowanceEntry = dropDown.populateEmploymentAllowanceEntry(new ComboBox());
        grid.addComponent(employmentAllowanceEntry, 2, 7);         
        
        final TextField bankAccountNo = new TextField("Bank Account #: ");
        bankAccountNo.setWidth("100%");
        bankAccountNo.setNullSettingAllowed(true);
        grid.addComponent(bankAccountNo, 0, 8, 1, 8);
        
        if(employeeId != null){
            for(EmploymentInformation ei : employeeService.getEmployeeEmploymentInformation(employeeId)){
                firstname.setValue(ei.getFirstname());
                middlename.setValue(ei.getMiddlename());
                lastname.setValue(ei.getLastname());

                Object dependentStatusId = dependent.addItem();
                dependent.setItemCaption(dependentStatusId, ei.getTotalDependent());
                dependent.setValue(dependentStatusId);

                entryDate.setValue(ei.getEntryDate());
                hdmfNo.setValue(ei.gethdmfNo());
                sssNo.setValue(ei.getSssNo());
                tinNo.setValue(ei.getTinNo());
                phicNo.setValue(ei.getPhicNo());

                Object EmploymentStatusId = employmentStatus.addItem();
                employmentStatus.setItemCaption(EmploymentStatusId, ei.getEmploymentStatus());
                employmentStatus.setValue(EmploymentStatusId);

                Object EmploymentWageStatusId = employmentWageStatus.addItem();
                employmentWageStatus.setItemCaption(EmploymentWageStatusId, ei.getEmploymentWageStatus());
                employmentWageStatus.setValue(EmploymentWageStatusId);

                Object EmploymentWageEntryStatusId = employmentWageEntry.addItem();
                employmentWageEntry.setItemCaption(EmploymentWageEntryStatusId, ei.getEmploymentWageEntry());
                employmentWageEntry.setValue(EmploymentWageEntryStatusId);

                employmentWage.setValue(ei.getEmploymentWage());
                employmentAllowance.setValue(ei.getAllowance());

                Object allowanceEntryStatusId = employmentAllowanceEntry.addItem();
                employmentAllowanceEntry.setItemCaption(allowanceEntryStatusId, ei.getAllowanceEntry());
                employmentAllowanceEntry.setValue(allowanceEntryStatusId);

                bankAccountNo.setValue(ei.getBankAccountNo());
            }
            
            for(PositionHistory ph : employeeService.getEmployeePositionHistory(employeeId)){
                position.setValue(ph.getPosition());
                
                Object companyStatusId = corporation.addItem();
                corporation.setItemCaption(companyStatusId, ph.getCompany());
                corporation.setValue(companyStatusId);

                Object tradeStatusId = trade.addItem();
                trade.setItemCaption(tradeStatusId, ph.getTrade());
                trade.setValue(tradeStatusId);

                Object branchStatusId = branch.addItem();
                branch.setItemCaption(tradeStatusId, ph.getBranch());
                branch.setValue(tradeStatusId);
                
                department.setValue(ph.getDepartment());
            }
            
            NativeButton updateButton = new NativeButton("UPDATE EMPLOYEE");
            updateButton.setWidth("100%");
            updateButton.setHeight("40px");
            updateButton.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    boolean resultQueryUpdate, result1, result2, checkId;
                    Boolean checkResultForDuplicate;
                    result1 = util.checkInputIfDouble(employmentWage.getValue().toString().trim());
                    result2 = util.checkInputIfDouble(employmentAllowance.getValue().toString().trim());
                    
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
                    
                    String divCorporation;
                    if(util.checkInputIfInteger(corporation.getValue().toString())){
                        divCorporation = corporation.getItemCaption(corporation.getValue());
                    } else {
                        divCorporation = corporation.getValue().toString();
                    }
                    int corporate_id = companyService.getCorporateId(divCorporation);
                    
                    String divTrade;
                    if(util.checkInputIfInteger(trade.getValue().toString())){
                        divTrade = trade.getItemCaption(trade.getValue());
                    } else {
                        divTrade = trade.getValue().toString();
                    }
                    int trade_id = companyService.getTradeId(divTrade, corporate_id);
                    
                    String divBranch;
                    if(util.checkInputIfInteger(branch.getValue().toString())){
                        divBranch = branch.getItemCaption(branch.getValue());
                    } else {
                        divBranch = branch.getValue().toString();
                    }
                    int branch_id = companyService.getBranchId(trade_id, divBranch);                              
                    
                    String divTotalDependent;
                    String divEmploymentStatus;
                    String divWageStatus;
                    String divWageEntry;
                    String divAllowanceEntry;                    
                    
                    if(util.checkInputIfInteger(dependent.getValue().toString())){
                        divTotalDependent = dependent.getItemCaption(dependent.getValue());
                    } else {
                        divTotalDependent = dependent.getValue().toString();
                    }
                    
                    if(util.checkInputIfInteger(employmentStatus.getValue().toString())){
                        divEmploymentStatus = employmentStatus.getItemCaption(employmentStatus.getValue());
                    } else {
                        divEmploymentStatus = employmentStatus.getValue().toString();
                    }
                    
                    if(util.checkInputIfInteger(employmentWageStatus.getValue().toString())){
                        divWageStatus = employmentWageStatus.getItemCaption(employmentWageStatus.getValue());
                    } else {
                        divWageStatus  = employmentWageStatus.getValue().toString();
                    }
                    
                    if(util.checkInputIfInteger(employmentWageEntry.getValue().toString())){
                        divWageEntry = employmentWageEntry.getItemCaption(employmentWageEntry.getValue());
                    } else {
                        divWageEntry  = employmentWageEntry.getValue().toString();
                    }
                    
                    if(util.checkInputIfInteger(employmentAllowanceEntry.getValue().toString())){
                        divAllowanceEntry = employmentAllowanceEntry.getItemCaption(employmentAllowanceEntry.getValue());
                    } else {
                        divAllowanceEntry  = employmentAllowanceEntry.getValue().toString();
                    }
                    
                    List<PositionHistory> updateList = new ArrayList<PositionHistory>();
                    PositionHistory ph = new PositionHistory();
                    ph.setFirstname(firstname.getValue().toString().trim());
                    ph.setMiddlename(middlename.getValue().toString().trim());
                    ph.setLastname(lastname.getValue().toString().trim());
                    ph.setCompany(divCorporation);
                    ph.setTrade(divTrade);
                    ph.setBranch(divBranch);
                    ph.setBranchId(branch_id);
                    ph.setDepartment(department.getValue().toString().trim());
                    ph.setTotalDependent(divTotalDependent);
                    ph.setPosition(position.getValue().toString().trim());
                    ph.setEntryDate(util.parsingDate(util.convertDateFormat(entryDate.getValue().toString())));
                    ph.setSssNo(sssNo.getValue().toString().trim());
                    ph.setTinNo(tinNo.getValue().toString().trim());
                    ph.setPhicNo(phicNo.getValue().toString().trim());
                    ph.setHdmfNo(hdmfNo.getValue().toString().trim());
                    ph.setEmploymentStatus(divEmploymentStatus);
                    ph.setEmploymentWageStatus(divWageStatus);
                    ph.setEmploymentWageEntry(divWageEntry);
                    ph.setEmploymentWage(Double.parseDouble(employmentWage.getValue().toString().trim()));
                    ph.setAllowance(Double.parseDouble(employmentAllowance.getValue().toString().trim()));
                    ph.setAllowanceEntry(divAllowanceEntry);
                    ph.setBankAccountNo(bankAccountNo.getValue().toString().trim());
                    updateList.add(ph);
                    
                    resultQueryUpdate = employeeService.updateEmployeeEmploymentInformation(employee_id.getValue().toString(), updateList);
                    if(resultQueryUpdate){
                    (subWindow.getParent()).removeWindow(subWindow);
                        employeesTable(branchId);                       
                    }else{
                        getWindow().showNotification("SQL ERROR!");
                    }
                }
            });
            grid.addComponent(updateButton, 1, 9, 2, 9);
        } else {
            NativeButton saveButton = new NativeButton("SAVE NEW EMPLOYEE");
            saveButton.setWidth("100%");
            saveButton.setHeight("40px");
            saveButton.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    boolean resultQueryInsert, result1, result2, checkId;
                    Boolean checkResultForDuplicate;
                    result1 = util.checkInputIfDouble(employmentWage.getValue().toString().trim());
                    result2 = util.checkInputIfDouble(employmentAllowance.getValue().toString().trim());

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
                    }

                    int corporate_id = companyService.getCorporateId(corporation.getValue().toString());
                    int trade_id = companyService.getTradeId(trade.getValue().toString(), corporateId);
                    int branch_id = companyService.getBranchId(tradeId, branch.getValue().toString());
                    
                    List<PositionHistory> insertList = new ArrayList<PositionHistory>();
                    PositionHistory ph = new PositionHistory();
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
                    ph.setEntryDate(util.parsingDate(util.convertDateFormat(entryDate.getValue().toString())));
                    ph.setSssNo(sssNo.getValue().toString().trim());
                    ph.setTinNo(tinNo.getValue().toString().trim());
                    ph.setPhicNo(phicNo.getValue().toString().trim());
                    ph.setHdmfNo(hdmfNo.getValue().toString().trim());
                    ph.setEmploymentStatus(employmentStatus.getValue().toString());
                    ph.setEmploymentWageStatus(employmentWage.getValue().toString().trim());
                    ph.setEmploymentWageEntry(employmentWageEntry.getValue().toString());
                    ph.setEmploymentWage(Double.parseDouble(employmentWage.getValue().toString().trim()));
                    ph.setAllowance(Double.parseDouble(employmentAllowance.getValue().toString().trim()));
                    ph.setAllowanceEntry(employmentAllowanceEntry.getValue().toString().trim());
                    ph.setBankAccountNo(bankAccountNo.getValue().toString().trim());
                    
                    checkResultForDuplicate = employeeService.checkForDuplicateEmployee(
                            ph.getFirstname(), 
                            ph.getMiddlename(), 
                            ph.getLastname());
                    if(checkResultForDuplicate){
                        getWindow().showNotification("ERROR! Duplicate Entry in Database!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                    
                    insertList.add(ph);
                    resultQueryInsert = employeeService.insertNewEmployee(insertList);
                    if(resultQueryInsert == true){
                        (subWindow.getParent()).removeWindow(subWindow);
                        employeesTable(branchId);                       
                    }else{
                        getWindow().showNotification("SQL ERROR!");
                    }
                }
                

            });
            grid.addComponent(saveButton, 1, 9, 2, 9);
        }
        
        NativeButton cancelButton = new NativeButton("CANCEL");
        cancelButton.setWidth("100%");
        cancelButton.setHeight("40px");
        cancelButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                (subWindow.getParent()).removeWindow(subWindow);
            }
        });
        grid.addComponent(cancelButton, 0, 9);
        
        subWindow.addComponent(grid);        
        
        return subWindow;
    }     

    public Window addAllowanceForLiquidation(final String employeeId){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("Allowance For Liquidation", vlayout);
        subWindow.setWidth("200px");
        
        final TextField aflField = new TextField("AFL: ");
        aflField.setWidth("100%");
        double aflAmount = employeeService.getEmploymentAllowanceForLiquidation(employeeId);
        aflField.setValue(aflAmount);   
        aflField.addStyleName("numerical");
        vlayout.addComponent(aflField);
        
        Button updateAflButton = new Button("UPDATE");
        updateAflButton.setWidth("100%");
        updateAflButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean checkIfEnteredValueIsDouble = util.checkInputIfDouble(aflField.getValue().toString().trim());
                if(!checkIfEnteredValueIsDouble){
                    subWindow.getWindow().showNotification("Please enter numeric format!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
                
                boolean result = employeeService.updateEmploymentAllowanceForLiquidation(
                        util.convertStringToDouble(aflField.getValue().toString().trim()), 
                        employeeId);
                if(result == true){
                    employeesTable(branchId);
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    subWindow.getWindow().showNotification("UNABLE TO UPDATE AFL!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
        vlayout.addComponent(updateAflButton);
        
        return subWindow;
    }
}
