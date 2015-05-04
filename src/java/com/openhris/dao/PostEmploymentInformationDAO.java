/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.PostEmploymentInformationBean;
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
public class PostEmploymentInformationDAO {
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public List<PostEmploymentInformationBean> getPositionHistory(String employeeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;         
        List<PostEmploymentInformationBean> positionList = new ArrayList<PostEmploymentInformationBean>();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM employee_position_history WHERE employeeId = '"+employeeId+"' ORDER BY id DESC ");
            while(rs.next()){
                PostEmploymentInformationBean positionHistory = new PostEmploymentInformationBean();
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
            Logger.getLogger(PostEmploymentInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PostEmploymentInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return positionList;
    }
    
    public boolean updatePositionHistory(String employeeId, PostEmploymentInformationBean positionHistory, boolean isEdit, int positionId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        String query;
                        
        try {
            conn.setAutoCommit(false);
            
            if(isEdit){
                pstmt = conn.prepareStatement("UPDATE employee_position_history SET "
                        + "position = ?, "
                        + "corporate = ?, "
                        + "trade = ?, "
                        + "branch = ?, "
                        + "department = ?, "
                        + "entryDate = ?, "
                        + "branchId = ? "
                        + "WHERE id = ?");                
                pstmt.setString(1, positionHistory.getPosition());
                pstmt.setString(2, positionHistory.getCompany());
                pstmt.setString(3, positionHistory.getTrade());
                pstmt.setString(4, positionHistory.getBranch());
                pstmt.setString(5, positionHistory.getDepartment());
                pstmt.setString(6, util.convertDateFormat(positionHistory.getEntryDate().toString()));
                pstmt.setInt(7, positionHistory.getBranchId());
                pstmt.setInt(8, positionHistory.getPositionId());
                pstmt.executeUpdate();
                                
                pstmt = conn.prepareStatement("UPDATE employee SET branchId = ? WHERE employeeId = ?");
                pstmt.setInt(1, positionHistory.getBranchId());
                pstmt.setString(2, employeeId);
                pstmt.executeUpdate();
            } else {
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
            }
            
            conn.commit();
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(PostEmploymentInformationDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(PostEmploymentInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PostEmploymentInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(PostEmploymentInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PostEmploymentInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean insertEndDate(String employeeId, String endDate){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("UPDATE employee SET endDate = ?, currentStatus = ? WHERE employeeId = ? ");
            pstmt.setString(1, endDate);
            pstmt.setString(2, "resigned");
            pstmt.setString(3, employeeId);
            pstmt.executeUpdate();
                        
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(PostEmploymentInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PostEmploymentInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public List<PostEmploymentInformationBean> getPositionHistoryById(int positionId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;         
        List<PostEmploymentInformationBean> positionList = new ArrayList<PostEmploymentInformationBean>();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM employee_position_history WHERE id = "+positionId+" ");
            while(rs.next()){
                PostEmploymentInformationBean positionHistory = new PostEmploymentInformationBean();
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
            Logger.getLogger(PostEmploymentInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PostEmploymentInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return positionList;
    }

    public boolean editDateEntryFromEmployment(String employeeId, String entryDate){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("UPDATE employee SET entryDate = ? WHERE employeeId = ? ");
            pstmt.setString(1, entryDate);
            pstmt.setString(2, employeeId);
            pstmt.executeUpdate();
                        
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(PostEmploymentInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PostEmploymentInformationDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
}
