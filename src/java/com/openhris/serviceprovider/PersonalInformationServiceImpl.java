/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.serviceprovider;

import com.hrms.classes.GlobalVariables;
import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.dao.PersonalInformationDAO;
import com.openhris.model.PersonalInformation;
import com.openhris.service.PersonalInformationService;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class PersonalInformationServiceImpl implements PersonalInformationService{

    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    PersonalInformationDAO piDAO = new PersonalInformationDAO();

    @Override
    public PersonalInformation getPersonalInformationData(String employeeId) {
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
                    personalInformation.setImage(rs.getBytes("image"));
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
		    personalInformation.setContactPersonName(rs.getString("contactPerson"));
                    personalInformation.setContactPersonAddress(rs.getString("contactPersonAddress"));
                    personalInformation.setContactPersonNo(rs.getString("contactPersonNo"));
		    personalInformation.setSkills(rs.getString("skills"));
		    personalInformation.setHobby(rs.getString("hobby"));
		}    
	    }
	} catch (SQLException ex) {
	    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
	}finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
	
	return personalInformation;
//	return piDAO.getPersonalInformation(employeeId);
    }

    @Override
    public boolean updatePersonalInformation(PersonalInformation pi, String remarks) {
        Connection conn = getConnection.connection();
        boolean result = false;
        PreparedStatement pstmt = null;
	ResultSet rs = null;
	try {
	    conn.setAutoCommit(false);
	    
	    pstmt = conn.prepareStatement("UPDATE employee SET firstname = ?, middlename = ?, lastname = ? WHERE employeeId = ? ");
	    pstmt.setString(1, pi.getFirstname());
	    pstmt.setString(2, pi.getMiddlename());
	    pstmt.setString(3, pi.getLastname());
	    pstmt.setString(4, pi.getEmployeeId());
	    pstmt.executeUpdate();
	    
	    pstmt = conn.prepareStatement("UPDATE employee_personal_information "
                    + "SET dob = ?, "
                    + "pob = ?, "
                    + "gender = ?, "
                    + "citizenship = ?, "
                    + "height = ?, "
                    + "weight = ?, "
                    + "civilStatus = ?, "
                    + "religion = ?, "
                    + "spouseName = ?, "
                    + "spouseOccupation = ?, "
                    + "spouseOfficeAddress = ?, "
                    + "fathersName = ?, "
                    + "fathersOccupation = ?, "
                    + "mothersName = ?, "
                    + "mothersOccupation = ?, "
                    + "parentsAddress = ?, "
                    + "dialectSpeakWrite = ?, "
                    + "contactPerson = ?, "
                    + "contactPersonAddress = ?, "
                    + "contactPersonNo = ?, "
                    + "skills = ?, "
                    + "hobby = ? "
                    + "WHERE employeeId = ?");
            pstmt.setString(1, util.convertDateFormat(pi.getDob().toString()));
            pstmt.setString(2, pi.getPob());
            pstmt.setString(3, pi.getGender());
            pstmt.setString(4, pi.getCitizenship());
            pstmt.setDouble(5, pi.getHeight());
            pstmt.setDouble(6, pi.getWeight());
            pstmt.setString(7, pi.getCivilStatus());
            pstmt.setString(8, pi.getReligion());
            pstmt.setString(9, pi.getSpouseName());
            pstmt.setString(10, pi.getSpouseOccupation());
            pstmt.setString(11, pi.getSpouseOfficeAddress());
            pstmt.setString(12, pi.getFathersName());
            pstmt.setString(13, pi.getFathersOccupation());
            pstmt.setString(14, pi.getMothersName());
            pstmt.setString(15, pi.getMothersOccupation());
            pstmt.setString(16, pi.getParentsAddress());
            pstmt.setString(17, pi.getDialectSpeakWrite());
            pstmt.setString(18, pi.getContactPersonName());
            pstmt.setString(19, pi.getContactPersonAddress());
            pstmt.setString(20, pi.getContactPersonNo());
            pstmt.setString(21, pi.getSkills());
            pstmt.setString(22, pi.getHobby());
            pstmt.setString(23, pi.getEmployeeId());
            pstmt.executeUpdate();	    
	    	    
            pstmt = conn.prepareStatement("INSERT INTO employee_logs "
                    + "SET EmployeeID = ?, "
                    + "Remarks = ?, "
                    + "DateRemarked = now(), "
                    + "UserID = ?");
            pstmt.setString(1, pi.getEmployeeId());
            pstmt.setString(2, "Personal Information: "+remarks);
            pstmt.setInt(3, GlobalVariables.getUserId());
            pstmt.executeUpdate();
            
	    conn.commit();
            System.out.println("Transaction commit...");
            result = true;
	} catch (SQLException ex) {
	    try {
                conn.rollback();
                System.out.println("Connection rollback...");
            } catch (SQLException ex1) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex1);
            }              	
	    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
	} finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                 }
            } catch (SQLException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
	return result;
//	return piDAO.updatePersonalInformation(personalInformation);
    }

    @Override
    public boolean uploadImageForEmployee(FileInputStream inputStream, File file, String employeeId) {
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        PreparedStatement pstmt = null;
        Boolean result = false;
        
        try {
            conn.setAutoCommit(false);
            
            stmt = conn.createStatement();
	    rs = stmt.executeQuery("SELECT COUNT(*) FROM employee_personal_information WHERE employeeId = '"+employeeId+"' ");
	    while(rs.next()){
                if(rs.getString("COUNT(*)").equals("0")){
                    pstmt = conn.prepareStatement("INSERT INTO employee_personal_information (employeeId, image) VALUES(?, ?)");
                    pstmt.setString(1, employeeId);
                    pstmt.setBinaryStream(2, (InputStream) inputStream, (int) file.length());                    
                    pstmt.executeUpdate();
                } else {
                    pstmt = conn.prepareStatement("UPDATE employee_personal_information SET image = ? WHERE employeeId = ? ");
                    pstmt.setBinaryStream(1, (InputStream) inputStream, (int) file.length());
                    pstmt.setString(2, employeeId);
                    pstmt.executeUpdate();                    
                }                
            }
            
            pstmt = conn.prepareStatement("INSERT INTO employee_logs "
                    + "SET EmployeeID = ?, "
                    + "Remarks = ?, "
                    + "DateRemarked = now(), "
                    + "UserID = ?");
            pstmt.setString(1, employeeId);
            pstmt.setString(2, "Image: Add Photo for employee.");
            pstmt.setInt(3, GlobalVariables.getUserId());
            pstmt.executeUpdate();
            
            result = true;conn.commit();
            System.out.println("Transaction commit...");
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
                System.out.println("Connection rollback...");
            } catch (SQLException ex1) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex1);
            }              	
	    Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
               
        return result;
//        return piDAO.uploadImageForEmployee(inputStream, file, employeeId);
    }
    
    
	
}
