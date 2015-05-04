/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.timekeeping;

import com.openhris.administrator.model.UserAccessControl;
import com.openhris.commons.DateSelector;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.Employee;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.openhris.serviceprovider.PayrollServiceImpl;
import com.openhris.service.EmployeeService;
import com.openhris.service.PayrollService;
import com.openhris.service.TimekeepingService;
import com.openhris.model.Timekeeping;
import com.openhris.serviceprovider.TimekeepingServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
        vsplit.setSplitPosition(135, Sizeable.UNITS_PIXELS);
        
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
                String payrollPeriod;
                
                if(employeesName.getValue() == null){
                    getWindow().showNotification("Select an Employee!");
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
                
                Calendar cal = Calendar.getInstance();
                cal.setTime(parsedPayrollDate);
                
                if(cal.get(Calendar.DAY_OF_MONTH) == 15){
                    payrollPeriod = "15th of the month";
                } else {
                    payrollPeriod = "30th of the month";
                }
                
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

                Window subWindow = new AttendanceTableContainerWindow(employeesName.getValue().toString().toUpperCase(), 
                        dateList, 
                        getEmployeeId(), 
                        payrollPeriod, 
                        util.convertDateFormat(payrollDate.getValue().toString()), 
                        util.convertDateFormat(attendancePeriodFrom.getValue().toString()), 
                        util.convertDateFormat(attendancePeriodTo.getValue().toString()), 
                        getEmploymentWageEntry(), 
                        getEmploymentWage(), 
                        getBranchId());
                if(subWindow.getParent() == null){
                    getWindow().addWindow(subWindow);
                }
                subWindow.addListener(subWindowCloseListener);
            }
        });
        comboBoxGrid.addComponent(openAttendanceTableWindowButton, 2, 1);
        comboBoxGrid.setComponentAlignment(openAttendanceTableWindowButton, Alignment.BOTTOM_CENTER);
                 
        vsplit.setSecondComponent(timekeepingTbl);        
    }
         
    public void timekeepingTable(final String employeeId){     
        timekeepingTbl.removeAllItems();
        List<Timekeeping> attendanceList = timekeepingService.getAttendanceByEmployee(employeeId);
        int i = 0;
        for(Timekeeping t : attendanceList){
//            if(t.getId() != 0){
            timekeepingTbl.addItem(new Object[]{
                t.getId(), 
                util.convertDateFormat(t.getAttendancePeriodFrom().toString()), 
                util.convertDateFormat(t.getAttendancePeriodTo().toString()), 
                t.getLates(), 
                t.getUndertime(), 
                t.getOvertime(), 
                t.getNightDifferential(), 
                t.getLateDeduction(), 
                t.getUndertimeDeduction(), 
                t.getOvertimePaid(), 
                t.getNightDifferentialPaid(), 
                t.getLegalHolidayPaid(), 
                t.getSpecialHolidayPaid(), 
                t.getWorkingDayOffPaid(), 
                t.getNonWorkingHolidayPaid()
            }, new Integer(i));
//            }
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
                    
                    Window subWindow = new EditAttendanceTableContainerWindow(employeesName.getValue().toString().toUpperCase(), 
                            dateList, 
                            getEmployeeId(), 
                            payrollPeriod, 
                            payrollDate, 
                            item.getItemProperty("from").toString(), 
                            item.getItemProperty("to").toString(), 
                            getEmploymentWageEntry(), 
                            getEmploymentWage(), 
                            getBranchId(),
                            payrollId);
                    
                        if(subWindow.getParent() == null){
                            getWindow().addWindow(subWindow);
                        }
                        subWindow.center(); 
                        subWindow.addListener(subWindowCloseListener);
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

    Window.CloseListener subWindowCloseListener = new Window.CloseListener() {

        @Override
        public void windowClose(Window.CloseEvent e) {
            employmentWage = employeeService.getEmploymentWage(getEmployeeId());
            timekeepingTable(getEmployeeId());
        }
    };
}
