/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.vaadin.ui.Table;

/**
 *
 * @author jetdario
 */
public class PayrollRegisterTable extends Table {

    public PayrollRegisterTable() {
        setSizeFull();
        setImmediate(true);
        setSelectable(true);
        setColumnCollapsingAllowed(true);
        addStyleName("employees-table-layout");
        
        addContainerProperty("id", String.class, null);
        addContainerProperty("name", String.class, null);
        addContainerProperty("no. of days", Integer.class, null);        
        addContainerProperty("rate per day", Double.class, null);        
        addContainerProperty("basic salary", Double.class, null);        
        addContainerProperty("half-month salary", Double.class, null);        
        addContainerProperty("overtime pay", Double.class, null);        
        addContainerProperty("legal holiday", Double.class, null);        
        addContainerProperty("special holiday", Double.class, null);        
        addContainerProperty("night differential", Double.class, null);        
        addContainerProperty("wdo", Double.class, null);        
        addContainerProperty("absent", Double.class, null);        
        addContainerProperty("lates", Double.class, null);        
        addContainerProperty("undertime", Double.class, null);        
        addContainerProperty("gross pay", Double.class, null);         
        addContainerProperty("sss", Double.class, null);        
        addContainerProperty("phic", Double.class, null);        
        addContainerProperty("hdmf", Double.class, null);        
        addContainerProperty("tax", Double.class, null);        
        addContainerProperty("net pay", Double.class, null);        
        addContainerProperty("meal allowance", Double.class, null);        
        addContainerProperty("allowance for liquidation", Double.class, null);        
        addContainerProperty("advances to o/e", Double.class, null);         
        addContainerProperty("adjustments", Double.class, null);        
        addContainerProperty("amount to be receive", Double.class, null);        
        addContainerProperty("amount received", Double.class, null);  
	addContainerProperty("for adjustments", Double.class, null);
        
        setColumnAlignment("no. of days", Table.ALIGN_CENTER);
        setColumnAlignment("rate per day", Table.ALIGN_RIGHT);
        setColumnAlignment("basic salary", Table.ALIGN_RIGHT);
        setColumnAlignment("adjustments", Table.ALIGN_RIGHT);
        setColumnAlignment("half-month salary", Table.ALIGN_RIGHT);
        setColumnAlignment("overtime pay", Table.ALIGN_RIGHT);
        setColumnAlignment("legal holiday", Table.ALIGN_RIGHT);
        setColumnAlignment("special holiday", Table.ALIGN_RIGHT);
        setColumnAlignment("night differential", Table.ALIGN_RIGHT);
        setColumnAlignment("wdo", Table.ALIGN_RIGHT);
        setColumnAlignment("absent", Table.ALIGN_RIGHT);
        setColumnAlignment("lates", Table.ALIGN_RIGHT);
        setColumnAlignment("undertime", Table.ALIGN_RIGHT);
        setColumnAlignment("gross pay", Table.ALIGN_RIGHT);
        setColumnAlignment("sss", Table.ALIGN_RIGHT);
        setColumnAlignment("phic", Table.ALIGN_RIGHT);
        setColumnAlignment("hdmf", Table.ALIGN_RIGHT);
        setColumnAlignment("tax", Table.ALIGN_RIGHT);
        setColumnAlignment("net pay", Table.ALIGN_RIGHT);
        setColumnAlignment("meal allowance", Table.ALIGN_RIGHT);
        setColumnAlignment("allowance for liquidation", Table.ALIGN_RIGHT);
        setColumnAlignment("advances to o/e", Table.ALIGN_RIGHT);
        setColumnAlignment("amount to be receive", Table.ALIGN_RIGHT);
        setColumnAlignment("amount received", Table.ALIGN_RIGHT);
	setColumnAlignment("for adjustments", Table.ALIGN_RIGHT);
    }
    
}
