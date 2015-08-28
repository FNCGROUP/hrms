/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.administrator;

import com.hrms.classes.GlobalVariables;
import com.openhris.administrator.model.User;
import com.openhris.administrator.service.AdministratorService;
import com.openhris.administrator.serviceprovider.AdministratorServiceImpl;
import com.openhris.commons.OpenHrisUtilities;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author jetdario
 */
public class SpecialAccessControl extends VerticalLayout {

    AdministratorService service = new AdministratorServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    ComboBox users = new ComboBox();
    CheckBox allowedBackwardInputAttendance = new CheckBox();
    private int val;
    
    public SpecialAccessControl() {
        setMargin(true);
        setSpacing(true);
        
        addComponent(userList());
        
        allowedBackwardInputAttendance.setCaption("Allow user to enter previous attendance.");
        allowedBackwardInputAttendance.addListener(new ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(event.getButton().booleanValue()){
                    val = 1;
                } else  {
                    val = 0;
                }
            }
        });
        allowedBackwardInputAttendance.setImmediate(true);
        addComponent(allowedBackwardInputAttendance);
        
        Button button = new Button("UPDATE");
        button.setWidth("200px");
        button.addListener(new ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(users.getValue() == null){
                    getWindow().showNotification("Select a User!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                boolean isUserAllowed = service.updateUserAllowedToEnterPreviousAttendance(
                        util.convertStringToInteger(
                                users.getValue().toString()), isAllowed());
                if(isUserAllowed){
                    getWindow().showNotification("User is allowed to Enter Previous Attendance!", Window.Notification.TYPE_TRAY_NOTIFICATION);
                }
            }
        });
        button.setImmediate(true);
        addComponent(button);
    }
    
    ComboBox userList(){        
        users.setCaption("Users: ");
        users.setWidth("200px");
        users.setNullSelectionAllowed(false);        
        users.addContainerProperty("y", String.class, "");
        users.setItemCaptionPropertyId("y");
        
        Item i;
        for(User u : service.getUserList()){
            i = users.addItem(u.getId());
            i.getItemProperty("y").setValue(u.getUsername());
        }
        
        users.addListener(new ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                allowedBackwardInputAttendance.setValue(
                        service.isUserAllowedToEnterPreviousAttendance(
                                util.convertStringToInteger(event.getProperty().getValue().toString())));
            }
        });
        
        users.setImmediate(true);
        
        return users;
    }
    
    int isAllowed(){
        return val;
    }
}
