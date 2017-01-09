/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.openhris.administrator.model.UserAccessControl;
import com.openhris.administrator.service.AdministratorService;
import com.openhris.administrator.serviceprovider.AdministratorServiceImpl;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.dao.ServiceUpdateDAO;
import com.openhris.model.Employee;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.openhris.model.Advances;
import com.openhris.model.Payroll;
import com.openhris.service.CompanyService;
import com.openhris.serviceprovider.PayrollServiceImpl;
import com.openhris.service.EmployeeService;
import com.openhris.service.PayrollService;
import com.openhris.service.TimekeepingService;
import com.openhris.serviceprovider.CompanyServiceImpl;
import com.openhris.timekeeping.serviceprovider.TimekeepingServiceImpl;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
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
    
    OpenHrisUtilities utililities = new OpenHrisUtilities();
    PayrollService payrollService = new PayrollServiceImpl();
    EmployeeService employeeService = new EmployeeServiceImpl();
    CompanyService companyService = new CompanyServiceImpl();
    DropDownComponent dropDown = new DropDownComponent();
    ServiceUpdateDAO serviceUpdate = new ServiceUpdateDAO();
    AdministratorService administratorService = new AdministratorServiceImpl();
    PayrollSubModules subModules = new PayrollSubModules();
    
    Table payrollTbl = new PayrollTableProperties();
    Table advanceTbl = new Table();
    private int branchId;
    String employeeId;
    boolean lastAddedAdvanceType;
    boolean isPayrollLocked;
    
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
        
        vsplit.setSplitPosition(90, Sizeable.UNITS_PIXELS);
        
        GridLayout glayout = new GridLayout(3, 1);
        glayout.setWidth("60%");
        glayout.setMargin(true);
        glayout.setSpacing(true);
        
        employeeComboBox(getBranchId());   
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
                
                payrollTable(getBranchId(), getEmployeeId());
            }
        });
        glayout.addComponent(generatePayrollButton, 1, 0);
        glayout.setComponentAlignment(generatePayrollButton, Alignment.BOTTOM_LEFT);
        
        Button exportTableToExcel = new Button();
	if(!UserAccessControl.isPayroll()){
            exportTableToExcel.setCaption("Export Table to Excel is Disabled");
            exportTableToExcel.setEnabled(UserAccessControl.isPayroll());
        } else {
            exportTableToExcel.setCaption("Export Table to Excel");
            exportTableToExcel.setEnabled(UserAccessControl.isPayroll());
        }
	exportTableToExcel.setWidth("100%");
	exportTableToExcel.addListener(new Button.ClickListener() {

	    private static final long serialVersionUID = -73954695086117200L;
            private ExcelExport excelExport;
		
		@Override
		public void buttonClick(Button.ClickEvent event) {
			
                    if(employeesName.getValue() == null){
                        getWindow().showNotification("Select an Employee!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    int tradeId = companyService.getTradeIdByBranchId(getBranchId());
                    excelExport = new ExcelExport(payrollTbl, "Payroll Ledger");
                    excelExport.excludeCollapsedColumns();
                    excelExport.setReportTitle(employeesName.getValue().toString().toUpperCase()+" Payroll Ledger "
                            +new Label(companyService.getBranchById(getBranchId()), Label.CONTENT_PREFORMATTED));
                    excelExport.setExportFileName(employeesName.getValue().toString().replace(" ", "_").replace(",", "_").toUpperCase()+"-Payroll Ledger"+".xls");
                    excelExport.export();
		}
	});
	glayout.addComponent(exportTableToExcel, 2, 0);
        glayout.setComponentAlignment(exportTableToExcel, Alignment.BOTTOM_LEFT);
        
        vsplit.setFirstComponent(glayout);        
        addComponent(vsplit);
        
        setExpandRatio(vsplit, 1.0f);
                                
        payrollTable(branchId, null);        
        vsplit.setSecondComponent(payrollTbl); 
    }
    
    public void payrollTable(int branchId, String employeeId){
        payrollTbl.removeAllItems();
        String payrollStatus = null;
        TimekeepingService tk = new TimekeepingServiceImpl();
        int i = 0;
        for(Payroll p : payrollService.findPayrollByBranchAndEmployee(branchId, employeeId)){
            if(p.getId() != 0){
                if(p.getRowStatus().equals("unlocked")){
                    payrollStatus = "";
                } else {
                    payrollStatus = "locked";
                }
                              
                payrollTbl.addItem(new Object[]{
                    p.getId(), 
                    p.getRate(), 
                    String.valueOf(p.getWageEntry()), 
                    utililities.convertDateFormat(p.getAttendancePeriodFrom().toString()), 
                    utililities.convertDateFormat(p.getAttendancePeriodTo().toString()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getBasicSalary()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getHalfMonthSalary()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getTotalOvertimePaid()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getTotalLegalHolidayPaid()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getTotalSpecialHolidayPaid()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getTotalNightDifferentialPaid()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getTotalWorkingDayOffPaid()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getAbsences()), 
                    utililities.roundOffToTwoDecimalPlaces(tk.getTotalLatesDeduction(p.getId())), 
                    utililities.roundOffToTwoDecimalPlaces(tk.getTotalUndertimeDeduction(p.getId())), 
                    utililities.roundOffToTwoDecimalPlaces(p.getGrossPay()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getSss()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getPhic()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getHdmf()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getTax()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getNetSalary()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getCashBond()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getCommunicationAllowance()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getPerDiemAllowance()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getColaAllowance()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getMealAllowance()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getTransportationAllowance()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getOtherAllowances()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getAllowanceForLiquidation()),                      
                    utililities.roundOffToTwoDecimalPlaces(p.getTotalAdvances()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getAdjustment()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getAmountToBeReceive()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getAmountReceivable()), 
                    utililities.roundOffToTwoDecimalPlaces(p.getForAdjustments()), 
                    p.getPayrollPeriod(), 
                    utililities.convertDateFormat(p.getPayrollDate().toString()), 
                    payrollStatus, 
                    p.getBranch().getTradeName(), 
                    p.getBranch().getBranchName()
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
                isPayrollLocked = ((payrollStatus.isEmpty() || payrollStatus == null) ? false : true);
                
                if(event.getPropertyId().equals("advances to o/e")){
                    if(UserAccessControl.isAdvances()){
                        int payrollId = utililities.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                        double amountToBeReceive = utililities.convertStringToDouble(item.getItemProperty("amount to be receive").toString());
                        double amountReceivable = utililities.convertStringToDouble(item.getItemProperty("amount received").toString());

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
                
                if(event.getPropertyId().equals("rate")){
                    int payrollId = utililities.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                    String rate = item.getItemProperty("rate").getValue().toString();
                    
                    Window subWindow = updatePayrollData("rate", rate, payrollId, item);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow); 
                    }                    
                    subWindow.setModal(true);
                    subWindow.center();
                }
                
                if(event.getPropertyId().equals("wage entry")){
                    int payrollId = utililities.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                    String wageEntry = item.getItemProperty("wage entry").getValue().toString();
                    
                    Window subWindow = updatePayrollData("wageEntry", wageEntry, payrollId, item);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow); 
                    }                    
                    subWindow.setModal(true);
                    subWindow.center();
                }
                
                if(payrollStatus.isEmpty()){                    
                } else {
                    getWindow().showNotification("Contact your DBA to unlock this ROW!!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                    return;
                }
                
                if(event.getPropertyId().equals("id")){
                    int payrollId = utililities.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                    Window subWindow = removeSelectedRow(payrollId);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.setModal(true);
                    subWindow.center();
                }                
                
                if(event.getPropertyId().equals("phic")){
                    int payrollId = utililities.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                    double phicAmount = utililities.convertStringToDouble(item.getItemProperty("phic").toString());
                    double netPay = utililities.convertStringToDouble(item.getItemProperty("net pay").toString());
                    double amountToBeReceive = utililities.convertStringToDouble(item.getItemProperty("amount to be receive").toString());
                    double amountReceivable = utililities.convertStringToDouble(item.getItemProperty("amount received").toString());
                    
                    if(UserAccessControl.isEditPhic()){
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
                    } else {
                        getWindow().showNotification("Edit PHIC isDisabled!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                }
                
                if(event.getPropertyId().equals("hdmf")){
                    int payrollId = utililities.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                    double hdmfContribution = utililities.convertStringToDouble(item.getItemProperty("hdmf").toString());
                    double netPay = utililities.convertStringToDouble(item.getItemProperty("net pay").toString());
                    double amountToBeReceive = utililities.convertStringToDouble(item.getItemProperty("amount to be receive").toString());
                    double amountReceivable = utililities.convertStringToDouble(item.getItemProperty("amount received").toString());
                    
                    if(UserAccessControl.isEditHdmf()){
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
                    } else {
                        getWindow().showNotification("Edit HDMF isDisabled!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                }
                
                if(event.getPropertyId().equals("sss")){
                    int payrollId = utililities.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                    double sssContribution = utililities.convertStringToDouble(item.getItemProperty("sss").toString());
                    double netSalary = utililities.convertStringToDouble(item.getItemProperty("net pay").toString());
                    double amountToBeReceive = utililities.convertStringToDouble(item.getItemProperty("amount to be receive").toString());
                    double amountReceivable = utililities.convertStringToDouble(item.getItemProperty("amount received").toString());
                    
                    if(UserAccessControl.isEditSss()){
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
                    } else {
                        getWindow().showNotification("Edit SSS isDisabled!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                }
                
                if(event.getPropertyId().equals("tax")){
                    int payrollId = utililities.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                    double taxWitheldAmount = utililities.convertStringToDouble(item.getItemProperty("tax").toString());
                    double netSalary = utililities.convertStringToDouble(item.getItemProperty("net pay").toString());
                    double amountToBeReceive = utililities.convertStringToDouble(item.getItemProperty("amount to be receive").toString());
                    double amountReceivable = utililities.convertStringToDouble(item.getItemProperty("amount received").toString());
                    
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
                    int payrollId = utililities.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                    Window subWindow = updatePayrollDate(payrollId);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow); 
                    }                    
                    subWindow.setModal(true);
                    subWindow.center();
                }
                                
                if(event.getPropertyId().equals("status")){
                    int payrollId = utililities.convertStringToInteger(item.getItemProperty("id").getValue().toString());
                    String status = null;
                    if(item.getItemProperty("status").toString() == null){                            
                    } else {
                        status = item.getItemProperty("status").getValue().toString();
                    }                    
                                                
                    if(UserAccessControl.isLockPayroll()){
                        Window subWindow = lockRow(payrollId);
                        subWindow.setModal(true);
                        subWindow.center();
                        if(subWindow.getParent() == null){
                            getWindow().addWindow(subWindow); 
                        }                             
                    } else {
                        getWindow().showNotification("You are not allowed to lock this row!", Window.Notification.TYPE_WARNING_MESSAGE);
                    }
                                                                        
                }                
            }
        });
    }
    
    public void employeeComboBox(int branchId){  
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
                    payrollTable(branchId, getEmployeeId());
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
                
                double newNetSalary;
                double newAmountToBeReceive;
                double newAmountReceive;
                boolean isPayrollAdjusted;
                
                if(payrollService.isPayrollAdjusted(payrollId)){
                     newNetSalary = (netPay + phicAmount) - utililities.convertStringToDouble(phicNewAmount.getValue().toString().trim());
                    newAmountToBeReceive = (amountToBeReceive + phicAmount) - utililities.convertStringToDouble(phicNewAmount.getValue().toString().trim());
                    newAmountReceive = amountReceive;
                    isPayrollAdjusted = true;
                } else {
                    newNetSalary = (netPay + phicAmount) - utililities.convertStringToDouble(phicNewAmount.getValue().toString().trim());
                    newAmountToBeReceive = (amountToBeReceive + phicAmount) - utililities.convertStringToDouble(phicNewAmount.getValue().toString().trim());
                    newAmountReceive = (amountReceive + phicAmount) - utililities.convertStringToDouble(phicNewAmount.getValue().toString().trim());
                
                    isPayrollAdjusted = false; 
                }
                
                boolean result = payrollService.updatePhicContribution(payrollId, 
                        utililities.convertStringToDouble(phicNewAmount.getValue().toString().trim()), 
                        newNetSalary, 
                        newAmountToBeReceive, 
                        newAmountReceive, 
                        isPayrollAdjusted);
                
                if(result){
                    payrollTable(branchId, getEmployeeId());
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
                
                double newNetSalary;
                double newAmountToBeReceive;
                double newAmountReceive;
                boolean isPayrollAdjusted;
                
                if(payrollService.isPayrollAdjusted(payrollId)){
                    newNetSalary = (netPay + hdmfContribution) - utililities.convertStringToDouble(newHdmfContribution.getValue().toString().trim());
                    newAmountToBeReceive = (amountToBeReceive + hdmfContribution) - utililities.convertStringToDouble(newHdmfContribution.getValue().toString().trim());
                    newAmountReceive = amountReceive;
                    isPayrollAdjusted = true;
                } else {
                    newNetSalary = (netPay + hdmfContribution) - utililities.convertStringToDouble(newHdmfContribution.getValue().toString().trim());
                    newAmountToBeReceive = (amountToBeReceive + hdmfContribution) - utililities.convertStringToDouble(newHdmfContribution.getValue().toString().trim());
                    newAmountReceive = (amountReceive + hdmfContribution) - utililities.convertStringToDouble(newHdmfContribution.getValue().toString().trim());
                    isPayrollAdjusted = false;
                }
                
                boolean result = payrollService.updateHdmfContribution(payrollId, 
                        utililities.convertStringToDouble(newHdmfContribution.getValue().toString().trim()), 
                        newNetSalary, 
                        newAmountToBeReceive, 
                        newAmountReceive, 
                        isPayrollAdjusted);
                
                if(result){
                    payrollTable(branchId, getEmployeeId());
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
                
                double newNetSalary;
                double newAmountToBeReceive;
                double newAmountReceive;
                boolean isPayrollAdjusted;
                
                if(payrollService.isPayrollAdjusted(payrollId)){
                    newNetSalary = (netSalary + sssContribution) - utililities.convertStringToDouble(newSssContribution.getValue().toString().trim());
                    newAmountToBeReceive = (amountToBeReceive + sssContribution) - utililities.convertStringToDouble(newSssContribution.getValue().toString().trim());
                    newAmountReceive = amountReceive;
                    isPayrollAdjusted = true;
                } else {
                    newNetSalary = (netSalary + sssContribution) - utililities.convertStringToDouble(newSssContribution.getValue().toString().trim());
                    newAmountToBeReceive = (amountToBeReceive + sssContribution) - utililities.convertStringToDouble(newSssContribution.getValue().toString().trim());
                    newAmountReceive = (amountReceive + sssContribution) - utililities.convertStringToDouble(newSssContribution.getValue().toString().trim());
                    isPayrollAdjusted = false;
                }
                
                boolean result = payrollService.updateSssContribution(payrollId, 
                        utililities.convertStringToDouble(newSssContribution.getValue().toString().trim()), 
                        newNetSalary, 
                        newAmountToBeReceive, 
                        newAmountReceive, 
                        isPayrollAdjusted);
                
                if(result){
                    payrollTable(branchId, getEmployeeId());
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
                
                double newNetSalary;
                double newAmountToBeReceive;
                double newAmountReceive;
                boolean isPayrollAdjusted;
                
                if(payrollService.isPayrollAdjusted(payrollId)){
                    newNetSalary = (netSalary + taxWitheldAmount) - utililities.convertStringToDouble(newTaxWitheldAmount.getValue().toString().trim());
                    newAmountToBeReceive = (amountToBeReceive + taxWitheldAmount) - utililities.convertStringToDouble(newTaxWitheldAmount.getValue().toString().trim());
                    newAmountReceive = amountReceive;
                    isPayrollAdjusted = true;
                } else {
                    newNetSalary = (netSalary + taxWitheldAmount) - utililities.convertStringToDouble(newTaxWitheldAmount.getValue().toString().trim());
                    newAmountToBeReceive = (amountToBeReceive + taxWitheldAmount) - utililities.convertStringToDouble(newTaxWitheldAmount.getValue().toString().trim());
                    newAmountReceive = (amountReceive + taxWitheldAmount) - utililities.convertStringToDouble(newTaxWitheldAmount.getValue().toString().trim());
                    isPayrollAdjusted = false;
                }
                
                boolean result = payrollService.updateTaxWitheldAmount(payrollId, 
                        utililities.convertStringToDouble(newTaxWitheldAmount.getValue().toString().trim()), 
                        newNetSalary, 
                        newAmountToBeReceive, 
                        newAmountReceive, 
                        isPayrollAdjusted);
                
                if(result){
                    payrollTable(branchId, getEmployeeId());
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
                    payrollTable(branchId, getEmployeeId());
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
        vlayout.setEnabled(!isPayrollLocked);
        
        final ComboBox advanceType = dropDown.populateAdvanceTypeDropDownList(new ComboBox());
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
                    boolean result = utililities.checkInputIfDouble(amount.getValue().toString().trim());
                    if(!result){
                        getWindow().showNotification("Enter numeric format for advances!", Window.Notification.TYPE_ERROR_MESSAGE);
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
                a.setAmount(utililities.convertStringToDouble(amount.getValue().toString().trim()));
                a.setDatePosted(utililities.parsingDate(utililities.convertDateFormat(datePosted.getValue().toString())));
                a.setAdvanceType(advanceType.getValue().toString());
                a.setParticulars(particulars.getValue().toString());
                advanceList.add(a);
                
                Double advances = utililities.convertStringToDouble(amount.getValue().toString().trim());
                String postedDate = utililities.convertDateFormat(datePosted.getValue().toString());
                Boolean result = payrollService.updateSalaryByAdvances(advanceList);                
                if(result == true){
                    payrollTable(branchId, getEmployeeId());
                    (subWindow.getParent()).removeWindow(subWindow);
                }
            }
            
        }); 
        vlayout.addComponent(button);
        
        ts.addComponent(vlayout);       
        
        subWindow.addComponent(ts);
        
        return subWindow;
    }
    
    private Table advanceTable(final int payrollId, 
            final double amountToBeReceive, 
            final double amountReceivable, 
            final Window window){
                
        advanceTbl.removeAllItems();
        advanceTbl.setWidth("100%");
        advanceTbl.setImmediate(true);
        advanceTbl.setSelectable(true);
        
        advanceTbl.addContainerProperty("id", String.class, null);
        advanceTbl.addContainerProperty("amount", Double.class, null);
        advanceTbl.addContainerProperty("type", String.class, null);
        advanceTbl.addContainerProperty("particulars", String.class, null);
        advanceTbl.addContainerProperty("date posted", String.class, null);
        
        int i = 0;
        List<Advances> advancesList = payrollService.getAdvancesByPayroll(payrollId);
        for(Advances a : advancesList){
            advanceTbl.addItem(new Object[]{
                a.getAdvanceId(), a.getAmount(), a.getAdvanceType(), 
                a.getParticulars(), utililities.convertDateFormat(a.getDatePosted().toString())
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
                    Double removedAdvances = Double.valueOf(utililities.removeCommaFromString(amount));
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
    
    private Window removeAdvances(final int payrollId, 
            final int advanceId, 
            final double removedAmount, 
            final double amountToBeReceive, 
            final double amountReceivable, 
            final Window window){
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
                    payrollTable(branchId, getEmployeeId());
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

    private int getBranchId(){
        return branchId;
    }
    
    public void setBranchId(int branchId){
        this.branchId = branchId;
    }
    
    private String getEmployeeId(){
        return employeeId;
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
                String date = utililities.convertDateFormat(payrollDate.getValue().toString());
                Boolean result = payrollService.updatePayrollDate(payrollId, date);
                if(result == true){
                    payrollTable(branchId, getEmployeeId());
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    subWindow.getWindow().showNotification("UNABLE TO UPDATE ROW!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(button);
        return subWindow;
    }

    private Window updatePayrollData(final String column, String value, final int payrollId, final Item item){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("EDIT PAYROLL DATA", vlayout);
        subWindow.setWidth("200px");
        
        final TextField colTextField = new TextField(column+":");
        colTextField.setWidth("100%");
        colTextField.setValue(value);
        vlayout.addComponent(colTextField);
        
        Button button = new Button("SAVE?");
        button.setWidth("100%");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(colTextField.getValue() == null || colTextField.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Add Value!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                boolean result = payrollService.update(column, colTextField.getValue().toString(), payrollId);
                if(result == true){
                    item.getItemProperty("id").setValue(colTextField.getValue().toString());
                    payrollTable(getBranchId(), getEmployeeId());
                    (subWindow.getParent()).removeWindow(subWindow);
                }
            }
            
        });
        subWindow.addComponent(button);
        return subWindow;
    }
}
