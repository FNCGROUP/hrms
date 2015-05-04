/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.CharacterReference;
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
 * @author jetdario
 */
public class EmployeeCharacterReferenceDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
        
    public boolean updateEmployeeCharacterReference(CharacterReference characterReference){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            if(characterReference.getCharacterReferenceId() == 0){
                pstmt = conn.prepareStatement("INSERT INTO employee_character_references (employeeId, name, occupation, address, contactNo) "
                        + "VALUES (?, ?, ?, ?, ?)");
                pstmt.setString(1, characterReference.getEmployeeId());
                pstmt.setString(2, characterReference.getName());
                pstmt.setString(3, characterReference.getOccupation());
                pstmt.setString(4, characterReference.getAddress());
                pstmt.setString(5, characterReference.getContactNo());
                pstmt.executeUpdate();                
            } else {
                pstmt = conn.prepareStatement("UPDATE employee_character_references SET name = ?, occupation = ?, address = ?, contactNo = ? "
                        + "WHERE id = ?");
                pstmt.setString(1, characterReference.getName());
                pstmt.setString(2, characterReference.getOccupation());
                pstmt.setString(3, characterReference.getAddress());
                pstmt.setString(4, characterReference.getContactNo());
                pstmt.setInt(5, characterReference.getCharacterReferenceId());
                pstmt.executeUpdate();
            }
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeCharacterReferenceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeCharacterReferenceDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public List<CharacterReference> getEmployeeCharacterReferenceList(String employeeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        List<CharacterReference> characterReferenceList = new ArrayList<CharacterReference>();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM employee_character_references WHERE employeeId = '"+employeeId+"' ");
            while(rs.next()){
                CharacterReference charRef = new CharacterReference();
                charRef.setCharacterReferenceId(util.convertStringToInteger(rs.getString("id")));
                charRef.setName(rs.getString("name"));
                charRef.setOccupation(rs.getString("occupation"));
                charRef.setAddress(rs.getString("address"));
                charRef.setContactNo(rs.getString("contactNo"));
                characterReferenceList.add(charRef);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeCharacterReferenceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeCharacterReferenceDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return characterReferenceList;
    }

    public CharacterReference getEmployeeCharacterReferenceById(int characterReferenceId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        CharacterReference charRef = new CharacterReference();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM employee_character_references WHERE id = "+characterReferenceId+" ");
            while(rs.next()){
                charRef.setName(rs.getString("name"));
                charRef.setOccupation(rs.getString("occupation"));
                charRef.setAddress(rs.getString("address"));
                charRef.setContactNo(rs.getString("contactNo"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeCharacterReferenceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeCharacterReferenceDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return charRef;
    }
    
    public boolean removeEmployeeCharacterReference(int characterReferenceId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("DELETE FROM employee_character_references WHERE id = "+characterReferenceId+" ");
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeCharacterReferenceDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeCharacterReferenceDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
}
