/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.payroll.containers;

import com.hrms.utilities.CommonUtil;
import com.openhris.model.PhicSchedule;
import com.openhris.service.ContributionService;
import com.openhris.serviceprovider.ContributionServiceImpl;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jetdario
 */
public class PhicDataContainer extends IndexedContainer {

    ContributionService cs = new ContributionServiceImpl();
    
    private int branchId;
    private Date payrollDate;
//    private int month;
//    private int year;

    public PhicDataContainer() {
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);      
        addContainerProperty("phicNo", String.class, null);
        addContainerProperty("eePhic", Double.class, null);
        addContainerProperty("erPhic", Double.class, null); 
        addContainerProperty("branch", String.class, null);
        addContainerProperty("date", String.class, null);
    }
    
    public PhicDataContainer(int branchId, 
            Date payrollDate) {
        this.branchId = branchId;
        this.payrollDate = payrollDate;
        
        addContainerProperty("employeeId", String.class, null);
        addContainerProperty("name", String.class, null);      
        addContainerProperty("phicNo", String.class, null);
        addContainerProperty("eePhic", Double.class, null);
        addContainerProperty("erPhic", Double.class, null); 
        addContainerProperty("branch", String.class, null);
        addContainerProperty("date", String.class, null);
        
        for(PhicSchedule ps : cs.getPhicContribution(branchId, payrollDate)){
            Item item = getItem(addItem());
            item.getItemProperty("employeeId").setValue(ps.getEmployeeId());
            item.getItemProperty("name").setValue(ps.getEmployeeName().toUpperCase());
            item.getItemProperty("phicNo").setValue(ps.getPhicNo());
            item.getItemProperty("eePhic").setValue(ps.getEePhic());
            item.getItemProperty("erPhic").setValue(ps.getErPhic());
            item.getItemProperty("branch").setValue(ps.getBranchName());
            
            Date date = CommonUtil.parsingDate(CommonUtil.convertDateFormat(payrollDate.toString()));
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int month = (1 + c.get(Calendar.MONTH));
            int year = c.get(Calendar.YEAR);
            item.getItemProperty("date").setValue(getMonth().get(month)+" "+year);
        }
    }
    
    private Map<Integer, String> getMonth(){
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
