/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.serviceprovider;

import com.openhris.employee.dao.PersonalInformationDAO;
import com.openhris.employee.model.PersonalInformation;
import com.openhris.employee.service.PersonalInformationService;

/**
 *
 * @author jet
 */
public class PersonalInformationServiceImpl implements PersonalInformationService{

    PersonalInformationDAO piDAO = new PersonalInformationDAO();

    @Override
    public PersonalInformation getPersonalInformationData(String employeeId) {
	return piDAO.getPersonalInformation(employeeId);
    }

    @Override
    public boolean updatePersonalInformation(PersonalInformation personalInformation) {
	return piDAO.updatePersonalInformation(personalInformation);
    }
    
    
	
}
