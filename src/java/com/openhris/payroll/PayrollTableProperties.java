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
public class PayrollTableProperties extends Table {

    public PayrollTableProperties() {
        setSizeFull();
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        addContainerProperty("id", String.class, null);        
        addContainerProperty("start date", String.class, null);
        addContainerProperty("cut-off date", String.class, null);        
        addContainerProperty("basic salary", Double.class, null); 
        addContainerProperty("half-month salary", Double.class, null);
        addContainerProperty("overtime pay", Double.class, null);
        addContainerProperty("legal holiday", Double.class, null); 
        addContainerProperty("special holiday", Double.class, null); 
        addContainerProperty("night differential", Double.class, null); 
        addContainerProperty("wdo", Double.class, null);   
        addContainerProperty("absences", Double.class, null); 
        addContainerProperty("lates", Double.class, null);   
        addContainerProperty("undertime", Double.class, null); 
        addContainerProperty("gross pay", Double.class, null); 
        addContainerProperty("sss", Double.class, null); 
        addContainerProperty("phic", Double.class, null);
        addContainerProperty("hdmf", Double.class, null);  
        addContainerProperty("tax", Double.class, null); 
        addContainerProperty("net pay", Double.class, null);  
        addContainerProperty("cash bond", Double.class, null);
        addContainerProperty("communication", Double.class, null); 
        addContainerProperty("per diem", Double.class, null); 
        addContainerProperty("cola", Double.class, null); 
        addContainerProperty("meal", Double.class, null); 
        addContainerProperty("transportation", Double.class, null); 
        addContainerProperty("others", Double.class, null);   
        addContainerProperty("afl", Double.class, null);         
        addContainerProperty("advances to o/e", Double.class, null);  
        addContainerProperty("adjustments", Double.class, null); 
        addContainerProperty("amount to be receive", Double.class, null); 
        addContainerProperty("amount received", Double.class, null); 
        addContainerProperty("for adjustments", Double.class, null);
        addContainerProperty("payroll period", String.class, null);
        addContainerProperty("payroll date", String.class, null);
        addContainerProperty("status", String.class, null); 
        addContainerProperty("trade", String.class, null);
        addContainerProperty("branch", String.class, null);
        
        setColumnAlignment("basic salary", Table.ALIGN_RIGHT);
        setColumnAlignment("half-month salary", Table.ALIGN_RIGHT);
        setColumnAlignment("overtime pay", Table.ALIGN_RIGHT);
        setColumnAlignment("legal holiday", Table.ALIGN_RIGHT); 
        setColumnAlignment("special holiday", Table.ALIGN_RIGHT);
        setColumnAlignment("night differential", Table.ALIGN_RIGHT);
        setColumnAlignment("wdo", Table.ALIGN_RIGHT);
        setColumnAlignment("absences", Table.ALIGN_RIGHT);
        setColumnAlignment("lates", Table.ALIGN_RIGHT);
        setColumnAlignment("undertime", Table.ALIGN_RIGHT);
        setColumnAlignment("gross pay", Table.ALIGN_RIGHT);
        setColumnAlignment("sss", Table.ALIGN_RIGHT);
        setColumnAlignment("phic", Table.ALIGN_RIGHT); 
        setColumnAlignment("hdmf", Table.ALIGN_RIGHT);
        setColumnAlignment("tax", Table.ALIGN_RIGHT);  
        setColumnAlignment("net pay", Table.ALIGN_RIGHT);
        setColumnAlignment("cash bond", Table.ALIGN_RIGHT);
        setColumnAlignment("communication", Table.ALIGN_RIGHT);
        setColumnAlignment("per diem", Table.ALIGN_RIGHT);
        setColumnAlignment("cola", Table.ALIGN_RIGHT);
        setColumnAlignment("meal", Table.ALIGN_RIGHT);
        setColumnAlignment("transportation", Table.ALIGN_RIGHT);
        setColumnAlignment("others", Table.ALIGN_RIGHT);   
        setColumnAlignment("afl", Table.ALIGN_RIGHT);               
        setColumnAlignment("advances to o/e", Table.ALIGN_RIGHT);
        setColumnAlignment("adjustments", Table.ALIGN_RIGHT);
        setColumnAlignment("amount to be receive", Table.ALIGN_RIGHT);
        setColumnAlignment("amount received", Table.ALIGN_RIGHT);
        setColumnAlignment("for adjustments", Table.ALIGN_RIGHT);
    }
    
}
