/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee;

import com.openhris.employee.service.PositionHistoryService;
import com.openhris.employee.serviceprovider.PositionHistoryServiceImpl;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

/**
 *
 * @author jetdario
 */
public class ConfirmResignWindow extends Window implements Button.ClickListener {

    PositionHistoryService positionHistoryService = new PositionHistoryServiceImpl();
    private String employeeId;
    private String endDate;
    
    public ConfirmResignWindow(String employeeId, String endDate) {
        this.employeeId = employeeId;
        this.endDate = endDate;
        setCaption("END DATE (RESIGN)");
        setResizable(false);
        
        Button resignBtn = new Button("CONFIRM ACTION?");
        resignBtn.setWidth("100%");
        resignBtn.addListener(this);
        
        addComponent(resignBtn);
    }
    
    @Override
    public void buttonClick(Button.ClickEvent event) {
        boolean result = positionHistoryService.insertEndDate(getEmployeeId(), getEndDate());
        if(result){
            getWindow().showNotification("Resigned.", Window.Notification.TYPE_TRAY_NOTIFICATION);
            close();
        }
    }
    
    String getEmployeeId(){
        return employeeId;
    }
    
    String getEndDate(){
        return endDate;
    }
}