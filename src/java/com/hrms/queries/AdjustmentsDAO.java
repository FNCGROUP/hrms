/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.queries;

import com.hrms.dbconnection.GetSQLConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class AdjustmentsDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection();
        
    public String getAdjustmentsRemarks(Integer salaryId){
        String remarks = null;
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT remarks FROM adjustments WHERE salaryID = "+salaryId+" ");
            while(rs.next()){
                remarks = rs.getString("remarks");
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdjustmentsDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AdjustmentsDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return remarks;        
    }
    
}
