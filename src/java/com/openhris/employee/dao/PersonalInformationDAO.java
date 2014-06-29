/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.employee.model.PersonalInformation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class PersonalInformationDAO {

    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public PersonalInformation getPersonalInformation(String employeeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
	PersonalInformation personalInformation = new PersonalInformation();
	try {
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery("SELECT * FROM employee where employeeId = '"+employeeId+"' ");
	    while(rs.next()){		
		personalInformation.setFirstname(rs.getString("firstname"));
		personalInformation.setMiddlename(rs.getString("middlename"));
		personalInformation.setLastname(rs.getString("lastname"));
	    }
	    
	    rs = stmt.executeQuery("SELECT * FROM employee_personal_information WHERE employeeId = '"+employeeId+"' ");
	    while(rs.next()){
		if(rs.getString("id") == null || rs.getString("id").isEmpty()){			
		} else {
		    personalInformation.setDob(util.parsingDate(rs.getString("dob")));
		    personalInformation.setPob(rs.getString("pob"));
		    personalInformation.setGender(rs.getString("gender"));
		    personalInformation.setCitizenship(rs.getString("citizenship"));
		    personalInformation.setHeight(util.convertStringToDouble(rs.getString("height")));
		    personalInformation.setWeight(util.convertStringToDouble(rs.getString("weight")));
		    personalInformation.setCivilStatus(rs.getString("civilStatus"));
		    personalInformation.setReligion(rs.getString("religion"));
		    personalInformation.setSpouseName(rs.getString("spouseName"));
		    personalInformation.setSpouseOccupation(rs.getString("spouseOccupation"));
		    personalInformation.setSpouseOfficeAddress(rs.getString("spouseOfficeAddress"));
		    personalInformation.setFathersName(rs.getString("fathersName"));
		    personalInformation.setFathersOccupation(rs.getString("fathersOccupation"));
		    personalInformation.setMothersName(rs.getString("mothersName"));
		    personalInformation.setMothersOccupation(rs.getString("mothersOccupation"));
		    personalInformation.setParentsAddress(rs.getString("parentsAddress"));
		    personalInformation.setDialectSpeakWrite(rs.getString("dialectSpeakWrite"));
		    personalInformation.setContactPerson(rs.getString("contactPerson"));
		    personalInformation.setSkills(rs.getString("skills"));
		    personalInformation.setHobby(rs.getString("hobby"));
		}    
	    }
	} catch (SQLException ex) {
	    Logger.getLogger(PersonalInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
	}finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PersonalInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	
	return personalInformation;
    }
	
    public boolean updatePersonalInformation(PersonalInformation personalInformation){
	Connection conn = getConnection.connection();
        boolean result = false;
        PreparedStatement pstmt = null;
	Statement stmt = null;
	ResultSet rs = null;
	try {
	    conn.setAutoCommit(false);
	    
	    pstmt = conn.prepareStatement("UPDATE employee SET firstname = ?, middlename = ?, lastname = ? WHERE employeeId = ? ");
	    pstmt.setString(1, personalInformation.getFirstname());
	    pstmt.setString(2, personalInformation.getMiddlename());
	    pstmt.setString(3, personalInformation.getLastname());
	    pstmt.setString(4, personalInformation.getEmployeeId());
	    pstmt.executeUpdate();
	    
	    stmt = conn.createStatement();
	    rs = stmt.executeQuery("SELECT COUNT(*) FROM employee_personal_information WHERE employeeId = '"+personalInformation.getEmployeeId()+"' ");
	    while(rs.next()){
	        if(rs.getString("COUNT(*)").equals("0")){
		    pstmt = conn.prepareStatement("INSERT INTO employee_personal_information (employeeId, dob, pob, gender, citizenship, height, weight, "
                            + "civilStatus, religion, spouseName, spouseOccupation, spouseOfficeAddress, fathersName, fathersOccupation, "
                            + "mothersName, mothersOccupation, parentsAddress, dialectSpeakWrite, contactPerson, skills, hobby) "
                            + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		    pstmt.setString(1, personalInformation.getEmployeeId());
		    pstmt.setString(2, util.convertDateFormat(personalInformation.getDob().toString()));
		    pstmt.setString(3, personalInformation.getPob());
                    pstmt.setString(4, personalInformation.getGender());
                    pstmt.setString(5, personalInformation.getCitizenship());
                    pstmt.setDouble(6, personalInformation.getHeight());
                    pstmt.setDouble(7, personalInformation.getWeight());
                    pstmt.setString(8, personalInformation.getCivilStatus());
                    pstmt.setString(9, personalInformation.getReligion());
                    pstmt.setString(10, personalInformation.getSpouseName());
                    pstmt.setString(11, personalInformation.getSpouseOccupation());
                    pstmt.setString(12, personalInformation.getSpouseOfficeAddress());
                    pstmt.setString(13, personalInformation.getFathersName());
                    pstmt.setString(14, personalInformation.getFathersOccupation());
                    pstmt.setString(15, personalInformation.getMothersName());
                    pstmt.setString(16, personalInformation.getMothersOccupation());
                    pstmt.setString(17, personalInformation.getParentsAddress());
                    pstmt.setString(18, personalInformation.getDialectSpeakWrite());
                    pstmt.setString(19, personalInformation.getContactPerson());
                    pstmt.setString(20, personalInformation.getSkills());
                    pstmt.setString(21, personalInformation.getHobby());
		    pstmt.executeUpdate();
		} else {
		    pstmt = conn.prepareStatement("UPDATE employee_personal_information SET dob = ?, pob = ?, gender = ?, citizenship = ?, "
                            + "height = ?, weight = ?, civilStatus = ?, religion = ?, spouseName = ?, spouseOccupation = ?, spouseOfficeAddress = ?, "
                            + "fathersName = ?, fathersOccupation = ?, mothersName = ?, mothersOccupation = ?, parentsAddress = ?, dialectSpeakWrite = ?, "
                            + "contactPerson = ?, skills = ?, hobby = ? WHERE employeeId = ?");
		    pstmt.setString(1, util.convertDateFormat(personalInformation.getDob().toString()));
		    pstmt.setString(2, personalInformation.getPob());
                    pstmt.setString(3, personalInformation.getGender());
                    pstmt.setString(4, personalInformation.getCitizenship());
                    pstmt.setDouble(5, personalInformation.getHeight());
                    pstmt.setDouble(6, personalInformation.getWeight());
                    pstmt.setString(7, personalInformation.getCivilStatus());
                    pstmt.setString(8, personalInformation.getReligion());
                    pstmt.setString(9, personalInformation.getSpouseName());
                    pstmt.setString(10, personalInformation.getSpouseOccupation());
                    pstmt.setString(11, personalInformation.getSpouseOfficeAddress());
                    pstmt.setString(12, personalInformation.getFathersName());
                    pstmt.setString(13, personalInformation.getFathersOccupation());
                    pstmt.setString(14, personalInformation.getMothersName());
                    pstmt.setString(15, personalInformation.getMothersOccupation());
                    pstmt.setString(16, personalInformation.getParentsAddress());
                    pstmt.setString(17, personalInformation.getDialectSpeakWrite());
                    pstmt.setString(18, personalInformation.getContactPerson());
                    pstmt.setString(19, personalInformation.getSkills());
                    pstmt.setString(20, personalInformation.getHobby());
		    pstmt.setString(21, personalInformation.getEmployeeId());
		    pstmt.executeUpdate(); 	
		}	    
	    }	    
	    
	    conn.commit();
            System.out.println("Transaction commit...");
            result = true;
	} catch (SQLException ex) {
	    try {
                conn.rollback();
                System.out.println("Connection rollback...");
            } catch (SQLException ex1) {
                Logger.getLogger(PersonalInformationDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }              	
	    Logger.getLogger(PersonalInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
	} finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                 }
            } catch (SQLException ex) {
                Logger.getLogger(PersonalInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
	return result;
    }
}
