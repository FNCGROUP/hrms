/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee;

import com.openhris.employee.service.PostEmploymentInformationService;
import com.openhris.employee.serviceprovider.PostEmploymentInformationServiceImpl;
import com.vaadin.ui.Button;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author jetdario
 */
public class RemovePositionWindow extends Window implements Button.ClickListener {

    PostEmploymentInformationService positionHistoryService = new PostEmploymentInformationServiceImpl();
    private int id;
    
    public RemovePositionWindow(int id) {
        this.id = id;
        setCaption("REMOVE POSITION");
        setWidth("300px");
        
        VerticalLayout vlayout = new VerticalLayout();
        vlayout.setSpacing(true);
        vlayout.setMargin(true);
        
        final Window window = new Window("REMOVE POSITION", vlayout);
        window.setWidth("300px");
        
        Button removeBtn = new  Button("Remove Position?");
        removeBtn.setWidth("100%");
        removeBtn.addListener(this);
        addComponent(removeBtn);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        boolean result = positionHistoryService.removePositionHistory(getId());
        if(result){
            close();
        } else {
            getWindow().showNotification("Cannot Remove Position, Contact your DBA!", Window.Notification.TYPE_ERROR_MESSAGE);
        }
    }
    
    int getId(){
        return id;
    }
}
