/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee.containers;

import com.openhris.model.EmployeeSummary;
import com.openhris.service.EmployeeService;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

/**
 *
 * @author jetdario
 */
public class EmployeeSummaryDataContainer extends IndexedContainer {

    EmployeeService es = new EmployeeServiceImpl();
    
    private int corporateId;
    
    public EmployeeSummaryDataContainer() {
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);      
        addContainerProperty("position", String.class, null);
        addContainerProperty("branch", String.class, null);
    }

    public EmployeeSummaryDataContainer(int corporateId) {
        this.corporateId = corporateId;
        
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);      
        addContainerProperty("position", String.class, null);
        addContainerProperty("branch", String.class, null);
        
        for(EmployeeSummary esum : es.findAllEmployeeSummaryByCorporateId(corporateId)){
            Item item = getItem(addItem());
            item.getItemProperty("employeeId").setValue(esum.getEmployeeId());
            item.getItemProperty("name").setValue(esum.getEmployeeName().toUpperCase());
            if(esum.getPosition() == null || esum.getPosition().isEmpty()){
                item.getItemProperty("position").setValue(esum.getPosition());
            } else {
                item.getItemProperty("position").setValue(esum.getPosition().toUpperCase());  
            }            
            item.getItemProperty("branch").setValue(esum.getBranch());
        }
    }
    
}
