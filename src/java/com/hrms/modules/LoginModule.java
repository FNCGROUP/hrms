/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.modules;

import com.hrms.beans.LoginBean;
import com.hrms.dbconnection.AuthenticateLogin;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

/**
 *
 * @author jet
 */
public class LoginModule extends VerticalLayout{
    
    Label label = new Label();
    LoginBean login = new LoginBean();
    AuthenticateLogin authLog = new AuthenticateLogin();
    GridLayout grid = new GridLayout(1, 3);
    Panel panel = new Panel("Authorized Personnel");
    
    public LoginModule(){
        
        setMargin(true);
        setSpacing(true);
        grid.setSizeFull();
        grid.setSpacing(true);
        
        final TextField username = new TextField();
        username.setValue("username");
        username.setWidth("120px");
        username.setStyleName("small");
        grid.addComponent(username, 0, 0);
        
        final PasswordField password = new PasswordField();
        password.setValue("password");
        password.setWidth("120px");
        password.setStyleName("small");
        grid.addComponent(password, 0, 1);
        
        Button button = new Button("Login");
        button.setWidth("120px");
        button.setStyleName("small");
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                login.setUsername(username.getValue().toString());
                login.setPassword(password.getValue().toString());
                String user = login.getUsername();
                String pass = login.getPassword();
                //login.setResult(authLog.login(user, pass));
                boolean result = authLog.login(user, pass);
                if(result == true){
                    label.setCaption("Successfully Login");
                }else{
                    label.setCaption("SQL Error");
                }                
            }
            
        });
        grid.addComponent(button, 0, 2);
        panel.addComponent(grid);
        addComponent(panel);
        addComponent(label);
        
    }
    
}
