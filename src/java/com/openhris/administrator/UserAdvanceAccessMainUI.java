/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.administrator;

import com.openhris.administrator.model.UserAdvanceAccess;
import com.openhris.administrator.serviceprovider.AdministratorServiceImpl;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.administrator.service.AdministratorService;
import com.vaadin.data.Item;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.List;

/**
 *
 * @author jet
 */
public class UserAdvanceAccessMainUI extends VerticalLayout {
    
    AdministratorService administratorService = new AdministratorServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    Table userAdvanceAccessTbl = new Table();
    
    public UserAdvanceAccessMainUI(){
        setSpacing(false);
        setMargin(false);
        setSizeFull();
        setImmediate(true);
        
        userAdvanceAccessTable();
        addComponent(userAdvanceAccessTbl);
    }
    
    public void userAdvanceAccessTable(){
        userAdvanceAccessTbl.removeAllItems();
        userAdvanceAccessTbl.setSizeFull();
        userAdvanceAccessTbl.setImmediate(true);
        userAdvanceAccessTbl.setSelectable(true);
        
        userAdvanceAccessTbl.addContainerProperty("id", String.class, null);
        userAdvanceAccessTbl.addContainerProperty("name", String.class, null);
        userAdvanceAccessTbl.addContainerProperty("username", String.class, null);
        userAdvanceAccessTbl.addContainerProperty("role", String.class, null);
        userAdvanceAccessTbl.addContainerProperty("timekeeping", CheckBox.class, null);
        userAdvanceAccessTbl.addContainerProperty("contributions", CheckBox.class, null);
        userAdvanceAccessTbl.addContainerProperty("cash bond", CheckBox.class, null);
        userAdvanceAccessTbl.addContainerProperty("advances", CheckBox.class, null);
        userAdvanceAccessTbl.addContainerProperty("adjustment", CheckBox.class, null);
        userAdvanceAccessTbl.addContainerProperty("payroll", CheckBox.class, null);
        userAdvanceAccessTbl.addContainerProperty("edit employee", CheckBox.class, null);
        userAdvanceAccessTbl.addContainerProperty("add/edit events", CheckBox.class, null);
        userAdvanceAccessTbl.addContainerProperty("adjust payroll", CheckBox.class, null);
        userAdvanceAccessTbl.addContainerProperty("lock payroll", CheckBox.class, null);
        
        userAdvanceAccessTbl.setColumnAlignment("timekeeping", Table.ALIGN_CENTER);
        userAdvanceAccessTbl.setColumnAlignment("contributions", Table.ALIGN_CENTER);
        userAdvanceAccessTbl.setColumnAlignment("cash bond", Table.ALIGN_CENTER);
        userAdvanceAccessTbl.setColumnAlignment("advances", Table.ALIGN_CENTER);
        userAdvanceAccessTbl.setColumnAlignment("adjustment", Table.ALIGN_CENTER);
        userAdvanceAccessTbl.setColumnAlignment("payroll", Table.ALIGN_CENTER);
        userAdvanceAccessTbl.setColumnAlignment("edit employee", Table.ALIGN_CENTER);
        userAdvanceAccessTbl.setColumnAlignment("add/edit events", Table.ALIGN_CENTER);
        userAdvanceAccessTbl.setColumnAlignment("adjust payroll", Table.ALIGN_CENTER);
        userAdvanceAccessTbl.setColumnAlignment("lock payroll", Table.ALIGN_CENTER);
        
        List<UserAdvanceAccess> userAdvanceAccessList = administratorService.getListOfUserAdvanceAccess();
        int i = 0;
        for(UserAdvanceAccess uaa : userAdvanceAccessList){
            Object itemId = new Integer(i);
            
            final CheckBox timekeeping = new CheckBox();
            timekeeping.setValue(uaa.isTimekeeping());
            timekeeping.setData(itemId);
            timekeeping.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = timekeeping.getData();
                    Item item = userAdvanceAccessTbl.getItem(itemId);
                    
                    boolean result = administratorService.allowAccessOfUserAdvanceAccess(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "timekeeping", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled Timekeeping features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled Timekeeping features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            });
            timekeeping.setImmediate(true);
            
            final CheckBox contributions = new CheckBox();
            contributions.setValue(uaa.isContributions());
            contributions.setData(itemId);
            contributions.addListener(new Button.ClickListener(){

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = contributions.getData();
                    Item item = userAdvanceAccessTbl.getItem(itemId);
                    
                    boolean result = administratorService.allowAccessOfUserAdvanceAccess(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "contributions", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled Contribution features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled Enabled Contribution features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            
            });
            contributions.setImmediate(true);
            
            final CheckBox cashBond = new CheckBox();
            cashBond.setValue(uaa.isCashBond());
            cashBond.setData(itemId);
            cashBond.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = cashBond.getData();
                    Item item = userAdvanceAccessTbl.getItem(itemId);
                    
                    boolean result = administratorService.allowAccessOfUserAdvanceAccess(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "cashBond", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled CashBond features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled CashBond features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            });
            cashBond.setImmediate(true);
            
            final CheckBox advances = new CheckBox();
            advances.setValue(uaa.isAdvances());
            advances.setData(itemId);
            advances.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = advances.getData();
                    Item item = userAdvanceAccessTbl.getItem(itemId);
                    
                    boolean result = administratorService.allowAccessOfUserAdvanceAccess(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "advances", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled Advances features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled Advances features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            });
            advances.setImmediate(true);
            
            final CheckBox adjustment = new CheckBox();
            adjustment.setValue(uaa.isAdjustment());
            adjustment.setData(itemId);
            adjustment.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = adjustment.getData();
                    Item item = userAdvanceAccessTbl.getItem(itemId);
                    
                    boolean result = administratorService.allowAccessOfUserAdvanceAccess(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "adjustment", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled Adjustment features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled Adjustment features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            });
            adjustment.setImmediate(true);
            
            final CheckBox payroll = new CheckBox();
            payroll.setValue(uaa.isPayroll());
            payroll.setData(itemId);
            payroll.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = payroll.getData();
                    Item item = userAdvanceAccessTbl.getItem(itemId);
                    
                    boolean result = administratorService.allowAccessOfUserAdvanceAccess(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "payroll", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled Payroll features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled Payroll features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            });
            payroll.setImmediate(true);
            
            final CheckBox editEmployeesInfo = new CheckBox();
            editEmployeesInfo.setValue(uaa.isEditEmployeesInfo());
            editEmployeesInfo.setData(itemId);
            editEmployeesInfo.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = editEmployeesInfo.getData();
                    Item item = userAdvanceAccessTbl.getItem(itemId);
                    
                    boolean result = administratorService.allowAccessOfUserAdvanceAccess(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "editEmployeesInfo", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled Edit Employee Info features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled Edit Employee Info features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            });
            editEmployeesInfo.setImmediate(true);
            
            final CheckBox addEvents = new CheckBox();
            addEvents.setValue(uaa.isAddEvents());
            addEvents.setData(itemId);
            addEvents.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = addEvents.getData();
                    Item item = userAdvanceAccessTbl.getItem(itemId);
                    
                    boolean result = administratorService.allowAccessOfUserAdvanceAccess(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "addEvents", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled Add Events features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled Add Events features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            });
            addEvents.setImmediate(true);
            
            final CheckBox adjustPayroll = new CheckBox();
            adjustPayroll.setValue(uaa.isAdjustPayroll());
            adjustPayroll.setData(itemId);
            adjustPayroll.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = adjustPayroll.getData();
                    Item item = userAdvanceAccessTbl.getItem(itemId);
                    
                    boolean result = administratorService.allowAccessOfUserAdvanceAccess(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "adjustPayroll", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled Adjust Payroll features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled Adjust Payroll features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            });
            adjustPayroll.setImmediate(true);
            
            CheckBox lockPayroll = new CheckBox();
            lockPayroll.setValue(uaa.isLockPayroll());
            lockPayroll.setData(itemId);
            lockPayroll.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = adjustPayroll.getData();
                    Item item = userAdvanceAccessTbl.getItem(itemId);
                    
                    boolean result = administratorService.allowAccessOfUserAdvanceAccess(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "lockPayroll", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled Adjust Payroll features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled Adjust Payroll features!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            });
            lockPayroll.setImmediate(true);
            
            userAdvanceAccessTbl.addItem(new Object[]{
                uaa.getId(), 
                uaa.getName(), 
                uaa.getUsername(), 
                uaa.getRole(), 
                timekeeping, 
                contributions, 
                cashBond, 
                advances, 
                adjustment, 
                payroll, 
                editEmployeesInfo, 
                addEvents, 
                adjustPayroll, 
                lockPayroll
            }, new Integer(i));
            i++;
        }
        userAdvanceAccessTbl.setPageLength(userAdvanceAccessTbl.size());
    }
}
