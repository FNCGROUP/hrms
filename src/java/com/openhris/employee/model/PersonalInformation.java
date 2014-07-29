/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.employee.model;

import java.util.Date;

/**
 *
 * @author jet
 */
public class PersonalInformation extends Employee {
    
        private byte[] image;
	private Date dob; //Date of Birth
	private String pob; //Place of Birth
	private String gender;
	private String citizenship;
	private Double height;
	private Double weight;
	private String civilStatus;
	private String religion;
	private String spouseName;
	private String spouseOccupation;
	private String spouseOfficeAddress;
	private String fathersName;
	private String fathersOccupation;
	private String mothersName;
	private String mothersOccupation;
	private String parentsAddress;
	private String dialectSpeakWrite;
	private String contactPersonName;
        private String contactPersonAddress;
        private String contactPersonNo;
	private String skills;
	private String hobby;

	public byte[] getImage(){ return image; }
	public void setImage(byte[] image){ this.image = image; }
	
	public Date getDob(){ return dob; }
	public void setDob(Date dob){ this.dob = dob; }
	
	public String getPob(){ return pob; }
	public void setPob(String pob){ this.pob = pob; }
	
	public String getGender(){ return gender; }
	public void setGender(String gender){ this.gender = gender; }
	
	public String getCitizenship(){ return citizenship; }
	public void setCitizenship(String citizenship){ this.citizenship = citizenship; }
	
	public Double getHeight(){ return height; }
	public void setHeight(Double height){ this.height = height; }
	
	public Double getWeight(){ return weight; }
	public void setWeight(Double weight){ this.weight = weight; }
	
	public String getCivilStatus(){ return civilStatus; }
	public void setCivilStatus(String civilStatus){ this.civilStatus = civilStatus; }
	
	public String getReligion(){ return religion; }
	public void setReligion(String religion){ this.religion = religion; }
	
	public String getSpouseName(){ return spouseName; }
	public void setSpouseName(String spouseName){ this.spouseName = spouseName; }
	
	public String getSpouseOccupation(){ return spouseOccupation; }
	public void setSpouseOccupation(String spouseOccupation){ this.spouseOccupation = spouseOccupation; }
	
	public String getSpouseOfficeAddress(){ return spouseOfficeAddress; }
	public void setSpouseOfficeAddress(String spouseOfficeAddress){ this.spouseOfficeAddress = spouseOfficeAddress; }
	
	public String getFathersName(){ return fathersName; }
	public void setFathersName(String fathersName){ this.fathersName = fathersName; }
	
	public String getFathersOccupation(){ return fathersOccupation; }
	public void setFathersOccupation(String fathersOccupation){ this.fathersOccupation = fathersOccupation; }
	
	public String getMothersName(){ return mothersName; }
	public void setMothersName(String mothersName){ this.mothersName = mothersName; }
	
	public String getMothersOccupation(){ return mothersOccupation; }
	public void setMothersOccupation(String mothersOccupation){ this.mothersOccupation = mothersOccupation; }
		
	public String getParentsAddress(){ return parentsAddress; }
	public void setParentsAddress(String parentsAddress){ this.parentsAddress = parentsAddress; }
	
	public String getDialectSpeakWrite(){ return dialectSpeakWrite; }
	public void setDialectSpeakWrite(String dialectSpeakWrite){ this.dialectSpeakWrite = dialectSpeakWrite; }
	
	public String getContactPersonName(){ return contactPersonName; }
	public void setContactPersonName(String contactPersonName){ this.contactPersonName = contactPersonName; }
        
        public String getContactPersonAddress(){ return contactPersonAddress; }
	public void setContactPersonAddress(String contactPersonAddress){ this.contactPersonAddress = contactPersonAddress; }
        
        public String getContactPersonNo(){ return contactPersonNo; }
	public void setContactPersonNo(String contactPersonNo){ this.contactPersonNo = contactPersonNo; }
	
	public String getSkills(){ return skills; }
	public void setSkills(String skills){ this.skills = skills; }
	
	public String getHobby(){ return hobby; }
	public void setHobby(String hobby){ this.hobby = hobby; }
    
}
