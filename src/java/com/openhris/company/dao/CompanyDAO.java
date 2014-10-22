/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.openhris.company.dao;

import com.hrms.dbconnection.GetSQLConnection;
import com.openhris.commons.OpenHrisUtilities;
import com.openhris.company.model.Branch;
import com.openhris.company.model.Company;
import com.openhris.company.model.Trade;
import com.openhris.dao.ServiceGetDAO;
import java.sql.Connection;
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
    
}
