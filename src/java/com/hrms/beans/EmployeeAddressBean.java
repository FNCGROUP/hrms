/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.beans;

import com.hrms.dbconnection.GetSQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class EmployeeAddressBean {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    
    private String employeeId;
    private String type;
    private String street;
    private String city;
    private String province;
    private String zipcode;    
    
    public void setEmployeeId(String employeeId){ this.employeeId = employeeId; }
    public void setType(String type){ this.type = type; }
    public void setStreet(String street){ this.street = street; }
    public void setCity(String city){ this.city = city; }
    public void setProvince(String province){ this.province = province; }
    public void setZipcode(String zipcode){ this.zipcode = zipcode; }
    
    public Boolean addNewAddress(){
        Connection conn = getConnection.connection();
        Boolean result = false;
        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO employee_address(employeeId, type, street, city, province, zipcode) "
                    + "VALUES(?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, employeeId);
            pstmt.setString(2, type);
            pstmt.setString(3, street);
            pstmt.setString(4, city);
            pstmt.setString(5, province);
            pstmt.setString(6, zipcode);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddressBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EmployeeAddressBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public Boolean updateAddressStatus(String id, String status){
        Connection conn = getConnection.connection();
        Boolean result = false;
        try {
            PreparedStatement pstmt = conn.prepareStatement("UPDATE employee_address SET addressStatus = '"+status+"' WHERE id = '"+id+"' ");
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddressBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
    public Boolean removeAddressStatus(String id){
        Connection conn = getConnection.connection();
        Boolean result = false;
        try {
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM employee_address WHERE id = '"+id+"' ");
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(EmployeeAddressBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    } 
}
