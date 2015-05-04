/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.serviceprovider;

import com.openhris.dao.PersonalInformationDAO;
import com.openhris.model.PersonalInformation;
import com.openhris.service.PersonalInformationService;
import java.io.File;
import java.io.FileInputStream;

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

    @Override
    public boolean uploadImageForEmployee(FileInputStream inputStream, File file, String employeeId) {
        return piDAO.uploadImageForEmployee(inputStream, file, employeeId);
    }
    
    
	
}
