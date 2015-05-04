/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.service;

import com.openhris.model.PersonalInformation;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 *
 * @author jet
 */
public interface PersonalInformationService {
    
    public PersonalInformation getPersonalInformationData(String employeeId);
    
    public boolean updatePersonalInformation(PersonalInformation personalInformation);
	
    public boolean uploadImageForEmployee(FileInputStream inputStream, File file, String employeeId);
}
