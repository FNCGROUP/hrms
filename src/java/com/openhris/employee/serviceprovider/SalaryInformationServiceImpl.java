/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.serviceprovider;

import com.openhris.employee.dao.SalaryInformationDAO;
import com.openhris.employee.model.EmploymentInformation;
import com.openhris.employee.service.SalaryInformationService;

/**
 *
 * @author jetdario
 */
public class SalaryInformationServiceImpl implements SalaryInformationService{
    
    SalaryInformationDAO siDAO = new SalaryInformationDAO();

    @Override
    public EmploymentInformation getEmployeeSalaryInformation(String employeeId) {
        return siDAO.getEmployeeSalaryInformation(employeeId);
    }

    @Override
    public boolean updateEmployeeSalaryInformation(String employeeId, EmploymentInformation employmentInformation) {
        return siDAO.updateEmployeeSalaryInformation(employeeId, employmentInformation);
    }
    
}
