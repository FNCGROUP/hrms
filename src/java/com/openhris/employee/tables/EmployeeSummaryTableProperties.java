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
        setWidth("100%");
        setHeight("100%");
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        setColumnHeader("employmentStatus", "EMPLOYMENT STATUS");
        setColumnHeader("employmentWageStatus", "WAGE STATUS");
        setColumnHeader("wageEntry", "WAGE ENTRY");
        setColumnHeader("perDiem", "PER DIEM");
        setColumnHeader("hdmfNo", "HDMF #");
        setColumnHeader("otherAllowance", "OTHER ALLOWANCE");
        setColumnHeader("entryDate", "ENTRY DATE");
        setColumnHeader("civilStatus", "CIVIL STATUS");
        setColumnHeader("hdmfNo", "HDMF #");
        setColumnHeader("sssNo", "SSS #");
        setColumnHeader("tinNo", "TIN #");
        
        setContainerDataSource(new EmployeeSummaryDataContainer());
        
        setColumnAlignment("salary", Table.ALIGN_RIGHT);
        setColumnAlignment("entry", Table.ALIGN_CENTER);
    }

    public EmployeeSummaryTableProperties(int corporateId) {
        this.corporateId = corporateId;
        
        setWidth("100%");
        setHeight("100%");
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        setColumnHeader("employmentStatus", "EMPLOYMENT STATUS");
        setColumnHeader("employmentWageStatus", "WAGE STATUS");
        setColumnHeader("wageEntry", "WAGE ENTRY");
        setColumnHeader("perDiem", "PER DIEM");
        setColumnHeader("hdmfNo", "HDMF #");
        setColumnHeader("otherAllowance", "OTHER ALLOWANCE");
        setColumnHeader("entryDate", "ENTRY DATE");
        setColumnHeader("civilStatus", "CIVIL STATUS");
        setColumnHeader("hdmfNo", "HDMF #");
        setColumnHeader("sssNo", "SSS #");
        setColumnHeader("tinNo", "TIN #");
        
        setContainerDataSource(new EmployeeSummaryDataContainer(corporateId));
        
        setColumnAlignment("salary", Table.ALIGN_RIGHT);
        setColumnAlignment("entry", Table.ALIGN_CENTER);
    }
}
