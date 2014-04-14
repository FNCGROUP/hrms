/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.administrator;

import com.openhris.administrator.model.UserToolbarMenuAccess;
import com.openhris.administrator.serviceprovider.AdministratorServiceImpl;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.service.AdministratorService;
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
public class UserToolbarMenuAccessMainUI extends VerticalLayout {
    
    AdministratorService administratorService = new AdministratorServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    Table userToolbarMenuAccessTbl = new Table();
    
    public UserToolbarMenuAccessMainUI(){
        setSpacing(false);
        setMargin(false);
        setSizeFull();
        setImmediate(true);
        
        userToolbarMenuAccessTable();
        addComponent(userToolbarMenuAccessTbl);
    }
    
    public void userToolbarMenuAccessTable(){
        userToolbarMenuAccessTbl.removeAllItems();
        userToolbarMenuAccessTbl.setSizeFull();
        userToolbarMenuAccessTbl.setImmediate(true);
        userToolbarMenuAccessTbl.setSelectable(true);
        
        userToolbarMenuAccessTbl.addContainerProperty("id", String.class, null);
        userToolbarMenuAccessTbl.addContainerProperty("name", String.class, null);
        userToolbarMenuAccessTbl.addContainerProperty("username", String.class, null);
        userToolbarMenuAccessTbl.addContainerProperty("role", String.class, null);
        userToolbarMenuAccessTbl.addContainerProperty("main", CheckBox.class, null);
        userToolbarMenuAccessTbl.addContainerProperty("timekeeping", CheckBox.class, null);
        userToolbarMenuAccessTbl.addContainerProperty("payroll", CheckBox.class, null);
        userToolbarMenuAccessTbl.addContainerProperty("loans", CheckBox.class, null);
        userToolbarMenuAccessTbl.addContainerProperty("contributions", CheckBox.class, null);
        userToolbarMenuAccessTbl.addContainerProperty("events", CheckBox.class, null);
        
        userToolbarMenuAccessTbl.setColumnAlignment("main", Table.ALIGN_CENTER);
        userToolbarMenuAccessTbl.setColumnAlignment("timekeeping", Table.ALIGN_CENTER);
        userToolbarMenuAccessTbl.setColumnAlignment("payroll", Table.ALIGN_CENTER);
        userToolbarMenuAccessTbl.setColumnAlignment("loans", Table.ALIGN_CENTER);
        userToolbarMenuAccessTbl.setColumnAlignment("contributions", Table.ALIGN_CENTER);
        userToolbarMenuAccessTbl.setColumnAlignment("events", Table.ALIGN_CENTER);
        
        List<UserToolbarMenuAccess> userToolbarMenuAccessList = administratorService.getListOfUserToolbarMenuAccess();
        int i = 0;
        for(UserToolbarMenuAccess utma : userToolbarMenuAccessList){
            Object itemId = new Integer(i);
                
            final CheckBox main = new CheckBox();
            main.setValue(utma.isMainMenu());
            main.setData(itemId);
            main.addListener(new Button.ClickListener(){

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = main.getData();
                    Item item = userToolbarMenuAccessTbl.getItem(itemId);
                        
                    boolean result = administratorService.allowAccessOfUserToolbarMenu(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "main", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled Main Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled Main Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            });
            main.setImmediate(true);
            
            final CheckBox timekeeping = new CheckBox();
            timekeeping.setValue(utma.isTimekeepingMenu());
            timekeeping.setData(itemId);
            timekeeping.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = timekeeping.getData();
                    Item item = userToolbarMenuAccessTbl.getItem(itemId);
                        
                    boolean result = administratorService.allowAccessOfUserToolbarMenu(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "timekeeping", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled Timekeeping Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled Timekeeping Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            });
            timekeeping.setImmediate(true);
            
            final CheckBox payroll = new CheckBox();
            payroll.setValue(utma.isPayrollMenu());
            payroll.setData(itemId);
            payroll.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = payroll.getData();
                    Item item = userToolbarMenuAccessTbl.getItem(itemId);
                        
                    boolean result = administratorService.allowAccessOfUserToolbarMenu(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "payroll", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled Payroll Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled Payroll Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            });
            payroll.setImmediate(true);
            
            final CheckBox loans = new CheckBox();
            loans.setValue(utma.isLoansMenu());
            loans.setData(itemId);
            loans.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = loans.getData();
                    Item item = userToolbarMenuAccessTbl.getItem(itemId);
                        
                    boolean result = administratorService.allowAccessOfUserToolbarMenu(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "loans", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled Loans Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled Loans Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            });
            loans.setImmediate(true);
            
            final CheckBox contributions = new CheckBox();
            contributions.setValue(utma.isContributionsMenu());
            contributions.setData(itemId);
            contributions.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = contributions.getData();
                    Item item = userToolbarMenuAccessTbl.getItem(itemId);
                        
                    boolean result = administratorService.allowAccessOfUserToolbarMenu(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "contributions", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled Contributions Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled Contributions Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            });
            contributions.setImmediate(true);
            
            final CheckBox events = new CheckBox();
            events.setValue(utma.isEventsMenu());
            events.setData(itemId);
            events.addListener(new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    Object itemId = events.getData();
                    Item item = userToolbarMenuAccessTbl.getItem(itemId);
                        
                    boolean result = administratorService.allowAccessOfUserToolbarMenu(
                            util.convertStringToInteger(item.getItemProperty("id").toString()), 
                            "events", event.getButton().booleanValue());
                    if(result){
                        if(event.getButton().booleanValue()){
                            getWindow().showNotification("Enabled Events Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        } else {
                            getWindow().showNotification("Disabled Events Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                        }
                    }
                }
            });
            events.setImmediate(true);
            
            userToolbarMenuAccessTbl.addItem(new Object[]{
                utma.getId(), 
                utma.getName(), 
                utma.getUsername(), 
                utma.getRole(), 
                main, 
                timekeeping, 
                payroll, 
                loans, 
                contributions, 
                events
            }, new Integer(i));
            i++;
        }    
        userToolbarMenuAccessTbl.setPageLength(userToolbarMenuAccessTbl.size());
    }
    
}
