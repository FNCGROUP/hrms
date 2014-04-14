/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.admin.module;

import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.queries.GetSQLQueryUpdate;
import com.hrms.utilities.ConvertionUtilities;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
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
public class AdvanceUserAccessModule extends VerticalLayout {
    
    GetSQLQueryUpdate queryUpdate = new GetSQLQueryUpdate();
    ConvertionUtilities conUtil = new ConvertionUtilities();
    Table advanceUserAccessTbl = new Table();
    
    public AdvanceUserAccessModule(){
        setSpacing(false);
        setMargin(false);
        setSizeFull();
        setImmediate(true);
        
        addComponent(advanceUserAccessTbl);
    }
    
    public void advanceUserAccessTable(){
        GetSQLConnection getConnection = new GetSQLConnection();
        Connection conn = getConnection.connection();
        advanceUserAccessTbl.removeAllItems();
        advanceUserAccessTbl.setSizeFull();
        advanceUserAccessTbl.setImmediate(true);
        advanceUserAccessTbl.setSelectable(true);
        
        advanceUserAccessTbl.addContainerProperty("id", String.class, null);
        advanceUserAccessTbl.addContainerProperty("name", String.class, null);
        advanceUserAccessTbl.addContainerProperty("trade", String.class, null);
        advanceUserAccessTbl.addContainerProperty("branch", String.class, null);
        advanceUserAccessTbl.addContainerProperty("username", String.class, null);
        advanceUserAccessTbl.addContainerProperty("role", String.class, null);
        advanceUserAccessTbl.addContainerProperty("main", CheckBox.class, null);
        advanceUserAccessTbl.addContainerProperty("payroll", CheckBox.class, null);
        advanceUserAccessTbl.addContainerProperty("loans", CheckBox.class, null);
        advanceUserAccessTbl.addContainerProperty("contributions", CheckBox.class, null);
        advanceUserAccessTbl.addContainerProperty("calendar", CheckBox.class, null);
        
        advanceUserAccessTbl.setColumnAlignment("main", Table.ALIGN_CENTER);
        advanceUserAccessTbl.setColumnAlignment("payroll", Table.ALIGN_CENTER);
        advanceUserAccessTbl.setColumnAlignment("loans", Table.ALIGN_CENTER);
        advanceUserAccessTbl.setColumnAlignment("contributions", Table.ALIGN_CENTER);
        advanceUserAccessTbl.setColumnAlignment("calendar", Table.ALIGN_CENTER);
        try {
            int i = 0;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user_access_control");
            while(rs.next()){
                Object itemId = new Integer(i);
                
                final CheckBox main = new CheckBox();
                main.setValue(conUtil.convertStringToBoolean(rs.getString("mainMenu")));
                main.setData(itemId);
                main.setImmediate(true);
                main.addListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Object itemId = main.getData();
                        Item item = advanceUserAccessTbl.getItem(itemId);
                        
                        String id = item.getItemProperty("id").getValue().toString();
                        String advanceUserAccess = "main";
                        String value = event.getProperty().getValue().toString();
                        boolean resultValue = (Boolean) event.getProperty().getValue();
                        boolean result = queryUpdate.updateAdvanceUserAccessControl(id, advanceUserAccess, value);
                        if(result == true){
                            if(resultValue == true){
                                getWindow().showNotification("Enable Main Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }else{
                                getWindow().showNotification("Disable Main Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }
                        }                        
                    }
                    
                });
                
                final CheckBox payroll = new CheckBox();
                payroll.setValue(conUtil.convertStringToBoolean(rs.getString("payrollMenu")));
                payroll.setData(itemId);
                payroll.setImmediate(true);
                payroll.addListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Object itemId = payroll.getData();
                        Item item = advanceUserAccessTbl.getItem(itemId);
                        
                        String id = item.getItemProperty("id").getValue().toString();
                        String advanceUserAccess = "payroll";
                        String value = event.getProperty().getValue().toString();
                        boolean resultValue = (Boolean) event.getProperty().getValue();
                        boolean result = queryUpdate.updateAdvanceUserAccessControl(id, advanceUserAccess, value);
                        if(result == true){
                            if(resultValue == true){
                                getWindow().showNotification("Enable Payroll Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }else{
                                getWindow().showNotification("Disable Payroll Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }
                        } 
                    }
                    
                });
                
                final CheckBox loans = new CheckBox();
                loans.setValue(conUtil.convertStringToBoolean(rs.getString("loansMenu")));
                loans.setData(itemId);
                loans.setImmediate(true);
                loans.addListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Object itemId = payroll.getData();
                        Item item = advanceUserAccessTbl.getItem(itemId);
                        
                        String id = item.getItemProperty("id").getValue().toString();
                        String advanceUserAccess = "loans";
                        String value = event.getProperty().getValue().toString();
                        boolean resultValue = (Boolean) event.getProperty().getValue();
                        boolean result = queryUpdate.updateAdvanceUserAccessControl(id, advanceUserAccess, value);
                        if(result == true){
                            if(resultValue == true){
                                getWindow().showNotification("Enable Loans Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }else{
                                getWindow().showNotification("Disable Loans Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }
                        } 
                    }
                    
                });
                
                final CheckBox contributions = new CheckBox();
                contributions.setValue(conUtil.convertStringToBoolean(rs.getString("contributionsMenu")));
                contributions.setData(itemId);
                contributions.setImmediate(true);
                contributions.addListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Object itemId = contributions.getData();
                        Item item = advanceUserAccessTbl.getItem(itemId);
                        
                        String id = item.getItemProperty("id").getValue().toString();
                        String advanceUserAccess = "contributions";
                        String value = event.getProperty().getValue().toString();
                        boolean resultValue = (Boolean) event.getProperty().getValue();
                        boolean result = queryUpdate.updateAdvanceUserAccessControl(id, advanceUserAccess, value);
                        if(result == true){
                            if(resultValue == true){
                                getWindow().showNotification("Enable Contributions Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }else{
                                getWindow().showNotification("Disable Contributions Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                                return;
                            }
                        }
                    }
                    
                });
                
                final CheckBox calendar = new CheckBox();
                calendar.setValue(conUtil.convertStringToBoolean(rs.getString("calendarMenu")));
                calendar.setData(itemId);
                calendar.setImmediate(true);
                calendar.addListener(new Property.ValueChangeListener() {

                    @Override
                    public void valueChange(ValueChangeEvent event) {
                        Object itemId = calendar.getData();
                        Item item = advanceUserAccessTbl.getItem(itemId);
                        
                        String id = item.getItemProperty("id").getValue().toString();
                        String advanceUserAccess = "calendar";
                        String value = event.getProperty().getValue().toString();
                        boolean resultValue = (Boolean) event.getProperty().getValue();
                        boolean result = queryUpdate.updateAdvanceUserAccessControl(id, advanceUserAccess, value);
                        if(result == true){
                            if(resultValue == true){
                                getWindow().showNotification("Enable Calendar Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }else{
                                getWindow().showNotification("Disable Calendar Menu!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                            }
                        }
                    }
                    
                });
                
                advanceUserAccessTbl.addItem(new Object[]{ 
                    rs.getString("advanceUserAccessId"),
                    rs.getString("name").toUpperCase(), 
                    rs.getString("trade").toUpperCase(), 
                    rs.getString("branch").toUpperCase(), 
                    rs.getString("username"),
                    rs.getString("role"),
                    main, 
                    payroll, 
                    loans,
                    contributions,
                    calendar
                }, new Integer(i));
                i++;
            }
            advanceUserAccessTbl.setPageLength(advanceUserAccessTbl.size());
        } catch (SQLException ex) {
            Logger.getLogger(AdvanceUserAccessModule.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdvanceUserAccessModule.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
