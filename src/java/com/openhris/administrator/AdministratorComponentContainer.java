/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.administrator;

import com.openhris.administrator.serviceprovider.AdministratorServiceImpl;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.service.AdministratorService;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author jet
 */
public class AdministratorComponentContainer extends VerticalLayout {
    
    AdministratorService administratorService = new AdministratorServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    Table userAccessControlTbl = new Table();
    
    public AdministratorComponentContainer(){
        setSpacing(false);
        setMargin(false);
        setSizeFull();
        setImmediate(true);                
    }
    
    public ComponentContainer administratorComponentContainer(){
        TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addStyleName("bar");
        
        VerticalLayout adminMenuGrid = new VerticalLayout();
        adminMenuGrid.setSizeFull();
        adminMenuGrid.setCaption("Users");
//        adminMenuGrid.addComponent(createNewUser);
        ts.addComponent(adminMenuGrid);
        
        adminMenuGrid = new VerticalLayout();
        adminMenuGrid.setSizeFull();
        adminMenuGrid.setCaption("Toolbar User Access Control");
//        adminMenuGrid.addComponent(adminApp);
        ts.addComponent(adminMenuGrid);
        
        adminMenuGrid = new VerticalLayout();
        adminMenuGrid.setSizeFull();
        adminMenuGrid.setCaption("Advance User Access Control");
//        adminMenuGrid.addComponent(advanceUserAccess);
        ts.addComponent(adminMenuGrid);
        
        ts.addListener(new TabSheet.SelectedTabChangeListener() {

            @Override
            public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
                System.out.println("NONE");
//                adminApp.userAccessTable();
//                createNewUser.userTable();
//                advanceUserAccess.advanceUserAccessTable();
            }
        });
        
        return ts;
    }
    
}
