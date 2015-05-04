/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee;

import com.openhris.service.PostEmploymentInformationService;
import com.openhris.serviceprovider.PostEmploymentInformationServiceImpl;
import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

/**
 *
 * @author jetdario
 */
public class EditEmploymentDateEntryWindow extends Window implements Button.ClickListener {
    PostEmploymentInformationService positionHistoryService = new PostEmploymentInformationServiceImpl();
    private String employeeId;
    private String entryDate;

    public EditEmploymentDateEntryWindow(String employeeId, String entryDate) {
        this.employeeId = employeeId;
        this.entryDate = entryDate;
        
        setCaption("ENTRY DATE OF EMPLOYMENT ");
        setModal(true);
        setResizable(false);
        center();
        
        Button editBtn = new Button("CONFIRM ACTION?");
        editBtn.setWidth("100%");
        editBtn.addListener(this);
        
        addComponent(editBtn);
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getEntryDate() {
        return entryDate;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        boolean result = positionHistoryService.editDateEntryFromEmployment(getEmployeeId(), getEntryDate());
            if(result){
                getApplication().getMainWindow().showNotification("ENTRY DATE UPDATED.", Window.Notification.TYPE_TRAY_NOTIFICATION);
                close();
            }
    }
    
}
