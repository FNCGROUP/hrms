/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.containers;

import com.openhris.model.Employee;
import com.openhris.model.EmploymentInformation;
import com.openhris.model.HdmfSchedule;
import com.openhris.service.ContributionService;
import com.openhris.service.EmployeeService;
import com.openhris.serviceprovider.ContributionServiceImpl;
import com.openhris.serviceprovider.EmployeeServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jetdario
 */
public class HdmfDataContainer extends IndexedContainer {

    ContributionService cs = new ContributionServiceImpl();
    EmployeeService es = new EmployeeServiceImpl();
    
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
        addContainerProperty("date", String.class, null);
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
        addContainerProperty("date", String.class, null);
        
        for(HdmfSchedule hs : cs.getHdmfContribution(corporateId, month, year)){
            EmploymentInformation ei = es.findEmployeeById(hs.getEmployeeId());
            
            Item item = getItem(addItem());
            item.getItemProperty("employeeId").setValue(hs.getEmployeeId());
            item.getItemProperty("name").setValue(hs.getEmployeeName().toUpperCase());
            item.getItemProperty("hdmfNo").setValue(hs.getHdmfNo());
            item.getItemProperty("eeHdmf").setValue(hs.getEeHdmf());
            
            if(ei.getEmploymentWageStatus().equals("minimum")){
                item.getItemProperty("erHdmf").setValue(100);
            } else {
                item.getItemProperty("erHdmf").setValue(hs.getErHdmf());
            }            
            
            item.getItemProperty("branch").setValue(hs.getBranchName());
            item.getItemProperty("date").setValue(getMonthString().get(month)+" "+year);
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
    
    Map<Integer, String> getMonthString(){
        Map<Integer, String> m = new HashMap<>();
        m.put(1, "January");
        m.put(2, "February");
        m.put(3, "March");
        m.put(4, "April");
        m.put(5, "May");
        m.put(6, "June");
        m.put(7, "July");
        m.put(8, "August");
        m.put(9, "September");
        m.put(10, "October");
        m.put(11, "November");
        m.put(12, "December");
        return m;
    }
}
