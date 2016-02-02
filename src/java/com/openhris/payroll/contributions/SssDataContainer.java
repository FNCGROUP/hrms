/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.contributions;

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
        addContainerProperty("sssNo", String.class, null);
        addContainerProperty("eeShare", Double.class, null);
        addContainerProperty("erShare", Double.class, null); 
        addContainerProperty("ec", Double.class, null);
        addContainerProperty("branch", String.class, null);
    }
        
    public SssDataContainer(int corporateId, 
            int month, 
            int year) {
        this.corporateId = corporateId;
        this.month = month;
        this.year = year;
        
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);      
        addContainerProperty("sssNo", String.class, null);
        addContainerProperty("eeShare", Double.class, null);
        addContainerProperty("erShare", Double.class, null); 
        addContainerProperty("ec", Double.class, null);
        addContainerProperty("branch", String.class, null);
        
        for(SssSchedule s : cs.getSssContribution(corporateId, month, year)){
            Item item = getItem(addItem());
            item.getItemProperty("employeeId").setValue(s.getEmployeeId());
            item.getItemProperty("name").setValue(s.getName().toUpperCase());
            item.getItemProperty("sssNo").setValue(s.getSssNo());
            item.getItemProperty("eeShare").setValue(s.getEeShare());
            item.getItemProperty("erShare").setValue(s.getErShare());
            item.getItemProperty("ec").setValue(s.getEc());
            item.getItemProperty("branch").setValue(s.getBranch());
        }
    }
    
}
