/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.administrator;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author jetdario
 */
public class ChangePassword extends Window{
    
    
    private int userId;
    
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
        
        PasswordField currentPassword = createPasswordField("Current Password: ");
        vlayout.addComponent(currentPassword);
        
        PasswordField newPassword = createPasswordField("New Password: ");
        vlayout.addComponent(newPassword);
        
        PasswordField rePassword = createPasswordField("Re-enter Password: ");
        vlayout.addComponent(rePassword);        
        
        Button changeBtn = new Button("UPDATE PASSWORD");
        changeBtn.setWidth("100%");
        changeBtn.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                getWindow().showNotification("Password Changed!");
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
    
}
