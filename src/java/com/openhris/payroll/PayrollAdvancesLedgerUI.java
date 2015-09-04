/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.openhris.administrator.model.UserAccessControl;
import com.openhris.model.Employee;
import com.openhris.service.EmployeeService;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.terminal.Sizeable;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class PayrollAdvancesLedgerUI extends VerticalLayout {

    EmployeeService es = new EmployeeServiceImpl();
    
    ComboBox employee = new ComboBox("Employees");
    
    private int branchId;
    private String employeeId;
    
    public PayrollAdvancesLedgerUI(int branchId) {
        this.branchId = branchId;
        
        setSpacing(false);
        setMargin(false);
        setWidth("100%");
        setHeight("100%");
        setImmediate(true);        
        
        final VerticalSplitPanel vsplit = new VerticalSplitPanel();   
        
        vsplit.setImmediate(true);
        vsplit.setMargin(false);
        vsplit.setSizeFull();
        vsplit.setLocked(true);
        
        vsplit.setSplitPosition(90, Sizeable.UNITS_PIXELS);
        
        GridLayout glayout = new GridLayout(2, 1);
        glayout.setWidth("60%");
        glayout.setMargin(true);
        glayout.setSpacing(true);
        
        employeeComboBox(getBranchId());   
        glayout.addComponent(employee, 0, 0);
        
        Button button = new Button();
        button.setWidth("100%");
        button.setCaption("Generate Ledger");
        button.setEnabled(UserAccessControl.isPayroll());
        button.addListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                System.out.println("employeeId: "+employee.getValue());
            }
        });
        
        glayout.addComponent(button, 1, 0);
        glayout.setComponentAlignment(button, Alignment.BOTTOM_LEFT);
        
        vsplit.setFirstComponent(glayout);        
        addComponent(vsplit);
        
        setExpandRatio(vsplit, 1.0f);
    }
    
    int getBranchId(){
        return branchId;
    }
    
    String getEmployeeId(){
        return employeeId;
    }
    
    public void employeeComboBox(int branchId){  
        employee.removeAllItems();
        employee.setWidth("100%");
        employee.setNullSelectionAllowed(false);   
        employee.addContainerProperty("y", String.class, "");
        employee.setItemCaptionPropertyId("y");
        
        Item i;
        for(Employee e : es.getEmployeePerBranchForDropDownList(branchId)){
            String name = e.getLastname()+ ", " + e.getFirstname() + " " + e.getMiddlename();
            i = employee.addItem(e.getEmployeeId());
            i.getItemProperty("y").setValue(name.toUpperCase());
        }
        
        employee.setImmediate(true);
    }
    
}
