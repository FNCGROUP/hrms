/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee.containers;

import com.openhris.commons.OpenHrisUtilities;
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

    OpenHrisUtilities util = new OpenHrisUtilities();
    EmployeeService es = new EmployeeServiceImpl();
    
    private int corporateId;
    
    public EmployeeSummaryDataContainer() {
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);     
        addContainerProperty("employmentStatus", String.class, null);
        addContainerProperty("position", String.class, null);
        addContainerProperty("branch", String.class, null);
        addContainerProperty("department", String.class, null);
        addContainerProperty("corporate", String.class, null);
        addContainerProperty("employmentWageStatus", String.class, null);
        addContainerProperty("wageEntry", String.class, null);
        addContainerProperty("salary", String.class, null);
        addContainerProperty("afl", Double.class, null);
        addContainerProperty("perDiem", Double.class, null);
        addContainerProperty("transportation", Double.class, null);
        addContainerProperty("communication", Double.class, null);
        addContainerProperty("otherAllowance", Double.class, null);
        addContainerProperty("cola", Double.class, null);
        addContainerProperty("meal", Double.class, null);
        addContainerProperty("entryDate", String.class, null);
        addContainerProperty("dob", String.class, null);
        addContainerProperty("gender", String.class, null);
        addContainerProperty("civilStatus", String.class, null);
        addContainerProperty("dependent", String.class, null);
        addContainerProperty("hdmfNo", String.class, null);
        addContainerProperty("sssNo", String.class, null);
        addContainerProperty("tinNo", String.class, null);
    }

    public EmployeeSummaryDataContainer(int corporateId) {
        this.corporateId = corporateId;
        
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);     
        addContainerProperty("employmentStatus", String.class, null);
        addContainerProperty("position", String.class, null);
        addContainerProperty("branch", String.class, null);
        addContainerProperty("department", String.class, null);
        addContainerProperty("corporate", String.class, null);
        addContainerProperty("employmentWageStatus", String.class, null);
        addContainerProperty("wageEntry", String.class, null);
        addContainerProperty("salary", String.class, null);
        addContainerProperty("afl", Double.class, null);
        addContainerProperty("perDiem", Double.class, null);
        addContainerProperty("transportation", Double.class, null);
        addContainerProperty("communication", Double.class, null);
        addContainerProperty("otherAllowance", Double.class, null);
        addContainerProperty("cola", Double.class, null);
        addContainerProperty("meal", Double.class, null);
        addContainerProperty("entryDate", String.class, null);
        addContainerProperty("dob", String.class, null);
        addContainerProperty("gender", String.class, null);
        addContainerProperty("civilStatus", String.class, null);
        addContainerProperty("dependent", String.class, null);
        addContainerProperty("hdmfNo", String.class, null);
        addContainerProperty("sssNo", String.class, null);
        addContainerProperty("tinNo", String.class, null);
        
        for(EmployeeSummary esum : es.findAllEmployeeSummaryByCorporateId(corporateId)){
            Item item = getItem(addItem());
            item.getItemProperty("employeeId").setValue(esum.getEmployeeId());
            item.getItemProperty("name").setValue(esum.getEmployeeName().toUpperCase());
            item.getItemProperty("employmentStatus").setValue(esum.getEmploymentStatus());
            if(esum.getPosition() == null || esum.getPosition().isEmpty()){
                item.getItemProperty("position").setValue(esum.getPosition());
            } else {
                item.getItemProperty("position").setValue(esum.getPosition().toUpperCase());  
            }            
            item.getItemProperty("branch").setValue(esum.getBranch());
            item.getItemProperty("department").setValue(esum.getDepartment());
            item.getItemProperty("corporate").setValue(esum.getCorporate());
            item.getItemProperty("employmentWageStatus").setValue(esum.getEmploymentWageStatus());
            item.getItemProperty("wageEntry").setValue(esum.getEmploymentWageEntry());
            item.getItemProperty("salary").setValue(esum.getEmploymentWage());
            item.getItemProperty("afl").setValue(esum.getAfl());
            item.getItemProperty("perDiem").setValue(esum.getPerDiem());
            item.getItemProperty("transportation").setValue(esum.getTransportation());
            item.getItemProperty("communication").setValue(esum.getCommunication());
            item.getItemProperty("otherAllowance").setValue(esum.getOtherAllowance());
            item.getItemProperty("cola").setValue(esum.getCola());
            item.getItemProperty("meal").setValue(esum.getMeal());
            item.getItemProperty("entryDate").setValue((esum.getEntryDate() == null) ? "" : util.convertDateFormat(esum.getEntryDate().toString()));
            item.getItemProperty("dob").setValue((esum.getDob()== null) ? "" : util.convertDateFormat(esum.getDob().toString()));
            item.getItemProperty("gender").setValue(esum.getGender());
            item.getItemProperty("civilStatus").setValue(esum.getCivilStatus());
            item.getItemProperty("dependent").setValue(esum.getDependent());
            item.getItemProperty("hdmfNo").setValue(esum.getHdmfNo());
            item.getItemProperty("sssNo").setValue(esum.getSssNo());
            item.getItemProperty("tinNo").setValue(esum.getTinNo());
        }
    }
    
}
