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
public class AdvancesTypeBean {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    private String advancesType;
    
    public void setAdvancesType(String advancesType){ this.advancesType = advancesType; }
    public String getAdvancesType(){ return advancesType; }
    
    public Boolean saveAdvancesType(){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt;
        String queryInsert = "INSERT INTO advances_type(advancesType) VALUES(?)";
        boolean result = false;
        try {
            pstmt = conn.prepareStatement(queryInsert);
            pstmt.setString(1, advancesType.toLowerCase());
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(AdvancesTypeBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }
    
}
