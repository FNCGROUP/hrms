/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.model.Branch;
import com.openhris.model.Company;
import com.openhris.model.Trade;
import com.openhris.dao.ServiceGetDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jetdario
 */
public class CompanyDAO {
    
    GetSQLConnection getConnection = new GetSQLConnection(); 
    OpenHrisUtilities util = new OpenHrisUtilities();
    
    public ArrayList<ArrayList<ArrayList<String>>> getCompanyTreeMap(){
        ArrayList<ArrayList<ArrayList<String>>> corporateList = new ArrayList<ArrayList<ArrayList<String>>>();
        ArrayList<ArrayList<String>> tradeList = new ArrayList<ArrayList<String>>();
        ArrayList<String> branchList = new ArrayList<String>();
        Connection conn = getConnection.connection();
        Statement corporateStmt = null;
        Statement tradeStmt = null;
        Statement branchStmt = null;
        ResultSet corporateRs = null;
        ResultSet tradeRs = null;
        ResultSet branchRs = null;
        
        try {
            corporateStmt = conn.createStatement();
            corporateRs = corporateStmt.executeQuery("SELECT * FROM corporate_table ORDER BY name ASC");
            while(corporateRs.next()){
                tradeStmt = conn.createStatement();
                tradeRs = tradeStmt.executeQuery("SELECT * FROM trade_table WHERE corporateId =  '"+corporateRs.getString("id")+"'ORDER BY name ASC");
                while(tradeRs.next()){
                    branchStmt = conn.createStatement();
                    branchRs = branchStmt.executeQuery("SELECT * FROM branch_table WHERE tradeId = '"+tradeRs.getString("id")+"' ORDER BY name ASC");
                    while(branchRs.next()){
                        branchList.add(branchRs.getString("name"));
                    }
                    tradeList.add(branchList);
                }
                corporateList.add(tradeList);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    corporateStmt.close();
                    tradeStmt.close();
                    branchStmt.close();
                    corporateRs.close();
                    tradeRs.close();
                    branchRs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return corporateList;
    }
    
    public List<Company> getCorporateLists(){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Company> corporateLists = new ArrayList<Company>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT id, name FROM corporate_table ORDER BY name ASC");
            while(rs.next()){
                Company company = new Company();
                company.setCompanyId(util.convertStringToInteger(rs.getString("id")));
                company.setCompanyName(rs.getString("name"));
                corporateLists.add(company);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return corporateLists;
    }
    
    public List<Trade> getTradeByCorporateId(int corporateId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Trade> tradeLists = new ArrayList<Trade>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT id, name FROM trade_table WHERE "
                    + "corporateId = "+corporateId+" ORDER BY name ASC");
            while(rs.next()){
                Trade trade = new Trade();
                trade.setTradeId(util.convertStringToInteger(rs.getString("id")));
                trade.setTradeName(rs.getString("name"));
                tradeLists.add(trade);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tradeLists;
    }
    
    public List<Branch> getBranchByTradeId(int tradeId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Branch> branchLists = new ArrayList<Branch>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT id, name FROM branch_table "
                + "WHERE tradeId = "+tradeId+" AND b.actionTaken IS NULL ORDER BY b.name ASC");
            while(rs.next()){
                Branch branch = new Branch();
                branch.setBranchId(util.convertStringToInteger(rs.getString("id")));
		branch.setBranchName(rs.getString("name"));
		branchLists.add(branch);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return branchLists;
    }
    
    public boolean updateBranchName(int branchId, 
            String name){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("UPDATE branch_table SET name = ? WHERE id = ? ");
            pstmt.setString(1, name);
            pstmt.setInt(2, branchId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public boolean updateBranchAddress(int branchId, 
            String address){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("UPDATE branch_table SET address = ? WHERE id = ? ");
            pstmt.setString(1, address);
            pstmt.setInt(2, branchId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public String getCorporateNameByBranchId(int branchId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null;
        String corporate = null;
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT corporateName FROM payroll_register WHERE branchId = "+branchId+" ");
            while(rs.next()){
                corporate = rs.getString("corporateName");
            }
        } catch (SQLException ex) {
            Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null ||  !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return corporate;
    }
    
    public List<Branch> getBranchListForUser(int userId){
        List<Branch> branchLists = new ArrayList<Branch>();
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT bt.name AS name, uba.id AS id FROM user_branch_access uba "
                    + "INNER JOIN branch_table bt ON uba.branchId = bt.id "
                    + "WHERE uba.userId = "+userId+" ");
            while(rs.next()){
                Branch b = new Branch();
                b.setBranchId(util.convertStringToInteger(rs.getString("id")));
                b.setBranchName(rs.getString("name"));
                branchLists.add(b);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return branchLists;
    }
    
    public boolean removeBranchFromUser(int rowId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        boolean result = false;
        
        try {
            pstmt = conn.prepareStatement("DELETE FROM user_branch_access WHERE id = ? ");
            pstmt.setInt(1, rowId);
            pstmt.executeUpdate();
            
            result = true;
        } catch (SQLException ex) {
            Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public List<Trade> getTradeListForUser(int userId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Trade> tradeLists = new ArrayList<Trade>();
        
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT tt.name AS name, uta.id AS id FROM user_trade_access uta "
                    + "INNER JOIN trade_table tt ON uta.tradeId = tt.id "
                    + "WHERE uta.userId = "+userId+" ");
            while(rs.next()){
                Trade t = new Trade();
                t.setTradeId(util.convertStringToInteger(rs.getString("id")));
                t.setTradeName(rs.getString("name"));
                tradeLists.add(t);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return tradeLists;
    }
    
    public boolean removeTradeFromUser(int rowId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        
        try {
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement("DELETE FROM user_trade_access WHERE id = ? ");
            pstmt.setInt(1, rowId);
            pstmt.executeUpdate();
            
            int tradeId = 0;
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT tradeId FROM user_trade_access "
                    + "WHERE id = "+rowId+" ");
            while(rs.next()){
                tradeId = util.convertStringToInteger(rs.getString("tradeId"));
            }
            
            pstmt = conn.prepareStatement("DELETE FROM user_branch_access WHERE tradeId = ? ");
            pstmt.setInt(1, tradeId);
            pstmt.executeUpdate();
            
            conn.commit();
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
    
    public List<Company> getCorporateListForUser(int userId){
        Connection conn = getConnection.connection();
        Statement stmt = null;
        ResultSet rs = null; 
        List<Company> corporateLists = new ArrayList<Company>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(" SELECT ct.name AS name, uca.id AS id FROM user_corporate_access uca "
                    + "INNER JOIN corporate_table ct ON uca.corporateId = ct.id "
                    + "WHERE uca.userId = '"+userId+"' ");
            while(rs.next()){
                Company c = new Company();
                c.setCompanyId(util.convertStringToInteger(rs.getString("id")));
                c.setCompanyName(rs.getString("name"));
                corporateLists.add(c);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(conn != null || !conn.isClosed()){
                    stmt.close();
                    rs.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(ServiceGetDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return corporateLists;
    }
    
    public boolean removeCorporateFromUser(int rowId){
        Connection conn = getConnection.connection();
        PreparedStatement pstmt = null;
        Statement stmt = null;
        ResultSet rs = null;
        boolean result = false;
        
        try {
            conn.setAutoCommit(false);            
            
            int corporateId = 0;
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT corporateId FROM user_corporate_access "
                    + "WHERE id = "+rowId+" ");
            while(rs.next()){
                corporateId = util.convertStringToInteger(rs.getString("corporateId"));
            }
            
            int tradeId = 0;
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT tradeId FROM user_trade_access "
                    + "WHERE corporateId = "+corporateId+" ");
            while(rs.next()){
                tradeId = util.convertStringToInteger(rs.getString("tradeId"));
            }
            
            pstmt = conn.prepareStatement("DELETE FROM user_corporate_access WHERE id = ? ");
            pstmt.setInt(1, rowId);
            pstmt.executeUpdate();
            
            pstmt = conn.prepareStatement("DELETE FROM user_trade_access WHERE corporateId = ? ");
            pstmt.setInt(1, corporateId);
            pstmt.executeUpdate();
                        
            pstmt = conn.prepareStatement("DELETE FROM user_branch_access WHERE tradeId = ? ");
            pstmt.setInt(1, tradeId);
            pstmt.executeUpdate();
            
            conn.commit();
            result = true;
        } catch (SQLException ex) {
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(conn != null || !conn.isClosed()){
                    pstmt.close();
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(CompanyDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return result;
    }
}
