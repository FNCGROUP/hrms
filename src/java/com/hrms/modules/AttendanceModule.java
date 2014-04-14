/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.modules;

import com.openhris.administrator.model.UserAccessControl;
import com.hrms.beans.AdvancesTypeBean;
import com.hrms.classes.*;
import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.entities.Attendance;
import com.hrms.queries.*;
import com.hrms.utilities.ConvertionUtilities;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class AttendanceModule extends VerticalLayout{

    GetSQLQuery query = new GetSQLQuery();
    GetSQLQueryUpdate queryUpdate = new GetSQLQueryUpdate();
    ConvertionUtilities conUtil = new ConvertionUtilities();
    
    CorporateName corporateNames = new CorporateName();
    TradeName tradeNames = new TradeName();
    BranchName branchNames = new BranchName();
    EmployeesListPerBranch employeesListPerBranch = new EmployeesListPerBranch();
    SalaryComputation salaryComputation = new SalaryComputation();
    AttendanceProcess attendanceProcess = new AttendanceProcess();
    NativeSelect corporateName;
    NativeSelect tradeName;
    NativeSelect branchName;
    NativeSelect employeesName;
    Table newAttendanceTable = new Table();
    Table newEmployeeSalaryTable = new Table();
    Table attendanceTablePreview = new Table();
    Table newAdvancesTable = new Table();
    Table editAttendanceTable = new Table();
    Button saveButton = new Button();
    
    String entry = null;
    Double wage = null;
    String attendanceDateFrom = null;
    String attendanceDateTo = null;
    String salaryDate = null;
    
    Double deduction = null;
    Double lateDeduction = null;
    Double undertimeDeduction = null;
    Double latePerMinute = null;
    Double deductionAmount = null;
    
    Double latesPremiumRate = 0.0;
    Double undertimePremiumRate = 0.0;
    Double overtimePremiumRate = 0.0;
    
    Double addition = null;
    Double overtimeAddition = null;
    Double nightDifferentialAddition = null;
    Double nightDifferentialPerMinute = null;
    Double overtimePerMinute = null;
    Double additionAmount = null;
    
    Double totalDeduction = null;
    Double totaAddition = null;            
    
    String salaryId;
    String net;
    String receivable;
    Object advancesToOE;
    Boolean editRow= false;
    String employmentWageEntry;
    
    private String userRole;
    
    DecimalFormat df = new DecimalFormat("0.00");
    private String employeeId;
    private Boolean lastAdded = false;
    
    public AttendanceModule(String userRole){
        
        this.userRole = userRole;
        corporateNames.setUserRole(userRole);
        tradeNames.setUserRole(userRole);
        branchNames.setUserRole(userRole);
        
        if(userRole.equals("administrator")){
            UserAccessControl.setTimekeeping(false);
        }
        
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
        
        vsplit.setSplitPosition(32, Sizeable.UNITS_PERCENTAGE);
        GridLayout comboBoxGrid = new GridLayout(3, 3);
        comboBoxGrid.setMargin(true);
        comboBoxGrid.setSpacing(true);
        comboBoxGrid.setWidth(90, Sizeable.UNITS_PERCENTAGE);
        
        vsplit.setFirstComponent(comboBoxGrid);
        addComponent(vsplit);
        
        setExpandRatio(vsplit, 1.0f);
        
        corporateName = new NativeSelect("Select Corporate Name:");
        corporateName.setWidth("270px");
        corporateNames.getCorporateName(corporateName);
        comboBoxGrid.addComponent(corporateName, 0, 0);
        
        tradeName = new NativeSelect("Select Trade Name:"); 
        tradeName.setWidth("270px");
        corporateName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                tradeNames.getTradeName(tradeName, corporateName.getValue().toString());
            }
            
        });        
        comboBoxGrid.addComponent(tradeName, 1, 0);
        
        branchName = new NativeSelect("Select Branch:");
        branchName.setWidth("270px");        
        tradeName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(tradeName.getValue() == null){
                    getWindow().showNotification("Select a Trade Name!");
                }else{
                    branchNames.getBranchName(branchName, tradeName.getValue().toString(), corporateName.getValue().toString());
                }                
            }
        });
        comboBoxGrid.addComponent(branchName, 2, 0);
                
        employeesName = new NativeSelect("Select Employee:");
        employeesName.setWidth("270px");
        branchName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(branchName.getValue() == null){
                    getWindow().showNotification("Select a Branch!");
                }else{
                    employeesListPerBranch.getEmployeesListPerBranch(employeesName, 
                            query.getBranchId(branchName.getValue().toString(), tradeName.getValue().toString(), 
                            corporateName.getValue().toString()));
                }
            }
            
        });
        employeesName.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(employeesName.getValue()== null){
                    getWindow().showNotification("Select an Employee!");
                }else{
                    employeeId = query.getEmployeesId(employeesName.getValue().toString());
                    salaryComputation.getEmploymentWageAndEntry(employeeId);
                    entry = salaryComputation.getEmploymentWageEntry();
                    wage = salaryComputation.getEmploymentWage(); 
                    employmentWageEntry = salaryComputation.getEmploymentWageEntry();
                    if(entry.equals("monthly")){
                        wage = (wage * 12) / 314;
                    }
                    
                    employeeSalaryLedgerTable();
                }                
            }
            
        });        
        comboBoxGrid.addComponent(employeesName, 0, 1);
        
        final PopupDateField attendancePeriodFrom = new PopupDateField("Attendance Period From: ");
        attendancePeriodFrom.addStyleName("mydate");
        attendancePeriodFrom.setValue(new Date());
        attendancePeriodFrom.setWidth("270px");
        attendancePeriodFrom.setDateFormat("EEE - MMM dd, yyyy");
        attendancePeriodFrom.setLenient(true);
        attendancePeriodFrom.setResolution(DateField.RESOLUTION_DAY);
        comboBoxGrid.addComponent(attendancePeriodFrom, 1, 1);
        
        final PopupDateField attendancePeriodTo = new PopupDateField("Attendance Period To: ");
        attendancePeriodTo.addStyleName("mydate");
        attendancePeriodTo.setValue(new Date());
        attendancePeriodTo.setWidth("270px");
        attendancePeriodTo.setDateFormat("EEE - MMM dd, yyyy");
        attendancePeriodTo.setLenient(true);
        attendancePeriodTo.setResolution(DateField.RESOLUTION_DAY);
        comboBoxGrid.addComponent(attendancePeriodTo, 2, 1);
        
        final PopupDateField payrollDate = new PopupDateField("Payroll Date");
        payrollDate.addStyleName("mydate");
        payrollDate.setValue(new Date());
        payrollDate.setWidth("270px");
        payrollDate.setDateFormat("EEE - MMM dd, yyyy");
        payrollDate.setLenient(true);
        payrollDate.setResolution(DateField.RESOLUTION_DAY);
        comboBoxGrid.addComponent(payrollDate, 0, 2);
        
        final ComboBox payrollPeriod = new ComboBox("Payroll Period: ");
        payrollPeriod.setWidth("270px");
        payrollPeriod.setNullSelectionAllowed(false);
        payrollPeriod.setImmediate(true);
        payrollPeriod.addItem("15th of the month");
        payrollPeriod.addItem("30th of the month");
        comboBoxGrid.addComponent(payrollPeriod, 1, 2);
        
        final Button attendanceTable = new Button("Generate Attendance Table");
        attendanceTable.setWidth("270px");
        
        for(Object listener : attendanceTable.getListeners(ClickListener.class)){
            attendanceTable.removeListener(ClickListener.class, listener);
        }

        attendanceTable.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(UserAccessControl.isTimekeeping()== true){
                    if(employeesName.getValue() == null){
                        getWindow().showNotification("Select an Employee!");
                        return;
                    }

                    if(payrollPeriod.getValue() == null){
                        getWindow().showNotification("Select Payroll Period!");   
                        return;
                    }

                    if(payrollDate.getValue() == null){
                        getWindow().showNotification("Select Payroll Date!");
                        return;
                    }

                    attendanceDateFrom = conUtil.convertDateFormat(attendancePeriodFrom.getValue().toString());
                    attendanceDateTo = conUtil.convertDateFormat(attendancePeriodTo.getValue().toString());
                    salaryDate = conUtil.convertDateFormat(payrollDate.getValue().toString());
                    
                    Date parsedAttendanceDateFrom = conUtil.parsingDate(attendanceDateFrom);
                    Date parsedAttendanceDateTo = conUtil.parsingDate(attendanceDateTo);
                    Date parsedSalaryDate = conUtil.parsingDate(salaryDate);

                    boolean attendanceDateFromExist = query.checkDateIfExist(attendanceDateFrom, employeeId);
                    boolean attendanceDateToExist = query.checkDateIfExist(attendanceDateTo, employeeId);

                    if(attendanceDateFromExist == false || attendanceDateToExist == false){
                        getWindow().showNotification("Attendance Date Range Already Exist", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }

                    if(parsedAttendanceDateTo.before(parsedAttendanceDateFrom)){
                        getWindow().showNotification("Error Attendance Date Entry!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }

                    if(parsedSalaryDate.before(parsedAttendanceDateTo) || parsedSalaryDate.equals(parsedAttendanceDateTo)){
                        getWindow().showNotification("Error Payroll Date Entry!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }                      
                    
                    Boolean rowStatusResult = query.checkEmployeesStatus(employeeId);

                    List dateLists = null;
                    
                    if(rowStatusResult == false){
                        String status = query.getEmploymentStatus(employeeId);
                        String dateEnd;
                        Date parseDateEnd;
                        if(status != null){
                            dateEnd = query.getEmployeesEndDate(employeeId);
                            
                            if(dateEnd == null){
                                getWindow().showNotification("Employee has been removed!", Window.Notification.TYPE_ERROR_MESSAGE);
                                return;
                            }
                            
                            parseDateEnd = conUtil.parsingDate(dateEnd);
                            
                            if(parseDateEnd.before(parsedAttendanceDateFrom)){
                                getWindow().showNotification("RESIGNED!", Window.Notification.TYPE_ERROR_MESSAGE);
                                return;
                            }
                            if(parseDateEnd.before(parsedAttendanceDateTo)){
                                attendanceDateTo = dateEnd;
                            }                        
                            dateLists = obtainPayrollDateList(attendanceDateFrom, attendanceDateTo); 
                        }                                                               
                    }else{
                        String dateEntry = query.getEmployeesEntryDate(employeeId);
                        Date parseDateEntry = conUtil.parsingDate(dateEntry);
                        if(parsedAttendanceDateFrom.before(parseDateEntry)){
                            attendanceDateFrom = dateEntry;
                            dateLists = obtainPayrollDateList(attendanceDateFrom, attendanceDateTo);
                        }else{
                            dateLists = obtainPayrollDateList(attendanceDateFrom, attendanceDateTo);
                            if(dateLists.size() < 14){
                                getWindow().showNotification("Payroll Date Range Error!", Window.Notification.TYPE_ERROR_MESSAGE);
                                return;
                            }
                        }
                        
                    }
                                      
                    salaryId = null;
                    Window subWindow = new Window("ATTENDANCE TABLE for "+employeesName.getValue());
                    subWindow = (Window) attendanceTableContainer(subWindow, newAttendanceTable, dateLists, 
                            payrollPeriod.getValue().toString(), salaryDate, salaryId);
                    subWindow.setModal(true);
                    if(subWindow.getParent() == null){
                        getWindow().addWindow(subWindow);
                    }
                    subWindow.center();
                }else{
                    getWindow().showNotification("Access Disabled", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }
            }
        });
        attendanceTable.setImmediate(true);
        
        comboBoxGrid.addComponent(attendanceTable, 2, 2);
        comboBoxGrid.setComponentAlignment(attendanceTable, Alignment.BOTTOM_LEFT);               
        
        vsplit.setSecondComponent(newEmployeeSalaryTable);
    }    
           
    public final Table employeeSalaryLedgerTable(){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        newEmployeeSalaryTable.removeAllItems();
        newEmployeeSalaryTable.setSizeFull();
        newEmployeeSalaryTable.setImmediate(true);
        newEmployeeSalaryTable.setSelectable(true);
        newEmployeeSalaryTable.setColumnCollapsingAllowed(true);
        
        newEmployeeSalaryTable.addContainerProperty("id", String.class, null);        
        newEmployeeSalaryTable.addContainerProperty("start date", String.class, null);
        newEmployeeSalaryTable.addContainerProperty("cut-off date", String.class, null);
        newEmployeeSalaryTable.addContainerProperty("basic salary", String.class, null); 
        newEmployeeSalaryTable.setColumnAlignment("basic salary", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("half-month salary", String.class, null);
        newEmployeeSalaryTable.setColumnAlignment("half-month salary", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("overtime pay", String.class, null);
        newEmployeeSalaryTable.setColumnAlignment("overtime pay", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("legal holiday", String.class, null); 
        newEmployeeSalaryTable.setColumnAlignment("legal holiday", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("special holiday", String.class, null); 
        newEmployeeSalaryTable.setColumnAlignment("special holiday", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("night differential", String.class, null); 
        newEmployeeSalaryTable.setColumnAlignment("night differential", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("wdo", String.class, null);   
        newEmployeeSalaryTable.setColumnAlignment("wdo", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("absences", String.class, null); 
        newEmployeeSalaryTable.setColumnAlignment("absences", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("lates", String.class, null);   
        newEmployeeSalaryTable.setColumnAlignment("lates", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("undertime", String.class, null); 
        newEmployeeSalaryTable.setColumnAlignment("undertime", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("gross pay", String.class, null); 
        newEmployeeSalaryTable.setColumnAlignment("gross pay", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("sss", String.class, null); 
        newEmployeeSalaryTable.setColumnAlignment("sss", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("phic", String.class, null);
        newEmployeeSalaryTable.setColumnAlignment("phic", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("hdmf", String.class, null);  
        newEmployeeSalaryTable.setColumnAlignment("hdmf", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("tax", String.class, null); 
        newEmployeeSalaryTable.setColumnAlignment("tax", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("net pay", String.class, null);  
        newEmployeeSalaryTable.setColumnAlignment("net pay", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("cash bond", String.class, null);
        newEmployeeSalaryTable.setColumnAlignment("cash bond", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("allowance", String.class, null); 
        newEmployeeSalaryTable.setColumnAlignment("allowance", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("allowance for liquidation", String.class, null); 
        newEmployeeSalaryTable.setColumnAlignment("allowance for liquidation", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("advances to o/e", String.class, null);      
        newEmployeeSalaryTable.setColumnAlignment("advances to o/e", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("adjustments", String.class, null); 
        newEmployeeSalaryTable.setColumnAlignment("adjustments", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("amount to be receive", String.class, null); 
        newEmployeeSalaryTable.setColumnAlignment("amount to be receive", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("amount received", String.class, null); 
        newEmployeeSalaryTable.setColumnAlignment("amount receive", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("for adjustments", String.class, null);
        newEmployeeSalaryTable.setColumnAlignment("for adjustments", Table.ALIGN_RIGHT);
        newEmployeeSalaryTable.addContainerProperty("payroll period", String.class, null);
        newEmployeeSalaryTable.addContainerProperty("payroll date", String.class, null);
        newEmployeeSalaryTable.addContainerProperty("status", String.class, null);    
        newEmployeeSalaryTable.addContainerProperty("taxable salary", String.class, null); 
        
        newEmployeeSalaryTable.setColumnCollapsed("taxable salary", true);
        
        try {
            int i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(" SELECT id, attendancePeriodFrom, attendancePeriodTo, basicSalary, halfMonthSalary, "
                    + "philhealth, sss, hdmf, absences, taxableSalary, tax, netSalary, cashBond, advances, allowance, "
                    + "allowanceForLiquidation, amountToBeReceive, amountReceivable, ifnull(forAdjustments, 0) AS forAdjustments, "
                    + "totalOvertimePaid, totalLegalHolidayPaid, totalSpecialHolidayPaid, totalNightDifferentialPaid, totalWorkingDayOffPaid, "
                    + "totalLatesDeduction, totalUndertimeDeduction, "
                    + "(select ifnull(sum(`adjustments`.`amount`), 0) from `adjustments` "
                    + "where (`adjustments`.`salaryId` = `s`.`id`)) AS adjustments, payrollPeriod, payrollDate, rowStatus FROM salary s "
                    + "where employeeId = '"+employeeId+"' ORDER BY payrollDate DESC, id DESC");
            while(rs.next()){
                newEmployeeSalaryTable.addItem(new Object[]{
                    rs.getString("id"),
                    rs.getString("attendancePeriodFrom"),
                    rs.getString("attendancePeriodTo"),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("basicSalary"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("halfMonthSalary"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("totalOvertimePaid"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("totalLegalHolidayPaid"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("totalSpecialHolidayPaid"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("totalNightDifferentialPaid"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("totalWorkingDayOffPaid"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("absences"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("totalLatesDeduction"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("totalUndertimeDeduction"))),
                    (conUtil.formatValueWithComma(
                        (conUtil.convertStringToDouble(rs.getString("halfMonthSalary"))
                        +conUtil.convertStringToDouble(rs.getString("totalOvertimePaid"))
                        +conUtil.convertStringToDouble(rs.getString("totalLegalHolidayPaid"))
                        +conUtil.convertStringToDouble(rs.getString("totalSpecialHolidayPaid"))
                        +conUtil.convertStringToDouble(rs.getString("totalNightDifferentialPaid"))
                        +conUtil.convertStringToDouble(rs.getString("totalWorkingDayOffPaid"))) - 
                        (conUtil.convertStringToDouble(rs.getString("absences"))
                        +conUtil.convertStringToDouble(rs.getString("totalLatesDeduction"))
                        +conUtil.convertStringToDouble(rs.getString("totalUndertimeDeduction")))
                    )),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("sss"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("philhealth"))),                    
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("hdmf"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("tax"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("netSalary"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("cashBond"))),                    
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("allowance"))), 
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("allowanceForLiquidation"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("advances"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("adjustments"))),
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("amountToBeReceive"))), 
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("amountReceivable"))), 
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("forAdjustments"))),
                    rs.getString("payrollPeriod"), 
                    rs.getString("payrollDate"),
                    rs.getString("rowStatus"), 
                    conUtil.formatValueWithComma(conUtil.convertStringToDouble(rs.getString("taxableSalary")))
                }, new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Object listener : newEmployeeSalaryTable.getListeners(ItemClickEvent.class)){
            newEmployeeSalaryTable.removeListener(ItemClickEvent.class, listener);
        }
        
        newEmployeeSalaryTable.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                try{
                    Object itemId = event.getItemId();
                    Item item = newEmployeeSalaryTable.getItem(itemId);

                    Object loanItemId = event.getPropertyId();
                    newEmployeeSalaryTable.refreshRowCache();
                                        
                    String payrollDate = item.getItemProperty("payroll date").getValue().toString();
                    String id = item.getItemProperty("id").getValue().toString();
                    String rowSalaryResult = item.getItemProperty("status").getValue().toString();
                    
                    if(event.getPropertyId().equals("payroll period")){
                        String attendanceDateFrom = item.getItemProperty("start date").getValue().toString();
                        String attendanceDateTo = item.getItemProperty("cut-off date").getValue().toString();
                        salaryId = item.getItemProperty("id").getValue().toString();
                        
                        Window subWindow = new Window("ATTENDANCE DATA PREVIEW for "+employeesName.getValue());
                        subWindow = previewAttendanceTable(attendanceDateFrom, attendanceDateTo, subWindow, salaryId);
                        if(subWindow.getParent() == null){
                            getWindow().addWindow(subWindow); 
                        }                    
                        subWindow.setModal(true);
                        subWindow.center();                        
                    }
                    
                    if(event.getPropertyId().equals("advances to o/e")){
                        if(UserAccessControl.isAdvances()== true){
                            salaryId = item.getItemProperty("id").getValue().toString();
                            Double netSalary = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("net pay").getValue().toString()));
                            Double amountReceivable = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("amount received").getValue().toString()));
                            Double amountToBeReceive = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("amount to be receive").getValue().toString()));
                            Double adjustments = 0.00;
                            
                            if(item.getItemProperty("adjustments").getValue() != null){
                                adjustments = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("adjustments").getValue().toString()));
                            }
                            
                            Window subWindow = addAdvances(rowSalaryResult, salaryId, netSalary, amountToBeReceive, amountReceivable, adjustments);
                            if(subWindow.getParent() == null){
                                getWindow().addWindow(subWindow);
                            }
                            subWindow.setModal(true);
                            subWindow.center();
                        }else{
                            getWindow().showNotification("Access Disabled", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            return;
                        }
                    } 
                    
                    if(event.getPropertyId().equals("cut-off date")){
                        if(UserAccessControl.isAdjustPayroll()== true){
                            salaryId = item.getItemProperty("id").getValue().toString();
                            String start_date = item.getItemProperty("start date").getValue().toString();
                            String cut_off_date = item.getItemProperty("cut-off date").getValue().toString();
                            String payroll_date = item.getItemProperty("payroll date").getValue().toString();
                            String payroll_period = item.getItemProperty("payroll period").getValue().toString();
                            List dateList = obtainPayrollDateList(start_date, cut_off_date);    
                            
                            Window subWindow = (Window) editAttendanceTableContainer(start_date, cut_off_date, editAttendanceTable, payroll_period, 
                                    payroll_date, salaryId);
                            
                            if(subWindow.getParent() == null){
                                getWindow().addWindow(subWindow);
                            }
                            subWindow.setModal(true);
                            subWindow.center();
                        }else{
                            getWindow().showNotification("You are not allowed to Edit Attendance!", Window.Notification.TYPE_WARNING_MESSAGE);
                            return;
                        }
                    }
                    
                    if(event.getPropertyId().equals("adjustments")){
                        Integer rowId = Integer.parseInt(item.getItemProperty("id").toString());
                        Window subWindow = viewAdjustments(rowId);
                        if(subWindow.getParent() == null){
                            getWindow().addWindow(subWindow); 
                        }                    
                        subWindow.setModal(true);
                        subWindow.center();
                    }
                    
                    if(rowSalaryResult.equals("locked")){
                        getWindow().showNotification("Contact your DBA to unlock this ROW!!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }                                       
                    
                    if(event.getPropertyId().equals("id")){
                        if(UserAccessControl.isPayroll()== true){                        
                            salaryId = item.getItemProperty("id").getValue().toString();
                            Window subWindow = deleteSelectedRow(salaryId);
                            if(subWindow.getParent() == null){
                                getWindow().addWindow(subWindow);
                            }
                            subWindow.setModal(true);
                            subWindow.center();
                        }else{
                            getWindow().showNotification("Access Disabled", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            return;
                        }
                    }                    
                    
                    if(event.getPropertyId().equals("phic")){
                        if(UserAccessControl.isContributions()== true){                        
                            salaryId = item.getItemProperty("id").getValue().toString();
                            Double advances = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("advances to o/e").getValue().toString()));
                            Double phic = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("phic").getValue().toString()));
                            double cashBond = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("cash bond").getValue().toString()));
                            if(phic == 0){
                                getWindow().showNotification("No Remitances(PHIC/HDMF) to be REMOVED!", Window.Notification.TYPE_ERROR_MESSAGE);
                                return;
                            }
                            if(advances != 0){
                                getWindow().showNotification("Remove Advances before removing Phic!", Window.Notification.TYPE_WARNING_MESSAGE);
                                return;
                            }
                            if(cashBond != 0){
                                getWindow().showNotification("Remove Cash Bond before removing SSS!", Window.Notification.TYPE_WARNING_MESSAGE);
                                return;
                            }
                            Double hdmf = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("hdmf").getValue().toString()));
                            Double allowance = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("allowance").getValue().toString()));
                            Double tax = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("tax").getValue().toString()));
                            Double taxableSalary = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("taxable salary").getValue().toString()));
                            Double adjustments = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("adjustments").getValue().toString()));
                            Double absences = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("absences").getValue().toString()));
                            
                            Double taxable_salary = (taxableSalary + phic + hdmf) - absences;
                            
                            Double netPay = taxable_salary;
                            Double amountReceivable = netPay + allowance;
                            Double amountToBeReceive = Math.round((amountReceivable + adjustments)*100.0)/100.0;                           
                            
                            Window subWindow = removePhicAndHdmf(taxable_salary, netPay, amountToBeReceive, amountReceivable, salaryId);
                            if(subWindow.getParent() == null){
                                getWindow().addWindow(subWindow);
                            }
                            subWindow.setModal(true);
                            subWindow.center();
                        }else{
                            getWindow().showNotification("Access Disabled", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            return;
                        }
                    }
                        
                    if(event.getPropertyId().equals("sss")){
                        if(UserAccessControl.isContributions()== true){
                            salaryId = item.getItemProperty("id").getValue().toString();
                            double sss = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("sss").getValue().toString()));
                            double advances = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("advances to o/e").getValue().toString()));
                            double cashBond = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("cash bond").getValue().toString()));
                            if(sss == 0){
                                getWindow().showNotification("No Remitances(SSS) to be REMOVED!", Window.Notification.TYPE_WARNING_MESSAGE);
                                return;
                            }
                            if(advances != 0){
                                getWindow().showNotification("Remove Advances before removing SSS!", Window.Notification.TYPE_WARNING_MESSAGE);
                                return;
                            }
                            if(cashBond != 0){
                                getWindow().showNotification("Remove Cash Bond before removing SSS!", Window.Notification.TYPE_WARNING_MESSAGE);
                                return;
                            }
                            Double allowance = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("allowance").getValue().toString()));
                            Double taxableSalary = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("taxable salary").getValue().toString()));
                            Double adjustments = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("adjustments").getValue().toString()));
                            
                            Double taxable_salary = taxableSalary + sss;
                            Double netPay = taxable_salary;
                            Double amountReceivable = netPay + allowance;
                            Double amountToBeReceive = Math.round((amountReceivable + adjustments)*100.0)/100.0; 
                            
                            Window subWindow = removeSss(taxable_salary, netPay, amountToBeReceive, amountReceivable, salaryId);
                            if(subWindow.getParent() == null){
                                getWindow().addWindow(subWindow);
                            }
                            subWindow.setModal(true);
                            subWindow.center();
                        }else{
                            getWindow().showNotification("Access Disabled", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            return;
                        }
                    }
                        
                    if(event.getPropertyId().equals("hdmf")){
                        if(UserAccessControl.isContributions()== true){
                            salaryId = item.getItemProperty("id").getValue().toString();
                            double advances = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("advances to o/e").getValue().toString()));
                            double cashBond = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("cash bond").getValue().toString()));
                            
                            if(advances != 0){
                                getWindow().showNotification("Remove Advances before updating HDMF !", Window.Notification.TYPE_WARNING_MESSAGE);
                                return;
                            }
                            if(cashBond != 0){
                                getWindow().showNotification("Remove Cash Bond before updating HDMF!", Window.Notification.TYPE_WARNING_MESSAGE);
                                return;
                            }
                            
                            Double hdmf = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("hdmf").getValue().toString()));
                            Double allowance = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("allowance").getValue().toString()));
                            Double taxableSalary = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("taxable salary").getValue().toString()));
                            Double adjustments = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("adjustments").getValue().toString()));
                            
                            taxableSalary = taxableSalary + hdmf;
                            
                            Window subWindow = editHdmfAmount(taxableSalary, allowance, hdmf, adjustments, salaryId);
                            if(subWindow.getParent() == null){
                                getWindow().addWindow(subWindow);
                            }
                            subWindow.setModal(true);
                            subWindow.center();
                        }else{
                            getWindow().showNotification("Access Disabled", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            return;
                        }
                    }
                        
                    if(event.getPropertyId().equals("tax")){
                        if(UserAccessControl.isContributions()== true){
                            salaryId = item.getItemProperty("id").getValue().toString();
                            double advances = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("advances to o/e").getValue().toString()));
                            double cashBond = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("cash bond").getValue().toString()));
                            
                            if(advances != 0){
                                getWindow().showNotification("Remove Advances before updating TAX !", Window.Notification.TYPE_WARNING_MESSAGE);
                                return;
                            }
                            if(cashBond != 0){
                                getWindow().showNotification("Remove Cash Bond before updating TAX!", Window.Notification.TYPE_WARNING_MESSAGE);
                                return;
                            }

                            double allowance = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("allowance").getValue().toString()));
                            double taxableSalary = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("taxable salary").getValue().toString()));
                            double tax = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("tax").getValue().toString()));
                            Double adjustments = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("adjustments").getValue().toString()));
                            
                            Window subWindow = editTax(taxableSalary, allowance, tax, adjustments, salaryId);
                            if(subWindow.getParent() == null){
                                getWindow().addWindow(subWindow);
                            }
                            subWindow.setModal(true);
                            subWindow.center();
                        }else{
                            getWindow().showNotification("Access Disabled", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            return;
                        }        
                    }
                    
                    if(event.getPropertyId().equals("cash bond")){
                        if(UserAccessControl.isCashBond()== true){
                            salaryId = item.getItemProperty("id").getValue().toString();
                            Double amountReceivable = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("amount receive").getValue().toString()));
                            Double cashBond = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("cash bond").getValue().toString()));
                            Double adjustments = Double.parseDouble(conUtil.removeCommaFromString(item.getItemProperty("adjustments").getValue().toString()));
                            
                            amountReceivable = amountReceivable + cashBond;                            
                            
                            Window subWindow = addCashBond(salaryId, amountReceivable, adjustments);
                            if(subWindow.getParent() == null){
                                getWindow().addWindow(subWindow);
                            }
                            subWindow.setModal(true);
                            subWindow.center();
                        }else{
                            getWindow().showNotification("Access Disabled", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            return;
                        }
                    }                      
                    
                    if(event.getPropertyId().equals("payroll date")){
                        salaryId = item.getItemProperty("id").getValue().toString();
                        Window subWindow = editPayrollDate(salaryId);
                        if(subWindow.getParent() == null){
                            getWindow().addWindow(subWindow); 
                        }                    
                        subWindow.setModal(true);
                        subWindow.center();
                    }
                    
                    if(event.getPropertyId().equals("status")){
                        salaryId = item.getItemProperty("id").getValue().toString();
                        String status = item.getItemProperty("status").getValue().toString();
                        if(userRole.equals("accounting")){
                           if(!status.equals("locked")){
                                Window subWindow = lockRow(salaryId);
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
                    
                }catch(Exception e){
                    e.getMessage();
                }                
            }
            
        }); 
        
        newEmployeeSalaryTable.setPageLength(newEmployeeSalaryTable.size());
                
        return newEmployeeSalaryTable;
    }
        
    ComponentContainer attendanceTableContainer(final Window subWindow, final Table table, 
            final List dates, final String paydayPeriod, final String salaryDate, final String salaryRowId){
        //subWindow = new Window("ATTENDANCE TABLE for "+employeesName.getValue());
        subWindow.setWidth("90%");
        
        table.removeAllItems();
        table.setEnabled(true);
        table.setWidth("100%");
        table.setImmediate(true);
                
        table.addContainerProperty("date", String.class, null);
        table.addContainerProperty("policy", ComboBox.class, null);
        table.addContainerProperty("holidays", ComboBox.class, null); 
        table.addContainerProperty("lp", CheckBox.class, null); 
        table.addContainerProperty("lates", TextField.class, null);    
        table.addContainerProperty("up", CheckBox.class, null);
        table.addContainerProperty("undertime", TextField.class, null); 
        table.addContainerProperty("op", CheckBox.class, null);
        table.addContainerProperty("overtime", TextField.class, null);        
        table.addContainerProperty("night differential", TextField.class, null);  
        table.addContainerProperty("l/min", String.class, null); 
        table.addContainerProperty("u/min", String.class, null); 
        table.addContainerProperty("o/min", String.class, null);
        table.addContainerProperty("nd/min", String.class, null);
        table.addContainerProperty("lholiday", String.class, null);
        table.addContainerProperty("sholiday", String.class, null);
        table.addContainerProperty("wdo", String.class, null);
        table.addContainerProperty("psday", String.class, null);
        
        table.setColumnAlignment("date", Table.ALIGN_CENTER);
        table.setColumnAlignment("policy", Table.ALIGN_CENTER);
        table.setColumnAlignment("lp", Table.ALIGN_CENTER);
        table.setColumnAlignment("lates", Table.ALIGN_CENTER);
        table.setColumnAlignment("up", Table.ALIGN_CENTER);
        table.setColumnAlignment("undertime", Table.ALIGN_CENTER);
        table.setColumnAlignment("op", Table.ALIGN_CENTER);
        table.setColumnAlignment("overtime", Table.ALIGN_CENTER);
        table.setColumnAlignment("night differential", Table.ALIGN_CENTER);
        table.setColumnAlignment("l/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("u/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("o/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("nd/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("lholiday", Table.ALIGN_RIGHT);
        table.setColumnAlignment("sholiday", Table.ALIGN_RIGHT);
        table.setColumnAlignment("wdo", Table.ALIGN_RIGHT);
        table.setColumnAlignment("psday", Table.ALIGN_RIGHT);
        
        String[] policyList = {"absent", "day-off", "working-day-off", "paternity-leave", "service-incentive-leave", "holiday", 
        "working-holiday", "paid-vacation-leave", "paid-sick-leave", "unpaid-vacation-leave", "unpaid-sick-leave", "suspended"};
        
        final String[] holidayList = {"legal-holiday", "special-holiday"};
                
        for(int i = 0; i < dates.size(); i++){
            Object itemId = new Integer(i);
                        
            final ComboBox holidays = new ComboBox();
            holidays.setEnabled(false);
            holidays.setWidth("120px");
            for(String temp : holidayList){
                holidays.addItem(temp);
            }
            holidays.setNullSelectionAllowed(false);
            holidays.setImmediate(true);
            holidays.setData(itemId);
            
            final CheckBox lp = new CheckBox();
            lp.setData(itemId);
            lp.setImmediate(true);
            
            final TextField lates = new TextField();
            lates.setWidth("50px");
            lates.setData(itemId);
            lates.setImmediate(true);
            
            final CheckBox up = new CheckBox();
            up.setData(itemId);
            up.setImmediate(true);
            
            final TextField undertime = new TextField();
            undertime.setWidth("50px");
            undertime.setData(itemId);
            undertime.setImmediate(true);
            
            final CheckBox op = new CheckBox();
            op.setData(itemId);
            op.setImmediate(true);
            
            final TextField overtime = new TextField();
            overtime.setWidth("50px");
            overtime.setData(itemId);
            overtime.setImmediate(true);
            
            final TextField nightDifferential = new TextField();
            nightDifferential.setWidth("50px");
            nightDifferential.setData(itemId);
            nightDifferential.setImmediate(true);
            
            final ComboBox policy = new ComboBox();
            policy.setWidth("120px");
            policy.setNullSelectionAllowed(true);
            for(int j = 0; j < policyList.length; j++){
                policy.addItem(policyList[j]);
            }
            policy.setImmediate(true);
            policy.setData(itemId);       
            policy.addListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {  
                    Object itemId = policy.getData();
                    Item item = table.getItem(itemId);
                    String str = (String) event.getProperty().getValue();
                    
                    if(str == null){       
                        holidays.removeAllItems();
                        for(String temp : holidayList){
                            holidays.addItem(temp);
                        }
                        holidays.setEnabled(false);
                        lp.setEnabled(true);
                        lates.setEnabled(true);
                        up.setEnabled(true);
                        undertime.setEnabled(true);
                        op.setEnabled(true);
                        overtime.setEnabled(true);
                        nightDifferential.setEnabled(true);
                        item.getItemProperty("wdo").setValue(null);
                    }else if(str.equals("working-holiday")){
                        holidays.removeAllItems();
                        for(String temp : holidayList){
                            holidays.addItem(temp);
                        }
                        holidays.setEnabled(true);
                        lp.setEnabled(true);
                        lates.setEnabled(true);
                        up.setEnabled(true);
                        undertime.setEnabled(true);
                        op.setEnabled(true);
                        overtime.setEnabled(true);
                        nightDifferential.setEnabled(true);
                        item.getItemProperty("wdo").setValue(null);
                    }else if(str.equals("working-day-off")){
                        holidays.removeAllItems();
                        for(String temp : holidayList){
                            holidays.addItem(temp);
                        }
                        holidays.setEnabled(false);
                        lp.setEnabled(true);
                        lates.setEnabled(true);
                        up.setEnabled(true);
                        undertime.setEnabled(true);
                        op.setEnabled(true);
                        overtime.setEnabled(true);
                        nightDifferential.setEnabled(true);
                                                
                        double additionalWorkingDayOffRate = attendanceProcess.processAdditionalWorkingDayOff(wage, employmentWageEntry);
                        item.getItemProperty("wdo").setValue(new Double(df.format(additionalWorkingDayOffRate)));
                    }else if(str.equals("holiday")){
                        holidays.removeAllItems();
                        for(String temp : holidayList){
                            holidays.addItem(temp);
                        }
                        holidays.setEnabled(true);
                        lp.setEnabled(false);
                        lates.setEnabled(false);
                        up.setEnabled(false);
                        undertime.setEnabled(false);
                        op.setEnabled(false);
                        overtime.setEnabled(false);
                        nightDifferential.setEnabled(false);
                        item.getItemProperty("wdo").setValue(null);
                    }else{
                        holidays.removeAllItems();
                        for(String temp : holidayList){
                            holidays.addItem(temp);
                        }
                        holidays.setEnabled(false);
                        lp.setEnabled(false);
                        lates.setEnabled(false);
                        up.setEnabled(false);
                        undertime.setEnabled(false);
                        op.setEnabled(false);
                        overtime.setEnabled(false);
                        nightDifferential.setEnabled(false);
                        item.getItemProperty("wdo").setValue(null);
                    }                    
                }

            });            
            
            holidays.addListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    Object itemId = holidays.getData();
                    Item item = table.getItem(itemId);
                    double additionalHolidayRate;
                    String str = (String) event.getProperty().getValue();
                    String policyStr = item.getItemProperty("policy").toString();
                    
                    if(policyStr.equals("holiday") || policy.getValue().toString().equals("working-holiday")){
                        if(str == null || str.isEmpty()){
                            subWindow.getWindow().showNotification("Select Type of Holiday!", Window.Notification.TYPE_WARNING_MESSAGE);
                            return;
                        }
                    }
                    
                    if(policyStr.equals("working-holiday")){
                        if(str == null){
                            item.getItemProperty("sholiday").setValue(null);
                            item.getItemProperty("lholiday").setValue(null);
                        }else if(str.equals("legal-holiday")){
                            additionalHolidayRate = attendanceProcess.processAdditionalHolidayRate(event.getProperty().getValue().toString(), wage);
                            item.getItemProperty("lholiday").setValue(new Double(df.format(additionalHolidayRate)));
                            item.getItemProperty("sholiday").setValue(null);
                        }else{
                            additionalHolidayRate = attendanceProcess.processAdditionalHolidayRate(event.getProperty().getValue().toString(), wage);
                            item.getItemProperty("sholiday").setValue(new Double(df.format(additionalHolidayRate)));
                            item.getItemProperty("lholiday").setValue(null);
                        }
                    }else if(policyStr.equals("holiday")){
                        if(str == null || str.equals("legal-holiday")){
                            additionalHolidayRate = attendanceProcess.processAdditionalHolidayRate(event.getProperty().getValue().toString(), wage);
                            item.getItemProperty("psday").setValue(new Double(df.format(additionalHolidayRate)));                            
                        }else{
                            item.getItemProperty("psday").setValue(null);
                        }
                    }
                }
                
            });
            
            lp.addListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    Object itemId = lp.getData();
                    Item item = table.getItem(itemId);
                    
                    boolean value = (Boolean) event.getProperty().getValue();
                    if(value == true){
                        latesPremiumRate = 0.1; 
                    }else{
                        latesPremiumRate = 0.0;
                    }
                    
                    String policies = null, holiday = null;                    
                     
                    if(policy.getValue() != null){
                        policies = policy.getValue().toString();
                    }
                    
                    if(holidays.getValue() != null){
                        holiday = holidays.getValue().toString();
                    }
                    
                    if(!lates.getValue().toString().trim().isEmpty()){
                        latePerMinute = Double.parseDouble(lates.getValue().toString().trim());
                        lateDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                        item.getItemProperty("l/min").setValue(lateDeduction);
                    }else{
                        item.getItemProperty("l/min").setValue(null);
                    }
                    
                    if(!undertime.getValue().toString().trim().isEmpty()){
                        latePerMinute = Double.valueOf(undertime.getValue().toString().trim()).doubleValue();
                        undertimeDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                    }                   
                    
                    if(!lates.getValue().toString().trim().isEmpty()){
                        if(!undertime.getValue().toString().trim().isEmpty()){                            
                            deduction = lateDeduction + undertimeDeduction + 
                                    (lateDeduction * latesPremiumRate) + (undertimeDeduction * undertimePremiumRate);
                            item.getItemProperty("l/min").setValue(new Double(df.format(lateDeduction + (lateDeduction * latesPremiumRate))));
                        }else{
                            deduction = lateDeduction + (lateDeduction * latesPremiumRate);
                            item.getItemProperty("l/min").setValue(new Double(df.format(deduction)));
                        }                      
                    }else{
                        item.getItemProperty("l/min").setValue(null);
                        if(!undertime.getValue().toString().trim().isEmpty()){
                            deduction = undertimeDeduction + (undertimeDeduction * undertimePremiumRate);
                        }else{
                            deduction = null;
                        }
                    }
                    if(deduction != null){
                        deduction = new Double(df.format(deduction));
                    }                    
                    //item.getItemProperty("deduction").setValue(deduction);
                    
                }
                
            });
            
            lates.addListener(new FieldEvents.TextChangeListener() {
                
                @Override
                public void textChange(TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId);                    
                                       
                    String policies = null, holiday = null;                    
                     
                    if(policy.getValue() != null){
                        policies = policy.getValue().toString();
                    }
                    
                    if(holidays.getValue() != null){
                        holiday = holidays.getValue().toString();
                    }
                    
                    if(!event.getText().isEmpty()){
                        latePerMinute = Double.parseDouble(event.getText().trim());
                        lateDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                        item.getItemProperty("l/min").setValue(lateDeduction);
                    }else{
                        item.getItemProperty("l/min").setValue(null);
                    }
                    
                    if(!undertime.getValue().toString().trim().isEmpty()){
                        latePerMinute = Double.valueOf(undertime.getValue().toString().trim()).doubleValue();
                        undertimeDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                    }                   
                    
                    if(!event.getText().trim().isEmpty()){
                        if(!undertime.getValue().toString().trim().isEmpty()){                            
                            deduction = lateDeduction + undertimeDeduction + 
                                    (lateDeduction * latesPremiumRate) + (undertimeDeduction * undertimePremiumRate);
                            item.getItemProperty("l/min").setValue(new Double(df.format(lateDeduction + (lateDeduction * latesPremiumRate))));
                        }else{
                            deduction = lateDeduction + (lateDeduction * latesPremiumRate);
                            item.getItemProperty("l/min").setValue(new Double(df.format(deduction)));
                        }
                                               
                    }else{
                        item.getItemProperty("l/min").setValue(null);
                        if(!undertime.getValue().toString().trim().isEmpty()){
                            deduction = undertimeDeduction + (undertimeDeduction * undertimePremiumRate);
                        }else{
                            deduction = null;
                        }                        
                    }
                    if(deduction != null){
                        deduction = new Double(df.format(deduction));
                    }                    
                    //item.getItemProperty("deduction").setValue(deduction);
                }
                
            });            
            
            up.addListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    Object itemId = lp.getData();
                    Item item = table.getItem(itemId);
                    
                    boolean value = (Boolean) event.getProperty().getValue();
                    if(value == true){
                        undertimePremiumRate = 0.1; 
                    }else{
                        undertimePremiumRate = 0.0;
                    }
                    
                    String policies = null, holiday = null;
                    
                    if(policy.getValue() != null){
                        policies = policy.getValue().toString();
                    } 
                    
                    if(holidays.getValue() != null){
                        holiday = holidays.getValue().toString();
                    }
                    
                    if(!lates.getValue().toString().trim().isEmpty()){
                        latePerMinute = Double.valueOf(lates.getValue().toString().trim()).doubleValue();
                        lateDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                    }
                    
                    if(!undertime.getValue().toString().trim().isEmpty()){
                        latePerMinute = Double.parseDouble(undertime.getValue().toString().trim());
                        undertimeDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                        item.getItemProperty("u/min").setValue(undertimeDeduction);
                    }else{
                        item.getItemProperty("u/min").setValue(null);
                    }
                    
                    if(!undertime.getValue().toString().trim().isEmpty()){
                        if(!lates.getValue().toString().trim().isEmpty()){                            
                            deduction = lateDeduction + undertimeDeduction + 
                                    (undertimeDeduction * undertimePremiumRate) + (lateDeduction * latesPremiumRate);
                            item.getItemProperty("u/min").setValue(new Double(df.format(undertimeDeduction + (undertimeDeduction * undertimePremiumRate))));
                        }else{
                            deduction = undertimeDeduction + (undertimeDeduction * undertimePremiumRate);
                            item.getItemProperty("u/min").setValue(new Double(df.format(deduction)));
                        }
                                               
                    }else{
                        item.getItemProperty("u/min").setValue(null);
                        if(!lates.getValue().toString().trim().isEmpty()){
                            deduction = lateDeduction + (lateDeduction * latesPremiumRate);
                        }else{
                            deduction = null;
                        }                        
                    }
                    if(deduction != null){
                        deduction = new Double(df.format(deduction));
                    }                    
                    //item.getItemProperty("deduction").setValue(deduction);
                    
                }
                
            });
            
            undertime.addListener(new FieldEvents.TextChangeListener() {

                @Override
                public void textChange(TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId);
                                        
                    String policies = null, holiday = null;
                    
                    if(policy.getValue() != null){
                        policies = policy.getValue().toString();
                    } 
                    
                    if(holidays.getValue() != null){
                        holiday = holidays.getValue().toString();
                    }
                    
                    if(!lates.getValue().toString().trim().isEmpty()){
                        latePerMinute = Double.valueOf(lates.getValue().toString().trim()).doubleValue();
                        lateDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                    }
                    
                    if(!event.getText().isEmpty()){
                        latePerMinute = Double.parseDouble(event.getText().trim());
                        undertimeDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                        item.getItemProperty("u/min").setValue(undertimeDeduction);
                    }else{
                        item.getItemProperty("u/min").setValue(null);
                    }
                    
                    if(!event.getText().trim().isEmpty()){
                        if(!lates.getValue().toString().trim().isEmpty()){                            
                            deduction = lateDeduction + undertimeDeduction + 
                                    (undertimeDeduction * undertimePremiumRate) + + (lateDeduction * latesPremiumRate);
                            item.getItemProperty("u/min").setValue(new Double(df.format(undertimeDeduction + (undertimeDeduction * undertimePremiumRate))));
                        }else{
                            deduction = undertimeDeduction + (undertimeDeduction * undertimePremiumRate);
                            item.getItemProperty("u/min").setValue(new Double(df.format(deduction)));
                        }
                                               
                    }else{
                        item.getItemProperty("u/min").setValue(null);
                        if(!lates.getValue().toString().trim().isEmpty()){
                            deduction = lateDeduction + (lateDeduction * latesPremiumRate);
                        }else{
                            deduction = null;
                        }                        
                    }
                    if(deduction != null){
                        deduction = new Double(df.format(deduction));
                    }                    
                    //item.getItemProperty("deduction").setValue(deduction);
                }
                
            });
            
            op.addListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    Object itemId = lp.getData();
                    Item item = table.getItem(itemId);
                    
                    boolean value = (Boolean) event.getProperty().getValue();
                    if(value == true){
                        overtimePremiumRate = 0.1; 
                    }else{
                        overtimePremiumRate = 0.0;
                    }
                    
                    String policies = null, holiday = null;
                    
                    if(policy.getValue() != null){
                        policies = policy.getValue().toString();
                    }
                    
                    if(holidays.getValue() != null){
                        holiday = holidays.getValue().toString();
                    }
                    
                    if(!overtime.getValue().toString().trim().isEmpty()){
                        overtimePerMinute = Double.parseDouble(overtime.getValue().toString().trim());
                        overtimeAddition = attendanceProcess.processEmployeesOvertime(policies, holiday, overtimePerMinute, wage);
                        item.getItemProperty("o/min").setValue(overtimeAddition);
                    }else{
                        item.getItemProperty("o/min").setValue(null);
                    }
                    
                    if(!nightDifferential.getValue().toString().trim().isEmpty()){
                        nightDifferentialPerMinute = Double.valueOf(nightDifferential.getValue().toString().trim()).doubleValue();
                        nightDifferentialAddition = attendanceProcess.processEmployeesNightDifferential(policies, holiday, nightDifferentialPerMinute, wage);
                    }
                    
                    if(!overtime.getValue().toString().trim().isEmpty()){
                        if(!nightDifferential.getValue().toString().trim().isEmpty()){
                            addition = overtimeAddition + nightDifferentialAddition + (overtimeAddition * overtimePremiumRate);
                            item.getItemProperty("o/min").setValue(new Double(df.format(overtimeAddition + (overtimeAddition * overtimePremiumRate))));
                        }else{
                            addition = overtimeAddition + (overtimeAddition * overtimePremiumRate);
                            item.getItemProperty("o/min").setValue(new Double(df.format(addition)));
                        }
                    }else{
                        item.getItemProperty("o/min").setValue(null);
                        if(!nightDifferential.getValue().toString().trim().isEmpty()){
                            addition = nightDifferentialAddition;
                        }else{
                            addition = null;
                        }
                    }
                    if(addition != null){
                        addition = new Double(df.format(addition));
                    }                    
                    //item.getItemProperty("addition").setValue(addition);
                }
                
            });
            
            overtime.addListener(new FieldEvents.TextChangeListener() {

                @Override
                public void textChange(TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId);
                                       
                    String policies = null, holiday = null;
                    double hAmount = 0;
                    
                    if(policy.getValue() != null){
                        policies = policy.getValue().toString();
                    }
                    
                    if(holidays.getValue() != null){
                        holiday = holidays.getValue().toString();
                        if(holiday.equals("legal-holiday")){
                            hAmount = Double.parseDouble(item.getItemProperty("lholiday").getValue().toString());
                        }else{
                            hAmount = Double.parseDouble(item.getItemProperty("sholiday").getValue().toString());
                        }
                    }
                    
                    if(!event.getText().trim().isEmpty()){
                        overtimePerMinute = Double.parseDouble(event.getText().trim());
                        overtimeAddition = attendanceProcess.processEmployeesOvertime(policies, holiday, overtimePerMinute, wage);
                        item.getItemProperty("o/min").setValue(overtimeAddition);
                    }else{
                        item.getItemProperty("o/min").setValue(null);
                    }
                    
                    if(!nightDifferential.getValue().toString().trim().isEmpty()){
                        nightDifferentialPerMinute = Double.valueOf(nightDifferential.getValue().toString().trim()).doubleValue();
                        nightDifferentialAddition = attendanceProcess.processEmployeesNightDifferential(policies, holiday, nightDifferentialPerMinute, wage);
                    }
                    
                    if(!event.getText().trim().isEmpty()){
                        if(!nightDifferential.getValue().toString().trim().isEmpty()){
                            addition = overtimeAddition + nightDifferentialAddition + (overtimeAddition * overtimePremiumRate);
                            item.getItemProperty("o/min").setValue(new Double(df.format(overtimeAddition + (overtimeAddition * overtimePremiumRate))));
                        }else{
                            addition = overtimeAddition + (overtimeAddition * overtimePremiumRate);
                            item.getItemProperty("o/min").setValue(new Double(df.format(addition)));
                        }
                    }else{
                        item.getItemProperty("o/min").setValue(null);
                        if(!nightDifferential.getValue().toString().trim().isEmpty()){
                            addition = nightDifferentialAddition;
                        }else{
                            addition = null;
                        }
                    }
                    if(addition != null){
                        addition = new Double(df.format(addition));
                    }                    
                    //item.getItemProperty("addition").setValue(addition);
                }
            });
            
            nightDifferential.addListener(new FieldEvents.TextChangeListener() {

                @Override
                public void textChange(TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId);                    
                                        
                    String policies = null, holiday = null;
                    double hAmount = 0;
                    
                    if(policy.getValue() != null){
                        policies = policy.getValue().toString();
                    }
                    
                    if(holidays.getValue() != null){
                        holiday = holidays.getValue().toString();
                        if(holiday.equals("legal-holiday")){
                            hAmount = Double.parseDouble(item.getItemProperty("lholiday").getValue().toString());
                        }else{
                            hAmount = Double.parseDouble(item.getItemProperty("sholiday").getValue().toString());
                        }                        
                    }
                    
                    if(!overtime.getValue().toString().trim().isEmpty()){
                        overtimePerMinute = Double.valueOf(overtime.getValue().toString().trim()).doubleValue();
                        overtimeAddition = attendanceProcess.processEmployeesOvertime(policies, holiday, overtimePerMinute, wage);                        
                    }
                    
                    if(!event.getText().trim().isEmpty()){
                        nightDifferentialPerMinute = Double.parseDouble(event.getText().trim());
                        nightDifferentialAddition = attendanceProcess.processEmployeesNightDifferential(policies, holiday, nightDifferentialPerMinute, wage);
                        item.getItemProperty("nd/min").setValue(nightDifferentialAddition);
                    }else{
                        item.getItemProperty("nd/min").setValue(null);
                    }
                    
                    if(!event.getText().trim().isEmpty()){
                        if(!overtime.getValue().toString().trim().isEmpty()){
                            addition = overtimeAddition + nightDifferentialAddition + (overtimeAddition * overtimePremiumRate);
                            item.getItemProperty("nd/min").setValue(new Double(df.format(nightDifferentialAddition)));
                        }else{
                            addition = nightDifferentialAddition;
                            item.getItemProperty("nd/min").setValue(new Double(df.format(addition)));
                        }
                    }else{
                        item.getItemProperty("nd/min").setValue(null);
                        if(!overtime.getValue().toString().trim().isEmpty()){
                            addition = overtimeAddition + hAmount;
                        }else{
                            addition = null;
                        }
                    }
                    if(addition != null){
                        addition = new Double(df.format(addition));
                    }                    
                    //item.getItemProperty("addition").setValue(addition);
                }
                
            });
            
            table.addItem(new Object[]{conUtil.convertDateFormat(dates.get(i).toString()), policy, holidays, lp, lates, up, undertime,
                op, overtime, nightDifferential, null, null, null, null, null, null, null, null}, new Integer(i));            
        }                
        table.setPageLength(table.size());
        
        subWindow.addComponent(table); 
        
        final Button button = new Button();
        button.setCaption("Save Attendance Data");
        
        for(Object listener : button.getListeners(ClickListener.class)){
            button.removeListener(ClickListener.class, listener);
        }
        
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {                
                try{                                       
                    Collection attendanceTableCollection = table.getContainerDataSource().getItemIds();
                    List<String> attendanceList = new ArrayList<String>();
                    for(int i = 0; i < attendanceTableCollection.size(); i++){
                        String str = table.getItem(i).toString();
                        attendanceList.add(str);
                    }
                    
                    boolean result = salaryComputation.saveAttendanceData(attendanceList, employeeId, attendanceDateFrom, 
                            attendanceDateTo, paydayPeriod, salaryDate, salaryRowId);
                    button.setDisableOnClick(true);                    
                    if(result == true){
                        employeeSalaryLedgerTable();
                        (subWindow.getParent()).removeWindow(subWindow);
                    }else{
                        getWindow().showNotification("There is an error in your Salary Information!", Window.Notification.TYPE_ERROR_MESSAGE);
                    }
                                         
                }catch(Exception e){
                    e.getMessage();
                }
            }
            
        });
        
        subWindow.addComponent(button);
        
        return subWindow;
    }
    
    private ComponentContainer editAttendanceTableContainer(String fromDate, String toDate, final Table table, final String paydayPeriod, 
            final String payroll_date, final String salaryRowId){
        final Window subWindow = new Window();
        subWindow.setWidth("90%");
        
        table.removeAllItems();
        table.setEnabled(true);
        table.setWidth("100%");
        table.setImmediate(true);
        
        table.addContainerProperty("edit", CheckBox.class, null);
        table.addContainerProperty("date", String.class, null);
        table.addContainerProperty("policy", String.class, null);
        table.addContainerProperty("holidays", String.class, null); 
        table.addContainerProperty("lp", CheckBox.class, null); 
        table.addContainerProperty("lates", TextField.class, null);    
        table.addContainerProperty("up", CheckBox.class, null);
        table.addContainerProperty("undertime", TextField.class, null); 
        table.addContainerProperty("op", CheckBox.class, null);
        table.addContainerProperty("overtime", TextField.class, null);        
        table.addContainerProperty("night differential", TextField.class, null);  
        table.addContainerProperty("l/min", String.class, null); 
        table.addContainerProperty("u/min", String.class, null); 
        table.addContainerProperty("o/min", String.class, null);
        table.addContainerProperty("nd/min", String.class, null);
        table.addContainerProperty("lholiday", String.class, null);
        table.addContainerProperty("sholiday", String.class, null);
        table.addContainerProperty("wdo", String.class, null);
        table.addContainerProperty("psday", String.class, null); //Policy: Holiday, Holiday: Legal Holiday (DOUBLE PAY)
                
        table.setColumnAlignment("date", Table.ALIGN_CENTER);
        table.setColumnAlignment("policy", Table.ALIGN_LEFT);
        table.setColumnAlignment("holiday", Table.ALIGN_LEFT);
        table.setColumnAlignment("lp", Table.ALIGN_CENTER);
        table.setColumnAlignment("lates", Table.ALIGN_CENTER);
        table.setColumnAlignment("up", Table.ALIGN_CENTER);
        table.setColumnAlignment("undertime", Table.ALIGN_CENTER);
        table.setColumnAlignment("op", Table.ALIGN_CENTER);
        table.setColumnAlignment("overtime", Table.ALIGN_CENTER);
        table.setColumnAlignment("night differential", Table.ALIGN_CENTER);
        table.setColumnAlignment("l/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("u/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("o/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("nd/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("lholiday", Table.ALIGN_RIGHT);
        table.setColumnAlignment("sholiday", Table.ALIGN_RIGHT);
        table.setColumnAlignment("wdo", Table.ALIGN_RIGHT);
        table.setColumnAlignment("psday", Table.ALIGN_RIGHT);
        
        final String[] policyList = {"absent", "day-off", "working-day-off", "paternity-leave", "service-incentive-leave", "holiday", 
        "working-holiday", "paid-vacation-leave", "paid-sick-leave", "unpaid-vacation-leave", "unpaid-sick-leave", "suspended"};
        
        final String[] holidayList = {"legal-holiday", "special-holiday"};
        
        Integer i = 0;
        AttendanceDAO attendanceDAO = new AttendanceDAO();
        List<Attendance> attendanceList = attendanceDAO.getAttendanceOfEmployee(Integer.parseInt(salaryRowId));        
        for(Attendance a: attendanceList){            
            Object itemId = new Integer(i);
                                    
            final CheckBox edit = new CheckBox();
            edit.setData(i);
            edit.setImmediate(true);
            
            final CheckBox lp = new CheckBox();
            if(a.getLatesPremium() == true){ lp.setValue(a.getLatesPremium()); }
            lp.setData(i);
            lp.setEnabled(false);
            lp.setImmediate(true);
            
            final TextField lates = new TextField();
            lates.setWidth("50px");
            if(a.getLates() != 0){ lates.setValue(a.getLates()); }
            lates.setData(i);
            lates.setEnabled(false);
            lates.setImmediate(true);
            
            final CheckBox up = new CheckBox();
            if(a.getUndertimePremium() == true){ up.setValue(a.getUndertimePremium()); }
            up.setData(i);
            up.setEnabled(false);
            up.setImmediate(true);
            
            final TextField undertime = new TextField();
            undertime.setWidth("50px");
            if(a.getUndertime() != 0){ undertime.setValue(a.getUndertime()); }
            undertime.setData(i);
            undertime.setEnabled(false);
            undertime.setImmediate(true);
            
            final CheckBox op = new CheckBox();
            if(a.getOvertimePremium() == true){ op.setValue(a.getOvertimePremium()); }
            op.setData(i);
            op.setEnabled(false);
            op.setImmediate(true);
            
            final TextField overtime = new TextField();
            overtime.setWidth("50px");
            if(a.getOvertime() != 0){ overtime.setValue(a.getOvertime()); }
            overtime.setData(i);
            overtime.setEnabled(false);
            overtime.setImmediate(true);
            
            final TextField nightDifferential = new TextField();
            nightDifferential.setWidth("50px");
            if(a.getNightDifferential() != 0){ nightDifferential.setValue(a.nightDifferential); }
            nightDifferential.setData(i);
            nightDifferential.setEnabled(false);
            nightDifferential.setImmediate(true);
                    
            edit.addListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    Object itemId = lp.getData();
                    Item item = table.getItem(itemId);
                    
                    Boolean editRow = (Boolean) event.getProperty().getValue();
                    
                    if(editRow == true){
                        Window subWindow = clearRowTextField(lates, lp, undertime, up, overtime, op, nightDifferential, item, edit);
                        if(subWindow.getParent() == null){
                            getWindow().addWindow(subWindow);
                        }
                        subWindow.setModal(true);
                        subWindow.center();
                        lp.setEnabled(true);
                        lates.setEnabled(true);
                        up.setEnabled(true);
                        undertime.setEnabled(true);
                        op.setEnabled(true);
                        overtime.setEnabled(true);
                        nightDifferential.setEnabled(true);
                    }else{
                        lp.setEnabled(false);
                        lates.setEnabled(false);
                        up.setEnabled(false);
                        undertime.setEnabled(false);
                        op.setEnabled(false);
                        overtime.setEnabled(false);
                        nightDifferential.setEnabled(false);
                    }
                }
                
            });
            
            lp.addListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    Object itemId = lp.getData();
                    Item item = table.getItem(itemId);
                    
                    boolean value = (Boolean) event.getProperty().getValue();
                    if(value == true){
                        latesPremiumRate = 0.1; 
                    }else{
                        latesPremiumRate = 0.0;
                    }
                    
                    String policies = null, holiday = null;    
                                        
                    if(item.getItemProperty("policy").getValue() != null){
                        policies = item.getItemProperty("policy").getValue().toString();
                    }
                    
                    if(item.getItemProperty("holidays").getValue() != null){
                        holiday = item.getItemProperty("holidays").getValue().toString();
                    }
                    
                    if(!lates.getValue().toString().trim().isEmpty()){
                        latePerMinute = Double.parseDouble(lates.getValue().toString().trim());
                        lateDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                        item.getItemProperty("l/min").setValue(lateDeduction);
                    }else{
                        item.getItemProperty("l/min").setValue(0.0);
                    }
                    
                    if(!undertime.getValue().toString().trim().isEmpty()){
                        latePerMinute = Double.valueOf(undertime.getValue().toString().trim()).doubleValue();
                        undertimeDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                    }                   
                    
                    if(!lates.getValue().toString().trim().isEmpty()){
                        if(!undertime.getValue().toString().trim().isEmpty()){                            
                            deduction = lateDeduction + undertimeDeduction + 
                                    (lateDeduction * latesPremiumRate) + (undertimeDeduction * undertimePremiumRate);
                            item.getItemProperty("l/min").setValue(new Double(df.format(lateDeduction + (lateDeduction * latesPremiumRate))));
                        }else{
                            deduction = lateDeduction + (lateDeduction * latesPremiumRate);
                            item.getItemProperty("l/min").setValue(new Double(df.format(deduction)));
                        }                      
                    }else{
                        item.getItemProperty("l/min").setValue(null);
                        if(!undertime.getValue().toString().trim().isEmpty()){
                            deduction = undertimeDeduction + (undertimeDeduction * undertimePremiumRate);
                        }else{
                            deduction = null;
                        }
                    }
                    if(deduction != null){
                        deduction = new Double(df.format(deduction));
                    }                    
                    
                }
                
            });
            
            lates.addListener(new FieldEvents.TextChangeListener() {
                
                @Override
                public void textChange(TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId);                    
                                       
                    String policies = null, holiday = null;                    
                                         
                    if(item.getItemProperty("policy").getValue() != null){
                        policies = item.getItemProperty("policy").getValue().toString();
                    }
                    
                    if(item.getItemProperty("holidays").getValue() != null){
                        holiday = item.getItemProperty("holidays").getValue().toString();
                    }
                    
                    if(!event.getText().isEmpty()){
                        latePerMinute = Double.parseDouble(event.getText().trim());
                        lateDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                        item.getItemProperty("l/min").setValue(lateDeduction);
                    }else{
                        item.getItemProperty("l/min").setValue(0.0);
                    }
                    
                    if(!undertime.getValue().toString().trim().isEmpty()){
                        latePerMinute = Double.valueOf(undertime.getValue().toString().trim()).doubleValue();
                        undertimeDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                    }                   
                    
                    if(!event.getText().trim().isEmpty()){
                        if(!undertime.getValue().toString().trim().isEmpty()){                            
                            deduction = lateDeduction + undertimeDeduction + 
                                    (lateDeduction * latesPremiumRate) + (undertimeDeduction * undertimePremiumRate);
                            item.getItemProperty("l/min").setValue(new Double(df.format(lateDeduction + (lateDeduction * latesPremiumRate))));
                        }else{
                            deduction = lateDeduction + (lateDeduction * latesPremiumRate);
                            item.getItemProperty("l/min").setValue(new Double(df.format(deduction)));
                        }
                                               
                    }else{
                        item.getItemProperty("l/min").setValue(0.0);
                        if(!undertime.getValue().toString().trim().isEmpty()){
                            deduction = undertimeDeduction + (undertimeDeduction * undertimePremiumRate);
                        }else{
                            deduction = null;
                        }                        
                    }
                    if(deduction != null){
                        deduction = new Double(df.format(deduction));
                    }                    
                }
                
            });            
            
            up.addListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    Object itemId = lp.getData();
                    Item item = table.getItem(itemId);
                    
                    boolean value = (Boolean) event.getProperty().getValue();
                    if(value == true){
                        undertimePremiumRate = 0.1; 
                    }else{
                        undertimePremiumRate = 0.0;
                    }
                    
                    String policies = null, holiday = null;
                                        
                    if(item.getItemProperty("policy").getValue() != null){
                        policies = item.getItemProperty("policy").getValue().toString();
                    }
                    
                    if(item.getItemProperty("holidays").getValue() != null){
                        holiday = item.getItemProperty("holidays").getValue().toString();
                    }
                    
                    if(!lates.getValue().toString().trim().isEmpty()){
                        latePerMinute = Double.valueOf(lates.getValue().toString().trim()).doubleValue();
                        lateDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                    }
                    
                    if(!undertime.getValue().toString().trim().isEmpty()){
                        latePerMinute = Double.parseDouble(undertime.getValue().toString().trim());
                        undertimeDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                        item.getItemProperty("u/min").setValue(undertimeDeduction);
                    }else{
                        item.getItemProperty("u/min").setValue(0.0);
                    }
                    
                    if(!undertime.getValue().toString().trim().isEmpty()){
                        if(!lates.getValue().toString().trim().isEmpty()){                            
                            deduction = lateDeduction + undertimeDeduction + 
                                    (undertimeDeduction * undertimePremiumRate) + (lateDeduction * latesPremiumRate);
                            item.getItemProperty("u/min").setValue(new Double(df.format(undertimeDeduction + (undertimeDeduction * undertimePremiumRate))));
                        }else{
                            deduction = undertimeDeduction + (undertimeDeduction * undertimePremiumRate);
                            item.getItemProperty("u/min").setValue(new Double(df.format(deduction)));
                        }
                                               
                    }else{
                        item.getItemProperty("u/min").setValue(0.0);
                        if(!lates.getValue().toString().trim().isEmpty()){
                            deduction = lateDeduction + (lateDeduction * latesPremiumRate);
                        }else{
                            deduction = null;
                        }                        
                    }
                    if(deduction != null){
                        deduction = new Double(df.format(deduction));
                    }                                        
                }
                
            });
            
            undertime.addListener(new FieldEvents.TextChangeListener() {

                @Override
                public void textChange(TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId);
                                        
                    String policies = null, holiday = null;
                                        
                    if(item.getItemProperty("policy").getValue() != null){
                        policies = item.getItemProperty("policy").getValue().toString();
                    }
                    
                    if(item.getItemProperty("holidays").getValue() != null){
                        holiday = item.getItemProperty("holidays").getValue().toString();
                    }
                    
                    if(!lates.getValue().toString().trim().isEmpty()){
                        latePerMinute = Double.valueOf(lates.getValue().toString().trim()).doubleValue();
                        lateDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                    }
                    
                    if(!event.getText().isEmpty()){
                        latePerMinute = Double.parseDouble(event.getText().trim());
                        undertimeDeduction = attendanceProcess.processEmployeesLates(policies, holiday, latePerMinute, wage);
                        item.getItemProperty("u/min").setValue(undertimeDeduction);
                    }else{
                        item.getItemProperty("u/min").setValue(0.0);
                    }
                    
                    if(!event.getText().trim().isEmpty()){
                        if(!lates.getValue().toString().trim().isEmpty()){                            
                            deduction = lateDeduction + undertimeDeduction + 
                                    (undertimeDeduction * undertimePremiumRate) + + (lateDeduction * latesPremiumRate);
                            item.getItemProperty("u/min").setValue(new Double(df.format(undertimeDeduction + (undertimeDeduction * undertimePremiumRate))));
                        }else{
                            deduction = undertimeDeduction + (undertimeDeduction * undertimePremiumRate);
                            item.getItemProperty("u/min").setValue(new Double(df.format(deduction)));
                        }
                                               
                    }else{
                        item.getItemProperty("u/min").setValue(0.0);
                        if(!lates.getValue().toString().trim().isEmpty()){
                            deduction = lateDeduction + (lateDeduction * latesPremiumRate);
                        }else{
                            deduction = null;
                        }                        
                    }
                    if(deduction != null){
                        deduction = new Double(df.format(deduction));
                    }                    
                }
                
            });
            
            op.addListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    Object itemId = lp.getData();
                    Item item = table.getItem(itemId);
                    
                    boolean value = (Boolean) event.getProperty().getValue();
                    if(value == true){
                        overtimePremiumRate = 0.1; 
                    }else{
                        overtimePremiumRate = 0.0;
                    }
                    
                    String policies = null, holiday = null;
                                        
                    if(item.getItemProperty("policy").getValue() != null){
                        policies = item.getItemProperty("policy").getValue().toString();
                    }
                    
                    if(item.getItemProperty("holidays").getValue() != null){
                        holiday = item.getItemProperty("holidays").getValue().toString();
                    }
                    
                    if(!overtime.getValue().toString().trim().isEmpty()){
                        overtimePerMinute = Double.parseDouble(overtime.getValue().toString().trim());
                        overtimeAddition = attendanceProcess.processEmployeesOvertime(policies, holiday, overtimePerMinute, wage);
                        item.getItemProperty("o/min").setValue(overtimeAddition);
                    }else{
                        item.getItemProperty("o/min").setValue(0.0);
                    }
                    
                    if(!nightDifferential.getValue().toString().trim().isEmpty()){
                        nightDifferentialPerMinute = Double.valueOf(nightDifferential.getValue().toString().trim()).doubleValue();
                        nightDifferentialAddition = attendanceProcess.processEmployeesNightDifferential(policies, holiday, nightDifferentialPerMinute, wage);
                    }
                    
                    if(!overtime.getValue().toString().trim().isEmpty()){
                        if(!nightDifferential.getValue().toString().trim().isEmpty()){
                            addition = overtimeAddition + nightDifferentialAddition + (overtimeAddition * overtimePremiumRate);
                            item.getItemProperty("o/min").setValue(new Double(df.format(overtimeAddition + (overtimeAddition * overtimePremiumRate))));
                        }else{
                            addition = overtimeAddition + (overtimeAddition * overtimePremiumRate);
                            item.getItemProperty("o/min").setValue(new Double(df.format(addition)));
                        }
                    }else{
                        item.getItemProperty("o/min").setValue(0.0);
                        if(!nightDifferential.getValue().toString().trim().isEmpty()){
                            addition = nightDifferentialAddition;
                        }else{
                            addition = null;
                        }
                    }
                    if(addition != null){
                        addition = new Double(df.format(addition));
                    }                    
                }
                
            });
            
            overtime.addListener(new FieldEvents.TextChangeListener() {

                @Override
                public void textChange(TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId);
                                       
                    String policies = null, holiday = null;
                    double hAmount = 0;
                                        
                    if(item.getItemProperty("policy").getValue() != null){
                        policies = item.getItemProperty("policy").getValue().toString();
                    }
                    
                    if(item.getItemProperty("holidays").getValue() != null){
                        holiday = item.getItemProperty("holidays").getValue().toString();
                    }
                    
                    if(!event.getText().trim().isEmpty()){
                        overtimePerMinute = Double.parseDouble(event.getText().trim());
                        overtimeAddition = attendanceProcess.processEmployeesOvertime(policies, holiday, overtimePerMinute, wage);
                        item.getItemProperty("o/min").setValue(overtimeAddition);
                    }else{
                        item.getItemProperty("o/min").setValue(0.0);
                    }
                    
                    if(!nightDifferential.getValue().toString().trim().isEmpty()){
                        nightDifferentialPerMinute = Double.valueOf(nightDifferential.getValue().toString().trim()).doubleValue();
                        nightDifferentialAddition = attendanceProcess.processEmployeesNightDifferential(policies, holiday, nightDifferentialPerMinute, wage);
                    }
                    
                    if(!event.getText().trim().isEmpty()){
                        if(!nightDifferential.getValue().toString().trim().isEmpty()){
                            addition = overtimeAddition + nightDifferentialAddition + (overtimeAddition * overtimePremiumRate);
                            item.getItemProperty("o/min").setValue(new Double(df.format(overtimeAddition + (overtimeAddition * overtimePremiumRate))));
                        }else{
                            addition = overtimeAddition + (overtimeAddition * overtimePremiumRate);
                            item.getItemProperty("o/min").setValue(new Double(df.format(addition)));
                        }
                    }else{
                        item.getItemProperty("o/min").setValue(0.0);
                        if(!nightDifferential.getValue().toString().trim().isEmpty()){
                            addition = nightDifferentialAddition;
                        }else{
                            addition = null;
                        }
                    }
                    if(addition != null){
                        addition = new Double(df.format(addition));
                    }                    
                }
            });
            
            nightDifferential.addListener(new FieldEvents.TextChangeListener() {

                @Override
                public void textChange(TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId);                    
                                        
                    String policies = null, holiday = null;
                    double hAmount = 0;
                                        
                    if(item.getItemProperty("policy").getValue() != null){
                        policies = item.getItemProperty("policy").getValue().toString();
                    }
                    
                    if(item.getItemProperty("holidays").getValue() != null){
                        holiday = item.getItemProperty("holidays").getValue().toString();
                    }
                    
                    if(!overtime.getValue().toString().trim().isEmpty()){
                        overtimePerMinute = Double.valueOf(overtime.getValue().toString().trim()).doubleValue();
                        overtimeAddition = attendanceProcess.processEmployeesOvertime(policies, holiday, overtimePerMinute, wage);                        
                    }
                    
                    if(!event.getText().trim().isEmpty()){
                        nightDifferentialPerMinute = Double.parseDouble(event.getText().trim());
                        nightDifferentialAddition = attendanceProcess.processEmployeesNightDifferential(policies, holiday, nightDifferentialPerMinute, wage);
                        item.getItemProperty("nd/min").setValue(nightDifferentialAddition);
                    }else{
                        item.getItemProperty("nd/min").setValue(0.0);
                    }
                    
                    if(!event.getText().trim().isEmpty()){
                        if(!overtime.getValue().toString().trim().isEmpty()){
                            addition = overtimeAddition + nightDifferentialAddition + (overtimeAddition * overtimePremiumRate);
                            item.getItemProperty("nd/min").setValue(new Double(df.format(nightDifferentialAddition)));
                        }else{
                            addition = nightDifferentialAddition;
                            item.getItemProperty("nd/min").setValue(new Double(df.format(addition)));
                        }
                    }else{
                        item.getItemProperty("nd/min").setValue(0.0);
                        if(!overtime.getValue().toString().trim().isEmpty()){
                            addition = overtimeAddition + hAmount;
                        }else{
                            addition = null;
                        }
                    }
                    if(addition != null){
                        addition = new Double(df.format(addition));
                    }                    
                }
                
            });
            
            table.addItem(new Object[]{
                edit,
                a.getAttendanceDate(), a.getPolicy(), a.getHoliday(), 
                lp, lates, up, undertime, op, overtime, nightDifferential, 
                a.getLatesDeduction(), 
                a.getUndertimeDeduction(), 
                a.getOvertimePaid(), 
                a.getNightDifferentialPaid(), 
                a.getlegalHolidayPaid(), 
                a.getSpecialHolidayPaid(), 
                a.getWorkingDayOffPaid(), 
                a.getPsHolidayPaid()}, new Integer(i));            
            i++;
        }               
        table.setImmediate(true);
        table.setSelectable(true);
        table.setPageLength(table.size());
        
        for(Object listener : table.getListeners(ItemClickEvent.class)){
            table.removeListener(ItemClickEvent.class, listener);
        }
        
        table.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                Item item = table.getItem(itemId);
                
                Boolean editRow = Boolean.valueOf(item.getItemProperty("edit").getValue().toString());
                
                if(event.getPropertyId().equals("policy")){
                    if(editRow == true){
                        Window newWindow = policyWindow(policyList, holidayList, item);
                        if(newWindow.getParent() == null){
                            getWindow().addWindow(newWindow);
                        }
                        newWindow.setModal(true);
                        newWindow.center();
                    }else{
                        getWindow().showNotification("Click Edit!", Window.Notification.TYPE_WARNING_MESSAGE);
                    }                    
                }
            }
            
        });
        
        subWindow.addComponent(table);
        
        final Button button = new Button();
        button.setCaption("Save Attendance Data");
        
        for(Object listener : button.getListeners(ClickListener.class)){
            button.removeListener(ClickListener.class, listener);
        }
        
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                try{                                 
                    SalaryDAO salaryDAO = new SalaryDAO();
                    attendanceDateFrom = salaryDAO.getAttendanceFirsDay(salaryRowId);
                    attendanceDateTo = salaryDAO.getAttendanceLastDay(salaryRowId);
                    Collection attendanceTableCollection = table.getContainerDataSource().getItemIds();
                    List<String> attendanceList = new ArrayList<String>();
                    for(int i = 0; i < attendanceTableCollection.size(); i++){
                        String str = table.getItem(i).toString();
                        attendanceList.add(str);
                    }
                    
                    boolean result = salaryComputation.saveAttendanceData(attendanceList, employeeId, attendanceDateFrom, 
                            attendanceDateTo, paydayPeriod, payroll_date, salaryRowId);
                    button.setDisableOnClick(true);                    
                    if(result == true){
                        employeeSalaryLedgerTable();
                        (subWindow.getParent()).removeWindow(subWindow);
                    }else{
                        getWindow().showNotification("There is an error in your Salary Information!", Window.Notification.TYPE_ERROR_MESSAGE);
                    }
                                         
                }catch(Exception e){
                    e.getMessage();
                }
            }
            
        });
        subWindow.addComponent(button);
        
        return subWindow;
    }
    
    private List obtainPayrollDateList(String from, String to){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        Date datePeriodFrom = null;
        Date datePeriodTo = null;
        try {
            datePeriodFrom = (Date) dateFormat.parse(from);
            datePeriodTo = (Date) dateFormat.parse(to);
        } catch (ParseException ex) {
            Logger.getLogger(AttendanceModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        List<Date> dates = new ArrayList<Date>();
        long interval = 24*1000 * 60 * 60; // 1 hour in millis
        long toTime = datePeriodTo.getTime() ; // create your endtime here, possibly using Calendar or Date
        long fromTime = datePeriodFrom.getTime();
        while(fromTime <= toTime){
            dates.add(new Date(fromTime));
            fromTime += interval;
        }        
        return dates;
    }
            
    private Window addAdvances(String str, final String salaryRowId, Double netSalary, final Double amountToBeReceive, 
            final Double amountReceivable, final Double adjustments){        
        Boolean locked = true;
        
        if(str.equals("locked")){
            locked = false;
        }
                
        final Window subWindow = new Window("ADD/REMOVE ADVANCES");
        subWindow.setWidth("450px");
        
        TabSheet ts = new TabSheet();
        ts.addStyleName("bar");
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        vlayout.setCaption("View Advances");
        
        advancesTable(locked, salaryRowId, amountToBeReceive, amountReceivable, adjustments, subWindow);        
        vlayout.addComponent(newAdvancesTable);
        ts.addComponent(vlayout);
        
        vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        vlayout.setCaption("Post Advances");
        vlayout.setEnabled(locked);
        
        final ComboBox advancesType = new ComboBox("Select Type: ");
        advancesType.setWidth("100%");
        List<String> advancesTypeLists = query.getAdvancesTypeLists();
        for(int i = 0; i < advancesTypeLists.size(); i++){
            advancesType.addItem(advancesTypeLists.get(i));
        }
        advancesType.setNewItemsAllowed(true);
        advancesType.setNewItemHandler(new AbstractSelect.NewItemHandler() {

            @Override
            public void addNewItem(String newItemCaption) {
                AdvancesTypeBean advancesTypeBean = new AdvancesTypeBean();
                if (!advancesType.containsId(newItemCaption)) {
                    advancesTypeBean.setAdvancesType(newItemCaption);
                    Boolean result = advancesTypeBean.saveAdvancesType();
                    if(result = true){
                        getWindow().showNotification("Added Type: " + newItemCaption);
                        lastAdded = true;
                        advancesType.addItem(newItemCaption);
                        advancesType.setValue(newItemCaption);
                    }
                }
            }
            
        });
        advancesType.setImmediate(true);
        advancesType.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if (!lastAdded) {
                    getWindow().showNotification(
                            "Selected Type: " + event.getProperty());
                }
                lastAdded = false;
            }
            
        });
        vlayout.addComponent(advancesType);
        
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
            public void buttonClick(ClickEvent event) {       
                if(advancesType.getValue() == null){
                    getWindow().showNotification("Select Type!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(particulars.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Enter Particulars!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(!amount.getValue().toString().trim().isEmpty()){
                    boolean result = conUtil.checkInputIfDouble(amount.getValue().toString().trim());
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
                                
                Double advances = Double.parseDouble(amount.getValue().toString().trim());
                String postedDate = conUtil.convertDateFormat(datePosted.getValue().toString());
                Boolean result = queryUpdate.updateSalaryByAdvances(salaryRowId, advances, amountToBeReceive, amountReceivable, adjustments, 
                        postedDate, advancesType.getValue().toString(), particulars.getValue().toString().trim(), employeeId);
                
                if(result == true){
                    employeeSalaryLedgerTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }
            }
            
        }); 
        vlayout.addComponent(button);
        
        ts.addComponent(vlayout);       
        
        subWindow.addComponent(ts);
        
        return subWindow;
    }
    
    private Window adjustSalary(final String salaryRowId, final Window window, final Table table, final List dates, final String payrollPeriod, 
            final String payrollDate, final String start_date, final String cut_off_date){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("ADJUST SALARY", vlayout);
        subWindow.setWidth("200px");
        
        Button adjustButtonSalary = new Button("ADJUST SALARY?");
        adjustButtonSalary.setWidth("100%");
        adjustButtonSalary.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                attendanceDateFrom = start_date;
                attendanceDateTo = cut_off_date;
                Window newWindow = (Window) attendanceTableContainer(window, table, dates, payrollPeriod, payrollDate, salaryRowId);
                if(newWindow.getParent() == null){
                    getWindow().addWindow(newWindow); 
                    (subWindow.getParent()).removeWindow(subWindow);
                }                    
                newWindow.setModal(true);
                newWindow.center();
            }
            
        });
        subWindow.addComponent(adjustButtonSalary);
        
        return subWindow;
    }
    
    private Window deleteSelectedRow(final String id){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("REMOVE SALARY", vlayout);
        subWindow.setWidth("200px");        
        
        Button removeSalary = new Button("REMOVE SALARY?");
        removeSalary.setWidth("100%");
        removeSalary.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                Boolean result = queryUpdate.removeSalary(id);
                if(result == true){
                    employeeSalaryLedgerTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    subWindow.getWindow().showNotification("UNABLE TO DELETE ROW!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(removeSalary);
        
        return subWindow;
    }
    
    private Window previewAttendanceTable(String attendanceDateFrom, String attendanceDateTo, Window subWindow, String id){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();        
        //subWindow = new Window("ATTENDANCE DATA PREVIEW for "+employeesName.getValue());
        subWindow.setWidth("1100px");
        
        Table table = new Table();
        table.setSizeFull();
        table.setImmediate(true);
        table.setSelectable(true);
        
        table.addContainerProperty("date", String.class, null);
        table.addContainerProperty("policy", String.class, null);
        table.addContainerProperty("holidays", String.class, null); 
        table.addContainerProperty("lp", String.class, null);
        table.addContainerProperty("lates", String.class, null);   
        table.addContainerProperty("up", String.class, null);
        table.addContainerProperty("undertime", String.class, null); 
        table.addContainerProperty("op", String.class, null);
        table.addContainerProperty("overtime", String.class, null);        
        table.addContainerProperty("night differential", String.class, null);
        table.addContainerProperty("ldeduction", String.class, null);
        table.addContainerProperty("udeduction", String.class, null);
        table.addContainerProperty("opaid", String.class, null);
        table.addContainerProperty("ndpaid", String.class, null);
        table.addContainerProperty("lhpaid", String.class, null);
        table.addContainerProperty("shpaid", String.class, null);
        table.addContainerProperty("wdo", String.class, null);
        
        try {
            int i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(" SELECT * FROM attendance WHERE salaryId = '"+id+"' AND (attendanceDate BETWEEN '"+attendanceDateFrom+"' AND '"+attendanceDateTo+"') ");
            while(rs.next()){
                table.addItem(new Object[]{
                    rs.getString("attendanceDate"),
                    rs.getString("policy"), 
                    rs.getString("holiday"),
                    rs.getString("latesPremium"),
                    rs.getString("lates"),
                    rs.getString("undertimePremium"),
                    rs.getString("undertime"),
                    rs.getString("overtimePremium"),
                    rs.getString("overtime"), 
                    rs.getString("nightDifferential"),
                    rs.getString("latesDeduction"),
                    rs.getString("undertimeDeduction"),
                    rs.getString("overtimePaid"),
                    rs.getString("nightDifferentialPaid"),
                    rs.getString("legalHolidayPaid"),
                    rs.getString("specialHolidayPaid"),
                    rs.getString("workingDayOffPaid")
                }, new Integer(i));
                i++;
            }
            double totalLates = 0;
            double totalUndertime = 0;
            double totalOvertime = 0;
            double totalNightDifferential = 0;
            double totalLatesDeduction = 0;
            double totalUndertimeDeduction = 0;
            double totalOvertimePaid = 0;
            double totalNightDifferentialPaid = 0;
            double totalLegalHolidayPaid = 0;
            double totalSpecialHolidayPaid = 0;
            double totalWorkingDayOffPaid = 0;
            
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT SUM(lates) AS lates, SUM(undertime) AS undertime, SUM(overtime) AS overtime, "
                    + "SUM(nightDifferential) AS nightDifferential, SUM(ROUND(latesDeduction, 2)) AS latesDeduction, SUM(ROUND(undertimeDeduction, 2)) AS undertimeDeduction, "
                    + "SUM(ROUND(overtimePaid, 2)) AS overtimePaid, SUM(ROUND(nightDifferentialPaid, 2)) AS nightDifferentialPaid, "
                    + "SUM(ROUND(legalHolidayPaid, 2)) AS legalHolidayPaid, SUM(ROUND(specialHolidayPaid, 2)) AS specialHolidayPaid,"
                    + "SUM(ROUND(workingDayOffPaid, 2)) AS workingDayOffPaid FROM attendance WHERE  salaryId = '"+id+"' AND "
                    + "(attendanceDate BETWEEN '"+attendanceDateFrom+"' AND '"+attendanceDateTo+"')");
            while(rs.next()){
                totalLates = conUtil.convertStringToDouble(rs.getString("lates"));
                totalUndertime = conUtil.convertStringToDouble(rs.getString("undertime"));
                totalOvertime = conUtil.convertStringToDouble(rs.getString("overtime"));
                totalNightDifferential = conUtil.convertStringToDouble(rs.getString("nightDifferential"));
                totalLatesDeduction = conUtil.convertStringToDouble(rs.getString("latesDeduction"));
                totalUndertimeDeduction = conUtil.convertStringToDouble(rs.getString("undertimeDeduction"));
                totalOvertimePaid = conUtil.convertStringToDouble(rs.getString("overtimePaid"));
                totalNightDifferentialPaid = conUtil.convertStringToDouble(rs.getString("nightDifferentialPaid"));
                totalLegalHolidayPaid = conUtil.convertStringToDouble(rs.getString("legalHolidayPaid"));
                totalSpecialHolidayPaid = conUtil.convertStringToDouble(rs.getString("specialHolidayPaid"));
                totalWorkingDayOffPaid = conUtil.convertStringToDouble(rs.getString("workingDayOffPaid"));
            }
            
            // Set the footers
            table.setFooterVisible(true);
            table.setColumnFooter("date", "Total");
            table.setColumnFooter(null, null);
            table.setColumnFooter(null, null);
            table.setColumnFooter(null, null);
            table.setColumnFooter("lates", String.valueOf(totalLates)+" mins");
            table.setColumnFooter(null, null);
            table.setColumnFooter("undertime", String.valueOf(totalUndertime)+" mins");
            table.setColumnFooter(null, null);
            table.setColumnFooter("overtime", String.valueOf(totalOvertime)+" mins");
            table.setColumnFooter("night differential", String.valueOf(totalNightDifferential)+" mins");
            table.setColumnFooter("ldeduction", "P"+String.valueOf(totalLatesDeduction));
            table.setColumnFooter("udeduction", "P"+String.valueOf(totalUndertimeDeduction));
            table.setColumnFooter("opaid", "P"+String.valueOf(totalOvertimePaid));
            table.setColumnFooter("ndpaid", "P"+String.valueOf(totalNightDifferentialPaid));
            table.setColumnFooter("lhpaid", "P"+String.valueOf(totalLegalHolidayPaid));
            table.setColumnFooter("shpaid", "P"+String.valueOf(totalSpecialHolidayPaid));
            table.setColumnFooter("wdo", "P"+String.valueOf(totalWorkingDayOffPaid));
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        subWindow.addComponent(table);
        return subWindow;
    }
    
    private Table advancesTable(final Boolean locked, final String salaryRowId, final Double amountToBeReceive, final Double amountReceivable, 
            final Double adjustments, final Window window){
                
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        newAdvancesTable.removeAllItems();
        newAdvancesTable.setWidth("100%");
        newAdvancesTable.setImmediate(true);
        newAdvancesTable.setSelectable(true);
        
        newAdvancesTable.addContainerProperty("id", String.class, null);
        newAdvancesTable.addContainerProperty("amount", String.class, null);
        newAdvancesTable.addContainerProperty("type", String.class, null);
        newAdvancesTable.addContainerProperty("particulars", String.class, null);
        newAdvancesTable.addContainerProperty("date posted", String.class, null);
        try {
            int i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(" SELECT * FROM advances WHERE salaryId = "+salaryId+" AND rowStatus IS NULL ");
            while(rs.next()){
                newAdvancesTable.addItem(new Object[]{
                    rs.getString("id"), 
                    rs.getString("amount"),
                    rs.getString("advancesType"),
                    rs.getString("particulars"), 
                    rs.getString("datePosted")}, 
                        new Integer(i));
                i++;
            }
        } catch (SQLException ex) {
            Logger.getLogger(AttendanceModule.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        for(Object listener : newAdvancesTable.getListeners(ItemClickEvent.class)){
            newAdvancesTable.removeListener(ItemClickEvent.class, listener);
        }
        
        newAdvancesTable.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                Object itemId = event.getItemId();
                final Item item = newAdvancesTable.getItem(itemId);
                              
                if(event.getPropertyId().equals("id")){
                    if(locked == true){
                        String obj = item.getItemProperty("amount").getValue().toString();                
                        Double removedAdvances = Double.valueOf(conUtil.removeCommaFromString(obj)).doubleValue();
                        int rowId = Integer.parseInt(item.getItemProperty("id").getValue().toString());
                        
                        Window subWindow = removeAdvances(salaryRowId, rowId, removedAdvances, amountToBeReceive, amountReceivable, 
                                adjustments, window);
                        if(subWindow.getParent() == null){
                            getWindow().addWindow(subWindow);
                        }
                        subWindow.setModal(true);
                        subWindow.center();
                    }else{
                        getWindow().showNotification("Contact your DBA to edit this ROW!", Window.Notification.TYPE_ERROR_MESSAGE);
                    }
                }
            }
            
        });
        return newAdvancesTable;
    }
    
    private Window removeAdvances(final String salaryRowId, final Integer rowId, final Double removedAdvances, final Double amountToBeReceive, 
            final Double amountReceivable, final Double adjustments, final Window window){
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
            public void buttonClick(ClickEvent event) {
                if(remarks.getValue() == null || remarks.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Add remarks!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                boolean result = queryUpdate.removeAdvancesOnSalary(salaryRowId, rowId, removedAdvances, 
                        amountToBeReceive, amountReceivable, adjustments, remarks.getValue().toString().trim());
                if(result == true){
                    employeeSalaryLedgerTable();
                    advancesTable(result, salaryRowId, amountToBeReceive, amountReceivable, adjustments, window);
                    (subWindow.getParent()).removeWindow(subWindow);
                    (window.getParent()).removeWindow(window);
                }
            }    

        });
        vlayout.addComponent(removeAdvances);
        
        subWindow.addComponent(vlayout);
        return subWindow;
    }
    
    private Window removePhicAndHdmf(final Double taxableSalary, final Double netPay, final Double amountToBeReceive, 
            final Double amountReceivable, final String id){
        final Window subWindow = new Window("REMOVE PHIC/HDMF");
        subWindow.setWidth("220px");
                        
        Button removePhicAndHdmf = new Button("REMOVE PHIC/HDMF AMOUNT");
        removePhicAndHdmf.setWidth("100%");
        removePhicAndHdmf.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                boolean result = queryUpdate.removePhicAndHdmf(taxableSalary, netPay, amountToBeReceive, amountReceivable, id);
                if(result == true){
                    employeeSalaryLedgerTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }
            }    

        });
        subWindow.addComponent(removePhicAndHdmf);
        return subWindow;
    }
    
    private Window removeSss(final double taxableSalary, final Double netPay, final Double amountToBeReceive, 
            final Double amountReceivable, final String id){
        final Window subWindow = new Window("REMOVE SSS");
        subWindow.setWidth("220px");
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        
        final TextField remarks = new TextField("Remarks: ");
        remarks.setWidth("100%");
        remarks.setNullSettingAllowed(true);
        vlayout.addComponent(remarks);
                        
        Button removeSss = new Button("REMOVE SSS AMOUNT");
        removeSss.setWidth("100%");
        removeSss.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(remarks.getValue() == null || remarks.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Add Remarks!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                boolean result = queryUpdate.removeSss(taxableSalary, netPay, amountToBeReceive, amountReceivable, id, remarks.getValue().toString().trim());
                if(result == true){
                    employeeSalaryLedgerTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }
            }    

        });
        vlayout.addComponent(removeSss);
        
        subWindow.addComponent(vlayout);
        return subWindow;
    }
    
    private Window editHdmfAmount(final double taxableSalary, final double allowance, final double hdmf, final Double adjustments, 
            final String id){
        final Window subWindow = new Window("EDIT HDMF");
        subWindow.setWidth("220px");
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        
        final TextField amount = new TextField("Amount: ");
        amount.setWidth("100%");
        amount.setValue(hdmf);
        amount.setNullSettingAllowed(true);
        vlayout.addComponent(amount);
        
        final TextField remarks = new TextField("Remarks: ");
        remarks.setWidth("100%");
        remarks.setNullSettingAllowed(true);
        vlayout.addComponent(remarks);
        
        Button editAmount = new Button("EDIT HDMF AMOUNT");
        editAmount.setWidth("100%");
        editAmount.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(remarks.getValue() == null || remarks.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Add Remarks!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                if(amount.getValue() == null || amount.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Null/Blank is not allowed!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }else{
                    Boolean result = conUtil.checkInputIfDouble(amount.getValue().toString().trim());
                    if(result == false ){
                        getWindow().showNotification("Amount is not a Numeric value!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                }
                Double newHdmf = Double.parseDouble(amount.getValue().toString().trim());
                Double taxable_salary = taxableSalary - newHdmf;
                String employmentWageStatus = salaryComputation.getEmploymentWageStatus();
                Double tax = 0.0;            
                if(employmentWageStatus.equals("regular") ){
                    tax = salaryComputation.getTax(salaryComputation.getTotalDependent(), taxable_salary);
                } 
                Double netPay = taxable_salary - tax;
                Double amountReceivable = netPay + allowance;
                Double amountToBeReceive = Math.round((amountReceivable + adjustments)*100.0)/100.0;
                Boolean result = queryUpdate.updateHdmf(newHdmf, hdmf, taxable_salary, netPay, amountToBeReceive, amountReceivable, tax, 
                        id, remarks.getValue().toString().trim());
                if(result == true){
                    employeeSalaryLedgerTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }
            }
            
        });
        vlayout.addComponent(editAmount);
        
        subWindow.addComponent(vlayout);
        return subWindow;
    }
    
    private Window editTax(final double taxableSalary, final double allowance, final double tax, final Double adjustments, final String id){
        final Window subWindow = new Window("EDIT TAX AMOUNT");
        subWindow.setWidth("220px");
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        
        final TextField amount = new TextField("Amount: ");
        amount.setWidth("100%");
        amount.setValue(tax);
        amount.setNullSettingAllowed(true);
        vlayout.addComponent(amount);
        
        final TextField remarks = new TextField("Remarks: ");
        remarks.setWidth("100%");
        remarks.setNullSettingAllowed(true);
        vlayout.addComponent(remarks);
        
        Button editAmount = new Button("EDIT HDMF AMOUNT");
        editAmount.setWidth("100%");
        editAmount.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(remarks.getValue() == null || remarks.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Add Remarks!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                if(amount.getValue() == null || amount.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Null/Blank is not allowed!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }else{
                    Boolean result = conUtil.checkInputIfDouble(amount.getValue().toString().trim());
                    if(result == false ){
                        getWindow().showNotification("Amount is not a Numeric value!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                }
                Double newTax = Double.parseDouble(amount.getValue().toString().trim());
                Double netPay = taxableSalary - newTax;
                Double amountReceivable = netPay + allowance;
                Double amountToBeReceive = Math.round((amountReceivable + adjustments)*100.0)/100.0;
                Boolean result = queryUpdate.updateTax(newTax, tax, netPay, amountToBeReceive, amountReceivable, id, 
                        remarks.getValue().toString().trim());
                if(result == true){
                    employeeSalaryLedgerTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }
            }
            
        });
        vlayout.addComponent(editAmount);
        
        subWindow.addComponent(vlayout);
        return subWindow;
    }
    
    private Window addCashBond(final String id, final Double amountReceivable, final Double adjustments){       
        
        final Window subWindow = new Window("ADD CASH BOND");
        subWindow.setWidth("300px");
        
        TabSheet ts = new TabSheet();
        ts.addStyleName("bar");
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        vlayout.setCaption("Post Cash Bond");
        
        final TextField amount = new TextField("Amount: ");
        amount.setWidth("100%");
        amount.setNullSettingAllowed(false);
        amount.setImmediate(true);
        vlayout.addComponent(amount);        
        
        Button saveCashBond = new Button("ADD CASH BOND");
        saveCashBond.setWidth("100%");
        saveCashBond.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                
                if(amount.getValue() == null){
                    subWindow.getWindow().showNotification("Enter an Amount!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }else{
                    Boolean result = conUtil.checkInputIfDouble(amount.getValue().toString().trim());
                    if(result == false){
                        subWindow.getWindow().showNotification("Entered Amount is INVALID!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                }
                
                Double cashBondAmount = conUtil.convertStringToDouble(amount.getValue().toString().trim());
                Double amount_receivable = amountReceivable - cashBondAmount;
                Double amountToBeReceive = Math.round((amount_receivable + adjustments)*100.0)/100.0;
                
                Boolean result = queryUpdate.updateSalaryByCashBond(id, cashBondAmount, amountToBeReceive, amount_receivable);
                if(result == true){
                    employeeSalaryLedgerTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }
            }
            
        });
        vlayout.addComponent(saveCashBond);
        
        ts.addComponent(vlayout);
        
        /*vlayout = new VerticalLayout();
        vlayout.setMargin(true);
        vlayout.setSpacing(true);
        vlayout.setCaption("Remove Cash Bond");
        
        Button removeCashBond = new Button("REMOVE CASH BOND");
        removeCashBond.setWidth("100%");
        removeCashBond.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if(cash_bond == 0.0){
                    subWindow.getWindow().showNotification("No Cash Bond to be REMOVED!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                double totalReceivable = amount_receivable + cash_bond;
                double bond = 0.0;
                Boolean result = queryUpdate.removeSalaryByCashBond(id, bond, totalReceivable);
                if(result == true){
                    employeeSalaryTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }
                
            }
            
        });
        vlayout.addComponent(removeCashBond); */
        
        ts.addComponent(vlayout);
        
        subWindow.addComponent(ts);
        return subWindow;
    }
    
    private Window editPayrollDate(final String id){
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
            public void buttonClick(ClickEvent event) {
                String date = conUtil.convertDateFormat(payrollDate.getValue().toString());
                Boolean result = queryUpdate.updatePayrollDate(id, date);
                if(result == true){
                    employeeSalaryLedgerTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    subWindow.getWindow().showNotification("UNABLE TO UPDATE ROW!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(button);
        return subWindow;
    }
    
    private Window lockRow(final String id){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("DISABLE EDITING", vlayout);
        subWindow.setWidth("200px");
        
        Button button = new Button("ARE YOU SURE YOU WANT TO LOCK THIS ROW?");
        button.setWidth("100%");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {                
                Boolean result = queryUpdate.updateSalaryRowStatus(id);
                if(result == true){
                    employeeSalaryLedgerTable();
                    (subWindow.getParent()).removeWindow(subWindow);
                }else{
                    subWindow.getWindow().showNotification("UNABLE TO UPDATE ROW!", Window.Notification.TYPE_ERROR_MESSAGE);
                }
            }
            
        });
        subWindow.addComponent(button);
        return subWindow;
    }
    
    private Window policyWindow(String[] policyList, final String[] holidayList, final Item item){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("CHANGE POLICY", vlayout);
        subWindow.setWidth("225px");
                
        final ComboBox policy = new ComboBox("Policy: ");
        policy.setWidth("100%");
        policy.setNullSelectionAllowed(true);
        for(String temp : policyList){
            policy.addItem(temp);
        }        
        policy.setImmediate(true);
        subWindow.addComponent(policy);
        
        final ComboBox holidayType = new ComboBox("Type: ");
        holidayType.setWidth("100%");
        holidayType.setNullSelectionAllowed(false);
        holidayType.setVisible(false);
        holidayType.setImmediate(true);
        for(String temp : holidayList){
            holidayType.addItem(temp);
        }
        policy.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                if(event.getProperty().getValue() == null){  
                    holidayType.setVisible(false);
                    holidayType.removeAllItems();
                    for(String temp : holidayList){
                        holidayType.addItem(temp);
                    }
                }else if(event.getProperty().getValue().toString().equals("holiday") || 
                        event.getProperty().getValue().toString().equals("working-holiday")){
                    holidayType.setVisible(true);
                }else{
                    holidayType.removeAllItems();
                    for(String temp : holidayList){
                        holidayType.addItem(temp);
                    }
                    holidayType.setVisible(false);
                }
            }
            
        });
        holidayType.addListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                double additionalHolidayRate;
                if(policy.getValue().equals("working-holiday")){
                    if(event.getProperty().getValue().toString().equals("legal-holiday")){
                        additionalHolidayRate = attendanceProcess.processAdditionalHolidayRate(event.getProperty().getValue().toString(), wage);
                        item.getItemProperty("lholiday").setValue(new Double(df.format(additionalHolidayRate)));
                        item.getItemProperty("sholiday").setValue(0.0);
                    }else{
                        additionalHolidayRate = attendanceProcess.processAdditionalHolidayRate(event.getProperty().getValue().toString(), wage);
                        item.getItemProperty("sholiday").setValue(new Double(df.format(additionalHolidayRate)));
                        item.getItemProperty("lholiday").setValue(0.0);
                    }
                }else if(policy.getValue().equals("holiday")){
                    if(event.getProperty().getValue().toString().equals("legal-holiday")){
                        additionalHolidayRate = attendanceProcess.processAdditionalHolidayRate(event.getProperty().getValue().toString(), wage);
                        item.getItemProperty("psday").setValue(new Double(df.format(additionalHolidayRate)));                            
                    }else{
                        item.getItemProperty("psday").setValue(0.0);
                    }
                }
            }
            
        });
        subWindow.addComponent(holidayType);
        
        Button button = new Button("UPDATE POLICY");
        button.setWidth("100%");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                String policyStr;
                if(policy.getValue() == null || policy.getValue().toString().isEmpty()){
                    policyStr = "";
                }else{
                    policyStr = policy.getValue().toString();
                }
                if(policyStr.equals("holiday") || policyStr.equals("working-holiday")){
                    if(holidayType.getValue() == null){
                        getWindow().showNotification("Select a Holiday type!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                }
                item.getItemProperty("policy").setValue(policyStr);
                item.getItemProperty("holidays").setValue(holidayType.getValue());                
                
                (subWindow.getParent()).removeWindow(subWindow);
            }
            
        });
        subWindow.addComponent(button);
        
        return subWindow;
    }
    
    private Window viewAdjustments(Integer salaryId){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("ADJUSTMENTS", vlayout);
        subWindow.setWidth("350px");
        
        AdjustmentsDAO adjustmentsDAO = new AdjustmentsDAO();
        
        Panel panel = new Panel("Remarks");
        panel.addComponent(new Label(adjustmentsDAO.getAdjustmentsRemarks(salaryId)));
        subWindow.addComponent(panel);
        
        return subWindow;
    }
    
    private Window clearRowTextField(final TextField lates, final CheckBox lp,final TextField undertime, final CheckBox up,
            final TextField overtime, final CheckBox op,final TextField nightDifferential, final Item item, final CheckBox edit){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("EDIT ROW", vlayout);
        subWindow.setWidth("200px");
        subWindow.setClosable(false);
        
        Label label = new Label("This will reset row data to 0.");
        subWindow.addComponent(label);
                
        Button proceed = new Button("Proceed");
        proceed.setWidth("100%");
        proceed.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                                 
                lates.setValue(0.0);
                lp.setValue(false);
                undertime.setValue(0.0);
                up.setValue(false);
                overtime.setValue(0.0);
                op.setValue(false);
                nightDifferential.setValue(0.0);
                item.getItemProperty("l/min").setValue(0.0); 
                item.getItemProperty("u/min").setValue(0.0); 
                item.getItemProperty("o/min").setValue(0.0);
                item.getItemProperty("nd/min").setValue(0.0);
                item.getItemProperty("lholiday").setValue(0.0);
                item.getItemProperty("sholiday").setValue(0.0);
                item.getItemProperty("psday").setValue(0.0);
                item.getItemProperty("wdo").setValue(0.0);
                (subWindow.getParent()).removeWindow(subWindow);                
            }
            
        });
        subWindow.addComponent(proceed);
        
        Button cancel = new Button("CANCEL");
        cancel.setWidth("100%");
        cancel.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                lates.setEnabled(false);
                lp.setEnabled(false);
                undertime.setEnabled(false);
                up.setEnabled(false);
                overtime.setEnabled(false);
                op.setEnabled(false);
                nightDifferential.setEnabled(false);
                edit.setValue(false);
                (subWindow.getParent()).removeWindow(subWindow);
            }
            
        });
        subWindow.addComponent(cancel);
        
        return subWindow;
    }
}
