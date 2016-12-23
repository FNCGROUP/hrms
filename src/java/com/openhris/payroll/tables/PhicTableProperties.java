/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.tables;

import com.openhris.payroll.containers.PhicDataContainer;
import com.vaadin.ui.Table;

/**
 *
 * @author jetdario
 */
public class PhicTableProperties extends Table  {

    private int corporateId;
    private int month;
    private int year;
    
    public PhicTableProperties() {
        setWidth("900px");
        setHeight("100%");
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        setContainerDataSource(new PhicDataContainer());
        
        setColumnHeader("employeeId", "Employee ID.");
        setColumnHeader("phicNo", "Phic No.");
        setColumnHeader("eePhic", "Employee Share");
        setColumnHeader("erPhic", "Employer Share");
        
        setColumnWidth("name", 250);
        setColumnAlignment("eePhic", Table.ALIGN_RIGHT);
        setColumnAlignment("erPhic", Table.ALIGN_RIGHT);
    }

//    public PhicTableProperties(int corporateId, int month, int year) {
//        this.corporateId = corporateId;
//        this.month = month;
//        this.year = year;
//        
//        setWidth("900px");
//        setHeight("100%");
//        setImmediate(true);
//        setSelectable(true);
//        addStyleName("hris-table-layout");
//        
//        setContainerDataSource(new PhicDataContainer(corporateId, month, year));
//        
//        setColumnHeader("employeeId", "Employee ID.");
//        setColumnHeader("phicNo", "Phic No.");
//        setColumnHeader("eePhic", "Employee Share");
//        setColumnHeader("erPhic", "Employer Share");
//        
//        setColumnWidth("name", 250);
//        setColumnAlignment("eePhic", Table.ALIGN_RIGHT);
//        setColumnAlignment("erPhic", Table.ALIGN_RIGHT);
//    }
    
}
