/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.Address;
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
public class EmployeeAddressDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public boolean insertEmployeeAddress(Address address){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            if(address.getAddressId() == 0){
                pstmt = conn.prepareStatement("INSERT INTO employee_address (employeeId, type, street, city, province, zipCode) VALUES (?, ?, ?, ?, ?, ?)");
                pstmt.setString(1, address.getEmployeeId());
                pstmt.setString(2, address.getType());
                pstmt.setString(3, address.getStreet());
                pstmt.setString(4, address.getCity());
                pstmt.setString(5, address.getProvince());
                pstmt.setString(6, address.getZipCode());
                pstmt.executeUpdate();
            } else {
                pstmt = conn.prepareStatement("UPDATE employee_address SET type = ?, street = ?, city = ?, province = ?, zipCode = ? WHERE "
                        + "id = ?");
                pstmt.setString(1, address.getType());
                pstmt.setString(2, address.getStreet());
                pstmt.setString(3, address.getCity());
                pstmt.setString(4, address.getProvince());
                pstmt.setString(5, address.getZipCode());
                pstmt.setInt(6, address.getAddressId());
                pstmt.executeUpdate();
            }
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddressDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeAddressDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public List<Address> getEmployeeAddress(String employeeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        List<Address> addressList = new ArrayList<Address>();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM employee_address WHERE employeeId = '"+employeeId+"' ");
            while(rs.next()){
                Address address = new Address();
                address.setAddressId(util.convertStringToInteger(rs.getString("id")));
                address.setType(rs.getString("type"));
                address.setStreet(rs.getString("street"));
                address.setCity(rs.getString("city"));
                address.setProvince(rs.getString("province"));
                address.setZipcode(rs.getString("zipCode"));
                addressList.add(address);
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddressDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeAddressDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return addressList;
    }
    
    public Address getEmployeeAddressById(int addressId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Address address = new Address();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM employee_address WHERE id = "+addressId+" ");
            while(rs.next()){
                address.setType(rs.getString("type"));
                address.setStreet(rs.getString("street"));
                address.setCity(rs.getString("city"));
                address.setProvince(rs.getString("province"));
                address.setZipcode(rs.getString("zipCode"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddressDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeAddressDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return address;
    }

    public boolean removeEmployeeAddress(int addressId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("DELETE FROM employee_address WHERE id = "+addressId+" ");
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddressDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeAddressDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
}
