/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hrms.classes;

import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.queries.GetSQLQuery;
import com.hrms.utilities.ConvertionUtilities;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jet
 */
public class GenerateCompanyId {
    
    GetSQLConnection getConnection = new GetSQLConnection();
    GetSQLQuery query = new GetSQLQuery();
    ConvertionUtilities conUtil = new ConvertionUtilities();
    
    public String generateId(String corporate, String trade, String entryDate){
        Connection conn = getConnection.connection();
        
        SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");
        String companyId = null;
        String firstDigit;
        String secondDigit;
        String thirdDigit;
        int fourthDigit = 0;
        Statement stmt;
        ResultSet rs;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT RIGHT(employeeId,4) AS employeeId FROM employee "
                    + "WHERE (currentStatus IS NULL OR currentStatus != 'removed') ORDER BY id DESC LIMIT 1");
            while(rs.next()){
                if(rs.getString("employeeId") == null){
                    fourthDigit = 1001;
                } else {
                    fourthDigit = Integer.parseInt(rs.getString("employeeId")) + 1;
                }
            }
            
            int corporateId = query.getCoporateId(corporate);
            int tradeId = query.getTradeId(trade, corporate);
            
            if(corporateId < 10){
                firstDigit = "0"+String.valueOf(corporateId).toString();
            } else {
                firstDigit = String.valueOf(corporateId).toString();
            }
            
            if(tradeId < 10 ){
                secondDigit = "0"+String.valueOf(tradeId).toString();
            } else { 
                secondDigit = String.valueOf(tradeId).toString();
            }
            
            Date date = conUtil.parsingDate(entryDate);
            thirdDigit = sdf.format(date);
            
            companyId = firstDigit + "-" + secondDigit + "-" + thirdDigit + String.valueOf(fourthDigit).toString();
            
        } catch (SQLException ex) {
            Logger.getLogger(GenerateCompanyId.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        return companyId;
    }
        
}
