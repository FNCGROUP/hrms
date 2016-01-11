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
public class SssTableProperties extends Table {

    private int corporateId;
    private int month;
    private int year;

    public SssTableProperties() {
        setWidth("600px");
        setHeight("100%");
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        setContainerDataSource(new SssDataContainer());
        
        setColumnWidth("name", 250);
        setColumnAlignment("employer share", Table.ALIGN_RIGHT);
        setColumnAlignment("EC", Table.ALIGN_RIGHT);
    }
        
    public SssTableProperties(int corporateId, 
            int month, 
            int year) {
        this.corporateId = corporateId;
        this.month = month;
        this.year = year;
        
        setWidth("600px");
        setHeight("100%");
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        setContainerDataSource(new SssDataContainer(corporateId, month, year));
        
        setColumnWidth("name", 250);
        setColumnAlignment("employer share", Table.ALIGN_RIGHT);
        setColumnAlignment("EC", Table.ALIGN_RIGHT);
    }
    
    int getCorporateId(){
        return corporateId;
    }
    
}
