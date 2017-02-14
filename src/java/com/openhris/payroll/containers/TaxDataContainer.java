/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.containers;

import com.openhris.model.TaxSchedule;
import com.openhris.service.ContributionService;
import com.openhris.serviceprovider.ContributionServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import java.util.Date;

/**
 *
 * @author jetdario
 */
public class TaxDataContainer extends IndexedContainer {

    ContributionService cs = new ContributionServiceImpl();
    
    private int corporateId;
    private Date payrollDate;
    
    public TaxDataContainer() {
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);      
        addContainerProperty("tinNo", String.class, null);
        addContainerProperty("amount", Double.class, null);
        addContainerProperty("corporate", String.class, null);
        addContainerProperty("branch", String.class, null);
    }

    public TaxDataContainer(int corporateId, Date payrollDate) {
        this.corporateId = corporateId;
        this.payrollDate = payrollDate;
        
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);      
        addContainerProperty("tinNo", String.class, null);
        addContainerProperty("amount", Double.class, null);
        addContainerProperty("corporate", String.class, null);
        addContainerProperty("branch", String.class, null);
        
        for(TaxSchedule ts : cs.getTaxContribution(corporateId, payrollDate)){
            Item item = getItem(addItem());
            item.getItemProperty("employeeId").setValue(ts.getEmployeeId());
            item.getItemProperty("name").setValue(ts.getEmployeeName().toUpperCase());
            item.getItemProperty("tinNo").setValue(ts.getTinNo());
            item.getItemProperty("amount").setValue(ts.getTaxAmount());
            item.getItemProperty("corporate").setValue(ts.getCorporateName());
            item.getItemProperty("branch").setValue(ts.getBranchName());
        }
    }
    
}
