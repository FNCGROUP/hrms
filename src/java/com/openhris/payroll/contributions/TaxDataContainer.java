/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.contributions;

import com.openhris.model.TaxSchedule;
import com.openhris.service.ContributionService;
import com.openhris.serviceprovider.ContributionServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

/**
 *
 * @author jetdario
 */
public class TaxDataContainer extends IndexedContainer {

    ContributionService cs = new ContributionServiceImpl();
    
    private int corporateId;
    private int month;
    private int year;
    
    public TaxDataContainer() {
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);      
        addContainerProperty("tinNo", String.class, null);
        addContainerProperty("amount", Double.class, null);
        addContainerProperty("branch", String.class, null);
    }

    public TaxDataContainer(int corporateId, int month, int year) {
        this.corporateId = corporateId;
        this.month = month;
        this.year = year;
        
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);      
        addContainerProperty("tinNo", String.class, null);
        addContainerProperty("amount", Double.class, null);
        addContainerProperty("branch", String.class, null);
        
        for(TaxSchedule ts : cs.getTaxContribution(corporateId, month, year)){
            Item item = getItem(addItem());
            item.getItemProperty("employeeId").setValue(ts.getEmployeeId());
            item.getItemProperty("name").setValue(ts.getEmployeeName().toUpperCase());
            item.getItemProperty("tinNo").setValue(ts.getTinNo());
            item.getItemProperty("amount").setValue(ts.getTaxAmount());
            item.getItemProperty("branch").setValue(ts.getBranchName());
        }
    }
    
}
