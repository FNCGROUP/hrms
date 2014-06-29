/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.service;

import com.openhris.employee.model.EmploymentInformation;

/**
 *
 * @author jetdario
 */
public interface SalaryInformationService {
    
    public EmploymentInformation getEmployeeSalaryInformation(String employeeId);
    
    public boolean updateEmployeeSalaryInformation(String employeeId, EmploymentInformation employmentInformation);
    
}
