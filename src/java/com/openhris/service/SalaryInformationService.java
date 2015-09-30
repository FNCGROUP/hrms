/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.service;

import com.openhris.model.EmploymentInformation;

/**
 *
 * @author jetdario
 */
public interface SalaryInformationService {
    
    public EmploymentInformation getEmployeeSalaryInformation(String employeeId);
    
    public boolean updateEmployeeSalaryInformation(String employeeId, EmploymentInformation ei);
    
    public boolean updateEmployeeContributionBranch(String employeeId, int branchId, String remarks);    
    
    public boolean editEmploymentDateEntry(String employeeId, String entryDate);    
    
    public boolean updateBankAccountNo(String employeeId, String bankAccountNo);
}
