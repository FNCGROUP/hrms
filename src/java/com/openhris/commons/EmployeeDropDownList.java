/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.commons;

import com.openhris.employee.model.Employee;
import com.openhris.employee.service.EmployeeService;
import com.openhris.employee.serviceprovider.EmployeeServiceImpl;
import com.vaadin.ui.ComboBox;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class EmployeeDropDownList extends ComboBox {

    EmployeeService employeeService = new EmployeeServiceImpl();
    private int branchId;
    
    public EmployeeDropDownList(int branchId) {
        this.branchId = branchId;
        
        removeAllItems();
        setWidth("100%");
        setNullSelectionAllowed(false);
        List<Employee> employeesList = employeeService.getEmployeePerBranchForDropDownList(getBranchId());    
        for(Employee e : employeesList){
            String name = e.getLastname()+ ", " + e.getFirstname() + " " + e.getMiddlename();
            addItem(name.toUpperCase());
        }
        setImmediate(true);
    }
    
    int getBranchId(){
        return branchId;
    }
}
