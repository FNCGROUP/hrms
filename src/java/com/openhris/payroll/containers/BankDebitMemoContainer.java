/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.containers;

import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.BankDebitMemo;
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
        addContainerProperty("corporate", String.class, null);
        addContainerProperty("branch", String.class, null);
        addContainerProperty("payrollDate", String.class, null);
    }
    
    public BankDebitMemoContainer(int corporateId, String payrollDate) {
        addContainerProperty("employeeId", String.class, "");
        addContainerProperty("bankAccountNo", String.class, "");
        addContainerProperty("name", String.class, "");
        addContainerProperty("amount", Double.class, "");
        addContainerProperty("corporate", String.class, null);
        addContainerProperty("branch", String.class, null);
        addContainerProperty("payrollDate", String.class, null);
        
        for(BankDebitMemo dbm : es.findBankDebitMemo(corporateId, payrollDate)){
            Item item = getItem(addItem());
            item.getItemProperty("employeeId").setValue(dbm.getEmployeeId());
            item.getItemProperty("bankAccountNo").setValue(dbm.getBankAccountNo());
            item.getItemProperty("name").setValue(dbm.getLastname().toUpperCase()+", "+dbm.getFirstname().toUpperCase()+" "+dbm.getMiddlename().toUpperCase());
            item.getItemProperty("amount").setValue(util.roundOffToTwoDecimalPlaces(dbm.getAmount()));  
            item.getItemProperty("corporate").setValue(dbm.getCorporateName());
            item.getItemProperty("branch").setValue(dbm.getBranch());
            item.getItemProperty("payrollDate").setValue(util.convertDateFormat(dbm.getPayrollDate().toString()));
        }
    }
    
}
