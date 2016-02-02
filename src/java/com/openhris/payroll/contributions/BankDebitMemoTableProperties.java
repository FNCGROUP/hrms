/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.contributions;

import com.vaadin.ui.Table;

/**
 *
 * @author jetdario
 */
public class BankDebitMemoTableProperties extends Table {

    public BankDebitMemoTableProperties() {
        setWidth("700px");
        setHeight("100%");
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        setContainerDataSource(new BankDebitMemoContainer());
        
        setColumnWidth("name", 250);
        setColumnWidth("amount", 100);
        setColumnAlignment("amount", Table.ALIGN_RIGHT);
    }
    
    public BankDebitMemoTableProperties(int branchId, String payrollDate) {
        setWidth("700px");
        setHeight("100%");
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        setContainerDataSource(new BankDebitMemoContainer(branchId, payrollDate));
        
        setColumnWidth("name", 250);
        setColumnWidth("amount", 100);
        setColumnAlignment("amount", Table.ALIGN_RIGHT);
    }
    
}
