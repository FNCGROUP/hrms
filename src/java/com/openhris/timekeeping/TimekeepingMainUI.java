/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.timekeeping;

import com.openhris.administrator.model.UserAccessControl;
import com.openhris.commons.DateSelector;
import com.openhris.payroll.ProcessPayrollComputation;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.employee.model.Employee;
import com.openhris.employee.serviceprovider.EmployeeServiceImpl;
import com.openhris.payroll.PayrollMainUI;
import com.openhris.payroll.serviceprovider.PayrollServiceImpl;
import com.openhris.employee.service.EmployeeService;
import com.openhris.payroll.service.PayrollService;
import com.openhris.timekeeping.service.TimekeepingService;
import com.openhris.timekeeping.model.Timekeeping;
import com.openhris.timekeeping.serviceprovider.TimekeepingServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author jet
 */
public class TimekeepingMainUI extends VerticalLayout {
    
    DropDownComponent dropDown = new DropDownComponent();
    EmployeeService employeeService = new EmployeeServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    TimekeepingComputation tcal = new TimekeepingComputation();
    
    TimekeepingService timekeepingService = new TimekeepingServiceImpl();
    PayrollService payrollService = new PayrollServiceImpl();
    Table timekeepingTbl = new TimekeepingTable();
    Table attendanceTable = new Table();
    int branchId;
    String employeeId;
    double premiumRate;
    
    ComboBox employeesName = new ComboBox(); 
    DecimalFormat df = new DecimalFormat("0.00");
    
    double employmentWage = 0;
    String employmentWageStatus;
    String employmentWageEntry;
    
    private static boolean EDIT_PAYROLL;
    
    public TimekeepingMainUI(){        
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
        
        vsplit.setSplitPosition(23, Sizeable.UNITS_PERCENTAGE);
        GridLayout comboBoxGrid = new GridLayout(3, 2);
        comboBoxGrid.setMargin(true);
        comboBoxGrid.setSpacing(true);
        comboBoxGrid.setWidth(90, Sizeable.UNITS_PERCENTAGE);
        
        vsplit.setFirstComponent(comboBoxGrid);
        addComponent(vsplit);
        
        setExpandRatio(vsplit, 1.0f);
        
        employeeComboBox(getBranchId());        
        comboBoxGrid.addComponent(employeesName, 0, 0);
        
        final PopupDateField payrollDate = new DateSelector("Payroll Date");
        comboBoxGrid.addComponent(payrollDate, 1, 0);
        
        final ComboBox payrollPeriod = dropDown.populatePayrollPeriodDropDownList(new ComboBox());
        comboBoxGrid.addComponent(payrollPeriod, 2, 0);
        
        final PopupDateField attendancePeriodFrom = new DateSelector("Attendance Period From: ");
        comboBoxGrid.addComponent(attendancePeriodFrom, 0, 1);
        
        final PopupDateField attendancePeriodTo = new DateSelector("Attendance Period To: ");
        comboBoxGrid.addComponent(attendancePeriodTo, 1, 1);
                
        Button openAttendanceTableWindowButton = new Button();
        if(!UserAccessControl.isTimekeeping()){
            openAttendanceTableWindowButton.setCaption("Attendance Button is Disabled");
            openAttendanceTableWindowButton.setEnabled(UserAccessControl.isTimekeeping());
        } else {
            openAttendanceTableWindowButton.setCaption("Generate Attendance Table:");
            openAttendanceTableWindowButton.setEnabled(UserAccessControl.isTimekeeping());
        }
        
        openAttendanceTableWindowButton.setWidth("100%");
        openAttendanceTableWindowButton.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
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

                String attendanceDateFrom = util.convertDateFormat(attendancePeriodFrom.getValue().toString());
                String attendanceDateTo = util.convertDateFormat(attendancePeriodTo.getValue().toString());
                    
                Date parsedAttendanceDateFrom = util.parsingDate(attendanceDateFrom);
                Date parsedAttendanceDateTo = util.parsingDate(attendanceDateTo);
                Date parsedPayrollDate = util.parsingDate(util.convertDateFormat(payrollDate.getValue().toString()));
                
                Date previousPayrollDate = timekeepingService.getPreviousPayrollDate(getEmployeeId());
                boolean attendanceDateFromExist = timekeepingService.checkAttendanceDateIfExist(attendanceDateFrom, getEmployeeId());
                boolean attendanceDateToExist = timekeepingService.checkAttendanceDateIfExist(attendanceDateTo, getEmployeeId());
                
                if(attendanceDateFromExist == false || attendanceDateToExist == false){
                    getWindow().showNotification("Attendance Date Range Already Exist", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(parsedPayrollDate.before(previousPayrollDate)){
                    getWindow().showNotification("Entered payroll date entry is not allowed!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                if(parsedAttendanceDateTo.before(parsedAttendanceDateFrom)){
                    getWindow().showNotification("Error Attendance Date Entry!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }

                if(parsedPayrollDate.before(parsedAttendanceDateTo) || parsedPayrollDate.equals(parsedAttendanceDateTo)){
                    getWindow().showNotification("Error Payroll Date Entry!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                List dateList;
                String checkEmployeeCurrentStatus = employeeService.getEmployeeCurrentStatus(getEmployeeId());
                if(checkEmployeeCurrentStatus == null){
                    Date parsedEntryDate = util.parsingDate(employeeService.getEmploymentEntryDate(getEmployeeId()));                    
                    if(parsedAttendanceDateFrom.before(parsedEntryDate)){
                        attendanceDateFrom = parsedEntryDate.toString();
                        dateList = obtainPayrollDateList(util.convertDateFormat(attendanceDateFrom), attendanceDateTo);
                    }else{
                        dateList = obtainPayrollDateList(attendanceDateFrom, attendanceDateTo);
                        if(dateList.size() < 12){
                            getWindow().showNotification("Payroll Date Range is less than 12 days!", Window.Notification.TYPE_ERROR_MESSAGE);
                            return;
                        }
                    }
                } else {
                    Date parsedEndDate = util.parsingDate(employeeService.getEmploymentEndDate(getEmployeeId()));
                    if(parsedEndDate.before(parsedAttendanceDateFrom)){
                        getWindow().showNotification("RESIGNED!", Window.Notification.TYPE_ERROR_MESSAGE);
                        return;
                    }
                    if(parsedEndDate.before(parsedAttendanceDateTo)){
                        attendanceDateTo = util.convertDateFormat(parsedEndDate.toString());
                    } 
                    dateList = obtainPayrollDateList(attendanceDateFrom, attendanceDateTo);
                }
                                
//                Window subWindow = attendanceTableContainer(employeesName.getValue().toString().toUpperCase(), 
//                        dateList, 
//                        getEmployeeId(), 
//                        payrollPeriod.getValue().toString(), 
//                        util.convertDateFormat(payrollDate.getValue().toString()), 
//                        util.convertDateFormat(attendancePeriodFrom.getValue().toString()), 
//                        util.convertDateFormat(attendancePeriodTo.getValue().toString()));
                Window subWindow = new AttendanceTableContainerWindow(employeesName.getValue().toString().toUpperCase(), 
                        dateList, 
                        getEmployeeId(), 
                        payrollPeriod.getValue().toString(), 
                        util.convertDateFormat(payrollDate.getValue().toString()), 
                        util.convertDateFormat(attendancePeriodFrom.getValue().toString()), 
                        util.convertDateFormat(attendancePeriodTo.getValue().toString()), 
                        getEmploymentWageEntry(), 
                        getEmploymentWage(), 
                        getBranchId());
                subWindow.setWidth("100%");
                subWindow.setModal(true);
                if(subWindow.getParent() == null){
                    getWindow().addWindow(subWindow);
                }
                subWindow.center();
                subWindow.addListener(new Window.CloseListener() {

                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        employmentWage = employeeService.getEmploymentWage(getEmployeeId());
                    }
                });
                subWindow.addListener(new Window.CloseListener() {

                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        timekeepingTable(getEmployeeId());                        
                    }
                });
            }
        });
        comboBoxGrid.addComponent(openAttendanceTableWindowButton, 2, 1);
        comboBoxGrid.setComponentAlignment(openAttendanceTableWindowButton, Alignment.BOTTOM_CENTER);
                 
        vsplit.setSecondComponent(timekeepingTbl);        
    }
        
//    public Window attendanceTableContainer(String name, 
//            List dateList, 
//            final String employeeId, 
//            final String payrollPeriod, 
//            final String payrollDate, 
//            final String attendancePeriodFrom, 
//            final String attendancePeriodTo){
//        final Window subWindow = new Window("ATTENDANCE TABLE for " +name);
//        subWindow.setSizeFull();
//        
//        final Table table = new Table();        
//        table.removeAllItems();
//        table.setEnabled(true);
////        table.setWidth("100%");
//        table.setSizeFull();
//        table.setImmediate(true);
//        table.setColumnCollapsingAllowed(true);
//                
//        table.addContainerProperty("date", String.class, null);
//        table.addContainerProperty("policy", ComboBox.class, null);
//        table.addContainerProperty("holidays", ComboBox.class, null); 
//        table.addContainerProperty("premium", CheckBox.class, null); 
//        table.addContainerProperty("lates", TextField.class, null);  
//        table.addContainerProperty("undertime", TextField.class, null); 
//        table.addContainerProperty("overtime", TextField.class, null);        
//        table.addContainerProperty("night differential", TextField.class, null);  
//        table.addContainerProperty("duty manager", TextField.class, null);
//        table.addContainerProperty("l/min", Double.class, null); 
//        table.addContainerProperty("u/min", Double.class, null); 
//        table.addContainerProperty("o/min", Double.class, null);
//        table.addContainerProperty("nd/min", Double.class, null);
//        table.addContainerProperty("dm/min", Double.class, null);
//        table.addContainerProperty("lholiday", Double.class, null);
//        table.addContainerProperty("sholiday", Double.class, null);
//        table.addContainerProperty("wdo", Double.class, null);
//        table.addContainerProperty("psday", Double.class, null); //paid non-working holiday
//        
//        table.setColumnAlignment("date", Table.ALIGN_CENTER);
//        table.setColumnAlignment("policy", Table.ALIGN_CENTER);
//        table.setColumnAlignment("premium", Table.ALIGN_CENTER);
//        table.setColumnAlignment("lates", Table.ALIGN_CENTER);
//        table.setColumnAlignment("undertime", Table.ALIGN_CENTER);
//        table.setColumnAlignment("overtime", Table.ALIGN_CENTER);
//        table.setColumnAlignment("night differential", Table.ALIGN_CENTER);
//        table.setColumnAlignment("duty manager", Table.ALIGN_CENTER);
//        table.setColumnAlignment("l/min", Table.ALIGN_RIGHT);
//        table.setColumnAlignment("u/min", Table.ALIGN_RIGHT);
//        table.setColumnAlignment("o/min", Table.ALIGN_RIGHT);
//        table.setColumnAlignment("nd/min", Table.ALIGN_RIGHT);
//        table.setColumnAlignment("dm/min", Table.ALIGN_RIGHT);
//        table.setColumnAlignment("lholiday", Table.ALIGN_RIGHT);
//        table.setColumnAlignment("sholiday", Table.ALIGN_RIGHT);
//        table.setColumnAlignment("wdo", Table.ALIGN_RIGHT);
//        table.setColumnAlignment("psday", Table.ALIGN_RIGHT);
//        
//        table.setColumnWidth("date", 70);
//        table.setColumnWidth("policy", 125);
//        table.setColumnWidth("holidays", 125);
//        table.setColumnWidth("premium", 60);
//        table.setColumnWidth("lates", 50);
//        table.setColumnWidth("undertime", 60);
//        table.setColumnWidth("overtime", 50);
//        table.setColumnWidth("night differential", 110);
//        table.setColumnWidth("duty manager", 80);
//        table.setColumnWidth("l/min", 40);
//        table.setColumnWidth("u/min", 40);
//        table.setColumnWidth("o/min", 40);
//        table.setColumnWidth("nd/min", 50);
//        
////        table.setColumnCollapsed("l/min", true);
////        table.setColumnCollapsed("u/min", true);
////        table.setColumnCollapsed("o/min", true);
////        table.setColumnCollapsed("nd/min", true);
////        table.setColumnCollapsed("dm/min", true);
////        table.setColumnCollapsed("lholiday", true);
////        table.setColumnCollapsed("sholiday", true);
////        table.setColumnCollapsed("wdo", true);
////        table.setColumnCollapsed("psday", true);
//                        
//        final String[] holidayList = {"legal-holiday", "special-holiday"};        
//        if(employmentWageEntry.equals("monthly")){
//            employmentWage = util.roundOffToTwoDecimalPlaces((employmentWage * 12) / 314);
//        }
//        
//        for(int i = 0; i < dateList.size(); i++){ 
//            Object itemId = new Integer(i);
//            final ComboBox holidays = dropDown.populateAttendanceHolidayDropDownList(new ComboBox());
//            holidays.setEnabled(false);
//            holidays.setWidth("120px");
//            holidays.setNullSelectionAllowed(false);
//            holidays.setData(itemId);
//            
//            final CheckBox premium = new CheckBox();
//            premium.setData(itemId);
//            premium.setImmediate(true);
//            
//            final TextField lates = new TextField();
//            lates.setWidth("100%");
//            lates.setValue("0");
//            lates.addStyleName("numerical");
//            lates.setEnabled(true);
//            lates.setData(itemId);
//            lates.setImmediate(true);
//                        
//            final TextField undertime = new TextField();
//            undertime.setWidth("100%");
//            undertime.setValue("0");
//            undertime.addStyleName("numerical");
//            undertime.setEnabled(true);
//            undertime.setData(itemId);
//            undertime.setImmediate(true);
//                        
//            final TextField overtime = new TextField();
//            overtime.setWidth("100%");
//            overtime.setValue("0");
//            overtime.addStyleName("numerical");
//            overtime.setEnabled(true);
//            overtime.setData(itemId);
//            overtime.setImmediate(true);
//            
//            final TextField nightDifferential = new TextField();
//            nightDifferential.setWidth("70%");
//            nightDifferential.setValue("0");
//            nightDifferential.addStyleName("numerical");
//            nightDifferential.setEnabled(true);
//            nightDifferential.setData(itemId);
//            nightDifferential.setImmediate(true);
//            
//            final TextField dutyManager = new TextField();
//            dutyManager.setWidth("80%");
//            dutyManager.setValue("0");
//            dutyManager.addStyleName("numerical");
//            dutyManager.setEnabled(true);
//            dutyManager.setData(itemId);
//            dutyManager.setImmediate(true);
//            
//            final ComboBox policy = dropDown.populateAttendancePolicyDropDownList(new ComboBox());
//            policy.setWidth("120px");
//            policy.setNullSelectionAllowed(true);
//            policy.setData(itemId);              
//            policy.addListener(new Property.ValueChangeListener() {
//
//                @Override
//                public void valueChange(Property.ValueChangeEvent event) {
//                    Object itemId = policy.getData();
//                    Item item = table.getItem(itemId);
//                    double additionalWorkingDayOffPay = 0;
//                    
//                    holidays.removeAllItems();
//                    for(String temp : holidayList){
//                        holidays.addItem(temp);
//                    }
//                                        
//                    premium.setValue(false);
//                    lates.setValue("0");
//                    undertime.setValue("0");
//                    overtime.setValue("0");
//                    nightDifferential.setValue("0");
//                    item.getItemProperty("l/min").setValue(0.0);
//                    item.getItemProperty("u/min").setValue(0.0);
//                    item.getItemProperty("o/min").setValue(0.0);
//                    item.getItemProperty("nd/min").setValue(0.0);
//                    item.getItemProperty("dm/min").setValue(0.0);
//                    item.getItemProperty("sholiday").setValue(0.0);
//                    item.getItemProperty("lholiday").setValue(0.0);
//                    item.getItemProperty("wdo").setValue(0.0);
//                    item.getItemProperty("psday").setValue(0.0);
//                    
//                    if(event.getProperty().getValue() == null){                        
//                        holidays.setEnabled(false);
//                        lates.setEnabled(true);
//                        undertime.setEnabled(true);
//                        overtime.setEnabled(true);
//                        nightDifferential.setEnabled(true);
//                        dutyManager.setEnabled(true);
//                    } else if(event.getProperty().getValue().equals("holiday")){
//                        holidays.setEnabled(true);   
//                        lates.setEnabled(false);
//                        undertime.setEnabled(false);
//                        overtime.setEnabled(false);
//                        nightDifferential.setEnabled(false);
//                        dutyManager.setEnabled(false);
//                    } else if(event.getProperty().getValue().equals("working-holiday")) { 
//                        holidays.setEnabled(true);
//                        lates.setEnabled(true);
//                        undertime.setEnabled(true);
//                        overtime.setEnabled(true);
//                        nightDifferential.setEnabled(true);
//                        dutyManager.setEnabled(true);
//                    } else if(event.getProperty().getValue().equals("working-day-off")){
//                        holidays.setEnabled(true);
//                        lates.setEnabled(true);
//                        undertime.setEnabled(true);
//                        overtime.setEnabled(true);
//                        nightDifferential.setEnabled(true);
//                        dutyManager.setEnabled(true);
//                        
//                        additionalWorkingDayOffPay = tcal.processAdditionalWorkingDayOff(employmentWage, employmentWageEntry);
//                        item.getItemProperty("wdo").setValue(df.format(additionalWorkingDayOffPay));
//                    } else {
//                        holidays.setEnabled(false);
//                        lates.setEnabled(false);
//                        undertime.setEnabled(false);
//                        overtime.setEnabled(false);
//                        nightDifferential.setEnabled(false);
//                        dutyManager.setEnabled(false);
//                    }                    
//                }
//            });    
//            policy.setImmediate(true);
//            
//            holidays.addListener(new ComboBox.ValueChangeListener() {
//
//                @Override
//                public void valueChange(Property.ValueChangeEvent event) {
//                    Object itemId = holidays.getData();
//                    Item item = table.getItem(itemId);
//                    
//                    String policyStr = item.getItemProperty("policy").toString();                    
//                    double additionalHolidayPay = 0;
//                    double multiplePremiumPay = 0;
//                    double additionalWorkingDayOffPay = 0;
//                    
//                    premium.setValue(false);
//                    lates.setValue("0");
//                    undertime.setValue("0");
//                    overtime.setValue("0");
//                    nightDifferential.setValue("0");
//                    item.getItemProperty("sholiday").setValue(0.0);
//                    item.getItemProperty("lholiday").setValue(0.0);
//                    
//                    if(policyStr.equals("holiday") || policy.getValue().toString().equals("working-holiday")){
//                        if(event.getProperty().getValue() == null || event.getProperty().getValue().toString().isEmpty()){
//                            getWindow().showNotification("Select Type of Holiday!", Window.Notification.TYPE_WARNING_MESSAGE);
//                            return;
//                        }
//                    }
//                    
//                    if(policyStr.equals("working-holiday")){
//                        if(event.getProperty().getValue().equals("legal-holiday")){
//                            additionalHolidayPay = tcal.processAdditionalHolidayPay(event.getProperty().getValue().toString(), employmentWage);
//                            item.getItemProperty("lholiday").setValue(new Double(df.format(additionalHolidayPay)));
//                            item.getItemProperty("sholiday").setValue(0.0);
//                        } else {
//                            additionalHolidayPay = tcal.processAdditionalHolidayPay(event.getProperty().getValue().toString(), employmentWage);
//                            item.getItemProperty("sholiday").setValue(new Double(df.format(additionalHolidayPay)));
//                            item.getItemProperty("lholiday").setValue(0.0);
//                        }
//                    } else if(policyStr.equals("holiday")) {
//                        if(event.getProperty().getValue().equals("legal-holiday")){
//                            if(employmentWageEntry.equals("daily")){
//                                additionalHolidayPay = tcal.processAdditionalHolidayPay(event.getProperty().getValue().toString(), employmentWage);
//                                item.getItemProperty("psday").setValue(new Double(df.format(additionalHolidayPay))); 
//                            } else {
//                                item.getItemProperty("psday").setValue(0.0);
//                            }
//                        } else {
//                            item.getItemProperty("psday").setValue(0.0);
//                        }
//                    } else if(policyStr.equals("working-day-off")) {
//                        if(event.getProperty().getValue() == null){
//                            item.getItemProperty("psday").setValue(0.0);
//                        } else if(event.getProperty().getValue().equals("legal-holiday")){
//                            additionalWorkingDayOffPay = tcal.processAdditionalWorkingDayOff(employmentWage, employmentWageEntry);
//                            item.getItemProperty("wdo").setValue(df.format(additionalWorkingDayOffPay));
//                            multiplePremiumPay = tcal.processMultiplePremiumPay(event.getProperty().getValue().toString(), employmentWage);
//                            item.getItemProperty("lholiday").setValue(multiplePremiumPay);
//                        } else {
//                            additionalWorkingDayOffPay = tcal.processAdditionalWorkingDayOff(employmentWage, employmentWageEntry);
//                            item.getItemProperty("wdo").setValue(df.format(additionalWorkingDayOffPay));
//                            multiplePremiumPay = tcal.processMultiplePremiumPay(event.getProperty().getValue().toString(), employmentWage);
//                            item.getItemProperty("sholiday").setValue(multiplePremiumPay);
//                        }
//                    }                   
//                }
//            });
//            holidays.setImmediate(true);
//                    
//            premium.addListener(new Button.ClickListener() {
//
//                @Override
//                public void buttonClick(Button.ClickEvent event) {
//                    Object itemId = lates.getData();
//                    Item item = table.getItem(itemId);
//                    
//                    lates.setValue("0");
//                    undertime.setValue("0");
//                    overtime.setValue("0");
//                    nightDifferential.setValue("0");   
//                    dutyManager.setValue("0");
//                    
//                    item.getItemProperty("l/min").setValue(0.0);
//                    item.getItemProperty("u/min").setValue(0.0);
//                    item.getItemProperty("o/min").setValue(0.0);
//                    item.getItemProperty("nd/min").setValue(0.0);
//                    item.getItemProperty("dm/min").setValue(0.0);
//                    
//                    if(event.getButton().booleanValue() == true){
//                        premiumRate = 0.1;                        
//                    } else {
//                        premiumRate = 0.0;
//                    }
//                    
//                    item.getItemProperty("wdo").setValue(
//                        df.format(Double.parseDouble(item.getItemProperty("wdo").getValue().toString()) + 
//                            (Double.parseDouble(item.getItemProperty("wdo").getValue().toString()) * premiumRate))
//                    );
//                    
//                    item.getItemProperty("lholiday").setValue(
//                        df.format(Double.parseDouble(item.getItemProperty("lholiday").getValue().toString()) + 
//                            (Double.parseDouble(item.getItemProperty("lholiday").getValue().toString()) * premiumRate))
//                    );
//                    
//                    item.getItemProperty("sholiday").setValue(
//                        df.format(Double.parseDouble(item.getItemProperty("sholiday").getValue().toString()) + 
//                            (Double.parseDouble(item.getItemProperty("sholiday").getValue().toString()) * premiumRate))
//                    );
//                }
//            });
//            
//            lates.addListener(new FieldEvents.TextChangeListener() {
//
//                @Override
//                public void textChange(FieldEvents.TextChangeEvent event) {
//                    Object itemId = lates.getData();
//                    Item item = table.getItem(itemId); 
//                    String policyStr = item.getItemProperty("policy").toString();
//                    String holidayStr = item.getItemProperty("holidays").toString();
//                    double lateDeduction;
//                    
//                    boolean checkIfInputIsInteger = util.checkInputIfInteger(event.getText().trim());
//                    if(!checkIfInputIsInteger){
//                        getWindow().showNotification("Enter numeric format for lates!", Window.Notification.TYPE_WARNING_MESSAGE);
//                        return;
//                    }
//                    
//                    if(!event.getText().isEmpty()){
//                        lateDeduction = tcal.processEmployeesLates(policyStr, holidayStr, Integer.parseInt(event.getText().trim()), employmentWage);
//                        item.getItemProperty("l/min").setValue(df.format(lateDeduction + (lateDeduction*premiumRate)));
//                    }else{
//                        item.getItemProperty("l/min").setValue(0.0);
//                    }
//                    
//                }
//            });
//            
//            undertime.addListener(new FieldEvents.TextChangeListener() {
//
//                @Override
//                public void textChange(FieldEvents.TextChangeEvent event) {
//                    Object itemId = lates.getData();
//                    Item item = table.getItem(itemId); 
//                    String policyStr = item.getItemProperty("policy").toString();
//                    String holidayStr = item.getItemProperty("holidays").toString();
//                    double undertimeDeduction;
//                    
//                    boolean checkIfInputIsInteger = util.checkInputIfInteger(event.getText().trim());
//                    if(!checkIfInputIsInteger){
//                        getWindow().showNotification("Enter numeric format for undertime!", Window.Notification.TYPE_WARNING_MESSAGE);
//                        return;
//                    }
//                    
//                    if(!event.getText().isEmpty()){
//                        undertimeDeduction = tcal.processEmployeesUndertime(policyStr, holidayStr, Integer.parseInt(event.getText().trim()), employmentWage);
//                        item.getItemProperty("u/min").setValue(df.format(undertimeDeduction + (undertimeDeduction*premiumRate)));
//                    }else{
//                        item.getItemProperty("u/min").setValue(0.0);
//                    }
//                }
//            });
//            
//            overtime.addListener(new FieldEvents.TextChangeListener() {
//
//                @Override
//                public void textChange(FieldEvents.TextChangeEvent event) {
//                    Object itemId = lates.getData();
//                    Item item = table.getItem(itemId); 
//                    String policyStr = item.getItemProperty("policy").toString();
//                    String holidayStr = item.getItemProperty("holidays").toString();
//                    double overtimeAddition;
//                    
//                    boolean checkIfInputIsInteger = util.checkInputIfInteger(event.getText().trim());
//                    if(!checkIfInputIsInteger){
//                        getWindow().showNotification("Enter numeric format for undertime!", Window.Notification.TYPE_WARNING_MESSAGE);
//                        return;
//                    }
//                    
//                    if(!event.getText().isEmpty()){
//                        overtimeAddition = tcal.processEmployeesOvertime(policyStr, holidayStr, Integer.parseInt(event.getText().trim()), employmentWage);
//                        item.getItemProperty("o/min").setValue(df.format(overtimeAddition + (overtimeAddition*premiumRate)));
//                    }else{
//                        item.getItemProperty("o/min").setValue(0.0);
//                    }
//                }
//            });
//            
//            nightDifferential.addListener(new FieldEvents.TextChangeListener() {
//
//                @Override
//                public void textChange(FieldEvents.TextChangeEvent event) {
//                    Object itemId = lates.getData();
//                    Item item = table.getItem(itemId); 
//                    String policyStr = item.getItemProperty("policy").toString();
//                    String holidayStr = item.getItemProperty("holidays").toString();
//                    double nightDifferentialAddition;
//                    
//                    boolean checkIfInputIsInteger = util.checkInputIfInteger(event.getText().trim());
//                    if(!checkIfInputIsInteger){
//                        getWindow().showNotification("Enter numeric format for undertime!", Window.Notification.TYPE_WARNING_MESSAGE);
//                        return;
//                    }
//                    
//                    if(!event.getText().isEmpty()){
//                        nightDifferentialAddition = tcal.processEmployeesNightDifferential(policyStr, holidayStr, util.convertStringToInteger(event.getText().trim()), employmentWage);
//                        item.getItemProperty("nd/min").setValue(df.format(nightDifferentialAddition + (nightDifferentialAddition*premiumRate)));
//                    }else{
//                        item.getItemProperty("nd/min").setValue(0.0);
//                    }
//                }
//            });
//            
//            dutyManager.addListener(new FieldEvents.TextChangeListener() {
//
//                @Override
//                public void textChange(FieldEvents.TextChangeEvent event) {
//                    Object itemId = lates.getData();
//                    Item item = table.getItem(itemId); 
//                    String policyStr = item.getItemProperty("policy").toString();
//                    String holidayStr = item.getItemProperty("holidays").toString();
//                    double dutyManagerAddition;
//                    
//                    boolean checkIfInputIsInteger = util.checkInputIfInteger(event.getText().trim());
//                    if(!checkIfInputIsInteger){
//                        getWindow().showNotification("Enter numeric format for Duty Manager!", Window.Notification.TYPE_WARNING_MESSAGE);
//                        return;
//                    }
//                    
//                    if(!event.getText().isEmpty()){
//                        dutyManagerAddition = tcal.processEmployeeDutyManager(policyStr, holidayStr, util.convertStringToInteger(event.getText().trim()), employmentWage);
//                        item.getItemProperty("dm/min").setValue(df.format(dutyManagerAddition));
//                    }else{
//                        item.getItemProperty("dm/min").setValue(0.0);
//                    }
//                }
//            });
//            
//            table.addItem(new Object[]{
//                util.convertDateFormat(dateList.get(i).toString()), 
//                policy, holidays, 
//                premium, lates, 
//                undertime, overtime, 
//                nightDifferential, 
//                dutyManager,
//                0.0, 0.0, 0.0, 
//                0.0, 0.0, 0.0, 
//                0.0, 0.0, 0.0
//           }, i);                       
//        }                
//        table.setPageLength(table.size());        
//        
//        subWindow.addComponent(table);
//        
//        final Button button = new Button();
//        button.setCaption("Save Attendance Data");
//        
//        
//        for(Object listener : button.getListeners(Button.ClickListener.class)){
//            button.removeListener(Button.ClickListener.class, listener);
//        }
//        
//        button.addListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(Button.ClickEvent event) {                
//                try{                                       
//                    Collection attendanceTableCollection = table.getContainerDataSource().getItemIds();
//                    List<Timekeeping> attendanceList = new ArrayList<Timekeeping>();
//                    for(int i = 0; i < attendanceTableCollection.size(); i++){
//                        String str = table.getItem(i).toString();
//                        String[] attStr = str.split(" ");
//                        List<String> tkeepList = new ArrayList<String>(Arrays.asList(attStr));
//                        
//                        Timekeeping t = new Timekeeping();
//                        t.setAttendanceDate(util.parsingDate(tkeepList.get(0)));
//                        t.setPolicy(tkeepList.get(1));
//                        t.setHoliday(tkeepList.get(2));
//                        t.setPremium(util.convertStringToBoolean(tkeepList.get(3)));
//                        t.setLates(util.convertStringToDouble(tkeepList.get(4)));
//                        t.setUndertime(util.convertStringToDouble(tkeepList.get(5)));
//                        t.setOvertime(util.convertStringToDouble(tkeepList.get(6)));
//                        t.setNightDifferential(util.convertStringToDouble(tkeepList.get(7)));
//                        t.setDutyManager(util.convertStringToDouble(tkeepList.get(8)));
//                        t.setLateDeduction(util.convertStringToDouble(tkeepList.get(9)));
//                        t.setUndertimeDeduction(util.convertStringToDouble(tkeepList.get(10)));
//                        t.setOvertimePaid(util.convertStringToDouble(tkeepList.get(11)));
//                        t.setNightDifferentialPaid(util.convertStringToDouble(tkeepList.get(12)));
//                        t.setDutyManagerPaid(util.convertStringToDouble(tkeepList.get(13)));
//                        t.setLegalHolidayPaid(util.convertStringToDouble(tkeepList.get(14)));
//                        t.setSpecialHolidayPaid(util.convertStringToDouble(tkeepList.get(15)));
//                        t.setWorkingDayOffPaid(util.convertStringToDouble(tkeepList.get(16)));
//                        t.setNonWorkingHolidayPaid(util.convertStringToDouble(tkeepList.get(17)));
//                        attendanceList.add(t);
//                    }
//                    
//                    ProcessPayrollComputation processPayroll = new ProcessPayrollComputation(employeeId, branchId);
//                    processPayroll.initVariables();
//                    processPayroll.initVariablesForComputation(attendanceList);
//                    boolean result = processPayroll.processPayrollComputation(payrollDate, 
//                            payrollPeriod, 
//                            attendancePeriodFrom, 
//                            attendancePeriodTo, 
//                            false, 
//                            0);
//                    PayrollMainUI payrollMainUI = new PayrollMainUI(branchId);
//                    if(result){
//                        (subWindow.getParent()).removeWindow(subWindow);  
//                        timekeepingTable(getEmployeeId());
//                        payrollMainUI.payrollTable(branchId, employeeId);
//                    } else {
//                        getWindow().showNotification("SQL ERROR");
//                    }
//                }catch(Exception e){
//                    e.getMessage();
//                }
//            }
//            
//        });
//               
//        subWindow.addComponent(button);
//        
//        return subWindow;
//    }
    
    public Window editAttendanceTableContainer(String name, 
            List dateList, 
            final String employeeId, 
            final String payrollPeriod, 
            final String payrollDate, 
            final String attendancePeriodFrom, 
            final String attendancePeriodTo, 
            final int payrollId){
        final Window subWindow = new Window("ATTENDANCE TABLE for " +name);
        final Table table = new Table();
        
        table.removeAllItems();
        table.setEnabled(true);
        table.setWidth("100%");
        table.setImmediate(true);
        table.setColumnCollapsingAllowed(true);
                
        table.addContainerProperty("edit", CheckBox.class, null);
        table.addContainerProperty("date", String.class, null);
        table.addContainerProperty("policy", String.class, null);
        table.addContainerProperty("holidays", String.class, null); 
        table.addContainerProperty("premium", CheckBox.class, null); 
        table.addContainerProperty("lates", TextField.class, null);  
        table.addContainerProperty("undertime", TextField.class, null); 
        table.addContainerProperty("overtime", TextField.class, null);        
        table.addContainerProperty("night differential", TextField.class, null);  
        table.addContainerProperty("l/min", Double.class, null); 
        table.addContainerProperty("u/min", Double.class, null); 
        table.addContainerProperty("o/min", Double.class, null);
        table.addContainerProperty("nd/min", Double.class, null);
        table.addContainerProperty("lholiday", Double.class, null);
        table.addContainerProperty("sholiday", Double.class, null);
        table.addContainerProperty("wdo", Double.class, null);
        table.addContainerProperty("psday", Double.class, null); //paid non-working holiday
        
        table.setColumnAlignment("edit", Table.ALIGN_CENTER);
        table.setColumnAlignment("date", Table.ALIGN_CENTER);
        table.setColumnAlignment("policy", Table.ALIGN_CENTER);
        table.setColumnAlignment("premium", Table.ALIGN_CENTER);
        table.setColumnAlignment("lates", Table.ALIGN_CENTER);
        table.setColumnAlignment("undertime", Table.ALIGN_CENTER);
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
        
        table.setColumnCollapsed("l/min", true);
        table.setColumnCollapsed("u/min", true);
        table.setColumnCollapsed("o/min", true);
        table.setColumnCollapsed("nd/min", true);
        table.setColumnCollapsed("lholiday", true);
        table.setColumnCollapsed("sholiday", true);
        table.setColumnCollapsed("wdo", true);
        table.setColumnCollapsed("psday", true);
                        
        final String[] holidayList = {"legal-holiday", "special-holiday"};
        employmentWage = employeeService.getEmploymentWage(employeeId);
        employmentWageStatus = employeeService.getEmploymentWageStatus(employeeId);
        employmentWageEntry = employeeService.getEmploymentWageEntry(employeeId);
        if(employmentWageEntry.equals("monthly")){
            employmentWage = (employmentWage * 12) / 314;
        }
                
        for(int i = 0; i < dateList.size(); i++){ 
            Object itemId = new Integer(i);
            
            final CheckBox edit = new CheckBox();
            edit.setData(i);
            edit.setImmediate(true);
            
            final CheckBox premium = new CheckBox();
            premium.setData(itemId);
            premium.setEnabled(false);
            premium.setImmediate(true);
            
            final TextField lates = new TextField();
            lates.setWidth("50px");
            lates.setValue("0");
            lates.setEnabled(true);
            lates.setData(itemId);
            lates.setEnabled(false);
            lates.setImmediate(true);
                        
            final TextField undertime = new TextField();
            undertime.setWidth("50px");
            undertime.setValue("0");
            undertime.setEnabled(true);
            undertime.setData(itemId);
            undertime.setEnabled(false);
            undertime.setImmediate(true);
                        
            final TextField overtime = new TextField();
            overtime.setWidth("50px");
            overtime.setValue("0");
            overtime.setEnabled(true);
            overtime.setData(itemId);
            overtime.setEnabled(false);
            overtime.setImmediate(true);
            
            final TextField nightDifferential = new TextField();
            nightDifferential.setWidth("50px");
            nightDifferential.setValue("0");
            nightDifferential.setEnabled(true);
            nightDifferential.setData(itemId);
            nightDifferential.setEnabled(false);
            nightDifferential.setImmediate(true);
                       
            edit.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    premium.setEnabled(event.getButton().booleanValue());
                    lates.setEnabled(event.getButton().booleanValue());
                    undertime.setEnabled(event.getButton().booleanValue());
                    overtime.setEnabled(event.getButton().booleanValue());
                    nightDifferential.setEnabled(event.getButton().booleanValue());
                }
            });
            
            premium.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId);
                    
                    lates.setValue("0");
                    undertime.setValue("0");
                    overtime.setValue("0");
                    nightDifferential.setValue("0");                    
                    
                    item.getItemProperty("l/min").setValue(0.0);
                    item.getItemProperty("u/min").setValue(0.0);
                    item.getItemProperty("o/min").setValue(0.0);
                    item.getItemProperty("nd/min").setValue(0.0);
                    
                    if(event.getButton().booleanValue() == true){
                        premiumRate = 0.1;                        
                    } else {
                        premiumRate = 0.0;
                    }
                    
                    item.getItemProperty("wdo").setValue(
                        df.format(Double.parseDouble(item.getItemProperty("wdo").getValue().toString()) + 
                            (Double.parseDouble(item.getItemProperty("wdo").getValue().toString()) * premiumRate))
                    );
                    
                    item.getItemProperty("lholiday").setValue(
                        df.format(Double.parseDouble(item.getItemProperty("lholiday").getValue().toString()) + 
                            (Double.parseDouble(item.getItemProperty("lholiday").getValue().toString()) * premiumRate))
                    );
                    
                    item.getItemProperty("sholiday").setValue(
                        df.format(Double.parseDouble(item.getItemProperty("sholiday").getValue().toString()) + 
                            (Double.parseDouble(item.getItemProperty("sholiday").getValue().toString()) * premiumRate))
                    );
                }
            });
            
            lates.addListener(new FieldEvents.TextChangeListener() {

                @Override
                public void textChange(FieldEvents.TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId); 
                    String policyStr = item.getItemProperty("policy").toString();
                    String holidayStr = item.getItemProperty("holidays").toString();
                    double lateDeduction;
                    
                    boolean checkIfInputIsInteger = util.checkInputIfInteger(event.getText().trim());
                    if(!checkIfInputIsInteger){
                        getWindow().showNotification("Enter numeric format for lates!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    if(!event.getText().isEmpty()){
                        lateDeduction = tcal.processEmployeesLates(policyStr, holidayStr, Integer.parseInt(event.getText().trim()), employmentWage);
//			System.out.println("wage: " + employmentWage);
//			System.out.println("policy" + policyStr);
//			System.out.println("late deduction: " + lateDeduction);
                        item.getItemProperty("l/min").setValue(df.format(lateDeduction + (lateDeduction*premiumRate)));
                    }else{
                        item.getItemProperty("l/min").setValue(0.0);
                    }
                    
                }
            });
            
            undertime.addListener(new FieldEvents.TextChangeListener() {

                @Override
                public void textChange(FieldEvents.TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId); 
                    String policyStr = item.getItemProperty("policy").toString();
                    String holidayStr = item.getItemProperty("holidays").toString();
                    double undertimeDeduction;
                    
                    boolean checkIfInputIsInteger = util.checkInputIfInteger(event.getText().trim());
                    if(!checkIfInputIsInteger){
                        getWindow().showNotification("Enter numeric format for undertime!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    if(!event.getText().isEmpty()){
                        undertimeDeduction = tcal.processEmployeesUndertime(policyStr, holidayStr, Integer.parseInt(event.getText().trim()), employmentWage);
                        item.getItemProperty("u/min").setValue(df.format(undertimeDeduction + (undertimeDeduction*premiumRate)));
                    }else{
                        item.getItemProperty("u/min").setValue(0.0);
                    }
                }
            });
            
            overtime.addListener(new FieldEvents.TextChangeListener() {

                @Override
                public void textChange(FieldEvents.TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId); 
                    String policyStr = item.getItemProperty("policy").toString();
                    String holidayStr = item.getItemProperty("holidays").toString();
                    double overtimeAddition;
                    
                    boolean checkIfInputIsInteger = util.checkInputIfInteger(event.getText().trim());
                    if(!checkIfInputIsInteger){
                        getWindow().showNotification("Enter numeric format for undertime!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    if(!event.getText().isEmpty()){
                        overtimeAddition = tcal.processEmployeesOvertime(policyStr, holidayStr, Integer.parseInt(event.getText().trim()), employmentWage);
                        item.getItemProperty("o/min").setValue(df.format(overtimeAddition + (overtimeAddition*premiumRate)));
                    }else{
                        item.getItemProperty("o/min").setValue(0.0);
                    }
                }
            });
            
            nightDifferential.addListener(new FieldEvents.TextChangeListener() {

                @Override
                public void textChange(FieldEvents.TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId); 
                    String policyStr = item.getItemProperty("policy").toString();
                    String holidayStr = item.getItemProperty("holidays").toString();
                    double nightDifferentialAddition;
                    
                    boolean checkIfInputIsInteger = util.checkInputIfInteger(event.getText().trim());
                    if(!checkIfInputIsInteger){
                        getWindow().showNotification("Enter numeric format for undertime!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    if(!event.getText().isEmpty()){
                        nightDifferentialAddition = tcal.processEmployeesNightDifferential(policyStr, holidayStr, Integer.parseInt(event.getText().trim()), employmentWage);
                        item.getItemProperty("nd/min").setValue(df.format(nightDifferentialAddition + (nightDifferentialAddition*premiumRate)));
                    }else{
                        item.getItemProperty("nd/min").setValue(0.0);
                    }
                }
            });
            
            List<Timekeeping> timekeepingListByRowData = timekeepingService.getTimekeepingRowData(
                util.convertDateFormat(dateList.get(i).toString()), 
                payrollId);
            for(Timekeeping t : timekeepingListByRowData){
                String policy;
                String holiday;
                if(!t.getPolicy().equals("null")){
                    policy = t.getPolicy();
                } else {
                    policy = "";
                }
                    
                if(!t.getHoliday().equals("null")){
                    holiday = t.getHoliday();
                } else {
                    holiday = "";
                }
                    
                premium.setValue(t.isPremium());
                    
                lates.setValue(t.getLates());
                undertime.setValue(t.getUndertime());
                overtime.setValue(t.getOvertime());
                nightDifferential.setValue(t.getNightDifferential());
                    
                table.addItem(new Object[]{
                    edit, 
                    util.convertDateFormat(dateList.get(i).toString()), 
                    policy, holiday, 
                    premium, lates, 
                    undertime, overtime, 
                    nightDifferential, 
                    t.getLateDeduction(), 
                    t.getUndertimeDeduction(), 
                    t.getOvertimePaid(), 
                    t.getNightDifferentialPaid(), 
                    t.getLegalHolidayPaid(), 
                    t.getSpecialHolidayPaid(), 
                    t.getWorkingDayOffPaid(), 
                    t.getNonWorkingHolidayPaid()
                }, new Integer(i));
            }                       
        }                
        table.setPageLength(table.size());        
        
        for(Object listener : table.getListeners(ItemClickEvent.class)){
            table.removeListener(ItemClickEvent.class, listener);
        }
        
        table.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                final Object itemId = event.getItemId();
                final Item item = table.getItem(itemId);
                
                Boolean editRow = Boolean.valueOf(item.getItemProperty("edit").getValue().toString());
                
                if(event.getPropertyId().equals("policy")){
                    if(editRow){
                        final Window newWindow = policyWindow(holidayList, item);
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
        
        
        for(Object listener : button.getListeners(Button.ClickListener.class)){
            button.removeListener(Button.ClickListener.class, listener);
        }
        
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {                
                try{                                       
                    Collection attendanceTableCollection = table.getContainerDataSource().getItemIds();
                    List<Timekeeping> attendanceList = new ArrayList<Timekeeping>();
                    for(int i = 0; i < attendanceTableCollection.size(); i++){
                        String str = table.getItem(i).toString();
                        String[] attStr = str.split(" ");
                        List<String> tkeepList = new ArrayList<String>(Arrays.asList(attStr));
			
                        Timekeeping t = new Timekeeping();
                        t.setAttendanceDate(util.parsingDate(tkeepList.get(1)));
                        t.setPolicy(tkeepList.get(2));
                        t.setHoliday(tkeepList.get(3));
                        t.setPremium(util.convertStringToBoolean(tkeepList.get(4)));
                        t.setLates(util.convertStringToDouble(tkeepList.get(5)));
                        t.setUndertime(util.convertStringToDouble(tkeepList.get(6)));
                        t.setOvertime(util.convertStringToDouble(tkeepList.get(7)));
                        t.setNightDifferential(util.convertStringToDouble(tkeepList.get(8)));
                        t.setLateDeduction(util.convertStringToDouble(tkeepList.get(9)));
                        t.setUndertimeDeduction(util.convertStringToDouble(tkeepList.get(10)));
                        t.setOvertimePaid(util.convertStringToDouble(tkeepList.get(11)));
                        t.setNightDifferentialPaid(util.convertStringToDouble(tkeepList.get(12)));
                        t.setLegalHolidayPaid(util.convertStringToDouble(tkeepList.get(13)));
                        t.setSpecialHolidayPaid(util.convertStringToDouble(tkeepList.get(14)));
                        t.setWorkingDayOffPaid(util.convertStringToDouble(tkeepList.get(15)));
                        t.setNonWorkingHolidayPaid(util.convertStringToDouble(tkeepList.get(16)));
                        attendanceList.add(t);
                    }
                    ProcessPayrollComputation processPayroll = new ProcessPayrollComputation(employeeId, branchId);
                    processPayroll.initVariables();
                    processPayroll.initVariablesForComputation(attendanceList);
                    boolean result = processPayroll.processPayrollComputation(payrollDate, 
                            payrollPeriod, 
                            attendancePeriodFrom, 
                            attendancePeriodTo, 
                            EDIT_PAYROLL, 
                            payrollId);
                    PayrollMainUI payrollMainUI = new PayrollMainUI(branchId);
                    if(result){
                        (subWindow.getParent()).removeWindow(subWindow);  
                        timekeepingTable(getEmployeeId());
                        payrollMainUI.payrollTable(branchId, employeeId);
                    } else {
                        getWindow().showNotification("SQL ERROR");
                    }
                }catch(Exception e){
                    e.getMessage();
                }
            }
            
        });
               
        subWindow.addComponent(button);
        
        return subWindow;
    }
    
    public void timekeepingTable(final String employeeId){     
        timekeepingTbl.removeAllItems();
        List<Timekeeping> attendanceList = timekeepingService.getAttendanceByEmployee(employeeId);
        int i = 0;
        for(Timekeeping t : attendanceList){
            if(t.getId() != 0){
                timekeepingTbl.addItem(new Object[]{
                    t.getId(), 
                    util.convertDateFormat(t.getAttendancePeriodFrom().toString()), 
                    util.convertDateFormat(t.getAttendancePeriodTo().toString()), 
                    t.getLates(), t.getUndertime(), t.getOvertime(), t.getNightDifferential(), 
                    t.getLateDeduction(), t.getUndertimeDeduction(), t.getOvertimePaid(), 
                    t.getNightDifferentialPaid(), t.getLegalHolidayPaid(), t.getSpecialHolidayPaid(), 
                    t.getWorkingDayOffPaid(), t.getNonWorkingHolidayPaid()
                }, new Integer(i));
            }
            i++;
        }
        timekeepingTbl.setPageLength(25);
        
        for(Object listener : timekeepingTbl.getListeners(ItemClickEvent.class)){
            timekeepingTbl.removeListener(ItemClickEvent.class, listener);
        }
        
        timekeepingTbl.addListener(new ItemClickEvent.ItemClickListener() {

            @Override
            public void itemClick(ItemClickEvent event) {
                if(employeesName.getValue() == null || employeesName.getValue().toString().trim().isEmpty()){
                    getWindow().showNotification("Select an Employee!", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
                Object itemId = event.getItemId();
                Item item = timekeepingTbl.getItem(itemId);
                
                int payrollId = util.convertStringToInteger(item.getItemProperty("id").toString());
                
                if(event.getPropertyId().equals("id")){
                    List dateList = obtainPayrollDateList(item.getItemProperty("from").toString(), item.getItemProperty("to").toString());
                    String payrollPeriod = payrollService.getPayrollPeriodByPayrollId(payrollId);
                    String payrollDate = payrollService.getPayrollDateByPayrollId(payrollId);
                    Window subWindow = editAttendanceTableContainer(employeesName.getValue().toString().toUpperCase(), 
                        dateList, 
                        employeeId, 
                        payrollPeriod, 
                        payrollDate, 
                        item.getItemProperty("from").toString(), 
                        item.getItemProperty("to").toString(), 
                        payrollId);
                    
                        subWindow.setWidth("65%");
                        subWindow.setModal(true);
                        if(subWindow.getParent() == null){
                            getWindow().addWindow(subWindow);
                        }
                        subWindow.center();                        
                }
            }
        });        
    }
    
    public void employeeComboBox(final int branchId){     
        employeesName.removeAllItems();
        employeesName.setWidth("100%");
        employeesName.setCaption("Employees: ");
        employeesName.setNullSelectionAllowed(false);
        List<Employee> employeesList = employeeService.getEmployeePerBranchForDropDownList(branchId);        
        for(Employee e : employeesList){
            String name = e.getLastname()+ ", " + e.getFirstname() + " " + e.getMiddlename();
            employeesName.addItem(name.toUpperCase());
        }
        employeesName.addListener(employeesNameChangeListener);
        employeesName.setImmediate(true);
    }
    
    private List obtainPayrollDateList(String from, String to) {
        Date datePeriodFrom = util.parsingDate(from);
        Date datePeriodTo = util.parsingDate(to);
        
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
    
    private Window policyWindow(final String[] holidayList, final Item item){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window subWindow = new Window("CHANGE POLICY", vlayout);
        subWindow.setWidth("225px");
                
        final ComboBox policy = dropDown.populateAttendancePolicyDropDownList(new ComboBox());
        policy.setWidth("100%");
        policy.setNullSelectionAllowed(true);              
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
            public void valueChange(Property.ValueChangeEvent event) {
                if(event.getProperty().getValue() == null){  
                    holidayType.setVisible(false);
                    holidayType.removeAllItems();
                    for(String temp : holidayList){
                        holidayType.addItem(temp);
                    }
                } else if(event.getProperty().getValue().toString().equals("holiday") || 
                        event.getProperty().getValue().toString().equals("working-holiday")){
                    holidayType.setVisible(true);
                } else if(event.getProperty().getValue().toString().equals("working-day-off")){
                    double additionalWorkingDayOffPay = tcal.processAdditionalWorkingDayOff(getEmploymentWage(), getEmploymentWageEntry());
//                    System.out.println("wdo pay: " + additionalWorkingDayOffPay);
                    item.getItemProperty("wdo").setValue(df.format(additionalWorkingDayOffPay));
                } else{
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
            public void valueChange(Property.ValueChangeEvent event) {
                double additionalHolidayPay;
                if(policy.getValue() == null){                    
                } else if(policy.getValue().equals("working-holiday")){
                    if(event.getProperty().getValue().toString().equals("legal-holiday")){
                        additionalHolidayPay = tcal.processAdditionalHolidayPay(event.getProperty().getValue().toString(), getEmploymentWage());
                        item.getItemProperty("lholiday").setValue(new Double(df.format(additionalHolidayPay)));
                        item.getItemProperty("sholiday").setValue(0.0);
                    }else{
                        additionalHolidayPay = tcal.processAdditionalHolidayPay(event.getProperty().getValue().toString(), getEmploymentWage());
                        item.getItemProperty("sholiday").setValue(new Double(df.format(additionalHolidayPay)));
                        item.getItemProperty("lholiday").setValue(0.0);
                    }
                }else if(policy.getValue().equals("holiday")){
                    if(event.getProperty().getValue().toString().equals("legal-holiday")){
                        additionalHolidayPay = tcal.processAdditionalHolidayPay(event.getProperty().getValue().toString(), getEmploymentWage());
                        item.getItemProperty("psday").setValue(new Double(df.format(additionalHolidayPay)));                            
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
            public void buttonClick(Button.ClickEvent event) {
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

    void setEmployeeId(String employeeId){
        this.employeeId = employeeId;
    }
    
    public void setBranchId(int branchId){
        this.branchId = branchId;
    }
    
    String getEmployeeId(){
        return employeeId;
    }
    
    int getBranchId(){
        return branchId;
    }
    
    String getEmploymentWageEntry(){
        return employmentWageEntry;
    }
    
    double getEmploymentWage(){
        return employmentWage;
    }
    
    ValueChangeListener employeesNameChangeListener = new Property.ValueChangeListener() {

        @Override
        public void valueChange(Property.ValueChangeEvent event) {
            if(event.getProperty().getValue() == null){                    
            } else {
                employeeId = employeeService.getEmployeeId(employeesName.getValue().toString());
                setEmployeeId(employeeId);
                timekeepingTable(employeeId);
                employmentWage = employeeService.getEmploymentWage(employeeId);
                employmentWageStatus = employeeService.getEmploymentWageStatus(employeeId);
                employmentWageEntry = employeeService.getEmploymentWageEntry(employeeId);
            }
        }
    };
}
