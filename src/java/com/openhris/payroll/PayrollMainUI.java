/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.hrms.beans.AdvancesTypeBean;
import com.hrms.classes.GlobalVariables;
import com.openhris.administrator.model.UserAccessControl;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.dao.ServiceUpdateDAO;
import com.openhris.employee.model.Employee;
import com.openhris.employee.serviceprovider.EmployeeServiceImpl;
import com.openhris.payroll.model.Advances;
import com.openhris.payroll.model.Payroll;
import com.openhris.payroll.serviceprovider.PayrollServiceImpl;
import com.openhris.employee.service.EmployeeService;
import com.openhris.payroll.service.PayrollService;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.Window;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jet
 */
public class PayrollMainUI extends VerticalLayout {
    
    OpenHrisUtilities util = new OpenHrisUtilities();
    PayrollService payrollService = new PayrollServiceImpl();
    EmployeeService employeeService = new EmployeeServiceImpl();
    DropDownComponent dropDown = new DropDownComponent();
    ServiceUpdateDAO serviceUpdate = new ServiceUpdateDAO();
    
    Table payrollTbl = new Table();
    Table advanceTbl = new Table();
    int branchId;
    String employeeId;
    boolean lastAddedAdvanceType;
    
    ComboBox employeesName = new ComboBox("Employees: ");
    DecimalFormat df = new DecimalFormat("0.00");
        
    public PayrollMainUI(int branchId){
        this.branchId = branchId;
        
        setSpacing(false);
        setMargin(false);
        setWidth("100%");
        setHeight("100%");
        setImmediate(true);        
        
        final VerticalSplitPanel vsplit = new VerticalSplitPanel();   
        
        vsplit.setImmediate(true);
        vsplit.setMargin(false);
        vsplit.setSizeFull();
        vsplit.setLocked(true);
        
        vsplit.setSplitPosition(14, Sizeable.UNITS_PERCENTAGE);
        
        employeeComboBox(branchId);
        GridLayout glayout = new GridLayout(2, 1);
        glayout.setWidth("60%");
        glayout.setMargin(true);
        glayout.setSpacing(true);
        glayout.addComponent(employeesName, 0, 0);
        
        Button generatePayrollButton = new Button();
        if(!UserAccessControl.isPayroll()){
            generatePayrollButton.setCaption("Payroll Button is Disabled");
            generatePayrollButton.setEnabled(UserAccessControl.isPayroll());
        } else {
            generatePayrollButton.setCaption("Generate Payroll");
            generatePayrollButton.setEnabled(UserAccessControl.isPayroll());
        }
        generatePayrollButton.setWidth("100%");
        generatePayrollButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(employeesName.getValue() == null){
                    getWindow().showNotification("Select an Employee!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                }
                
                payrollTable(getBranchId(), employeeId);
            }
        });
        glayout.addComponent(generatePayrollButton, 1, 0);
        glayout.setComponentAlignment(generatePayrollButton, Alignment.BOTTOM_LEFT);
        
        vsplit.setFirstComponent(glayout);        
        addComponent(vsplit);
        
        setExpandRatio(vsplit, 1.0f);
                                
        payrollTable(branchId, null);        
        vsplit.setSecondComponent(payrollTbl); 
    }
    
    public void payrollTable(int branchId, String employeeId){
        payrollTbl.removeAllItems();
        payrollTbl.setSizeFull();
        payrollTbl.setImmediate(true);
        payrollTbl.setSelectable(true);
        payrollTbl.addStyleName("hris-table-layout");
        
        payrollTbl.addContainerProperty("id", String.class, null);        
        payrollTbl.addContainerProperty("start date", String.class, null);
        payrollTbl.addContainerProperty("cut-off date", String.class, null);
        
        payrollTbl.addContainerProperty("basic salary", String.class, null); 
        payrollTbl.setColumnAlignment("basic salary", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("half-month salary", String.class, null);
        payrollTbl.setColumnAlignment("half-month salary", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("overtime pay", String.class, null);
        payrollTbl.setColumnAlignment("overtime pay", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("legal holiday", String.class, null); 
        payrollTbl.setColumnAlignment("legal holiday", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("special holiday", String.class, null); 
        payrollTbl.setColumnAlignment("special holiday", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("night differential", String.class, null); 
        payrollTbl.setColumnAlignment("night differential", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("wdo", String.class, null);   
        payrollTbl.setColumnAlignment("wdo", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("absences", String.class, null); 
        payrollTbl.setColumnAlignment("absences", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("lates", String.class, null);   
        payrollTbl.setColumnAlignment("lates", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("undertime", String.class, null); 
        payrollTbl.setColumnAlignment("undertime", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("gross pay", String.class, null); 
        payrollTbl.setColumnAlignment("gross pay", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("sss", String.class, null); 
        payrollTbl.setColumnAlignment("sss", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("phic", String.class, null);
        payrollTbl.setColumnAlignment("phic", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("hdmf", String.class, null);  
        payrollTbl.setColumnAlignment("hdmf", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("tax", String.class, null); 
        payrollTbl.setColumnAlignment("tax", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("net pay", String.class, null);  
        payrollTbl.setColumnAlignment("net pay", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("cash bond", String.class, null);
        payrollTbl.setColumnAlignment("cash bond", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("allowance", String.class, null); 
        payrollTbl.setColumnAlignment("allowance", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("allowance for liquidation", String.class, null); 
        payrollTbl.setColumnAlignment("allowance for liquidation", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("advances to o/e", String.class, null);      
        payrollTbl.setColumnAlignment("advances to o/e", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("adjustments", String.class, null); 
        payrollTbl.setColumnAlignment("adjustments", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("amount to be receive", String.class, null); 
        payrollTbl.setColumnAlignment("amount to be receive", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("amount received", String.class, null); 
        payrollTbl.setColumnAlignment("amount received", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("for adjustments", String.class, null);
        payrollTbl.setColumnAlignment("for adjustments", Table.ALIGN_RIGHT);
        
        payrollTbl.addContainerProperty("payroll period", String.class, null);
        payrollTbl.addContainerProperty("payroll date", String.class, null);
        payrollTbl.addContainerProperty("status", String.class, null);    
        
        List<Payroll> payrollList = payrollService.getPayrollByBranchAndEmployee(branchId, employeeId);
        String payrollStatus = null;
        int i = 0;
        for(Payroll p : payrollList){
            if(p.getId() != 0){
                if(p.getRowStatus().equals("unlocked")){
                    payrollStatus = "";
                } else {
                    payrollStatus = "locked";
                }
                payrollTbl.addItem(new Object[]{
                    p.getId(), 
                    util.convertDateFormat(p.getAttendancePeriodFrom().toString()), 
                    util.convertDateFormat(p.getAttendancePeriodTo().toString()), 
                    util.roundOffToTwoDecimalPlaces(p.getBasicSalary()), 
                    util.roundOffToTwoDecimalPlaces(p.getHalfMonthSalary()), 
                    util.roundOffToTwoDecimalPlaces(p.getTotalOvertimePaid()), 
                    util.roundOffToTwoDecimalPlaces(p.getTotalLegalHolidayPaid()), 
                    util.roundOffToTwoDecimalPlaces(p.getTotalSpecialHolidayPaid()), 
                    util.roundOffToTwoDecimalPlaces(p.getTotalNightDifferentialPaid()), 
                    util.roundOffToTwoDecimalPlaces(p.getTotalWorkingDayOffPaid()), 
                    util.roundOffToTwoDecimalPlaces(p.getAbsences()), 
                    util.roundOffToTwoDecimalPlaces(p.getTotalLatesDeduction()), 
                    util.roundOffToTwoDecimalPlaces(p.getTotalUndertimeDeduction()), 
                    util.roundOffToTwoDecimalPlaces(p.getGrossPay()), 
                    util.roundOffToTwoDecimalPlaces(p.getSss()), 
                    util.roundOffToTwoDecimalPlaces(p.getPhic()), 
                    util.roundOffToTwoDecimalPlaces(p.getHdmf()), 
                    util.roundOffToTwoDecimalPlaces(p.getTax()), 
                    util.roundOffToTwoDecimalPlaces(p.getNetSalary()), 
                    util.roundOffToTwoDecimalPlaces(p.getCashBond()), 
                    util.roundOffToTwoDecimalPlaces(p.getAllowance()), 
                    util.roundOffToTwoDecimalPlaces(p.getAllowanceForLiquidation()), 
                    util.roundOffToTwoDecimalPlaces(p.getTotalAdvances()), 
                    util.roundOffToTwoDecimalPlaces(p.getAdjustment()), 
                    util.roundOffToTwoDecimalPlaces(p.getAmountToBeReceive()), 
                    util.roundOffToTwoDecimalPlaces(p.getAmountReceivable()), 
                    util.roundOffToTwoDecimalPlaces(p.getForAdjustments()), 
                    p.getPayrollPeriod(), 
                    util.convertDateFormat(p.getPayrollDate().toString()), 
                    payrollStatus
                }, new Integer(i));
            }
            i++;
        }
        payrollTbl.setPageLength(25);
        
        for(Object listener : payrollTbl.getListeners(ItemClickEvent.class)){
            payrollTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        payrollTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = payrollTbl.getItem(itemId);
                
                String payrollStatus = item.getItemProperty("status").toString().trim();
                
                if(event.getPropertyId().equals("advances to o/e")){
                    if(UserAccessControl.isAdvances()){
                        int payrollId = Integer.parseInt(item.getItemProperty("id").getValue().toString());
                        double amountToBeReceive = Double.parseDouble(item.getItemProperty("amount to be receive").toString());
                        double amountReceivable = Double.parseDouble(item.getItemProperty("amount received").toString());

                        Window subWindow = addAdvances(payrollId, amountToBeReceive, amountReceivable);
                        if(subWindow.getParent() == null){
                            getWindow().addWindow(subWindow);
                        }
                        subWindow.setModal(true);
                        subWindow.center();                        
                    } else {
                        getWindow().showNotification("You are not allowed to add/delete advances!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }                    
                }
                
                if(payrollStatus == null || payrollStatus.isEmpty()){                    
                } else {
                    getWindow().showNotification("Contact your DBA to unlock this ROW!!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(event.getPropertyId().equals("id")){
                    int payrollId = Integer.parseInt(item.getItemProperty("id").getValue().toString());
                    Window subWindow = removeSelectedRow(payrollId);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }                
                
                if(event.getPropertyId().equals("phic")){
                    int payrollId = Integer.parseInt(item.getItemProperty("id").getValue().toString());
                    double phicAmount = Double.parseDouble(item.getItemProperty("phic").toString());
                    double netPay = Double.parseDouble(item.getItemProperty("net pay").toString());
                    double amountToBeReceive = Double.parseDouble(item.getItemProperty("amount to be receive").toString());
                    double amountReceivable = Double.parseDouble(item.getItemProperty("amount received").toString());
                    
                    Window subWindow = updatePhicContribution(payrollId, 
                            phicAmount, 
                            netPay, 
                            amountToBeReceive, 
                            amountReceivable);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
                
                if(event.getPropertyId().equals("hdmf")){
                    int payrollId = Integer.parseInt(item.getItemProperty("id").getValue().toString());
                    double hdmfContribution = Double.parseDouble(item.getItemProperty("hdmf").toString());
                    double netPay = Double.parseDouble(item.getItemProperty("net pay").toString());
                    double amountToBeReceive = Double.parseDouble(item.getItemProperty("amount to be receive").toString());
                    double amountReceivable = Double.parseDouble(item.getItemProperty("amount received").toString());
                    
                    Window subWindow = updateHdmfContribution(payrollId, 
                            hdmfContribution, 
                            netPay, 
                            amountToBeReceive, 
                            amountReceivable);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
                
                if(event.getPropertyId().equals("sss")){
                    int payrollId = Integer.parseInt(item.getItemProperty("id").getValue().toString());
                    double sssContribution = Double.parseDouble(item.getItemProperty("sss").toString());
                    double netSalary = Double.parseDouble(item.getItemProperty("net pay").toString());
                    double amountToBeReceive = Double.parseDouble(item.getItemProperty("amount to be receive").toString());
                    double amountReceivable = Double.parseDouble(item.getItemProperty("amount received").toString());
                    
                    Window subWindow = updateSssContribution(payrollId, 
                            sssContribution, 
                            netSalary, 
                            amountToBeReceive, 
                            amountReceivable);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
                
                if(event.getPropertyId().equals("tax")){
                    int payrollId = Integer.parseInt(item.getItemProperty("id").getValue().toString());
                    double taxWitheldAmount = Double.parseDouble(item.getItemProperty("tax").toString());
                    double netSalary = Double.parseDouble(item.getItemProperty("net pay").toString());
                    double amountToBeReceive = Double.parseDouble(item.getItemProperty("amount to be receive").toString());
                    double amountReceivable = Double.parseDouble(item.getItemProperty("amount received").toString());
                    
                    Window subWindow = updateTaxWitheld(payrollId, 
                            taxWitheldAmount, 
                            netSalary, 
                            amountToBeReceive, 
                            amountReceivable);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
                
                if(event.getPropertyId().equals("payroll date")){
                    int payrollId = util.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                    Window subWindow = updatePayrollDate(payrollId);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow); 
                    }                    
                    subWindow.setModal(true);
                    subWindow.center();
                }
                
                if(event.getPropertyId().equals("status")){
                        int payrollId = util.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                        String status = null;
                        if(item.getItemProperty("status").toString() == null){                            
                        } else {
                            status = item.getItemProperty("status").getValue().toString();
                        }                    
                        
                        if(GlobalVariables.getUserRole().equals("accounting")){
                           if("locked".equals(status)){
                           } else {
                                Window subWindow = lockRow(payrollId);
                                if(subWindow.getParent() == null){
                                    getWindow().addWindow(subWindow); 
                                }                    
                                subWindow.setModal(true);
                                subWindow.center();
                            }
                        }else{
                            getWindow().showNotification("You dont have permission to LOCK this ROW!", Window.Notification.TYPE_WARNING_MESSAGE);
                        }                                                
                    }
            }
        });
    }
    
    public void employeeComboBox(final int branchId){ 
        this.branchId = branchId;        
        employeesName.removeAllItems();
        employeesName.setWidth("100%");
        employeesName.setNullSelectionAllowed(false);
        List<Employee> employeesList = employeeService.getEmployeePerBranchForDropDownList(branchId);        
        for(Employee e : employeesList){
            String name = e.getLastname()+ ", " + e.getFirstname() + " " + e.getMiddlename();
            employeesName.addItem(name.toUpperCase());
        }
        employeesName.addListener(new ComboBox.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(event.getProperty().getValue() == null){                    
                } else {
                    employeeId = employeeService.getEmployeeId(employeesName.getValue().toString());                    
                }
            }
        });
        employeesName.setImmediate(true);
    }
    
    private Window removeSelectedRow(final int id){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
                
        final Window subWindow = new Window("REMOVE SALARY", vlayout);
        subWindow.setWidth("200px");           
        
        Button removeSalary = new Button("REMOVE SALARY?");
        removeSalary.setWidth("100%");
        removeSalary.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(employeesName.getValue() == null){
                    subWindow.getWindow().showNotification("Select an Employee!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                Boolean result = payrollService.removeSelectedRow(id);
                if(result == true){
                    payrollTable(branchId, employeeId);
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    subWindow.getWindow().showNotification("UNABLE TO DELETE ROW!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(removeSalary);
        
        return subWindow;
    }
    
    private Window updatePhicContribution(final int payrollId, 
            final double phicAmount, 
            final double netPay, 
            final double amountToBeReceive, 
            final double amountReceive){
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("EDIT PHIC", vlayout);
        subWindow.setWidth("220px");
        
        final TextField phicNewAmount = new TextField("Amount: ");
        phicNewAmount.setWidth("100%");
        phicNewAmount.setValue(phicAmount);
        phicNewAmount.setNullSettingAllowed(false);
        subWindow.addComponent(phicNewAmount);
        
        Button updatePhicButton = new Button("UPDATE");
        updatePhicButton.setWidth("100%");
        updatePhicButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(phicNewAmount.getValue().toString().trim().isEmpty() || 
                        phicNewAmount.getValue() == null){
                    subWindow.getWindow().showNotification("Amount is REQUIRED!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                double newNetSalary = (netPay + phicAmount) - util.convertStringToDouble(phicNewAmount.getValue().toString().trim());
                double newAmountToBeReceive = (amountToBeReceive + phicAmount) - util.convertStringToDouble(phicNewAmount.getValue().toString().trim());
                double newAmountReceive = (amountReceive + phicAmount) - util.convertStringToDouble(phicNewAmount.getValue().toString().trim());
                
                boolean result = payrollService.updatePhicContribution(payrollId, 
                        util.convertStringToDouble(phicNewAmount.getValue().toString().trim()), 
                        newNetSalary, 
                        newAmountToBeReceive, 
                        newAmountReceive);
                
                if(result){
                    payrollTable(branchId, employeeId);
                    (subWindow.getParent()).removeWindow(subWindow);
                } else{
                    subWindow.getWindow().showNotification("UNABLE TO UPDATE ROW!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(updatePhicButton);
        
        return subWindow;
    }
    
    private Window updateHdmfContribution(final int payrollId, 
            final double hdmfContribution, 
            final double netPay, 
            final double amountToBeReceive, 
            final double amountReceive){
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("EDIT PHIC", vlayout);
        subWindow.setWidth("220px");
        
        final TextField newHdmfContribution = new TextField("Amount: ");
        newHdmfContribution.setWidth("100%");
        newHdmfContribution.setValue(hdmfContribution);
        newHdmfContribution.setNullSettingAllowed(false);
        subWindow.addComponent(newHdmfContribution);
        
        Button updatePhicButton = new Button("UPDATE");
        updatePhicButton.setWidth("100%");
        updatePhicButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(newHdmfContribution.getValue().toString().trim().isEmpty() || 
                        newHdmfContribution.getValue() == null){
                    subWindow.getWindow().showNotification("Amount is REQUIRED!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                double newNetSalary = (netPay + hdmfContribution) - util.convertStringToDouble(newHdmfContribution.getValue().toString().trim());
                double newAmountToBeReceive = (amountToBeReceive + hdmfContribution) - util.convertStringToDouble(newHdmfContribution.getValue().toString().trim());
                double newAmountReceive = (amountReceive + hdmfContribution) - util.convertStringToDouble(newHdmfContribution.getValue().toString().trim());
                
                boolean result = payrollService.updateHdmfContribution(payrollId, 
                        util.convertStringToDouble(newHdmfContribution.getValue().toString().trim()), 
                        newNetSalary, 
                        newAmountToBeReceive, 
                        newAmountReceive);
                
                if(result){
                    payrollTable(branchId, employeeId);
                    (subWindow.getParent()).removeWindow(subWindow);
                } else{
                    subWindow.getWindow().showNotification("UNABLE TO UPDATE ROW!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(updatePhicButton);
        
        return subWindow;
    }
    
    private Window updateSssContribution(final int payrollId, 
            final double sssContribution, 
            final double netSalary, 
            final double amountToBeReceive, 
            final double amountReceive){
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("EDIT SSS", vlayout);
        subWindow.setWidth("220px");
        
        final TextField newSssContribution = new TextField("Amount: ");
        newSssContribution.setWidth("100%");
        newSssContribution.setValue(sssContribution);
        newSssContribution.setNullSettingAllowed(false);
        subWindow.addComponent(newSssContribution);
        
        Button updatePhicButton = new Button("UPDATE");
        updatePhicButton.setWidth("100%");
        updatePhicButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(newSssContribution.getValue().toString().trim().isEmpty() || 
                        newSssContribution.getValue() == null){
                    subWindow.getWindow().showNotification("Amount is REQUIRED!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                double newNetSalary = (netSalary + sssContribution) - util.convertStringToDouble(newSssContribution.getValue().toString().trim());
                double newAmountToBeReceive = (amountToBeReceive + sssContribution) - util.convertStringToDouble(newSssContribution.getValue().toString().trim());
                double newAmountReceive = (amountReceive + sssContribution) - util.convertStringToDouble(newSssContribution.getValue().toString().trim());
                
                boolean result = payrollService.updateSssContribution(payrollId, 
                        util.convertStringToDouble(newSssContribution.getValue().toString().trim()), 
                        newNetSalary, 
                        newAmountToBeReceive, 
                        newAmountReceive);
                
                if(result){
                    payrollTable(branchId, employeeId);
                    (subWindow.getParent()).removeWindow(subWindow);
                } else{
                    subWindow.getWindow().showNotification("UNABLE TO UPDATE ROW!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(updatePhicButton);
        
        return subWindow;
    }
    
    private Window updateTaxWitheld(final int payrollId, 
            final double taxWitheldAmount, 
            final double netSalary, 
            final double amountToBeReceive, 
            final double amountReceive){
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("EDIT TAX", vlayout);
        subWindow.setWidth("220px");
        
        final TextField newTaxWitheldAmount = new TextField("Amount: ");
        newTaxWitheldAmount.setWidth("100%");
        newTaxWitheldAmount.setValue(taxWitheldAmount);
        newTaxWitheldAmount.setNullSettingAllowed(false);
        subWindow.addComponent(newTaxWitheldAmount);
        
        Button updatePhicButton = new Button("UPDATE");
        updatePhicButton.setWidth("100%");
        updatePhicButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(newTaxWitheldAmount.getValue().toString().trim().isEmpty() || 
                        newTaxWitheldAmount.getValue() == null){
                    subWindow.getWindow().showNotification("Amount is REQUIRED!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                double newNetSalary = (netSalary + taxWitheldAmount) - util.convertStringToDouble(newTaxWitheldAmount.getValue().toString().trim());
                double newAmountToBeReceive = (amountToBeReceive + taxWitheldAmount) - util.convertStringToDouble(newTaxWitheldAmount.getValue().toString().trim());
                double newAmountReceive = (amountReceive + taxWitheldAmount) - util.convertStringToDouble(newTaxWitheldAmount.getValue().toString().trim());
                
                boolean result = payrollService.updateTaxWitheldAmount(payrollId, 
                        util.convertStringToDouble(newTaxWitheldAmount.getValue().toString().trim()), 
                        newNetSalary, 
                        newAmountToBeReceive, 
                        newAmountReceive);
                
                if(result){
                    payrollTable(branchId, employeeId);
                    (subWindow.getParent()).removeWindow(subWindow);
                } else{
                    subWindow.getWindow().showNotification("UNABLE TO UPDATE ROW!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(updatePhicButton);
        
        return subWindow;
    }
    
    private Window lockRow(final int payrollId){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("DISABLE EDITING", vlayout);
        subWindow.setWidth("400px");
                
        Button button = new Button("ARE YOU SURE YOU WANT TO LOCK THIS ROW?");
        button.setWidth("100%");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {                
                Boolean result = payrollService.lockPayroll(payrollId);
                if(result == true){
                    payrollTable(branchId, employeeId);
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    subWindow.getWindow().showNotification("UNABLE TO UPDATE ROW!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(button);
        return subWindow;
    }
    
    private Window addAdvances(final int payrollId, 
            final double amountToBeReceive, 
            final double amountReceivable){       
        final Window subWindow = new Window("ADD/REMOVE ADVANCES");
        subWindow.setWidth("450px");
        
        TabSheet ts = new TabSheet();
        ts.addStyleName("bar");
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        vlayout.setCaption("View Advances");
        
        advanceTable(payrollId, amountToBeReceive, amountReceivable, subWindow);        
        vlayout.addComponent(advanceTbl);
        ts.addComponent(vlayout);
        
        vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        vlayout.setCaption("Post Advances");
        
        final ComboBox advanceType = dropDown.populateAdvanceTypeDropDownList(new ComboBox());
        advanceType.setNewItemsAllowed(true);
        advanceType.setNewItemHandler(new AbstractSelect.NewItemHandler() {

            @Override
            public void addNewItem(String newItemCaption) {
                AdvancesTypeBean advancesTypeBean = new AdvancesTypeBean();
                if (!advanceType.containsId(newItemCaption)) {
                    advancesTypeBean.setAdvancesType(newItemCaption);
                    Boolean result = payrollService.insertAdvanceType(newItemCaption);
                    if(result = true){
                        getWindow().showNotification("Added Type: " + newItemCaption);
                        lastAddedAdvanceType = true;
                        advanceType.addItem(newItemCaption);
                        advanceType.setValue(newItemCaption);
                    }
                }
            }
            
        });
        advanceType.setImmediate(true);
        advanceType.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (!lastAddedAdvanceType) {
                    getWindow().showNotification(
                            "Selected Type: " + event.getProperty());
                }
                lastAddedAdvanceType = false;
            }
            
        });
        vlayout.addComponent(advanceType);
        
        final TextField particulars = new TextField("Particulars:");
        particulars.setWidth("100%");
        vlayout.addComponent(particulars);
        
        final TextField amount = new TextField("Amount:");
        amount.setWidth("100%");
        vlayout.addComponent(amount);
        
        final PopupDateField datePosted = new PopupDateField("Date Posted:");
        datePosted.addStyleName("mydate");
        datePosted.setValue(new Date());
        datePosted.setWidth("100%");
        datePosted.setDateFormat("EEE - MMM dd, yyyy");
        datePosted.setLenient(true);
        datePosted.setResolution(DateField.RESOLUTION_DAY);
        vlayout.addComponent(datePosted);
        
        Button button = new Button("POST ADVANCE AMOUNT");
        button.setWidth("100%");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {       
                if(advanceType.getValue() == null){
                    getWindow().showNotification("Select Type!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(particulars.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Enter Particulars!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(!amount.getValue().toString().trim().isEmpty()){
                    boolean result = util.checkInputIfDouble(amount.getValue().toString().trim());
                    if(result == false){
                        getWindow().showNotification("Error entered Amount!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                    
                    if(Double.parseDouble(amount.getValue().toString().trim()) < 0){
                        getWindow().showNotification("Add advances on Payroll Register if Amount is less than 0!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                }else{
                    getWindow().showNotification("Please Enter an Amount!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                                
                List<Advances> advanceList = new ArrayList<Advances>();
                Advances a = new Advances();
                a.setId(payrollId);
                a.setAmountToBeReceive(amountToBeReceive);
                a.setAmountReceivable(amountReceivable);
                a.setAmount(util.convertStringToDouble(amount.getValue().toString().trim()));
                a.setDatePosted(util.parsingDate(util.convertDateFormat(datePosted.getValue().toString())));
                a.setAdvanceType(advanceType.getValue().toString());
                a.setParticulars(particulars.getValue().toString());
                advanceList.add(a);
                
                Double advances = Double.parseDouble(amount.getValue().toString().trim());
                String postedDate = util.convertDateFormat(datePosted.getValue().toString());
                Boolean result = payrollService.updateSalaryByAdvances(advanceList);                
                if(result == true){
                    payrollTable(branchId, employeeId);
                    (subWindow.getParent()).removeWindow(subWindow);
                }
            }
            
        }); 
        vlayout.addComponent(button);
        
        ts.addComponent(vlayout);       
        
        subWindow.addComponent(ts);
        
        return subWindow;
    }
    
    private Table advanceTable(final int payrollId, final double amountToBeReceive, 
            final double amountReceivable, final Window window){
                
        advanceTbl.removeAllItems();
        advanceTbl.setWidth("100%");
        advanceTbl.setImmediate(true);
        advanceTbl.setSelectable(true);
        
        advanceTbl.addContainerProperty("id", String.class, null);
        advanceTbl.addContainerProperty("amount", String.class, null);
        advanceTbl.addContainerProperty("type", String.class, null);
        advanceTbl.addContainerProperty("particulars", String.class, null);
        advanceTbl.addContainerProperty("date posted", String.class, null);
        
        int i = 0;
        List<Advances> advancesList = payrollService.getAdvancesByPayroll(payrollId);
        for(Advances a : advancesList){
            advanceTbl.addItem(new Object[]{
                a.getAdvanceId(), a.getAmount(), a.getAdvanceType(), 
                a.getParticulars(), util.convertDateFormat(a.getDatePosted().toString())
            }, new Integer(i));
            i++;
        }
        advanceTbl.setPageLength(advanceTbl.size());
        
        for(Object listener : advanceTbl.getListeners(ItemClickEvent.class)){
            advanceTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        advanceTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                final Item item = advanceTbl.getItem(itemId);
                              
                if(event.getPropertyId().equals("id")){
                    String amount = item.getItemProperty("amount").getValue().toString();                
                    Double removedAdvances = Double.valueOf(util.removeCommaFromString(amount)).doubleValue();
                    int advanceId = Integer.parseInt(item.getItemProperty("id").getValue().toString());
                        
                    Window subWindow = removeAdvances(payrollId, advanceId, removedAdvances, 
                            amountToBeReceive, amountReceivable, window);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }
            }
            
        });
        
        return advanceTbl;
    }
    
    private Window removeAdvances(final int payrollId, final int advanceId, final double removedAmount, final double amountToBeReceive, 
            final double amountReceivable, final Window window){
        final Window subWindow = new Window("REMOVE ADVANCES");
        subWindow.setWidth("220px");
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        
        final TextField remarks = new TextField("Remarks: ");
        remarks.setWidth("100%");
        vlayout.addComponent(remarks);
        
        Button removeAdvances = new Button("REMOVE ADVANCE AMOUNT");
        removeAdvances.setWidth("100%");
        removeAdvances.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(remarks.getValue() == null || remarks.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Add remarks!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                boolean result = payrollService.removeAdvanceById(advanceId, payrollId, 
                        removedAmount, amountToBeReceive, amountReceivable, remarks.getValue().toString());
                if(result == true){
                    payrollTable(branchId, employeeId);
                    advanceTable(payrollId, amountToBeReceive, amountReceivable, window);
                    (subWindow.getParent()).removeWindow(subWindow);
                    (window.getParent()).removeWindow(window);
                }
            }    

        });
        vlayout.addComponent(removeAdvances);
        
        subWindow.addComponent(vlayout);
        return subWindow;
    }

    public int getBranchId(){
        return branchId;
    }
    
    private Window updatePayrollDate(final int payrollId){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("EDIT PAYROLL DATE", vlayout);
        subWindow.setWidth("200px");
        
        final PopupDateField payrollDate = new PopupDateField("Payroll Date");
        payrollDate.addStyleName("mydate");
        payrollDate.setValue(new Date());
        payrollDate.setWidth("100%");
        payrollDate.setDateFormat("EEE - MMM dd, yyyy");
        payrollDate.setLenient(true);
        payrollDate.setResolution(DateField.RESOLUTION_DAY);
        subWindow.addComponent(payrollDate);
        
        Button button = new Button("SAVE?");
        button.setWidth("100%");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                String date = util.convertDateFormat(payrollDate.getValue().toString());
                Boolean result = payrollService.updatePayrollDate(payrollId, date);
                if(result == true){
                    payrollTable(branchId, employeeId);
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    subWindow.getWindow().showNotification("UNABLE TO UPDATE ROW!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(button);
        return subWindow;
    }
}
