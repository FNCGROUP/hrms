/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.contributions;

import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.EmploymentInformation;
import com.openhris.service.EmployeeService;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

/**
 *
 * @author jetdario
 */
public class BankDebitMemoContainer extends IndexedContainer {

    EmployeeService es = new EmployeeServiceImpl();
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public BankDebitMemoContainer() {
        addContainerProperty("employeeId", String.class, "");
        addContainerProperty("bankAccountNo", String.class, "");
        addContainerProperty("name", String.class, "");
        addContainerProperty("amount", Double.class, "");        
    }
    
    public BankDebitMemoContainer(int branchId, String payrollDate) {
        addContainerProperty("employeeId", String.class, "");
        addContainerProperty("bankAccountNo", String.class, "");
        addContainerProperty("name", String.class, "");
        addContainerProperty("amount", Double.class, "");
        
        for(EmploymentInformation ei : es.findBankDebitMemo(branchId, payrollDate)){
            Item item = getItem(addItem());
            item.getItemProperty("employeeId").setValue(ei.getEmployeeId());
            item.getItemProperty("bankAccountNo").setValue(ei.getBankAccountNo());
            item.getItemProperty("name").setValue(ei.getLastname().toUpperCase()+", "+ei.getFirstname().toUpperCase()+" "+ei.getMiddlename().toUpperCase());
            item.getItemProperty("amount").setValue(util.roundOffToTwoDecimalPlaces(ei.getEmploymentWage()));            
        }
    }
    
}
