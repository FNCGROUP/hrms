/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.serviceprovider;

import com.openhris.dao.EmployeeCharacterReferenceDAO;
import com.openhris.model.CharacterReference;
import com.openhris.service.CharacterReferenceService;
import java.util.List;

/**
 *
 * @author jetdario
 */
public class CharacterReferenceServiceImpl implements CharacterReferenceService{

    EmployeeCharacterReferenceDAO charRefDAO = new EmployeeCharacterReferenceDAO();
    
    @Override
    public boolean updateEmployeeCharacterReference(CharacterReference characterReference) {
        return charRefDAO.updateEmployeeCharacterReference(characterReference);
    }

    @Override
    public List<CharacterReference> getEmployeeCharacterReferenceList(String employeeId) {
        return charRefDAO.getEmployeeCharacterReferenceList(employeeId);
    }

    @Override
    public CharacterReference getEmployeeCharacterReferenceById(int characterReferenceId) {
        return charRefDAO.getEmployeeCharacterReferenceById(characterReferenceId);
    }

    @Override
    public boolean removeEmployeeCharacterReferenceC(int characterReferenceId) {
        return charRefDAO.removeEmployeeCharacterReference(characterReferenceId);
    }
    
}
