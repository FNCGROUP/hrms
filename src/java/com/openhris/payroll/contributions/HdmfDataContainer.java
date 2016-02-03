/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.contributions;

import com.openhris.model.HdmfSchedule;
import com.openhris.service.ContributionService;
import com.openhris.serviceprovider.ContributionServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

/**
 *
 * @author jetdario
 */
public class HdmfDataContainer extends IndexedContainer {

    ContributionService cs = new ContributionServiceImpl();
    
    private int corporateId;
    private int month;
    private int year;
    
    public HdmfDataContainer() {
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);      
        addContainerProperty("hdmfNo", String.class, null);
        addContainerProperty("eeHdmf", Double.class, null);
        addContainerProperty("erHdmf", Double.class, null); 
        addContainerProperty("branch", String.class, null);
    }

    public HdmfDataContainer(int corporateId, int month, int year) {
        this.corporateId = corporateId;
        this.month = month;
        this.year = year;
        
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);      
        addContainerProperty("hdmfNo", String.class, null);
        addContainerProperty("eeHdmf", Double.class, null);
        addContainerProperty("erHdmf", Double.class, null); 
        addContainerProperty("branch", String.class, null);
        
        for(HdmfSchedule hs : cs.getHdmfContribution(corporateId, month, year)){
            Item item = getItem(addItem());
            item.getItemProperty("employeeId").setValue(hs.getEmployeeId());
            item.getItemProperty("name").setValue(hs.getEmployeeName().toUpperCase());
            item.getItemProperty("hdmfNo").setValue(hs.getHdmfNo());
            item.getItemProperty("eeHdmf").setValue(hs.getEeHdmf());
            item.getItemProperty("erHdmf").setValue(hs.getErHdmf());
            item.getItemProperty("branch").setValue(hs.getBranchName());
        }
    }

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
