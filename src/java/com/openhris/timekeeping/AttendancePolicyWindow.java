/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.timekeeping;

import com.openhris.commons.DropDownComponent;
import com.openhris.commons.OpenHrisUtilities;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.text.DecimalFormat;

/**
 *
 * @author jetdario
 */
public class AttendancePolicyWindow extends Window {

    OpenHrisUtilities utilities = new OpenHrisUtilities();
    TimekeepingComputation computation = new TimekeepingComputation();
    DropDownComponent dropDown = new DropDownComponent();
    
    String[] holidayList;
    Item item;
    String employmentWageEntry;
    double employmentWage;
    
    DecimalFormat df = new DecimalFormat("0.00");
    
    public AttendancePolicyWindow(String[] holidayList, 
            Item item, 
            String employmentWageEntry, 
            double employmentWage) {
        this.holidayList = holidayList;
        this.item = item; 
        this.employmentWageEntry = employmentWageEntry; 
        this.employmentWage = employmentWage;
        
        setCaption("CHANGE POLICY");
        setWidth("225px");
        setModal(true);
        center();        
        
        addComponent(getVlayout());
    }

    VerticalLayout getVlayout(){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
                
        final ComboBox policy = dropDown.populateAttendancePolicyDropDownList(new ComboBox());
        policy.setWidth("100%");
        policy.setNullSelectionAllowed(true);              
        policy.setImmediate(true);
        vlayout.addComponent(policy);
        
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
                    item.getItemProperty("wdo").setValue(0.0);
                    item.getItemProperty("sholiday").setValue(0.0);
                    item.getItemProperty("lholiday").setValue(0.0);
                    item.getItemProperty("psday").setValue(0.0);
                    holidayType.setVisible(false);
                    holidayType.removeAllItems();
                    for(String temp : holidayList){
                        holidayType.addItem(temp);
                    }
                } else if(event.getProperty().getValue().toString().equals("holiday") || 
                    event.getProperty().getValue().toString().equals("working-holiday")){
                    item.getItemProperty("wdo").setValue(0.0);
                    item.getItemProperty("sholiday").setValue(0.0);
                    item.getItemProperty("lholiday").setValue(0.0);
                    item.getItemProperty("psday").setValue(0.0);
                    holidayType.setVisible(true);
                } else if(event.getProperty().getValue().toString().equals("working-day-off")){
                    item.getItemProperty("wdo").setValue(0.0);
                    item.getItemProperty("sholiday").setValue(0.0);
                    item.getItemProperty("lholiday").setValue(0.0);
                    item.getItemProperty("psday").setValue(0.0);
                    holidayType.setVisible(true);
                    
                    double additionalWorkingDayOffPay = computation.processAdditionalWorkingDayOff(getEmploymentWage(), getEmploymentWageEntry());
                    item.getItemProperty("wdo").setValue(df.format(additionalWorkingDayOffPay));
                } else{
                    item.getItemProperty("wdo").setValue(0.0);
                    item.getItemProperty("sholiday").setValue(0.0);
                    item.getItemProperty("lholiday").setValue(0.0);
                    item.getItemProperty("psday").setValue(0.0);
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
                double additionalWorkingDayOffPay;
                double multiplePremiumPay;
                if(policy.getValue() == null){                    
                } else if(policy.getValue().equals("working-holiday")){
                    item.getItemProperty("psday").setValue(0.0);
                    if(event.getProperty().getValue().toString().equals("legal-holiday")){
                        additionalHolidayPay = computation.processAdditionalHolidayPay(event.getProperty().getValue().toString(), getEmploymentWage());
                        item.getItemProperty("lholiday").setValue(new Double(df.format(additionalHolidayPay)));
                        item.getItemProperty("sholiday").setValue(0.0);
                    }else{
                        additionalHolidayPay = computation.processAdditionalHolidayPay(event.getProperty().getValue().toString(), getEmploymentWage());
                        item.getItemProperty("sholiday").setValue(new Double(df.format(additionalHolidayPay)));
                        item.getItemProperty("lholiday").setValue(0.0);
                    }
                }else if(policy.getValue().equals("holiday")){
                    item.getItemProperty("lholiday").setValue(0.0);
                    item.getItemProperty("sholiday").setValue(0.0);
                    if(event.getProperty().getValue().toString().equals("legal-holiday")){
                        if(getEmploymentWageEntry().equals("daily")){
                            additionalHolidayPay = computation.processAdditionalHolidayPay(event.getProperty().getValue().toString(), getEmploymentWage());
                            item.getItemProperty("psday").setValue(new Double(df.format(additionalHolidayPay))); 
                        } else {
                            item.getItemProperty("psday").setValue(0.0);
                        }                            
                    }else{
                        item.getItemProperty("psday").setValue(0.0);
                    }
                } else if(policy.getValue().equals("working-day-off")) {
                    if(event.getProperty().getValue() == null){
                        item.getItemProperty("sholiday").setValue(0.0);
                        item.getItemProperty("lholiday").setValue(0.0);
                        item.getItemProperty("psday").setValue(0.0);                        
                    } else if(event.getProperty().getValue().equals("legal-holiday")){
                        additionalWorkingDayOffPay = computation.processAdditionalWorkingDayOff(getEmploymentWage(), getEmploymentWageEntry());
                        item.getItemProperty("wdo").setValue(df.format(additionalWorkingDayOffPay));
                        multiplePremiumPay = computation.processMultiplePremiumPay(event.getProperty().getValue().toString(), getEmploymentWage());
                        item.getItemProperty("lholiday").setValue(multiplePremiumPay);
                        item.getItemProperty("sholiday").setValue(0.0);
                    } else {
                        additionalWorkingDayOffPay = computation.processAdditionalWorkingDayOff(getEmploymentWage(), getEmploymentWageEntry());
                        item.getItemProperty("wdo").setValue(df.format(additionalWorkingDayOffPay));
                        multiplePremiumPay = computation.processMultiplePremiumPay(event.getProperty().getValue().toString(), getEmploymentWage());
                        item.getItemProperty("sholiday").setValue(multiplePremiumPay);
                        item.getItemProperty("lholiday").setValue(0.0);
                    }
                }
            }
            
        });
        vlayout.addComponent(holidayType);
        
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
                
                close();
            }
            
        });
        vlayout.addComponent(button);
        
        return vlayout;
    }
    
    public String[] getHolidayList() {
        return holidayList;
    }

    public Item getItem() {
        return item;
    }

    public String getEmploymentWageEntry() {
        return employmentWageEntry;
    }

    public double getEmploymentWage() {
        return employmentWage;
    }
        
}
