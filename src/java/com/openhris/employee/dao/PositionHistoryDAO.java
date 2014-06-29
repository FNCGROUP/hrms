/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.employee.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.hrms.utilities.ConvertionUtilities;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.employee.model.PositionHistory;
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
public class PositionHistoryDAO {
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public List<PositionHistory> getPositionHistory(String employeeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;         
        List<PositionHistory> positionList = new ArrayList<PositionHistory>();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM employee_position_history WHERE employeeId = '"+employeeId+"' ORDER BY id DESC ");
            while(rs.next()){
                PositionHistory positionHistory = new PositionHistory();
                positionHistory.setPositionId(util.convertStringToInteger(rs.getString("id")));
                positionHistory.setPosition(rs.getString("position"));
                positionHistory.setCompany(rs.getString("corporate"));
                positionHistory.setTrade(rs.getString("trade"));
                positionHistory.setBranch(rs.getString("branch"));
                positionHistory.setDepartment(rs.getString("department"));
                positionHistory.setEntryDate(util.parsingDate(rs.getString("entryDate")));
                positionList.add(positionHistory);
            }
        } catch (SQLException ex) {
            Logger.getLogger(PositionHistoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PositionHistoryDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return positionList;
    }
    
    public boolean updatePositionHistory(String employeeId, PositionHistory positionHistory){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("INSERT INTO employee_position_history (employeeId, position, corporate, trade, branch, "
                    + "department, entryDate, branchId) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ");
            pstmt.setString(1, employeeId);
            pstmt.setString(2, positionHistory.getPosition());
            pstmt.setString(3, positionHistory.getCompany());
            pstmt.setString(4, positionHistory.getTrade());
            pstmt.setString(5, positionHistory.getBranch());
            pstmt.setString(6, positionHistory.getDepartment());
            pstmt.setString(7, util.convertDateFormat(positionHistory.getEntryDate().toString()));
            pstmt.setInt(8, positionHistory.getBranchId());
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement("UPDATE employee SET branchId = ? WHERE employeeId = ?");
            pstmt.setInt(1, positionHistory.getBranchId());
            pstmt.setString(2, employeeId);
            pstmt.executeUpdate();
            
            conn.commit();
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(PositionHistoryDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(PositionHistoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PositionHistoryDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean removePosition(int positionId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("DELETE FROM employee_position_history WHERE id = "+positionId+" ");
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(PositionHistoryDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PositionHistoryDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
}
