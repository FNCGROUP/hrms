/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.serviceprovider;

import com.openhris.employee.dao.RemoveEmployeeDAO;
import com.openhris.employee.service.RemoveEmployeeService;

/**
 *
 * @author jetdario
 */
public class RemoveEmployeeServiceImpl implements RemoveEmployeeService{

    RemoveEmployeeDAO reDAO = new RemoveEmployeeDAO();
    
    @Override
    public boolean removeEmployee(String employeeId) {
        return reDAO.removeEmployee(employeeId);
    }
    
}
