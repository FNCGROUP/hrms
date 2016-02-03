/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.contributions;

import com.vaadin.ui.Table;
import java.util.Date;

/**
 *
 * @author jetdario
 */
public class TaxTableProperties extends Table {

    private int corporateId;
    private Date payrollDate;
    
    public TaxTableProperties() {
        setWidth("900px");
        setHeight("100%");
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        setContainerDataSource(new TaxDataContainer());
        
        setColumnHeader("employeeId", "Employee ID.");
        setColumnHeader("hdmfNo", "Hdmf No.");
        setColumnHeader("eeHdmf", "Employee Share");
        setColumnHeader("erHdmf", "Employer Share");
        
        setColumnWidth("name", 250);
        setColumnAlignment("eeHdmf", Table.ALIGN_RIGHT);
        setColumnAlignment("erHdmf", Table.ALIGN_RIGHT);
    }

    public TaxTableProperties(int corporateId, Date payrollDate) {
        this.corporateId = corporateId;
        this.payrollDate = payrollDate;
        
        setWidth("900px");
        setHeight("100%");
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        setContainerDataSource(new TaxDataContainer(corporateId, payrollDate));
        
        setColumnHeader("employeeId", "Employee ID.");
        setColumnHeader("hdmfNo", "Hdmf No.");
        setColumnHeader("eeHdmf", "Employee Share");
        setColumnHeader("erHdmf", "Employer Share");
        
        setColumnWidth("name", 250);
        setColumnAlignment("eeHdmf", Table.ALIGN_RIGHT);
        setColumnAlignment("erHdmf", Table.ALIGN_RIGHT);
    }
    
}
