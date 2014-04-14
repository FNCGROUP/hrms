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
public class CorporateNameBean {
    
    private String companyName;
    
    GetSQLConnection getConnection = new GetSQLConnection();
    
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
    
    public String getCompanyName(){
        return companyName;
    }
    
    public boolean updateCorporateName(String corporateName, int corporateId){
        Connection conn = getConnection.connection();
        Boolean result = false;
        PreparedStatement pstmt;
        
        String queryUpdate = " UPDATE corporate_name SET name = ? WHERE id = ? ";
        try {
            pstmt = conn.prepareStatement(queryUpdate);
            pstmt.setString(1, corporateName);
            pstmt.setInt(2, corporateId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(TradeNameBean.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null && !conn.isClosed()){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CorporateNameBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
}
