/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TabSheet;

/**
 *
 * @author jet
 */
public class PayrollComponentContainer {
        
    public PayrollComponentContainer(){
    }
    
    public ComponentContainer payrollComponentContainer(int branchId){
        PayrollMainUI payrollMainUI = new PayrollMainUI(branchId);
        
        TabSheet ts = new TabSheet();
        ts.setSizeFull();
        ts.addStyleName("bar");
        
        GridLayout payrollMenuGrid = new GridLayout();
        payrollMenuGrid.setSizeFull();
        payrollMenuGrid.setCaption("Payroll");
        payrollMenuGrid.addComponent(payrollMainUI);
        ts.addComponent(payrollMenuGrid);
        
        payrollMenuGrid = new GridLayout();
        payrollMenuGrid.setSizeFull();
        payrollMenuGrid.setCaption("Payroll Register");
//        payrollMenuGrid.addComponent(payrollRegisterModule);
        ts.addComponent(payrollMenuGrid);
        
        payrollMenuGrid = new GridLayout();
        payrollMenuGrid.setSizeFull();
        payrollMenuGrid.setCaption("201 Summary");
//        payrollMenuGrid.addComponent(form201);
        ts.addComponent(payrollMenuGrid);
        
        return ts;
    }
    
}
