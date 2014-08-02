/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee.model;

/**
 *
 * @author jet
 */
public class CharacterReference extends Employee {
    
        private int characterReferenceId;
        private String name;
	private String occupation;
	private String address;
	private String contactNo;

        public int getCharacterReferenceId(){ return characterReferenceId; }
        public void setCharacterReferenceId(int characterReferenceId){ this.characterReferenceId = characterReferenceId; }
        
	public String getName(){ return name; }
	public void setName(String name){ this.name = name; }
	
	public String getOccupation(){ return occupation; }
	public void setOccupation(String occupation){ this.occupation = occupation; }
	
	public String getAddress(){ return address; }
	public void setAddress(String address){ this.address = address; }
	
	public String getContactNo(){ return contactNo; }
	public void setContactNo(String contactNo){ this.contactNo = contactNo; }
    
}
