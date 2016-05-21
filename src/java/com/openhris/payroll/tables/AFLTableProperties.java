/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.tables;

import com.openhris.payroll.containers.AFLContainer;
import com.openhris.payroll.containers.TaxDataContainer;
import com.vaadin.ui.Table;
import java.util.Date;

/**
 *
 * @author jetdario
 */
public class AFLTableProperties extends Table {

    private int corporateId;
    private Date payrollDate;
    
    
    public AFLTableProperties() {
        setWidth("900px");
        setHeight("100%");
        setImmediate(true);
        setSelectable(true);
        addStyleName("hris-table-layout");
        
        setContainerDataSource(new AFLContainer());        
    }

    public AFLTableProperties(int corporateId, Date payrollDate) {
        this.corporateId = corporateId;
        this.payrollDate = payrollDate;
        
        setContainerDataSource(new AFLContainer(corporateId, payrollDate));  
    }
    
}
