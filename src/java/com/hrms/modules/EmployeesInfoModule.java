/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.modules;

import com.hrms.beans.EmployeesInfoBean;
import com.hrms.classes.BranchName;
import com.hrms.classes.CorporateName;
import com.hrms.classes.TradeName;
import com.hrms.queries.GetSQLQuery;
import com.openhris.dao.ServiceGetDAO;
import com.openhris.employee.model.PositionHistory;
import com.openhris.employee.serviceprovider.EmployeeServiceImpl;
import com.openhris.employee.service.EmployeeService;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class EmployeesInfoModule extends VerticalLayout{
    
    GetSQLQuery query = new GetSQLQuery();
    EmployeesInfoBean employeesInfoBean = new EmployeesInfoBean();
    EmployeesEditableInformation editableInformation;
    Table employeesTbl = new Table();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date convertDate;
        
    final CorporateName corporateNames = new CorporateName();
    final TradeName tradeNames = new TradeName();
    final BranchName branchNames = new BranchName();
    
    ServiceGetDAO serviceDAO = new ServiceGetDAO();
    
    private String userRole;
        
    EmployeeService es = new EmployeeServiceImpl();
    int branchId;
    
    public EmployeesInfoModule(final String userRole, int branchId){
        
        this.userRole = userRole;
        this.branchId = branchId;
                
        setSpacing(true);
        
        employeesTable(branchId);
        
        addComponent(employeesTbl);
        
        HorizontalLayout hlayout = new HorizontalLayout();
        hlayout.setWidth("100%");
        GridLayout gridLayout = new GridLayout(4, 2);
        gridLayout.setSizeFull();
        gridLayout.setSpacing(true);
                
        corporateNames.setUserRole(userRole);
        tradeNames.setUserRole(userRole);
        branchNames.setUserRole(userRole);
        
        NativeButton addEmployeeButton = new NativeButton("ADD NEW EMPLOYEE");
        addEmployeeButton.setWidth("100%");
        addEmployeeButton.setHeight("40px");
        addEmployeeButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Window subWindow = newEmployeeWindow();
                subWindow.setModal(true);
                if(subWindow.getParent() == null){
                    getWindow().addWindow(subWindow);
                }
                subWindow.center();
            }
            
        });
        gridLayout.addComponent(addEmployeeButton, 3, 0);        
        
        hlayout.addComponent(gridLayout);
        addComponent(hlayout);
    }
    
    public void employeesTable(int branchId){
        employeesTbl.removeAllItems();
        employeesTbl.setSizeFull();
        
        employeesTbl.addContainerProperty("id", String.class, null);
        employeesTbl.addContainerProperty("name", String.class, null);
        employeesTbl.addContainerProperty("corporate name", String.class, null);
        employeesTbl.addContainerProperty("trade name", String.class, null);
        employeesTbl.addContainerProperty("branch", String.class, null);
        
        List<PositionHistory> employeeList = serviceDAO.getEmployeePerBranch(branchId);
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
        employeesTbl.setPageLength(20);
        employeesTbl.setImmediate(true);
    }
    
    public Window newEmployeeWindow(){
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
        
        final NativeSelect corporateName = new NativeSelect("Corporate Name:");
        corporateName.setWidth("100%");
        corporateNames.getCorporateName(corporateName);
        grid.addComponent(corporateName, 0, 1, 1, 1);
        
        final TextField employeeId = new TextField("Company ID:");
        employeeId.setWidth("100%");
        employeeId.setNullSettingAllowed(false);
        employeeId.setEnabled(false);
        grid.addComponent(employeeId, 2, 1);
        
        final NativeSelect tradeName = new NativeSelect("Trade Name:");
        tradeName.setWidth("100%");
        corporateName.addListener(new ComboBox.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                tradeNames.getTradeName(tradeName, corporateName.getValue().toString());
            }
            
        });
        grid.addComponent(tradeName, 0, 2, 1, 2);
        
        final ComboBox status = new ComboBox("# Of Dependent: ");
        status.setNullSelectionAllowed(false);
        status.setWidth("100%");        
        status.addItem("s/me");
        status.addItem("s1/me1");
        status.addItem("s2/me2");
        status.addItem("s3/me3");
        status.addItem("s4/me4");
        status.setImmediate(true);
        grid.addComponent(status, 2, 2);
        
        final NativeSelect branchName = new NativeSelect("Branch:");
        branchName.setWidth("100%");
        tradeName.addListener(new ComboBox.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(tradeName.getValue() == null){                    
                }else{
                    branchNames.getBranchName(branchName, tradeName.getValue().toString(), corporateName.getValue().toString());
                }
            }
            
        });        
        grid.addComponent(branchName, 0, 3, 1, 3);
        
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
        
        final TextField philhealthNo = new TextField("Philhealth #");
        philhealthNo.setWidth("100%");
        philhealthNo.setNullSettingAllowed(true);
        grid.addComponent(philhealthNo, 2, 5);
        
        final ComboBox employmentStatus = new ComboBox("Employment Status");
        employmentStatus.setWidth("100%");
        employmentStatus.setNullSelectionAllowed(false);
        employmentStatus.setImmediate(true);
        employmentStatus.addItem("regular");
        employmentStatus.addItem("contractual");
        grid.addComponent(employmentStatus, 0, 6);
        
        final ComboBox employmentWageStatus = new ComboBox("Employment Wage Status");
        employmentWageStatus.setWidth("100%");
        employmentWageStatus.setNullSelectionAllowed(false);
        employmentWageStatus.setImmediate(true);
        employmentWageStatus.addItem("regular");
        employmentWageStatus.addItem("minimum");
        grid.addComponent(employmentWageStatus, 1, 6);
        
        final ComboBox employmentWageEntry = new ComboBox("Employment Wage Entry");
        employmentWageEntry.setWidth("100%");
        employmentWageEntry.setNullSelectionAllowed(false);
        employmentWageEntry.setImmediate(true);
        employmentWageEntry.addItem("monthly");
        employmentWageEntry.addItem("daily");
        grid.addComponent(employmentWageEntry, 2, 6);
        
        final TextField employmentWage = new TextField("Salary");
        employmentWage.setWidth("100%");
        employmentWage.setNullSettingAllowed(false);
        employmentWage.addListener(new FieldEvents.TextChangeListener() {

            @Override
            public void textChange(TextChangeEvent event) {
                boolean result = checkInputIfDouble(event.getText().trim());
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
            public void textChange(TextChangeEvent event) {
                boolean result = checkInputIfDouble(event.getText().trim());
                if(result != true){
                    (subWindow.getParent()).showNotification("Allowance value is invalid!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
        });
        grid.addComponent(employmentAllowance, 1, 7);
        
        final ComboBox employmentAllowanceEntry = new ComboBox("Allowance Entry");
        employmentAllowanceEntry.setWidth("100%");
        employmentAllowanceEntry.setNullSelectionAllowed(false);
        employmentAllowanceEntry.setImmediate(true);
        employmentAllowanceEntry.addItem("monthly");
        employmentAllowanceEntry.addItem("daily");
        employmentAllowanceEntry.addItem("N/A");
        grid.addComponent(employmentAllowanceEntry, 2, 7);         
        
        final TextField bankAccountNo = new TextField("Bank Account #: ");
        bankAccountNo.setWidth("100%");
        bankAccountNo.setNullSettingAllowed(true);
        grid.addComponent(bankAccountNo, 0, 8, 1, 8);
        
        NativeButton cancelButton = new NativeButton("CANCEL");
        cancelButton.setWidth("100%");
        cancelButton.setHeight("40px");
        cancelButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                (subWindow.getParent()).removeWindow(subWindow);
            }
        });
        grid.addComponent(cancelButton, 0, 9);
        
        NativeButton saveButton = new NativeButton("SAVE NEW EMPLOYEE");
        saveButton.setWidth("100%");
        saveButton.setHeight("40px");
        saveButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                boolean result, result1, result2, checkId;
                Boolean checkResultForDuplicate;
                result1 = checkInputIfDouble(employmentWage.getValue().toString().trim());
                result2 = checkInputIfDouble(employmentAllowance.getValue().toString().trim());;
                checkId = query.checkEmployeeIdIfExist(employeeId.getValue().toString());
                
                if(checkId == false){
                    getWindow().showNotification("Employee ID already EXIST!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }                
                
                if(status.getValue() == null){
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
                }else{
                                        
                    employeesInfoBean.setFirstname(firstname.getValue().toString().trim());
                    employeesInfoBean.setMiddlename(middlename.getValue().toString().trim());
                    employeesInfoBean.setLastname(lastname.getValue().toString().trim());
                    employeesInfoBean.setEmployeeId(employeeId.getValue().toString().trim());
                    employeesInfoBean.setSssNo(sssNo.getValue().toString().trim());
                    employeesInfoBean.setTinNo(tinNo.getValue().toString().trim());
                    employeesInfoBean.setPhilhealthNo(philhealthNo.getValue().toString().trim());
                    employeesInfoBean.setHdmfNo(hdmfNo.getValue().toString().trim());
                    employeesInfoBean.setEmploymentStatus(employmentStatus.getValue().toString().trim());
                    employeesInfoBean.setEmploymentWageStatus(employmentWageStatus.getValue().toString().trim());
                    employeesInfoBean.setEmploymentWageEntry(employmentWageEntry.getValue().toString().trim());
                    employeesInfoBean.setEmploymentWage(convertStringToDouble(employmentWage.getValue().toString().trim()));
                    employeesInfoBean.setAllowance(convertStringToDouble(employmentAllowance.getValue().toString()));
                    employeesInfoBean.setAllowanceEntry(employmentAllowanceEntry.getValue().toString());
                    employeesInfoBean.setBranchId(query.getBranchId(branchName.getValue().toString(), tradeName.getValue().toString(), corporateName.getValue().toString()));
                    employeesInfoBean.setEntryDate(convertDateFormat(entryDate.getValue().toString()));
                    employeesInfoBean.setDependent(status.getValue().toString());
                    employeesInfoBean.setPosition(position.getValue().toString().trim());
                    employeesInfoBean.setDepartment(department.getValue().toString().trim());
                    employeesInfoBean.setBankAccountNo(bankAccountNo.getValue().toString().trim());
                    employeesInfoBean.setCorporate(corporateName.getValue().toString());
                    employeesInfoBean.setTrade(tradeName.getValue().toString());
                    employeesInfoBean.setBranch(branchName.getValue().toString());
                    employeesInfoBean.setBankAccountNo(bankAccountNo.getValue().toString().trim());
                    
                    if(!userRole.equals("administrator")){
                        checkResultForDuplicate = employeesInfoBean.checkForDuplicateEntry();
                        if(!checkResultForDuplicate){
                            getWindow().showNotification("ERROR! Duplicate Entry in DB!", Window.Notification.TYPE_ERROR_MESSAGE);
                            return;
                        }
                    }
                    
                    result = employeesInfoBean.saveNewEmployeeInfo();
                    if(result == true){
                    (subWindow.getParent()).removeWindow(subWindow);
                        if(userRole.equals("encoder")){
                            employeesTable(branchId);
                        }else{
                            employeesTable(branchId);
                        }                        
                    }else{
                        getWindow().showNotification("SQL ERROR!");
                    }
                }
            }
            
        });
        grid.addComponent(saveButton, 1, 9, 2, 9);
        
        subWindow.addComponent(grid);        
        
        return subWindow;
    }
            
    private Double convertStringToDouble(String num){
        Double val = null;
        if(num != null){
            val = Double.parseDouble(num);
        }
        return val;
    }
    
    private Boolean checkInputIfDouble(String num){
        boolean result = false;
        try{
            Double val = Double.parseDouble(num);
            result = true;
        }catch (Exception e){
            e.getMessage();
        }        
        return result;
    }
    
    private String convertDateFormat(String date){
        DateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        DateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateConvert = null;
        try {
            Date newDate = sdf1.parse(date);
            dateConvert = sdf2.format(newDate);
        } catch (ParseException ex) {
            Logger.getLogger(EmployeesInfoModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dateConvert;
    }
}
