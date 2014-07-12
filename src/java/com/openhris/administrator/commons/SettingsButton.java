/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.administrator.commons;

import com.openhris.administrator.ChangePassword;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

/**
 *
 * @author jetdario
 */
public class SettingsButton extends Button implements Button.ClickListener{

    private int userId;
    
    public SettingsButton(int userId){
        this.userId = userId;  
        
        setCaption("Settings");
    }
    
    @Override
    public void buttonClick(ClickEvent event) {
        System.out.println("you pressed the settings button!");
        System.out.println("event: " + event.getButton().getCaption());
        Window window = new ChangePassword(getUserId());
        if(window.getParent() == null){
            getWindow().addWindow(window);
        }
//        window.setModal(true);
        window.center();
    }
    
    public int getUserId(){
        return userId;
    }
    
}
