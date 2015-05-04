/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.service;

import com.openhris.model.Employee;
import com.openhris.model.EmploymentInformation;
import com.openhris.model.PostEmploymentInformationBean;
import java.util.List;

/**
 *
 * @author jet
 */
public interface EmployeeService {
        
    public String getEmployeeId(String name);
    
    public List<Employee> getEmployeeById(String employeeId);
            
    public List<Employee> getAllEmployees();
        
    public List<Employee> getEmployeePerBranchForDropDownList(int branchId);
    
    public List<Employee> getEmployeePerBranch(int branchId);
    
    public List<PostEmploymentInformationBean> getEmployeePositionHistory(String employeeId);
    
    public List<EmploymentInformation> getEmployeeEmploymentInformation(String employeeId);
    
    public boolean insertNewEmployee(List<PostEmploymentInformationBean> insertList);
    
    public boolean updateEmployeeEmploymentInformation(String employeeId, List<PostEmploymentInformationBean> updateList);
    
    public boolean checkForDuplicateEmployee(String firstname, String middlename, String lastname);
    
    public double getEmploymentWage(String employeeId);
    
    public String getEmploymentWageStatus(String employeeId);
    
    public String getEmploymentWageEntry(String employeeId);
    
    public double getEmploymentAllowance(String employeeId);
    
    public String getEmploymentAllowanceEntry(String employeeId);
    
    public double getEmploymentAllowanceForLiquidation(String employeeId);
    
    public String getEmploymentEntryDate(String employeeId);
    
    public String getEmploymentEndDate(String employeeId);
    
    public String getEmployeeCurrentStatus(String employeeId);
    
    public String getEmployeeTotalDependent(String employeeId);
    
    public boolean updateEmploymentAllowanceForLiquidation(double afl, String employeeId);
}
