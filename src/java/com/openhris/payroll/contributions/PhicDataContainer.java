/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.contributions;

import com.openhris.model.PhicSchedule;
import com.openhris.service.ContributionService;
import com.openhris.serviceprovider.ContributionServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

/**
 *
 * @author jetdario
 */
public class PhicDataContainer extends IndexedContainer {

    ContributionService cs = new ContributionServiceImpl();
    
    private int corporateId;
    private int month;
    private int year;

    public PhicDataContainer() {
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);      
        addContainerProperty("phicNo", String.class, null);
        addContainerProperty("eePhic", Double.class, null);
        addContainerProperty("erPhic", Double.class, null); 
        addContainerProperty("branch", String.class, null);
    }
    
    public PhicDataContainer(int corporateId, 
            int month, 
            int year) {
        this.corporateId = corporateId;
        this.month = month;
        this.year = year;
        
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);      
        addContainerProperty("phicNo", String.class, null);
        addContainerProperty("eePhic", Double.class, null);
        addContainerProperty("erPhic", Double.class, null); 
        addContainerProperty("branch", String.class, null);
        
        for(PhicSchedule ps : cs.getPhicContribution(corporateId, month, year)){
            Item item = getItem(addItem());
            item.getItemProperty("employeeId").setValue(ps.getEmployeeId());
            item.getItemProperty("name").setValue(ps.getEmployeeName());
            item.getItemProperty("phicNo").setValue(ps.getPhicNo());
            item.getItemProperty("eePhic").setValue(ps.getEePhic());
            item.getItemProperty("erPhic").setValue(ps.getErPhic());
            item.getItemProperty("branch").setValue(ps.getBranchName());
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
