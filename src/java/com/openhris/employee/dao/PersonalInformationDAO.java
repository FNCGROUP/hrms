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
		    pstmt = conn.prepareStatement("INSERT INTO employee_personal_information (employeeId, dob, pob) values (?, ?, ?)");
		    pstmt.setString(1, personalInformation.getEmployeeId());
		    pstmt.setString(2, util.convertDateFormat(personalInformation.getDob().toString()));
		    pstmt.setString(3, personalInformation.getPob());
		    pstmt.executeUpdate();
		} else {
		    pstmt = conn.prepareStatement("UPDATE employee_personal_information SET dob = ?, pob = ? WHERE employeeId = ?");
		    pstmt.setString(1, util.convertDateFormat(personalInformation.getDob().toString()));
		    pstmt.setString(2, personalInformation.getPob());
		    pstmt.setString(3, personalInformation.getEmployeeId());
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
