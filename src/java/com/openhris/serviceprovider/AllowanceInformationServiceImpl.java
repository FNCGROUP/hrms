/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.openhris.serviceprovider;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.Allowances;
import com.openhris.service.AllowanceInformationService;
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
public class AllowanceInformationServiceImpl implements AllowanceInformationService {

    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    @Override
    public Allowances getAllowancesByEmployee(String employeeId) {
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        Allowances a = new Allowances();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM employee_allowances WHERE employeeId = '"+employeeId+"' ");
            while(rs.next()){
                a.setCommunication(util.convertStringToDouble(rs.getString("Communication")));
                a.setComEntryType(rs.getString("CommunicationEntryType"));
                a.setPerDiem(util.convertStringToDouble(rs.getString("PerDiem")));
                a.setPerDiemEntryType(rs.getString("PerDiemEntryType"));
                a.setCola(util.convertStringToDouble(rs.getString("Cola")));
                a.setColaEntryType(rs.getString("ColaEntryType"));
                a.setMeal(util.convertStringToDouble(rs.getString("Meal")));
                a.setMealEntryType(rs.getString("MealEntryType"));
                a.setTransportation(util.convertStringToDouble("Transportation"));
                a.setTransEntryType(rs.getString("TransportationEntryType"));
                a.setOthers(util.convertStringToDouble(rs.getString("Others")));
                a.setOthersEntryType(rs.getString("OthersEntryType"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
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
        
        return a;
    }

    @Override
    public double getAllowanceByEntryType(String entryType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateAllowance(String tableColAmount, 
            double amount, 
            String tableColEntryType, 
            String entryType, 
            String employeeId) {
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("UPDATE employee_allowances SET "
                    + ""+tableColAmount+" = ?, "
                    + ""+tableColEntryType+" = ? "
                    + "WHERE EmployeeID = ? ");
            pstmt.setDouble(1, amount);
            pstmt.setString(2, entryType);
            pstmt.setString(3, employeeId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
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
    }
    
}
