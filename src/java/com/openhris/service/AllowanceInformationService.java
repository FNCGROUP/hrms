/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.service;

import com.openhris.model.Allowances;
import java.util.List;

/**
 *
 * @author jetdario
 */
public interface AllowanceInformationService {
 
    public Allowances getAllowancesByEmployee(String employeeId);
    
    public double getAllowanceByEntryType(String entryType);
    
    public boolean updateAllowance(String tableColAmount, 
            double amount, 
            String tableColEntryType, 
            String entryType, 
            String employeeId);
    
}
