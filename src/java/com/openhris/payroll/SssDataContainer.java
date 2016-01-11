/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll;

import com.openhris.model.SssSchedule;
import com.openhris.service.ContributionService;
import com.openhris.serviceprovider.ContributionServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

/**
 *
 * @author jetdario
 */
public class SssDataContainer extends IndexedContainer {

    ContributionService cs = new ContributionServiceImpl();
    
    private int corporateId;
    private int month;
    private int year;

    public SssDataContainer() {
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);        
        addContainerProperty("employer share", Double.class, null); 
        addContainerProperty("EC", Double.class, null);
    }
        
    public SssDataContainer(int corporateId, 
            int month, 
            int year) {
        this.corporateId = corporateId;
        this.month = month;
        this.year = year;
        
        addContainerProperty("employeeId", String.class, null); 
        addContainerProperty("name", String.class, null);        
        addContainerProperty("employer share", Double.class, null); 
        addContainerProperty("EC", Double.class, null);
        
        for(SssSchedule s : cs.getSssEmployerShare(corporateId, month, year)){
            Item item = getItem(addItem());
            item.getItemProperty("employeeId").setValue(s.getEmployeeId());
            item.getItemProperty("name").setValue(s.getName().toUpperCase());
            item.getItemProperty("employer share").setValue(s.getErShare());
            item.getItemProperty("EC").setValue(s.getEc());
        }
    }
    
}
