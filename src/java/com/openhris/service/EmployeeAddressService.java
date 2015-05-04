/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.service;

import com.openhris.model.Address;
import java.util.List;

/**
 *
 * @author jetdario
 */
public interface EmployeeAddressService {
    
    public boolean insertEmployeeAddress(Address address);
    
    public List<Address> getEmployeeAddress(String employeeId);
    
    public boolean removeEmployeeAddress(int addressId);
    
    public Address getEmployeeAddressbyId(int addressId);
}
