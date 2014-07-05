/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.serviceprovider;

import com.openhris.employee.dao.EmployeeCurrentStatusDAO;
import com.openhris.employee.service.EmployeeCurrentStatusService;

/**
 *
 * @author jetdario
 */
public class EmployeeCurrentStatusServiceImpl implements EmployeeCurrentStatusService{

    EmployeeCurrentStatusDAO reDAO = new EmployeeCurrentStatusDAO();
    
    @Override
    public boolean removeEmployee(String employeeId) {
        return reDAO.removeEmployee(employeeId);
    }
    
}
