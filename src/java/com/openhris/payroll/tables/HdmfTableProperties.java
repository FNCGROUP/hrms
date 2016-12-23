/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.tables;

import com.openhris.payroll.containers.HdmfDataContainer;
import com.vaadin.ui.Table;

/**
 *
 * @author jetdario
 */
public class HdmfTableProperties extends Table {

    private int corporateId;
    private int month;
    private int year;
    
    public HdmfTableProperties() {
        setWidth("900px");
        setHeight("100%");
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        setContainerDataSource(new HdmfDataContainer());
        
        setColumnHeader("employeeId", "Employee ID.");
        setColumnHeader("hdmfNo", "Hdmf No.");
        setColumnHeader("eeHdmf", "Employee Share");
        setColumnHeader("erHdmf", "Employer Share");
        
        setColumnWidth("name", 250);
        setColumnAlignment("eeHdmf", Table.ALIGN_RIGHT);
        setColumnAlignment("erHdmf", Table.ALIGN_RIGHT);
    }

//    public HdmfTableProperties(int corporateId, int month, int year) {
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
//        setContainerDataSource(new HdmfDataContainer(corporateId, month, year));
//        
//        setColumnHeader("employeeId", "Employee ID.");
//        setColumnHeader("hdmfNo", "Hdmf No.");
//        setColumnHeader("eeHdmf", "Employee Share");
//        setColumnHeader("erHdmf", "Employer Share");
//        
//        setColumnWidth("name", 250);
//        setColumnAlignment("eeHdmf", Table.ALIGN_RIGHT);
//        setColumnAlignment("erHdmf", Table.ALIGN_RIGHT);
//    }

    public int getCorporateId() {
        return corporateId;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }
    
}
