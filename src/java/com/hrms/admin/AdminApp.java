/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.admin;

import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.queries.GetSQLQueryUpdate;
import com.hrms.utilities.ConvertionUtilities;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class AdminApp extends VerticalLayout {
    
    GetSQLQueryUpdate queryUpdate = new GetSQLQueryUpdate();
    ConvertionUtilities conUtil = new ConvertionUtilities();
    Table userAccessTbl = new Table();
            
    public AdminApp() {
        setSpacing(false);
        setMargin(false);
        setSizeFull();
        setImmediate(true);
                
        userAccessTable();
        addComponent(userAccessTbl);        
    }
    
    public final void userAccessTable(){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        userAccessTbl.removeAllItems();
        userAccessTbl.setSizeFull();
        userAccessTbl.setImmediate(true);
        userAccessTbl.setSelectable(true);
        
        userAccessTbl.addContainerProperty("id", String.class, null);
        userAccessTbl.addContainerProperty("name", String.class, null);
        userAccessTbl.addContainerProperty("trade", String.class, null);
        userAccessTbl.addContainerProperty("branch", String.class, null);
        userAccessTbl.addContainerProperty("username", String.class, null);
        userAccessTbl.addContainerProperty("role", String.class, null);
        userAccessTbl.addContainerProperty("attendance", CheckBox.class, null);
        userAccessTbl.addContainerProperty("contributions", CheckBox.class, null);
        userAccessTbl.addContainerProperty("cash bond", CheckBox.class, null);
        userAccessTbl.addContainerProperty("advances", CheckBox.class, null);
        userAccessTbl.addContainerProperty("adjustment", CheckBox.class, null);
        userAccessTbl.addContainerProperty("salary", CheckBox.class, null);
        userAccessTbl.addContainerProperty("edit employee", CheckBox.class, null);
        userAccessTbl.addContainerProperty("add/edit events", CheckBox.class, null);
        userAccessTbl.addContainerProperty("adjust salary", CheckBox.class, null);
        
        userAccessTbl.setColumnAlignment("attendance", Table.ALIGN_CENTER);
        userAccessTbl.setColumnAlignment("contributions", Table.ALIGN_CENTER);
        userAccessTbl.setColumnAlignment("cash bond", Table.ALIGN_CENTER);
        userAccessTbl.setColumnAlignment("advances", Table.ALIGN_CENTER);
        userAccessTbl.setColumnAlignment("adjustment", Table.ALIGN_CENTER);
        userAccessTbl.setColumnAlignment("salary", Table.ALIGN_CENTER);
        userAccessTbl.setColumnAlignment("edit employee", Table.ALIGN_CENTER);
        userAccessTbl.setColumnAlignment("add/edit events", Table.ALIGN_CENTER);
        userAccessTbl.setColumnAlignment("adjust salary", Table.ALIGN_CENTER);
        
        try {
            int i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user_access_control");
            while(rs.next()){
                Object itemId = new Integer(i);
                
                final CheckBox attendance = new CheckBox();
                attendance.setValue(conUtil.convertStringToBoolean(rs.getString("attendance")));
                attendance.setData(itemId);
                attendance.setImmediate(true);
                attendance.addListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Object itemId = attendance.getData();
                        Item item = userAccessTbl.getItem(itemId);
                        
                        String id = item.getItemProperty("id").getValue().toString();
                        String userAccess = "attendance";
                        String value = event.getProperty().getValue().toString();
                        boolean resultValue = (Boolean) event.getProperty().getValue();
                        boolean result = queryUpdate.updateUserAccessControl(id, userAccess, value);
                        if(result == true){
                            if(resultValue == true){
                                getWindow().showNotification("Enable user to encode Attendance!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }else{
                                getWindow().showNotification("Disable user to encode Attendance!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }                            
                        }                      
                    }
                    
                }); 
                
                final CheckBox contributions = new CheckBox();
                contributions.setValue(conUtil.convertStringToBoolean(rs.getString("contributions")));
                contributions.setData(itemId);
                contributions.setImmediate(true);
                contributions.addListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Object itemId = contributions.getData();
                        Item item = userAccessTbl.getItem(itemId);
                        
                        String id = item.getItemProperty("id").getValue().toString();
                        String userAccess = "contributions";
                        String value = event.getProperty().getValue().toString();
                        boolean resultValue = (Boolean) event.getProperty().getValue();
                        boolean result = queryUpdate.updateUserAccessControl(id, userAccess, value);
                        if(result == true){
                            if(resultValue == true){
                                getWindow().showNotification("Enable user to Edit/delete Contributions!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }else{
                                getWindow().showNotification("Disable user to Edit/delete Contributions!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }                            
                        }
                    }
                    
                });
                
                final CheckBox cashBond = new CheckBox();
                cashBond.setValue(conUtil.convertStringToBoolean(rs.getString("cashBond")));
                cashBond.setData(itemId);
                cashBond.setImmediate(true);
                cashBond.addListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Object itemId = cashBond.getData();
                        Item item = userAccessTbl.getItem(itemId);
                        
                        String id = item.getItemProperty("id").getValue().toString();
                        String userAccess = "cashBond";
                        String value = event.getProperty().getValue().toString();
                        boolean resultValue = (Boolean) event.getProperty().getValue();
                        boolean result = queryUpdate.updateUserAccessControl(id, userAccess, value);
                        if(result == true){
                            if(resultValue == true){
                                getWindow().showNotification("Enable user to Add/Remove Cash Bond!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }else{
                                getWindow().showNotification("Disable user to Add/Remove Cash Bond!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }                            
                        }
                    }
                    
                });
                
                final CheckBox advances = new CheckBox();
                advances.setValue(conUtil.convertStringToBoolean(rs.getString("advances")));
                advances.setData(itemId);
                advances.setImmediate(true);
                advances.addListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Object itemId = advances.getData();
                        Item item = userAccessTbl.getItem(itemId);
                        
                        String id = item.getItemProperty("id").getValue().toString();
                        String userAccess = "advances";
                        String value = event.getProperty().getValue().toString();
                        boolean resultValue = (Boolean) event.getProperty().getValue();
                        boolean result = queryUpdate.updateUserAccessControl(id, userAccess, value);
                        if(result == true){
                            if(resultValue == true){
                                getWindow().showNotification("Enable user to Add/Remove Advances!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }else{
                                getWindow().showNotification("Disable user to Add/Remove Advances!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }                            
                        }
                    }
                    
                });
                
                final CheckBox adjustment = new CheckBox();
                adjustment.setValue(conUtil.convertStringToBoolean(rs.getString("adjustment")));
                adjustment.setData(itemId);
                adjustment.setImmediate(true);
                adjustment.addListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Object itemId = adjustment.getData();
                        Item item = userAccessTbl.getItem(itemId);
                        
                        String id = item.getItemProperty("id").getValue().toString();
                        String userAccess = "adjustment";
                        String value = event.getProperty().getValue().toString();
                        boolean resultValue = (Boolean) event.getProperty().getValue();
                        boolean result = queryUpdate.updateUserAccessControl(id, userAccess, value);
                        if(result == true){
                            if(resultValue == true){
                                getWindow().showNotification("Enable user to Add/Remove Adjustment!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }else{
                                getWindow().showNotification("Disable user to Add/Remove Adjustment!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }                            
                        }
                    }
                    
                });
                
                final CheckBox salary = new CheckBox();
                salary.setValue(conUtil.convertStringToBoolean(rs.getString("salary")));
                salary.setData(itemId);
                salary.setImmediate(true);
                salary.addListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Object itemId = salary.getData();
                        Item item = userAccessTbl.getItem(itemId);
                        
                        String id = item.getItemProperty("id").getValue().toString();
                        String userAccess = "salary";
                        String value = event.getProperty().getValue().toString();
                        boolean resultValue = (Boolean) event.getProperty().getValue();
                        boolean result = queryUpdate.updateUserAccessControl(id, userAccess, value);
                        if(result == true){
                            if(resultValue == true){
                                getWindow().showNotification("Enable user to Remove Salary!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }else{
                                getWindow().showNotification("Disable user to Remove Salary!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }                            
                        }
                    }
                    
                });
                
                final CheckBox editEmployeesInfo = new CheckBox();
                editEmployeesInfo.setValue(conUtil.convertStringToBoolean(rs.getString("editEmployeesInfo")));
                editEmployeesInfo.setData(itemId);
                editEmployeesInfo.setImmediate(true);
                editEmployeesInfo.addListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Object itemId = editEmployeesInfo.getData();
                        Item item = userAccessTbl.getItem(itemId);
                        
                        String id = item.getItemProperty("id").getValue().toString();
                        String userAccess = "editEmployeesInfo";
                        String value = event.getProperty().getValue().toString();
                        boolean resultValue = (Boolean) event.getProperty().getValue();
                        boolean result = queryUpdate.updateUserAccessControl(id, userAccess, value);
                        if(result == true){
                            if(resultValue == true){
                                getWindow().showNotification("Enable user to Remove Edit Employees Info!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }else{
                                getWindow().showNotification("Disable user to Remove Edit Employees Info!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }                            
                        }
                    }
                    
                });
                
                final CheckBox addEvents = new CheckBox();
                addEvents.setValue(conUtil.convertStringToBoolean(rs.getString("addEvents")));
                addEvents.setData(itemId);
                addEvents.setImmediate(true);
                addEvents.addListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Object itemId = addEvents.getData();
                        Item item = userAccessTbl.getItem(itemId);
                        
                        String id = item.getItemProperty("id").getValue().toString();
                        String userAccess = "addEvents";
                        String value = event.getProperty().getValue().toString();
                        boolean resultValue = (Boolean) event.getProperty().getValue();
                        boolean result = queryUpdate.updateUserAccessControl(id, userAccess, value);
                        if(result == true){
                            if(resultValue == true){
                                getWindow().showNotification("Enable user to Add Events!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }else{
                                getWindow().showNotification("Disable user to Add Events!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }                            
                        }
                    }
                    
                });
                
                final CheckBox adjustSalary = new CheckBox();
                adjustSalary.setValue(conUtil.convertStringToBoolean(rs.getString("adjustSalary")));
                adjustSalary.setData(itemId);
                adjustSalary.setImmediate(true);
                adjustSalary.addListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Object itemId = addEvents.getData();
                        Item item = userAccessTbl.getItem(itemId);
                        
                        String id = item.getItemProperty("id").getValue().toString();
                        String userAccess = "adjustSalary";
                        String value = event.getProperty().getValue().toString();
                        boolean resultValue = (Boolean) event.getProperty().getValue();
                        boolean result = queryUpdate.updateUserAccessControl(id, userAccess, value);
                        if(result == true){
                            if(resultValue == true){
                                getWindow().showNotification("Enable user to Adjust Salary!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }else{
                                getWindow().showNotification("Disable user to Adjust Salary!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }                            
                        }
                    }
                    
                });
                
                userAccessTbl.addItem(new Object[]{
                    rs.getString("uaId"),
                    rs.getString("name").toUpperCase(), 
                    rs.getString("trade").toUpperCase(), 
                    rs.getString("branch").toUpperCase(), 
                    rs.getString("username"),
                    rs.getString("role"),
                    attendance, 
                    contributions, 
                    cashBond,
                    advances, 
                    adjustment, 
                    salary, 
                    editEmployeesInfo,
                    addEvents, 
                    adjustSalary
                }, new Integer(i));
                i++;
            }
            userAccessTbl.setPageLength(userAccessTbl.size());
        } catch (SQLException ex) {
            Logger.getLogger(AdminApp.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdminApp.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }           
    
}
