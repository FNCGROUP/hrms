/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.service;

import com.openhris.employee.model.CharacterReference;
import java.util.List;

/**
 *
 * @author jetdario
 */
public interface CharacterReferenceService {
    
    public boolean updateEmployeeCharacterReference(CharacterReference characterReference);
    
    public List<CharacterReference> getEmployeeCharacterReferenceList(String employeeId);
    
    public CharacterReference getEmployeeCharacterReferenceById(int characterReferenceId);
    
    public boolean removeEmployeeCharacterReferenceC(int characterReferenceId);
    
}
