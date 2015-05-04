/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.serviceprovider;

import com.openhris.dao.EmployeeAddressDAO;
import com.openhris.model.Address;
import com.openhris.service.EmployeeAddressService;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class EmployeeAddressServiceImpl implements EmployeeAddressService{

    EmployeeAddressDAO eaDAO = new EmployeeAddressDAO();
    
    @Override
    public boolean insertEmployeeAddress(Address address) {
        return eaDAO.insertEmployeeAddress(address);
    }

    @Override
    public List<Address> getEmployeeAddress(String employeeId) {
        return eaDAO.getEmployeeAddress(employeeId);
    }

    @Override
    public boolean removeEmployeeAddress(int addressId) {
        return eaDAO.removeEmployeeAddress(addressId);
    }

    @Override
    public Address getEmployeeAddressbyId(int addressId) {
        return eaDAO.getEmployeeAddressById(addressId);
    }
    
}
