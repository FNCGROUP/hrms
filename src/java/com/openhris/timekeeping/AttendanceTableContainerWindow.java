/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.timekeeping;

import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.payroll.ProcessPayrollComputation;
import com.openhris.model.Timekeeping;
import com.openhris.service.CompanyService;
import com.openhris.serviceprovider.CompanyServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class AttendanceTableContainerWindow extends Window {

    private String name;
    private List dateList;
    private String employeeId;
    private String payrollPeriod;
    private String payrollDate;
    private String attendancePeriodFrom;
    private String attendancePeriodTo;
    private String employmentWageEntry;
    private double employmentWage;
    private int branchId;
    private String branch;
    
    OpenHrisUtilities utilities = new OpenHrisUtilities();
    TimekeepingComputation computation = new TimekeepingComputation();
    DropDownComponent dropDown = new DropDownComponent();
    CompanyService cs = new CompanyServiceImpl();
//    DecimalFormat df = new DecimalFormat("0.00");
    
    double premiumRate;
    
    public AttendanceTableContainerWindow(String name, 
            List dateList, 
            String employeeId, 
            String payrollPeriod, 
            String payrollDate, 
            String attendancePeriodFrom, 
            String attendancePeriodTo, 
            String employmentWageEntry, 
            double employmentWage, 
            int branchId) {
        
        this.name = name;
        this.dateList = dateList;
        this.employeeId = employeeId;
        this.payrollPeriod = payrollPeriod;
        this.payrollDate = payrollDate;
        this.attendancePeriodFrom = attendancePeriodFrom;
        this.attendancePeriodTo = attendancePeriodTo;
        this.employmentWageEntry = employmentWageEntry;
        this.employmentWage = employmentWage;
        this.branchId = branchId;
        
        setCaption("ATTENDANCE TABLE for " +name);
        setSizeFull();
        setModal(true);
        center();
        
        branch = cs.getBranchById(getBranchId());
        
        addComponent(generateAttendanceTable());
    }
    
    VerticalLayout generateAttendanceTable(){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSizeFull();
        vlayout.setSpacing(true);
        
        final Table table = new Table();        
        table.removeAllItems();
        table.setEnabled(true);
        table.setSizeFull();
        table.setImmediate(true);
        table.setColumnCollapsingAllowed(true);
                
        table.addContainerProperty("date", String.class, null);
        table.addContainerProperty("policy", ComboBox.class, null);
        table.addContainerProperty("holidays", ComboBox.class, null); 
        table.addContainerProperty("premium", CheckBox.class, null); 
        table.addContainerProperty("lates", TextField.class, null);  
        table.addContainerProperty("undertime", TextField.class, null); 
        table.addContainerProperty("overtime", TextField.class, null);        
        table.addContainerProperty("night differential", TextField.class, null);  
        table.addContainerProperty("duty manager", TextField.class, null);
        table.addContainerProperty("l/min", Double.class, null); 
        table.addContainerProperty("u/min", Double.class, null); 
        table.addContainerProperty("o/min", Double.class, null);
        table.addContainerProperty("nd/min", Double.class, null);
        table.addContainerProperty("dm/min", Double.class, null);
        table.addContainerProperty("lholiday", Double.class, null);
        table.addContainerProperty("sholiday", Double.class, null);
        table.addContainerProperty("wdo", Double.class, null);
        table.addContainerProperty("psday", Double.class, null); //paid non-working holiday
        table.addContainerProperty("latesLH", Double.class, null);
        table.addContainerProperty("latesSH", Double.class, null);
        table.addContainerProperty("latesWO", Double.class, null);
        table.addContainerProperty("undertimeLH", Double.class, null);
        table.addContainerProperty("undertimeSH", Double.class, null);
        table.addContainerProperty("undertimeWO", Double.class, null);
        
        table.setColumnAlignment("date", Table.ALIGN_CENTER);
        table.setColumnAlignment("policy", Table.ALIGN_CENTER);
        table.setColumnAlignment("premium", Table.ALIGN_CENTER);
        table.setColumnAlignment("lates", Table.ALIGN_CENTER);
        table.setColumnAlignment("undertime", Table.ALIGN_CENTER);
        table.setColumnAlignment("overtime", Table.ALIGN_CENTER);
        table.setColumnAlignment("night differential", Table.ALIGN_CENTER);
        table.setColumnAlignment("duty manager", Table.ALIGN_CENTER);
        table.setColumnAlignment("l/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("u/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("o/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("nd/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("dm/min", Table.ALIGN_RIGHT);
        table.setColumnAlignment("lholiday", Table.ALIGN_RIGHT);
        table.setColumnAlignment("sholiday", Table.ALIGN_RIGHT);
        table.setColumnAlignment("wdo", Table.ALIGN_RIGHT);
        table.setColumnAlignment("psday", Table.ALIGN_RIGHT);
        table.setColumnAlignment("latesLH", Table.ALIGN_RIGHT);
        table.setColumnAlignment("latesSH", Table.ALIGN_RIGHT);
        table.setColumnAlignment("latesWO", Table.ALIGN_RIGHT);
        table.setColumnAlignment("undertimeLH", Table.ALIGN_RIGHT);
        table.setColumnAlignment("undertimeSH", Table.ALIGN_RIGHT);
        table.setColumnAlignment("undertimeWO", Table.ALIGN_RIGHT);
        
        table.setColumnWidth("date", 70);
        table.setColumnWidth("policy", 125);
        table.setColumnWidth("holidays", 125);
        table.setColumnWidth("premium", 60);
        table.setColumnWidth("lates", 50);
        table.setColumnWidth("undertime", 60);
        table.setColumnWidth("overtime", 50);
        table.setColumnWidth("night differential", 110);
        table.setColumnWidth("duty manager", 80);
        table.setColumnWidth("l/min", 40);
        table.setColumnWidth("u/min", 40);
        table.setColumnWidth("o/min", 40);
        table.setColumnWidth("nd/min", 50);
        
        table.setColumnCollapsed("latesLH", true);
        table.setColumnCollapsed("latesSH", true);
        table.setColumnCollapsed("latesWO", true);
        table.setColumnCollapsed("undertimeLH", true);
        table.setColumnCollapsed("undertimeSH", true);
        table.setColumnCollapsed("undertimeWO", true);
                                
        final String[] holidayList = {"legal-holiday", "special-holiday"};        
        if(getEmploymentWageEntry().equals("monthly")){
            employmentWage = utilities.roundOffToTwoDecimalPlaces((employmentWage * 12) / 314);
        }
        
        for(int i = 0; i < dateList.size(); i++){ 
            Object itemId = new Integer(i);
            final ComboBox holidays = dropDown.populateAttendanceHolidayDropDownList(new ComboBox());
            holidays.setEnabled(false);
            holidays.setWidth("120px");
            holidays.setNullSelectionAllowed(false);
            holidays.setData(itemId);
            
            final CheckBox premium = new CheckBox();
            premium.setData(itemId);
            premium.setImmediate(true);
            
            final TextField lates = new TextField();
            lates.setWidth("100%");
            lates.setValue("0");
            lates.addStyleName("numerical");
            lates.setEnabled(true);
            lates.setData(itemId);
            lates.setImmediate(true);
                        
            final TextField undertime = new TextField();
            undertime.setWidth("100%");
            undertime.setValue("0");
            undertime.addStyleName("numerical");
            undertime.setEnabled(true);
            undertime.setData(itemId);
            undertime.setImmediate(true);
                        
            final TextField overtime = new TextField();
            overtime.setWidth("100%");
            overtime.setValue("0");
            overtime.addStyleName("numerical");
            overtime.setEnabled(true);
            overtime.setData(itemId);
            overtime.setImmediate(true);
            
            final TextField nightDifferential = new TextField();
            nightDifferential.setWidth("70%");
            nightDifferential.setValue("0");
            nightDifferential.addStyleName("numerical");
            nightDifferential.setEnabled(true);
            nightDifferential.setData(itemId);
            nightDifferential.setImmediate(true);
            
            final TextField dutyManager = new TextField();
            dutyManager.setWidth("80%");
            dutyManager.setValue("0");
            dutyManager.addStyleName("numerical");
            dutyManager.setEnabled(true);
            dutyManager.setData(itemId);
            dutyManager.setImmediate(true);
            
            final ComboBox policy = dropDown.populateAttendancePolicyDropDownList(new ComboBox());
            policy.setWidth("120px");
            policy.setNullSelectionAllowed(true);
            policy.setData(itemId);              
            policy.addListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    Object itemId = policy.getData();
                    Item item = table.getItem(itemId);
                    double additionalWorkingDayOffPay = 0;
                    
                    holidays.removeAllItems();
                    for(String temp : holidayList){
                        holidays.addItem(temp);
                    }
                                        
                    premium.setValue(false);
                    lates.setValue("0");
                    undertime.setValue("0");
                    overtime.setValue("0");
                    nightDifferential.setValue("0");
                    item.getItemProperty("l/min").setValue(0.0);
                    item.getItemProperty("u/min").setValue(0.0);
                    item.getItemProperty("o/min").setValue(0.0);
                    item.getItemProperty("nd/min").setValue(0.0);
                    item.getItemProperty("dm/min").setValue(0.0);
                    item.getItemProperty("sholiday").setValue(0.0);
                    item.getItemProperty("lholiday").setValue(0.0);
                    item.getItemProperty("wdo").setValue(0.0);
                    item.getItemProperty("psday").setValue(0.0);
                    item.getItemProperty("psday").setValue(0.0);
                    item.getItemProperty("latesLH").setValue(0.0);
                    item.getItemProperty("latesSH").setValue(0.0);
                    item.getItemProperty("latesWO").setValue(0.0);
                    item.getItemProperty("undertimeLH").setValue(0.0);
                    item.getItemProperty("undertimeSH").setValue(0.0);
                    item.getItemProperty("undertimeWO").setValue(0.0);
                    
                    
                    if(event.getProperty().getValue() == null){                        
                        holidays.setEnabled(false);
                        lates.setEnabled(true);
                        undertime.setEnabled(true);
                        overtime.setEnabled(true);
                        nightDifferential.setEnabled(true);
                        dutyManager.setEnabled(true);
                    } else if(event.getProperty().getValue().equals("holiday")){
                        holidays.setEnabled(true);   
                        lates.setEnabled(false);
                        undertime.setEnabled(false);
                        overtime.setEnabled(false);
                        nightDifferential.setEnabled(false);
                        dutyManager.setEnabled(false);
                    } else if(event.getProperty().getValue().equals("working-holiday")) { 
                        holidays.setEnabled(true);
                        lates.setEnabled(true);
                        undertime.setEnabled(true);
                        overtime.setEnabled(true);
                        nightDifferential.setEnabled(true);
                        dutyManager.setEnabled(true);
                    } else if(event.getProperty().getValue().equals("working-day-off")){
                        holidays.setEnabled(true);
                        lates.setEnabled(true);
                        undertime.setEnabled(true);
                        overtime.setEnabled(true);
                        nightDifferential.setEnabled(true);
                        dutyManager.setEnabled(true);
                        
                        additionalWorkingDayOffPay = computation.processAdditionalWorkingDayOff(getEmploymentWage(), getEmploymentWageEntry());
                        if(getBranch().equals("on-call and trainees")){
                            item.getItemProperty("wdo").setValue(utilities.roundOffToTwoDecimalPlaces(getEmploymentWage()));
                        } else {
                            item.getItemProperty("wdo").setValue(utilities.roundOffToTwoDecimalPlaces(additionalWorkingDayOffPay));
                        }                        
                    } else {
                        holidays.setEnabled(false);
                        lates.setEnabled(false);
                        undertime.setEnabled(false);
                        overtime.setEnabled(false);
                        nightDifferential.setEnabled(false);
                        dutyManager.setEnabled(false);
                    }                    
                }
            });    
            policy.setImmediate(true);
            
            holidays.addListener(new ComboBox.ValueChangeListener() {

                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    Object itemId = holidays.getData();
                    Item item = table.getItem(itemId);
                    
                    String policyStr = item.getItemProperty("policy").toString();                    
                    double additionalHolidayPay = 0;
                    double multiplePremiumPay = 0;
                    double additionalWorkingDayOffPay = 0;
                    
                    premium.setValue(false);
                    lates.setValue("0");
                    undertime.setValue("0");
                    overtime.setValue("0");
                    nightDifferential.setValue("0");
                    item.getItemProperty("sholiday").setValue(0.0);
                    item.getItemProperty("lholiday").setValue(0.0);
                    item.getItemProperty("latesLH").setValue(0.0);
                    item.getItemProperty("latesSH").setValue(0.0);
                    item.getItemProperty("latesWO").setValue(0.0);
                    item.getItemProperty("undertimeLH").setValue(0.0);
                    item.getItemProperty("undertimeSH").setValue(0.0);
                    item.getItemProperty("undertimeWO").setValue(0.0);
                    
                    if(policyStr.equals("holiday") || policy.getValue().toString().equals("working-holiday")){
                        if(event.getProperty().getValue() == null || event.getProperty().getValue().toString().isEmpty()){
                            getWindow().showNotification("Select Type of Holiday!", Window.Notification.TYPE_WARNING_MESSAGE);
                            return;
                        }
                    }
                    
                    if(policyStr.equals("working-holiday")){
                        if(event.getProperty().getValue().equals("legal-holiday")){
                            additionalHolidayPay = computation.processAdditionalHolidayPay(event.getProperty().getValue().toString(), getEmploymentWage());
                            item.getItemProperty("lholiday").setValue(utilities.roundOffToTwoDecimalPlaces(additionalHolidayPay));
                            item.getItemProperty("sholiday").setValue(0.0);
                        } else {
                            additionalHolidayPay = computation.processAdditionalHolidayPay(event.getProperty().getValue().toString(), getEmploymentWage());
                            item.getItemProperty("sholiday").setValue(utilities.roundOffToTwoDecimalPlaces(additionalHolidayPay));
                            item.getItemProperty("lholiday").setValue(0.0);
                        }                        
                    } else if(policyStr.equals("holiday")) {
                        if(event.getProperty().getValue().equals("legal-holiday")){
                            if(getEmploymentWageEntry().equals("daily")){
                                additionalHolidayPay = computation.processAdditionalHolidayPay(event.getProperty().getValue().toString(), getEmploymentWage());
                                item.getItemProperty("psday").setValue(utilities.roundOffToTwoDecimalPlaces(additionalHolidayPay)); 
                            } else {
                                item.getItemProperty("psday").setValue(0.0);
                            }
                        } else {
                            item.getItemProperty("psday").setValue(0.0);
                        }
                        
                    } else if(policyStr.equals("working-day-off")) {   
                        if(event.getProperty().getValue() == null){
                            item.getItemProperty("psday").setValue(0.0);
                        } else if(event.getProperty().getValue().equals("legal-holiday")){
                            additionalWorkingDayOffPay = computation.processAdditionalWorkingDayOff(getEmploymentWage(), getEmploymentWageEntry());
                            item.getItemProperty("wdo").setValue(utilities.roundOffToTwoDecimalPlaces(additionalWorkingDayOffPay));
                            multiplePremiumPay = computation.processMultiplePremiumPay(event.getProperty().getValue().toString(), getEmploymentWage());
                            item.getItemProperty("lholiday").setValue(multiplePremiumPay); 
                            item.getItemProperty("sholiday").setValue(0.0);
                        } else {
                            additionalWorkingDayOffPay = computation.processAdditionalWorkingDayOff(getEmploymentWage(), getEmploymentWageEntry());
                            item.getItemProperty("wdo").setValue(utilities.roundOffToTwoDecimalPlaces(additionalWorkingDayOffPay));
                            multiplePremiumPay = computation.processMultiplePremiumPay(event.getProperty().getValue().toString(), getEmploymentWage());
                            item.getItemProperty("sholiday").setValue(multiplePremiumPay);   
                            item.getItemProperty("lholiday").setValue(0.0);
                        }                       
                    }                   
                }
            });
            holidays.setImmediate(true);
                    
            premium.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId);
                    
                    lates.setValue("0");
                    undertime.setValue("0");
                    overtime.setValue("0");
                    nightDifferential.setValue("0");   
                    dutyManager.setValue("0");
                    
                    item.getItemProperty("l/min").setValue(0.0);
                    item.getItemProperty("u/min").setValue(0.0);
                    item.getItemProperty("o/min").setValue(0.0);
                    item.getItemProperty("nd/min").setValue(0.0);
                    item.getItemProperty("dm/min").setValue(0.0);
                    
                    if(event.getButton().booleanValue() == true){
                        premiumRate = 0.1;                        
                    } else {
                        premiumRate = 0.0;
                    }
                    
                    item.getItemProperty("wdo").setValue(
                        utilities.roundOffToTwoDecimalPlaces(Double.parseDouble(item.getItemProperty("wdo").getValue().toString()) + 
                            (Double.parseDouble(item.getItemProperty("wdo").getValue().toString()) * premiumRate))
                    );
                    
                    item.getItemProperty("lholiday").setValue(
                        utilities.roundOffToTwoDecimalPlaces(Double.parseDouble(item.getItemProperty("lholiday").getValue().toString()) + 
                            (Double.parseDouble(item.getItemProperty("lholiday").getValue().toString()) * premiumRate))
                    );
                    
                    item.getItemProperty("sholiday").setValue(
                        utilities.roundOffToTwoDecimalPlaces(Double.parseDouble(item.getItemProperty("sholiday").getValue().toString()) + 
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
                    
                    boolean checkIfInputIsInteger = utilities.checkInputIfInteger(event.getText().trim());
                    if(!checkIfInputIsInteger){
                        getWindow().showNotification("Enter numeric format for lates!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    if(!event.getText().isEmpty()){
                        if(getBranch().equals("on-call and trainees")){
                            lateDeduction = computation.processEmployeesLatesForOnCall(policyStr, 
                                holidayStr, 
                                utilities.convertStringToInteger(event.getText().trim()), 
                                getEmploymentWage());
                            item.getItemProperty("l/min").setValue(utilities.roundOffToTwoDecimalPlaces(lateDeduction));
                        } else {
                            if(utilities.convertStringToInteger(event.getText().trim()) > 5){
                                lateDeduction = computation.processEmployeesLates(policyStr, 
                                    holidayStr, 
                                    utilities.convertStringToInteger(event.getText().trim()), 
                                    getEmploymentWage());
                                if(policyStr == null || policyStr.isEmpty()){
                                    item.getItemProperty("l/min").setValue(utilities.roundOffToTwoDecimalPlaces(lateDeduction));
                                }else if(policyStr.equals("working-holiday") && holidayStr.equals("legal-holiday")){
                                    item.getItemProperty("latesLH").setValue(utilities.roundOffToTwoDecimalPlaces(lateDeduction));
                                    item.getItemProperty("latesSH").setValue(0.0);
                                    item.getItemProperty("latesWO").setValue(0.0);
                                    item.getItemProperty("l/min").setValue(0.0);
                                } else if(policyStr.equals("working-holiday") && holidayStr.equals("special-holiday")){
                                    item.getItemProperty("latesLH").setValue(0.0);
                                    item.getItemProperty("latesSH").setValue(utilities.roundOffToTwoDecimalPlaces(lateDeduction * 0.3));
                                    item.getItemProperty("latesWO").setValue(0.0);
                                    if(getEmploymentWageEntry().equals("daily")){
                                        item.getItemProperty("l/min").setValue(lateDeduction);
                                    }else {
                                        item.getItemProperty("l/min").setValue(0.0);
                                    }                                    
                                } else if(policyStr.equals("working-day-off")){
                                    item.getItemProperty("latesLH").setValue(0.0);
                                    item.getItemProperty("latesSH").setValue(0.0);
                                    item.getItemProperty("latesWO").setValue(utilities.roundOffToTwoDecimalPlaces(lateDeduction));
                                    item.getItemProperty("l/min").setValue(0.0);
                                }                                 
                            } else {
                                item.getItemProperty("l/min").setValue(0.0);
                            }  
                        }
                                              
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
                    
                    boolean checkIfInputIsInteger = utilities.checkInputIfInteger(event.getText().trim());
                    if(!checkIfInputIsInteger){
                        getWindow().showNotification("Enter numeric format for undertime!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    if(!event.getText().isEmpty()){
                        if(getBranch().equals("on-call and trainees")){
                            undertimeDeduction = computation.processEmployeesUndertimeForOnCall(policyStr, 
                                    holidayStr, 
                                    utilities.convertStringToInteger(event.getText().trim()), 
                                    getEmploymentWage());
                            item.getItemProperty("u/min").setValue(utilities.roundOffToTwoDecimalPlaces(undertimeDeduction));
                        } else {
                            undertimeDeduction = computation.processEmployeesUndertime(policyStr, 
                                    holidayStr, 
                                    utilities.convertStringToInteger(event.getText().trim()), 
                                    getEmploymentWage());
                            if(policyStr == null || policyStr.isEmpty()){
                                item.getItemProperty("u/min").setValue(utilities.roundOffToTwoDecimalPlaces(undertimeDeduction));
                            }else if(policyStr.equals("working-holiday") && holidayStr.equals("legal-holiday")){
                                item.getItemProperty("undertimeLH").setValue(utilities.roundOffToTwoDecimalPlaces(undertimeDeduction));
                                item.getItemProperty("undertimeSH").setValue(0.0);
                                item.getItemProperty("undertimeWO").setValue(0.0);
                                item.getItemProperty("u/min").setValue(0.0);
                            } else if(policyStr.equals("working-holiday") && holidayStr.equals("special-holiday")){
                                item.getItemProperty("undertimeLH").setValue(0.0);
                                item.getItemProperty("undertimeSH").setValue(utilities.roundOffToTwoDecimalPlaces(undertimeDeduction *0.3));
                                item.getItemProperty("undertimeWO").setValue(0.0);
                                if(getEmploymentWageEntry().equals("daily")){
                                    item.getItemProperty("u/min").setValue(undertimeDeduction);
                                } else {
                                    item.getItemProperty("u/min").setValue(0.0);
                                }
                                
                            } else if(policyStr.equals("working-day-off")){
                                item.getItemProperty("undertimeLH").setValue(0.0);
                                item.getItemProperty("undertimeSH").setValue(0.0);
                                item.getItemProperty("undertimeWO").setValue(utilities.roundOffToTwoDecimalPlaces(undertimeDeduction));
                                item.getItemProperty("u/min").setValue(0.0);
                            }
                        }
                        
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
                    
                    boolean checkIfInputIsInteger = utilities.checkInputIfInteger(event.getText().trim());
                    if(!checkIfInputIsInteger){
                        getWindow().showNotification("Enter numeric format for undertime!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    if(!event.getText().isEmpty()){
                        if(getBranch().equals("on-call and trainees")){
                            overtimeAddition = computation.processEmployeesOvertimeForOnCall(policyStr, 
                                    holidayStr, 
                                    utilities.convertStringToInteger(event.getText().trim()), 
                                    getEmploymentWage());
                            item.getItemProperty("o/min").setValue(utilities.roundOffToTwoDecimalPlaces(overtimeAddition + (overtimeAddition*premiumRate)));
                        } else {
                            overtimeAddition = computation.processEmployeesOvertime(policyStr, 
                                    holidayStr, 
                                    utilities.convertStringToInteger(event.getText().trim()), 
                                    getEmploymentWage());
                            item.getItemProperty("o/min").setValue(utilities.roundOffToTwoDecimalPlaces(overtimeAddition + (overtimeAddition*premiumRate)));
                        }
                        
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
                    
                    boolean checkIfInputIsInteger = utilities.checkInputIfInteger(event.getText().trim());
                    if(!checkIfInputIsInteger){
                        getWindow().showNotification("Enter numeric format for undertime!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    if(!event.getText().isEmpty()){
                        if(getBranch().equals("on-call and trainees")){
                            nightDifferentialAddition = computation.processEmployeesNightDifferentialForOnCall(policyStr, 
                                    holidayStr, 
                                    utilities.convertStringToInteger(event.getText().trim()), 
                                    getEmploymentWage());
                            item.getItemProperty("nd/min").setValue(utilities.roundOffToTwoDecimalPlaces(nightDifferentialAddition + (nightDifferentialAddition*premiumRate)));
                        } else {
                            nightDifferentialAddition = computation.processEmployeesNightDifferential(policyStr, 
                                    holidayStr, 
                                    utilities.convertStringToInteger(event.getText().trim()), 
                                    getEmploymentWage());
                            item.getItemProperty("nd/min").setValue(utilities.roundOffToTwoDecimalPlaces(nightDifferentialAddition + (nightDifferentialAddition*premiumRate)));
                        }
                        
                    }else{
                        item.getItemProperty("nd/min").setValue(0.0);
                    }
                }
            });
            
            dutyManager.addListener(new FieldEvents.TextChangeListener() {

                @Override
                public void textChange(FieldEvents.TextChangeEvent event) {
                    Object itemId = lates.getData();
                    Item item = table.getItem(itemId); 
                    String policyStr = item.getItemProperty("policy").toString();
                    String holidayStr = item.getItemProperty("holidays").toString();
                    double dutyManagerAddition;
                    
                    boolean checkIfInputIsInteger = utilities.checkInputIfInteger(event.getText().trim());
                    if(!checkIfInputIsInteger){
                        getWindow().showNotification("Enter numeric format for Duty Manager!", Window.Notification.TYPE_WARNING_MESSAGE);
                        return;
                    }
                    
                    if(!event.getText().isEmpty()){
                        if(getBranch().equals("on-call and trainees")){
                            dutyManagerAddition = computation.processEmployeeDutyManagerForOnCall(policyStr, 
                                    holidayStr, 
                                    utilities.convertStringToInteger(event.getText().trim()), 
                                    getEmploymentWage());
                            item.getItemProperty("dm/min").setValue(utilities.roundOffToTwoDecimalPlaces(dutyManagerAddition));
                        } else {
                            dutyManagerAddition = computation.processEmployeeDutyManager(policyStr, 
                                    holidayStr, 
                                    utilities.convertStringToInteger(event.getText().trim()), 
                                    getEmploymentWage());
                            item.getItemProperty("dm/min").setValue(utilities.roundOffToTwoDecimalPlaces(dutyManagerAddition));
                        }
                        
                    }else{
                        item.getItemProperty("dm/min").setValue(0.0);
                    }
                }
            });
            
            table.addItem(new Object[]{
                utilities.convertDateFormat(dateList.get(i).toString()), 
                policy, holidays, 
                premium, lates, 
                undertime, overtime, 
                nightDifferential, 
                dutyManager,
                0.0, 0.0, 0.0, 
                0.0, 0.0, 0.0, 
                0.0, 0.0, 0.0, 
                0.0, 0.0, 0.0, //lates from legal & special holiday 
                0.0, 0.0, 0.0 //undertime from legal & special holiday
           }, i);                       
        }                
        table.setPageLength(table.size());        
        
        vlayout.addComponent(table);
        
        final Button button = new Button();
        button.setCaption("Save Attendance Data");
        vlayout.addComponent(button);
        
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
                        t.setAttendanceDate(utilities.parsingDate(tkeepList.get(0)));
                        t.setPolicy(tkeepList.get(1));
                        t.setHoliday(tkeepList.get(2));
                        t.setPremium(utilities.convertStringToBoolean(tkeepList.get(3)));
                        t.setLates(utilities.convertStringToDouble(tkeepList.get(4)));
                        t.setUndertime(utilities.convertStringToDouble(tkeepList.get(5)));
                        t.setOvertime(utilities.convertStringToDouble(tkeepList.get(6)));
                        t.setNightDifferential(utilities.convertStringToDouble(tkeepList.get(7)));
                        t.setDutyManager(utilities.convertStringToDouble(tkeepList.get(8)));
                        t.setLateDeduction(utilities.convertStringToDouble(tkeepList.get(9)));
                        t.setUndertimeDeduction(utilities.convertStringToDouble(tkeepList.get(10)));
                        t.setOvertimePaid(utilities.convertStringToDouble(tkeepList.get(11)));
                        t.setNightDifferentialPaid(utilities.convertStringToDouble(tkeepList.get(12)));
                        t.setDutyManagerPaid(utilities.convertStringToDouble(tkeepList.get(13)));
                        t.setLegalHolidayPaid(utilities.convertStringToDouble(tkeepList.get(14)));
                        t.setSpecialHolidayPaid(utilities.convertStringToDouble(tkeepList.get(15)));
                        t.setWorkingDayOffPaid(utilities.convertStringToDouble(tkeepList.get(16)));
                        t.setNonWorkingHolidayPaid(utilities.convertStringToDouble(tkeepList.get(17)));
                        t.setLatesLegalHolidayDeduction(utilities.convertStringToDouble(tkeepList.get(18)));
                        t.setLatesSpecialHolidayDeduction(utilities.convertStringToDouble(tkeepList.get(19)));
                        t.setLatesWorkingDayOffDeduction(utilities.convertStringToDouble(tkeepList.get(20)));
                        t.setUndertimeLegalHolidayDeduction(utilities.convertStringToDouble(tkeepList.get(21)));
                        t.setUndertimeSpecialHolidayDeduction(utilities.convertStringToDouble(tkeepList.get(22)));
                        t.setUndertimeWorkingDayOffDeduction(utilities.convertStringToDouble(tkeepList.get(23)));
                        attendanceList.add(t);
                    }
                    
                    ProcessPayrollComputation processPayroll = new ProcessPayrollComputation(employeeId, getBranchId());
                    processPayroll.initVariables();
                    processPayroll.initVariablesForComputation(attendanceList);
                    boolean result = processPayroll.processPayrollComputation(payrollDate, 
                            payrollPeriod, 
                            attendancePeriodFrom, 
                            attendancePeriodTo, 
                            0);
                    if(result){
                        close();
                    } else {
                        getWindow().showNotification("SQL ERROR");
                    }
                }catch(Exception e){
                    e.getMessage();
                }
            }
            
        });
        
        return vlayout;
    }
    
    String getEmployeesName(){
        return name;
    }
    
    String getEmployeeId(){
        return employeeId;
    }
    
    String getPayrollPeriod(){
        return payrollPeriod;
    }
    
    String getPayrollDate(){
        return payrollDate;
    }
    
    String getAttendancePeriodFrom(){
        return attendancePeriodFrom;
    }
    
    String getAttendancePeriodTo(){
        return attendancePeriodTo;
    }
    
    String getEmploymentWageEntry(){
        return employmentWageEntry;
    }
    
    double getEmploymentWage(){
        return employmentWage;
    }
    
    int getBranchId(){
        return branchId;
    }
    
    List getDateList(){
        return dateList;
    }
    
    String getBranch(){
        return branch;
    }
}
