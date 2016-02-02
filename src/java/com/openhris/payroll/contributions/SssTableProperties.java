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
public class SssTableProperties extends Table {

    private int corporateId;
    private int month;
    private int year;

    public SssTableProperties() {
        setWidth("900px");
        setHeight("100%");
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        setContainerDataSource(new SssDataContainer());
        
        setColumnHeader("employeeId", "Employee ID.");
        setColumnHeader("sssNo", "SSS No.");
        setColumnHeader("eeShare", "Employee Share");
        setColumnHeader("erShare", "Employer Share");
        
        setColumnWidth("name", 250);
        setColumnAlignment("eeShare", Table.ALIGN_RIGHT);
        setColumnAlignment("erShare", Table.ALIGN_RIGHT);
        setColumnAlignment("ec", Table.ALIGN_RIGHT);
    }
        
    public SssTableProperties(int corporateId, 
            int month, 
            int year) {
        this.corporateId = corporateId;
        this.month = month;
        this.year = year;
        
        setWidth("900px");
        setHeight("100%");
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        setContainerDataSource(new SssDataContainer(corporateId, month, year));
        
        setColumnHeader("employeeId", "Employee ID.");
        setColumnHeader("sssNo", "SSS No.");
        setColumnHeader("eeShare", "Employee Share");
        setColumnHeader("erShare", "Employer Share");
        
        setColumnWidth("name", 250);
        setColumnAlignment("eeShare", Table.ALIGN_RIGHT);
        setColumnAlignment("erShare", Table.ALIGN_RIGHT);
        setColumnAlignment("ec", Table.ALIGN_RIGHT);
    }
    
    int getCorporateId(){
        return corporateId;
    }
    
}
