/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee.tables;

import com.openhris.employee.containers.EmployeeSummaryDataContainer;
import com.vaadin.ui.Table;

/**
 *
 * @author jetdario
 */
public class EmployeeSummaryTableProperties extends Table {

    private int corporateId;
    
    public EmployeeSummaryTableProperties() {
        setWidth("800px");
        setHeight("100%");
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        setContainerDataSource(new EmployeeSummaryDataContainer());
        
        setColumnAlignment("salary", Table.ALIGN_RIGHT);
        setColumnAlignment("entry", Table.ALIGN_CENTER);
    }

    public EmployeeSummaryTableProperties(int corporateId) {
        this.corporateId = corporateId;
        
        setWidth("900px");
        setHeight("100%");
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        setContainerDataSource(new EmployeeSummaryDataContainer(corporateId));
        
        setColumnAlignment("salary", Table.ALIGN_RIGHT);
        setColumnAlignment("entry", Table.ALIGN_CENTER);
    }
}
