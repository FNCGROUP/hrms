/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.serviceprovider;

import com.openhris.dao.EmployeeCurrentStatusDAO;
import com.openhris.service.EmployeeCurrentStatusService;

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
