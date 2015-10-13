/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee.others;

import com.hrms.classes.GlobalVariables;
import com.openhris.commons.DropDownComponent;
import com.openhris.commons.HRISTextField;
import com.openhris.model.EmploymentInformation;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 *
 * @author jetdario
 */
public class OtherInformation extends VerticalLayout {

    DropDownComponent dropDown = new DropDownComponent();
    
    private String employeeId;
    private boolean userRoleResult = false;
    
    public OtherInformation(String employeeId) {
        this.employeeId = employeeId;
        
        if(GlobalVariables.getUserRole().equals("hr")){
            userRoleResult = true;
        }
        
        setSpacing(true);
	setMargin(true);
	setSizeFull();
	setImmediate(true);
        
        addComponent(component());
    }
    
    TextField tinField;
    TextField sssField;
    TextField phicField;
    TextField hdmfField;
    TextField bankAccountNo;
    TextField employmentStatusField;
    
    ComboBox employeeDependent;
    
    Component component(){
        GridLayout glayout = new GridLayout(2, 13);
        glayout.setSpacing(true);          
        glayout.setWidth("600px");
	glayout.setHeight("100%");
        
        tinField = new HRISTextField("TIN: ");
        glayout.addComponent(tinField, 0, 0);        
        
        Button tinBtn = new Button("UPDATE TIN NO.");
        tinBtn.setWidth("100%");
        glayout.addComponent(tinBtn, 1, 0);
        glayout.setComponentAlignment(tinBtn, Alignment.BOTTOM_CENTER);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 1, 1, 1);
        
        employeeDependent = dropDown.populateTotalDependent(new ComboBox());
        employeeDependent.setWidth("100%");
        glayout.addComponent(employeeDependent, 0, 2);
        
        Button edBtn = new Button("UPDATE EMPLOYEE DEPENDENT");
        edBtn.setWidth("100%");
        glayout.addComponent(edBtn, 1, 2);
        glayout.setComponentAlignment(edBtn, Alignment.BOTTOM_CENTER);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 3, 1, 3);
        
        sssField = new HRISTextField("SSS: ");
        glayout.addComponent(sssField, 0, 4);
        
        Button sssbtn = new Button("UPDATE SSS NO.");
        sssbtn.setWidth("100%");
        glayout.addComponent(sssbtn, 1, 4);
        glayout.setComponentAlignment(sssbtn, Alignment.BOTTOM_CENTER);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 5, 1, 5);
        
        phicField = new HRISTextField("Philhealth");
        glayout.addComponent(phicField, 0, 6);
        
        Button phicBtn = new Button("UPDATE PHIC NO.");
        phicBtn.setWidth("100%");
        glayout.addComponent(phicBtn, 1, 6);
        glayout.setComponentAlignment(phicBtn, Alignment.BOTTOM_CENTER);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 7, 1, 7);
        
        hdmfField = new HRISTextField("HDMF: ");
        glayout.addComponent(hdmfField, 0, 8);
        
        Button hdmfBtn = new Button("UPDATE HDMF NO.");
        hdmfBtn.setWidth("100%");
        glayout.addComponent(hdmfBtn, 1, 8);
        glayout.setComponentAlignment(hdmfBtn, Alignment.BOTTOM_CENTER);
        
        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 9, 1, 9);
                
        bankAccountNo = new HRISTextField("Bank Account No: ");
        bankAccountNo.setImmediate(true);
        glayout.addComponent(bankAccountNo, 0, 10);
        
        Button bankAccountBtn = new Button("UPDATE HDMF NO.");
        bankAccountBtn.setWidth("100%");
        glayout.addComponent(bankAccountBtn, 1, 10);
        glayout.setComponentAlignment(bankAccountBtn, Alignment.BOTTOM_CENTER);
        
//        glayout.addComponent(new Label("<HR>", Label.CONTENT_XHTML), 0, 11, 1, 11);
//        
//        employmentStatusField = new HRISTextField("Employment Status: ");
//        glayout.addComponent(employmentStatusField, 0, 12);
//                
//        Button setContributionBtn = new Button("SET EMPLOYEE'S CONTRIBUTIONS MAIN BRANCH");
//        setContributionBtn.setWidth("100%");
//        glayout.addComponent(setContributionBtn, 1, 12);
//        glayout.setComponentAlignment(setContributionBtn, Alignment.BOTTOM_CENTER);
        
        return glayout;
    }
        
    String getEmployeeId(){
        return employeeId;
    }
}
