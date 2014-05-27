/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.service;

import com.openhris.employee.model.PersonalInformation;
import java.util.List;

/**
 *
 * @author jet
 */
public interface PersonalInformationService {
    
    public PersonalInformation getPersonalInformationData(String employeeId);
    
    public boolean updatePersonalInformation(PersonalInformation personalInformation);
	
}
