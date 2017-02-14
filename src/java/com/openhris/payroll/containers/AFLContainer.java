/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.containers;

import com.openhris.model.AFLSchedule;
import com.openhris.service.ContributionService;
import com.openhris.serviceprovider.ContributionServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import java.util.Date;

/**
 *
 * @author jetdario
 */
public class AFLContainer extends IndexedContainer {

    ContributionService cs = new ContributionServiceImpl();
    
    private int corporateId;
    private Date payrollDate;
    
    public AFLContainer() {
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);     
        addContainerProperty("amount", Double.class, null);
        addContainerProperty("corporate", String.class, null);
        addContainerProperty("branch", String.class, null);
    }

    public AFLContainer(int corporateId, Date payrollDate) {
        this.corporateId = corporateId;
        this.payrollDate = payrollDate;
        
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);  
        addContainerProperty("amount", Double.class, null);
        addContainerProperty("corporate", String.class, null);
        addContainerProperty("branch", String.class, null);
        
        for(AFLSchedule a : cs.findAFLByCompany(corporateId, payrollDate)){
            Item item = getItem(addItem());
            item.getItemProperty("employeeId").setValue(a.getEmployeeId());
            item.getItemProperty("name").setValue(a.getEmployeeName().toUpperCase());
            item.getItemProperty("amount").setValue(a.getAmount());
            item.getItemProperty("corporate").setValue(a.getCorporateName());
            item.getItemProperty("branch").setValue(a.getBranchName());
        }
    }

    public int getCorporateId() {
        return corporateId;
    }

    public Date getPayrollDate() {
        return payrollDate;
    }
    
}
