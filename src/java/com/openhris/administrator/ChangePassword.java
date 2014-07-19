/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.administrator;

import com.hrms.classes.GlobalVariables;
import com.openhris.administrator.commons.CheckIfWindowIsClose;
import com.openhris.administrator.service.AdministratorService;
import com.openhris.administrator.serviceprovider.AdministratorServiceImpl;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author jetdario
 */
public class ChangePassword extends Window{
    
    AdministratorService adminService = new AdministratorServiceImpl();
    private int userId;
    boolean logout = false;
    
    public ChangePassword(int userId){
        this.userId = userId;
        
        init();
    }
    
    public void init(){
        setCaption("Settings");
        setWidth("300px");           
        addComponent(layout());
    }
    
    public ComponentContainer layout(){
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        vlayout.setSizeFull();
        vlayout.setImmediate(true);
        
        final PasswordField currentPassword = createPasswordField("Current Password: ");
        vlayout.addComponent(currentPassword);
        
        final PasswordField newPassword = createPasswordField("New Password: ");
        vlayout.addComponent(newPassword);
        
        final PasswordField rePassword = createPasswordField("Re-enter Password: ");
        vlayout.addComponent(rePassword);        
        
        Button changeBtn = new Button("UPDATE PASSWORD");
        changeBtn.setWidth("100%");
        changeBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(!adminService.checkEnteredPasswordIfCorrect(getUserId(), currentPassword.getValue().toString().toLowerCase().trim())){
                    getWindow().showNotification("Incorrect Password, contact your Administrator!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                if(!newPassword.getValue().toString().toLowerCase().trim().equals(rePassword.getValue().toString().toLowerCase().trim())){
                    getWindow().showNotification("Entered Password do not Match!", Window.Notification.TYPE_WARNING_MESSAGE);
                    return;
                }
                
                boolean result = adminService.updateUserPassword(getUserId(), rePassword.getValue().toString().toLowerCase().trim());
                if(result){
                    GlobalVariables.setLogoutAfterPasswordChange(true);
                    close();                        
                } else {
                    GlobalVariables.setLogoutAfterPasswordChange(false);
                    getWindow().showNotification("Change Password SQL Error!, Contact your DBA", Window.Notification.TYPE_ERROR_MESSAGE);
                    return;
                }
                
            }
        });
        vlayout.addComponent(changeBtn);
        
        return vlayout;
    }
    
    public int getUserId(){
        return userId;
    }
    
    private PasswordField createPasswordField(String str){
	PasswordField p = new PasswordField();
	p.setCaption(str);
	p.setWidth("100%");
	p.setStyleName(Reindeer.TEXTFIELD_SMALL);
        p.setNullSettingAllowed(true);
        
	return p;
    }
        
    public boolean getLogout(){
        return logout;
    }
}
