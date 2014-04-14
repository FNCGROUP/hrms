/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.classes;

import com.hrms.queries.GetSQLQuery;
import com.vaadin.ui.NativeSelect;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jet
 */
public class EmployeesListPerBranch {
    
    GetSQLQuery query = new GetSQLQuery();
    
    public NativeSelect getEmployeesListPerBranch(NativeSelect employeesName, int branchId){
        employeesName.removeAllItems();
        employeesName.setNullSelectionAllowed(false);
        List<String> employeesListPerBranch = query.getEmployeesListPerBranch(branchId);
        for(int i = 0; i < employeesListPerBranch.size(); i++){
            employeesName.addItem(employeesListPerBranch.get(i));
        }
        employeesName.setImmediate(true);
        return employeesName;
    }
    
}
